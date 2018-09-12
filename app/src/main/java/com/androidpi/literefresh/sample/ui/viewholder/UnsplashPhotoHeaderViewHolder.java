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
package com.androidpi.literefresh.sample.ui.viewholder;

import android.view.View;

import com.androidpi.literefresh.sample.R;
import com.androidpi.literefresh.sample.base.ui.BaseViewHolder;
import com.androidpi.literefresh.sample.base.ui.BindLayout;
import com.androidpi.literefresh.sample.common.image.GlideApp;
import com.androidpi.literefresh.sample.databinding.ViewHolderUnsplashPhotoHeaderBinding;
import com.androidpi.literefresh.sample.model.HeaderUnsplashPhoto;

@BindLayout(value = R.layout.view_holder_unsplash_photo_header, dataTypes = HeaderUnsplashPhoto.class)
public class UnsplashPhotoHeaderViewHolder extends BaseViewHolder<ViewHolderUnsplashPhotoHeaderBinding> {

    public UnsplashPhotoHeaderViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public <T> void onBind(T data, int position) {
        GlideApp.with(itemView).load(R.mipmap.photo8).into(binding.ivPhoto);
    }
}
