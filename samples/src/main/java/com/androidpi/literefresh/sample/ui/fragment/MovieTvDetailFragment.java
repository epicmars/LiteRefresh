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
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.androidpi.literefresh.sample.R;
import com.androidpi.literefresh.sample.base.ui.BaseFragment;
import com.androidpi.literefresh.sample.base.ui.BindLayout;
import com.androidpi.literefresh.sample.common.image.GlideApp;
import com.androidpi.literefresh.sample.data.remote.TheMovieDbApi;
import com.androidpi.literefresh.sample.data.remote.dto.ResMoviePage;
import com.androidpi.literefresh.sample.data.remote.dto.ResTrendingPage;
import com.androidpi.literefresh.sample.data.remote.dto.ResTvPage;
import com.androidpi.literefresh.sample.databinding.FragmentMovieTvDetailBinding;

@BindLayout(R.layout.fragment_movie_tv_detail)
public class MovieTvDetailFragment extends BaseFragment<FragmentMovieTvDetailBinding> {

    private static final String ARGS_DETAIL = "MovieTvDetailFragment.DETAIL";

    public static MovieTvDetailFragment newInstance(Parcelable data) {
        Bundle args = new Bundle();
        args.putParcelable(ARGS_DETAIL, data);
        MovieTvDetailFragment fragment = new MovieTvDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        if (args == null)
            return;
        Parcelable data = args.getParcelable(ARGS_DETAIL);
        if (data == null)
            return;
        String postTitle = null;
        String postUrl = null;
        String overview = null;

        if (data instanceof ResMoviePage.ResultsBean) {
            ResMoviePage.ResultsBean result = ((ResMoviePage.ResultsBean) data);
            postTitle = result.getTitle();
            postUrl = result.getPosterPath();
            overview = result.getOverview();
        }

        if (data instanceof ResTvPage.ResultsBean) {
            ResTvPage.ResultsBean result = ((ResTvPage.ResultsBean) data);
            postTitle = result.getName();
            postUrl = result.getPosterPath();
            overview = result.getOverview();
        }

        if (data instanceof ResTrendingPage.ResultsBean) {
            ResTrendingPage.ResultsBean result = ((ResTrendingPage.ResultsBean) data);
            postTitle = TextUtils.isEmpty(result.getTitle()) ? result.getName() : result.getTitle();
            postUrl = result.getPosterPath();
            overview = result.getOverview();
        }

        binding.tvNameOrTitle.setText(postTitle);
        binding.tvOverview.setText(overview);
        GlideApp.with(view)
                .load(TheMovieDbApi.Companion.getIMAGE_BASE_URL() +
                        TheMovieDbApi.Companion.getIMAGE_SIZE() +
                        postUrl)
                .into(binding.ivPostBg);

        GlideApp.with(view)
                .load(TheMovieDbApi.Companion.getIMAGE_BASE_URL() +
                        TheMovieDbApi.Companion.getIMAGE_SIZE() +
                        postUrl)
                .into(binding.ivPostSmall);
    }
}
