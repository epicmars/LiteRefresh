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
package com.androidpi.literefresh.sample.ui.fragment

import android.os.Bundle
import android.view.View
import com.androidpi.literefresh.sample.R
import com.androidpi.literefresh.sample.base.ui.BaseFragment
import com.androidpi.literefresh.sample.common.image.GlideApp
import com.androidpi.literefresh.sample.databinding.FragmentImageBinding
import layoutbinder.annotations.BindLayout

class ImageFragment : BaseFragment() {

    @BindLayout(R.layout.fragment_image)
    lateinit var binding : FragmentImageBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            val imageUrl = arguments?.getString(ARGS_IMAGE_URL) ?: return
            GlideApp.with(view)
                    .load(imageUrl)
                    .into(binding!!.ivImage)
        }
    }

    companion object {
        const val ARGS_IMAGE_URL = "ImageFragment.ARGS_IMAGE_URL"
        fun newInstance(imageUrl: String?): ImageFragment {
            val args = Bundle()
            args.putString(ARGS_IMAGE_URL, imageUrl)
            val fragment = ImageFragment()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(args: Bundle?): ImageFragment {
            val fragment = ImageFragment()
            fragment.arguments = args
            return fragment
        }
    }
}