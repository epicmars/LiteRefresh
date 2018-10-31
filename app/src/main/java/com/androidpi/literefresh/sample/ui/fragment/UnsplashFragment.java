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
import android.support.v4.math.MathUtils;
import android.view.View;

import com.androidpi.literefresh.LiteRefreshHelper;
import com.androidpi.literefresh.OnRefreshListener;
import com.androidpi.literefresh.OnScrollListener;
import com.androidpi.literefresh.behavior.RefreshHeaderBehavior;
import com.androidpi.literefresh.sample.base.model.Resource;
import com.androidpi.literefresh.sample.base.ui.BaseFragment;
import com.androidpi.literefresh.sample.base.ui.BindLayout;
import com.androidpi.literefresh.sample.data.remote.dto.ResUnsplashPhoto;
import com.androidpi.literefresh.sample.databinding.FragmentUnsplashBinding;
import com.androidpi.literefresh.sample.model.UnsplashPhotoPage;
import com.androidpi.literefresh.sample.vm.UnsplashViewModel;
import com.androidpi.literefresh.sample.R;


import java.util.List;


@BindLayout(value = R.layout.fragment_unsplash)
public class UnsplashFragment extends BaseFragment<FragmentUnsplashBinding> {

    private UnsplashViewModel unsplashViewModel;

    public static UnsplashFragment newInstance() {
        Bundle args = new Bundle();
        UnsplashFragment fragment = new UnsplashFragment();
        fragment.setArguments(args);
        return fragment;
    }

    int triggerOffset;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.imagePagerHeader.setFragmentManager(getChildFragmentManager());
        UnsplashPhotoListFragment fragment = ((UnsplashPhotoListFragment) getChildFragmentManager().findFragmentById(R.id.fragment));
        RefreshHeaderBehavior headerBehavior =
                LiteRefreshHelper.getHeaderBehavior(binding.scaleableHeader)
                        .with(getContext())
                        .maxOffsetRatio(R.fraction.percent_100p)
                        .triggerOffsetRes(R.dimen.unsplash_fragment_trigger_offset)
                        .visibleHeightRatioRes(R.fraction.percent_100)
                        .config();
        unsplashViewModel = getViewModelOfActivity(UnsplashViewModel.class);
        unsplashViewModel.getRandomPhotosResult().observe(this, new Observer<Resource<UnsplashPhotoPage>>() {
            @Override
            public void onChanged(@Nullable Resource<UnsplashPhotoPage> listResource) {
                if (null == listResource) return;
                if (listResource.data == null)
                    return;
                if (listResource.isSuccess()) {
                    if (listResource.data.isFirstPage()) {
                        List<ResUnsplashPhoto> photos = listResource.data.getPhotos();
                        if (photos == null || photos.isEmpty()) {
                            fragment.refreshError(new Exception("Empty data."));
                        } else {
                            if (photos.size() > 3) {
                                binding.imagePagerHeader.setImages(photos.subList(0, 3));
                                fragment.setPayloads(photos.subList(3, photos.size()));
                            } else {
                                binding.imagePagerHeader.setImages(photos.subList(0, 1));
                                if (photos.size() > 1) {
                                    fragment.setPayloads(photos.subList(1, photos.size()));
                                }
                            }
                        }
                    } else {
                        fragment.addPayloads(listResource.data.getPhotos());
                    }
                    headerBehavior.refreshComplete();
                } else if (listResource.isError()) {
                    if (listResource.data.isFirstPage()) {
                        fragment.refreshError(listResource.throwable);
                    }
                    headerBehavior.refreshError(listResource.throwable);
                }
            }
        });

        headerBehavior.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onStartScroll(CoordinatorLayout parent, View view, int initial, int trigger, int min, int max, int type) {
                triggerOffset = trigger - initial;
            }

            @Override
            public void onScroll(CoordinatorLayout parent, View view, int current, int delta, int initial, int trigger, int min, int max, int type) {
                float offset = current - initial;
                float triggerRange = trigger - initial;
                float progress = offset / (trigger - initial);
                binding.loadingView.setProgress(progress);
                binding.loadingView.setTranslationY(MathUtils.clamp(offset, 0, triggerRange));
            }

            @Override
            public void onStopScroll(CoordinatorLayout parent, View view, int current, int initial, int trigger, int min, int max, int type) {

            }
        });

        headerBehavior.addOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefreshStart() {
                binding.loadingView.startProgress();
            }

            @Override
            public void onReleaseToRefresh() {
                binding.loadingView.setTranslationY(triggerOffset);
                binding.loadingView.readyToLoad();
            }

            @Override
            public void onRefresh() {
                binding.loadingView.startLoading();
                firstPage();
            }

            @Override
            public void onRefreshEnd(Throwable throwable) {
                binding.loadingView.finishLoading();
            }
        });


        if (unsplashViewModel.getRandomPhotosResult().getValue() == null) {
            headerBehavior.refresh();
        }
    }

    private void firstPage() {
        unsplashViewModel.firstPage();
    }
}
