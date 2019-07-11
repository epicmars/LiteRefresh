/*
 * Copyright 2018 yinpinjiu@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.androidpi.literefresh.sample.ui.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.androidpi.literefresh.sample.R;
import com.androidpi.literefresh.sample.base.model.Resource;
import com.androidpi.literefresh.sample.base.ui.BaseFragment;
import com.androidpi.literefresh.sample.base.ui.BindLayout;
import com.androidpi.literefresh.sample.base.ui.RecyclerAdapter;
import com.androidpi.literefresh.sample.data.remote.dto.ResMoviePage;
import com.androidpi.literefresh.sample.data.remote.dto.ResTrendingPage;
import com.androidpi.literefresh.sample.data.remote.dto.ResTvPage;
import com.androidpi.literefresh.sample.databinding.FragmentMovieTvBinding;
import com.androidpi.literefresh.sample.model.ErrorItem;
import com.androidpi.literefresh.sample.model.LoadingItem;
import com.androidpi.literefresh.sample.ui.viewholder.ErrorViewHolder;
import com.androidpi.literefresh.sample.ui.viewholder.LoadingViewHolder;
import com.androidpi.literefresh.sample.ui.viewholder.MovieTvPostViewHolder;
import com.androidpi.literefresh.sample.vm.TheMovieDbViewModel;

import static com.androidpi.literefresh.sample.vm.TheMovieDbViewModel.TRENDING_ALL;
import static com.androidpi.literefresh.sample.vm.TheMovieDbViewModel.TRENDING_MOVIE;
import static com.androidpi.literefresh.sample.vm.TheMovieDbViewModel.TRENDING_TV;

@BindLayout(R.layout.fragment_movie_tv)
public class MovieTvFragment extends BaseFragment<FragmentMovieTvBinding> {

    private static final String ARGS_TYPE = "MovieTvFragment.ARGS_TYPE";

    private String type = TRENDING_ALL;
    private TheMovieDbViewModel viewModel;
    private RecyclerAdapter recentAdapter;
    private RecyclerAdapter trendingAdapter;

    public static MovieTvFragment newInstance(String type) {

        Bundle args = new Bundle();
        args.putString(ARGS_TYPE, type);
        MovieTvFragment fragment = new MovieTvFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = getViewModelOfActivity(TheMovieDbViewModel.class);
        recentAdapter = new RecyclerAdapter();
        recentAdapter.register(MovieTvPostViewHolder.class, ErrorViewHolder.class, LoadingViewHolder.class);

        trendingAdapter = new RecyclerAdapter();
        trendingAdapter.register(MovieTvPostViewHolder.class, ErrorViewHolder.class, LoadingViewHolder.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.recentRecyclerView.setAdapter(recentAdapter);
        binding.recentRecyclerView.setNestedScrollingEnabled(false);

        binding.trendingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.trendingRecyclerView.setAdapter(trendingAdapter);
        binding.trendingRecyclerView.setNestedScrollingEnabled(false);

        Bundle args = getArguments();
        if (args != null) {
            type = getArguments().getString(ARGS_TYPE);
        }

        Observer<Resource<ResTrendingPage>> trendingObserver = new Observer<Resource<ResTrendingPage>>() {
            @Override
            public void onChanged(@Nullable Resource<ResTrendingPage> resTrendingPageResource) {
                if (resTrendingPageResource == null)
                    return;
                if (resTrendingPageResource.isSuccess()) {
                    trendingAdapter.setPayloads(resTrendingPageResource.data.getResults());
                } else if (resTrendingPageResource.isError()) {
                    trendingAdapter.setPayloads(new ErrorItem(resTrendingPageResource.throwable.getMessage()));
                } else if (resTrendingPageResource.isLoading()) {
                    trendingAdapter.setPayloads(new LoadingItem());
                }
            }
        };

        Observer<Resource<ResMoviePage>> movieObserver = new Observer<Resource<ResMoviePage>>() {
            @Override
            public void onChanged(@Nullable Resource<ResMoviePage> resMoviePageResource) {
                if (resMoviePageResource == null)
                    return;
                if (resMoviePageResource.isSuccess()) {
                    recentAdapter.setPayloads(resMoviePageResource.data.getResults());
                } else if (resMoviePageResource.isError()) {
                    recentAdapter.setPayloads(new ErrorItem(resMoviePageResource.throwable.getMessage()));
                } else if (resMoviePageResource.isLoading()) {
                    recentAdapter.setPayloads(new LoadingItem());
                }
            }
        };

        Observer<Resource<ResTvPage>> tvObserver = new Observer<Resource<ResTvPage>>() {
            @Override
            public void onChanged(@Nullable Resource<ResTvPage> resTvPageResource) {
                if (resTvPageResource == null)
                    return;
                if (resTvPageResource.isSuccess()) {
                    recentAdapter.setPayloads(resTvPageResource.data.getResults());
                } else if (resTvPageResource.isError()) {
                    recentAdapter.setPayloads(new ErrorItem(resTvPageResource.throwable.getMessage()));
                } else if (resTvPageResource.isLoading()) {
                    recentAdapter.setPayloads(new LoadingItem());
                }
            }
        };

        MutableLiveData<Resource<ResTrendingPage>> trendingResults = getTrendingResults(type);
        trendingResults.observe(this, trendingObserver);
        if (trendingResults.getValue() == null) {
            viewModel.getWeekTrending(type, trendingResults);
        }

        switch (type) {
            case TRENDING_TV:
                viewModel.getTvWithinMonthResults().observe(this, tvObserver);
                if (viewModel.getTvWithinMonthResults().getValue() == null) {
                    viewModel.getTvWithinMonth();
                }
                break;
            default:
                viewModel.getMovieWithinMonthResults().observe(this, movieObserver);
                if (viewModel.getMovieWithinMonthResults().getValue() == null) {
                    viewModel.getMovieWithinMonth();
                }
                break;
        }

    }

    private MutableLiveData<Resource<ResTrendingPage>> getTrendingResults(String type) {
        switch (type) {
            case TRENDING_MOVIE:
                return viewModel.getWeekTrendingMovieResults();
            case TRENDING_TV:
                return viewModel.getWeekTrendingTvResults();
            case TRENDING_ALL:
            default:
                return viewModel.getWeekTrendingAllResults();
        }
    }
}
