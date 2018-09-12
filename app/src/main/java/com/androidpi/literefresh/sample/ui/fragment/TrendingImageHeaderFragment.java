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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.androidpi.literefresh.sample.R;
import com.androidpi.literefresh.sample.base.ui.BaseFragment;
import com.androidpi.literefresh.sample.base.ui.BindLayout;
import com.androidpi.literefresh.sample.common.image.GlideApp;
import com.androidpi.literefresh.sample.data.remote.TheMovieDbApi;
import com.androidpi.literefresh.sample.databinding.FragmentTrendingImageHeaderBinding;


@BindLayout(R.layout.fragment_trending_image_header)
public class TrendingImageHeaderFragment extends BaseFragment<FragmentTrendingImageHeaderBinding> {

    private static final String ARGS_IMAGE_URL = "TrendingImageHeaderFragment.ARGS_IMAGE_URL";

    public static TrendingImageHeaderFragment newInstance(String imageUrl) {
        Bundle args = new Bundle();
        args.putString(ARGS_IMAGE_URL, imageUrl);
        TrendingImageHeaderFragment fragment = new TrendingImageHeaderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() == null) return;
        GlideApp.with(view)
                .load(TheMovieDbApi.Companion.getIMAGE_BASE_URL() +
                        TheMovieDbApi.Companion.getIMAGE_SIZE() +
                        getArguments().getString(ARGS_IMAGE_URL))
                .into(binding.ivImage);
    }
}
