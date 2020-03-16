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
package literefresh.sample.ui.viewholder

import android.view.View
import literefresh.sample.R
import literefresh.sample.base.ui.BaseViewHolder
import literefresh.sample.base.ui.ViewBinder
import literefresh.sample.common.image.GlideApp
import literefresh.sample.data.remote.dto.ResUnsplashPhoto
import literefresh.sample.databinding.ViewHolderUnsplashPhotoListBinding

@ViewBinder(value = R.layout.view_holder_unsplash_photo_list, dataTypes = [ResUnsplashPhoto::class])
class UnsplashPhotoListViewHolder(itemView: View?) : BaseViewHolder<ViewHolderUnsplashPhotoListBinding>(itemView) {
    override fun <T> onBind(data: T, position: Int) {
        if (data is ResUnsplashPhoto) {
            val resUnsplashPhoto = data as ResUnsplashPhoto
            binding!!.tvName.text = resUnsplashPhoto.user?.name
            GlideApp.with(itemView)
                    .load(resUnsplashPhoto.user?.profileImage?.small)
                    .into(binding!!.ivProfile)
            GlideApp.with(itemView)
                    .load(resUnsplashPhoto.urls?.small)
                    .into(binding!!.ivImage)
        }
    }
}