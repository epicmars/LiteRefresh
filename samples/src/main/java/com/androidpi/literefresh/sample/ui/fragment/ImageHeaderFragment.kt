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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.androidpi.literefresh.sample.R
import com.androidpi.literefresh.sample.common.image.GlideApp

class ImageHeaderFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_image_header, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments == null) return
        val imageView = view.findViewById<ImageView>(R.id.iv_image)
        GlideApp.with(imageView)
                .load(arguments?.getString(ARGS_IMAGE_URL))
                .into(imageView)
    }

    companion object {
        private const val ARGS_IMAGE_URL = "ARGS_IMAGE_URL"
        fun newInstance(imageUrl: String?): ImageHeaderFragment {
            val args = Bundle()
            args.putString(ARGS_IMAGE_URL, imageUrl)
            val fragment = ImageHeaderFragment()
            fragment.arguments = args
            return fragment
        }
    }
}