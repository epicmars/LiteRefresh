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
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.androidpi.literefresh.LiteRefreshHelper;
import com.androidpi.literefresh.OnLoadListener;
import com.androidpi.literefresh.OnRefreshListener;
import com.androidpi.literefresh.OnScrollListener;
import com.androidpi.literefresh.behavior.RefreshFooterBehavior;
import com.androidpi.literefresh.behavior.RefreshHeaderBehavior;
import com.androidpi.literefresh.sample.R;
import com.androidpi.literefresh.sample.base.model.Resource;
import com.androidpi.literefresh.sample.base.ui.BaseFragment;
import com.androidpi.literefresh.sample.base.ui.BindLayout;
import com.androidpi.literefresh.sample.base.ui.FragmentFactory;
import com.androidpi.literefresh.sample.common.image.GlideApp;
import com.androidpi.literefresh.sample.databinding.FragmentPartialVisibleHeaderBinding;
import com.androidpi.literefresh.sample.model.UnsplashPhotoPage;
import com.androidpi.literefresh.sample.ui.TemplateActivity;
import com.androidpi.literefresh.sample.vm.UnsplashViewModel;


@BindLayout(R.layout.fragment_partial_visible_header)
public class PartialVisibleHeaderFragment extends BaseFragment<FragmentPartialVisibleHeaderBinding> {

    UnsplashViewModel unsplashViewModel;
    UnsplashPhotoListFragment photoListFragment;
    private boolean isLaunched = false;
    RefreshHeaderBehavior headerBehavior;
    RefreshFooterBehavior footerBehavior;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unsplashViewModel = getViewModelOfActivity(UnsplashViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GlideApp.with(view).load(R.mipmap.photo4).into(binding.ivPhoto);
        headerBehavior = LiteRefreshHelper.getAttachedBehavior(binding.viewHeader);
        footerBehavior = LiteRefreshHelper.getAttachedBehavior(binding.viewFooter);
        photoListFragment = ((UnsplashPhotoListFragment) getChildFragmentManager().findFragmentById(R.id.fragment_list));
        unsplashViewModel.getRandomPhotosResult().observe(this, new Observer<Resource<UnsplashPhotoPage>>() {
            @Override
            public void onChanged(@Nullable Resource<UnsplashPhotoPage> listResource) {
                if (listResource == null)
                    return;
                UnsplashPhotoPage page = listResource.data;
                if (page == null)
                    return;
                if (listResource.isSuccess()) {
                    headerBehavior.refreshComplete();
                    if (page.isFirstPage()) {
                        photoListFragment.setPayloads(page.getPhotos());
                    } else {
                        photoListFragment.addPayloads(page.getPhotos());
                    }
                } else if (listResource.isError()) {
                    headerBehavior.refreshError(listResource.throwable);
                    if (page.isFirstPage()) {
                        photoListFragment.refreshError(listResource.throwable);
                    }
                }
            }
        });

        if (headerBehavior != null) {
            headerBehavior.addOnScrollListener(new OnScrollListener() {

                @Override
                public void onStartScroll(CoordinatorLayout parent, View view, int initial, int trigger, int min, int max, int type) {
//                    binding.circleProgress.setVisibility(View.VISIBLE);
//                    circularProgressDrawable.start();
                }

                @Override
                public void onScroll(CoordinatorLayout parent, View view, int current, int delta, int initial, int trigger, int min, int max, int type) {
                    if (current >= view.getHeight() * 0.8f) {
                        if (!isLaunched) {
                            isLaunched = true;
                            view.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    String sharedElementName = getResources().getString(R.string.transition_header);
                                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), view, sharedElementName);
                                    TemplateActivity.Companion.startWith(options, getContext(), 0, ImageFragment.class.getName(), new FragmentFactory<Fragment>() {
                                        @Override
                                        public Fragment create() {
                                            return ImageFragment.newInstance((String) null);
                                        }
                                    });
                                }
                            }, 300L);
                        }
                        return;
                    }
                    if (current >= headerBehavior.getConfiguration().getVisibleHeight()) {
                        final float distance = current - initial;
                        final float triggerRange = trigger - initial;
                        binding.circleProgress.setProgress(distance/triggerRange);
                    } else {
                        binding.circleProgress.setProgress(0f);
                    }
                }

                @Override
                public void onStopScroll(CoordinatorLayout parent, View view, int current, int initial, int trigger, int min, int max, int type) {
                }
            });

            headerBehavior.addOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefreshStart() {
                    binding.circleProgress.setVisibility(View.VISIBLE);
                    binding.circleProgress.resetStyle();
                }

                @Override
                public void onReleaseToRefresh() {
                    binding.circleProgress.fillCircle();
                }

                @Override
                public void onRefresh() {
                    binding.circleProgress.startLoading();
                    if (!isLaunched) {
                        unsplashViewModel.firstPage();
                    } else {
                        headerBehavior.refreshComplete();
                    }
                }

                @Override
                public void onRefreshEnd(@Nullable Throwable throwable) {
                    binding.circleProgress.stopLoading();
                    binding.circleProgress.setVisibility(View.GONE);
                }
            });
        }

        if (footerBehavior != null) {
            footerBehavior.addOnScrollListener(new OnScrollListener() {
                @Override
                public void onStartScroll(CoordinatorLayout parent, View view, int initial, int trigger, int min, int max, int type) {

                }

                @Override
                public void onScroll(CoordinatorLayout parent, View view, int current, int delta, int initial, int trigger, int min, int max, int type) {
                    binding.footerCircleProgress.setProgress(Math.max(0f, (float) current / trigger));
                }

                @Override
                public void onStopScroll(CoordinatorLayout parent, View view, int current, int initial, int trigger, int min, int max, int type) {

                }
            });

            footerBehavior.addOnLoadListener(new OnLoadListener() {
                @Override
                public void onLoadStart() {
                    binding.footerCircleProgress.setVisibility(View.VISIBLE);
                    binding.footerTvMessage.setVisibility(View.GONE);
                    binding.footerCircleProgress.resetStyle();
                }

                @Override
                public void onReleaseToLoad() {
                    binding.footerCircleProgress.fillCircle();
                }

                @Override
                public void onLoad() {
                    binding.footerCircleProgress.startLoading();
                    binding.footerCircleProgress.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            footerBehavior.loadComplete();
                        }
                    }, 2000L);
                }

                @Override
                public void onLoadEnd(@Nullable Throwable throwable) {
                    binding.footerCircleProgress.stopLoading();
                    binding.footerCircleProgress.setVisibility(View.GONE);
                    binding.footerTvMessage.setVisibility(View.VISIBLE);
                    binding.footerTvMessage.setText("Mocked loading complete.");
                }
            });
        }

        if (unsplashViewModel.getRandomPhotosResult().getValue() == null) {
            headerBehavior.refresh();
        }
    }

    private void firstPage() {
        unsplashViewModel.firstPage();
    }

    @Override
    public void onResume() {
        super.onResume();
        isLaunched = false;
    }
}
