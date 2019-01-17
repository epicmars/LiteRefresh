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

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.TypedValue;
import android.view.View;

import com.androidpi.literefresh.LiteRefreshHelper;
import com.androidpi.literefresh.OnRefreshListener;
import com.androidpi.literefresh.OnScrollListener;
import com.androidpi.literefresh.behavior.RefreshContentBehavior;
import com.androidpi.literefresh.sample.R;
import com.androidpi.literefresh.sample.base.model.Resource;
import com.androidpi.literefresh.sample.base.ui.BaseFragment;
import com.androidpi.literefresh.sample.base.ui.BindLayout;
import com.androidpi.literefresh.sample.base.ui.RecyclerAdapter;
import com.androidpi.literefresh.sample.databinding.FragmentPartialVisibleListBinding;
import com.androidpi.literefresh.sample.model.ErrorItem;
import com.androidpi.literefresh.sample.model.HeaderUnsplashPhoto;
import com.androidpi.literefresh.sample.model.UnsplashPhotoPage;
import com.androidpi.literefresh.sample.ui.viewholder.ErrorViewHolder;
import com.androidpi.literefresh.sample.ui.viewholder.LoadingViewHolder;
import com.androidpi.literefresh.sample.ui.viewholder.UnsplashPhotoHeaderViewHolder;
import com.androidpi.literefresh.sample.ui.viewholder.UnsplashPhotoListViewHolder;
import com.androidpi.literefresh.sample.vm.UnsplashViewModel;

import static android.support.v4.view.ViewCompat.TYPE_TOUCH;

@BindLayout(R.layout.fragment_partial_visible_list)
public class PartialVisibleListFragment extends BaseFragment<FragmentPartialVisibleListBinding> {
    private UnsplashViewModel unsplashViewModel;
    private RecyclerAdapter adapter;
    private HeaderUnsplashPhoto headerUnsplashPhoto = new HeaderUnsplashPhoto(null);

    public static UnsplashPhotoListFragment newInstance() {

        Bundle args = new Bundle();

        UnsplashPhotoListFragment fragment = new UnsplashPhotoListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unsplashViewModel = getViewModelOfActivity(UnsplashViewModel.class);
        adapter = new RecyclerAdapter();
        adapter.register(UnsplashPhotoHeaderViewHolder.class,
                UnsplashPhotoListViewHolder.class,
                ErrorViewHolder.class,
                LoadingViewHolder.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RefreshContentBehavior behavior = LiteRefreshHelper.getAttachedBehavior(binding.recyclerView);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
        unsplashViewModel.getRandomPhotosResult().observe(this, new Observer<Resource<UnsplashPhotoPage>>() {
            @Override
            public void onChanged(@Nullable Resource<UnsplashPhotoPage> listResource) {
                if (listResource == null)
                    return;
                UnsplashPhotoPage page = listResource.data;
                if (page == null)
                    return;
                if (listResource.isSuccess()) {
                    behavior.refreshComplete();
                    if (page.isFirstPage()) {
                        adapter.setPayloads(headerUnsplashPhoto);
                        adapter.addPayloads(page.getPhotos());
                    } else {
                        adapter.addPayloads(page.getPhotos());
                    }
                } else if (listResource.isError()) {
                    if (page.isFirstPage()) {
                        behavior.refreshError(listResource.throwable);
                        adapter.setPayloads(new ErrorItem(listResource.throwable.getMessage()));
                    }
                }
            }
        });

        final float translationDistance = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 128, view.getResources().getDisplayMetrics());
        binding.circleProgress.resetStyle();
        binding.circleProgress.setProgress(1f);
        binding.circleProgress.setTranslationY(-translationDistance);

        if (behavior != null) {
            behavior.addOnScrollListener(new OnScrollListener() {
                @Override
                public void onStartScroll(CoordinatorLayout parent, View view, int initial, int trigger, int min, int max, int type) {
                }

                @Override
                public void onScroll(CoordinatorLayout parent, View view, int current, int delta, int initial, int trigger, int min, int max, int type) {
                }

                @Override
                public void onStopScroll(CoordinatorLayout parent, View view, int current, int initial, int trigger, int min, int max, int type) {
                    if (type == TYPE_TOUCH && !behavior.getController().isRefreshing()) {
                        binding.circleProgress.setProgress(1f);
                        binding.circleProgress.animate().translationY(-translationDistance);
                    }
                }
            });

            behavior.addOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefreshStart() {
//                    Timber.d("onRefreshStart");
                    binding.circleProgress.resetStyle();
                    binding.circleProgress.setProgress(1f);
                }

                @Override
                public void onReleaseToRefresh() {
//                    Timber.d("onReleaseToRefresh");
                    binding.circleProgress.fillCircle();
                    binding.circleProgress.animate().translationY(0);
                }

                @Override
                public void onRefresh() {
//                    Timber.d("onRefresh");
                    if (binding.circleProgress.getTranslationY() != 0) {
                        binding.circleProgress.setTranslationY(0);
                    }
                    binding.circleProgress.startLoading();
                    unsplashViewModel.firstPage();
                }

                @Override
                public void onRefreshEnd(@Nullable Throwable throwable) {
//                    Timber.d("onRefreshEnd");
                    binding.circleProgress.stopLoading();
                    binding.circleProgress.animate().translationY(-translationDistance);
                }
            });
        }

        if (unsplashViewModel.getRandomPhotosResult().getValue() == null) {
            behavior.refresh();
        }
    }
}
