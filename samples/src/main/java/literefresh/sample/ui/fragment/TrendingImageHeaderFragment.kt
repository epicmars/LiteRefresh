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
package literefresh.sample.ui.fragment

import android.os.Bundle
import android.view.View
import literefresh.sample.R
import literefresh.sample.base.ui.BaseFragment
import literefresh.sample.common.image.GlideApp
import literefresh.sample.data.remote.TheMovieDbApi.Companion.IMAGE_BASE_URL
import literefresh.sample.data.remote.TheMovieDbApi.Companion.IMAGE_SIZE
import literefresh.sample.databinding.FragmentTrendingImageHeaderBinding
import layoutbinder.annotations.BindLayout

class TrendingImageHeaderFragment : BaseFragment() {
    @BindLayout(R.layout.fragment_trending_image_header)
    lateinit var binding: FragmentTrendingImageHeaderBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments == null) return
        GlideApp.with(view)
                .load(IMAGE_BASE_URL +
                        IMAGE_SIZE +
                        arguments?.getString(ARGS_IMAGE_URL))
                .into(binding!!.ivImage)
    }

    companion object {
        private const val ARGS_IMAGE_URL = "TrendingImageHeaderFragment.ARGS_IMAGE_URL"
        fun newInstance(imageUrl: String?): TrendingImageHeaderFragment {
            val args = Bundle()
            args.putString(ARGS_IMAGE_URL, imageUrl)
            val fragment = TrendingImageHeaderFragment()
            fragment.arguments = args
            return fragment
        }
    }
}