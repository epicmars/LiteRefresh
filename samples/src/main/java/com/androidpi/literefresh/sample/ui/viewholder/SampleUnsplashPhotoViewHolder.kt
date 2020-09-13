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
package com.androidpi.literefresh.sample.ui.viewholder

import android.view.View
import com.androidpi.literefresh.sample.R
import com.androidpi.literefresh.sample.base.ui.BaseViewHolder
import com.androidpi.literefresh.sample.base.ui.ViewBinder
import com.androidpi.literefresh.sample.common.image.GlideApp
import com.androidpi.literefresh.sample.databinding.ViewHolderSampleUnsplashPhotoBinding
import com.androidpi.literefresh.sample.model.SampleUnsplashPhoto
import layoutbinder.annotations.BindLayout

@ViewBinder(value = R.layout.view_holder_sample_unsplash_photo, dataTypes = [SampleUnsplashPhoto::class])
class SampleUnsplashPhotoViewHolder(itemView: View?) : BaseViewHolder<ViewHolderSampleUnsplashPhotoBinding>(itemView) {
    override fun <T> onBind(data: T, position: Int) {
        if (data is SampleUnsplashPhoto) {
            val unsplashPhoto = data as SampleUnsplashPhoto
            binding!!.tvAuthor.text = itemView.resources
                    .getString(R.string.unsplash_photo_format, unsplashPhoto.author)
            GlideApp.with(itemView).load(unsplashPhoto.photoResId).into(binding!!.ivPhoto)
        }
    }
}