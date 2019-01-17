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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androidpi.literefresh.sample.R;
import com.androidpi.literefresh.sample.common.image.GlideApp;

public class ImageHeaderFragment extends Fragment {

    private static final String ARGS_IMAGE_URL = "ARGS_IMAGE_URL";

    public static ImageHeaderFragment newInstance(String imageUrl) {
        Bundle args = new Bundle();
        args.putString(ARGS_IMAGE_URL, imageUrl);
        ImageHeaderFragment fragment = new ImageHeaderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ImageHeaderFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_header, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() == null) return;
        ImageView imageView = view.findViewById(R.id.iv_image);
        GlideApp.with(imageView)
                .load(getArguments().getString(ARGS_IMAGE_URL))
                .into(imageView);
    }
}