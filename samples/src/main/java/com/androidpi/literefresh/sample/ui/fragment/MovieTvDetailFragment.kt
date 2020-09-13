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
import android.os.Parcelable
import android.text.TextUtils
import android.view.View
import com.androidpi.literefresh.sample.R
import com.androidpi.literefresh.sample.base.ui.BaseFragment
import com.androidpi.literefresh.sample.common.image.GlideApp
import com.androidpi.literefresh.sample.data.remote.TheMovieDbApi.Companion.IMAGE_BASE_URL
import com.androidpi.literefresh.sample.data.remote.TheMovieDbApi.Companion.IMAGE_SIZE
import com.androidpi.literefresh.sample.data.remote.dto.ResMoviePage
import com.androidpi.literefresh.sample.data.remote.dto.ResTrendingPage
import com.androidpi.literefresh.sample.data.remote.dto.ResTvPage
import com.androidpi.literefresh.sample.databinding.FragmentMovieTvDetailBinding
import layoutbinder.annotations.BindLayout


class MovieTvDetailFragment : BaseFragment() {

    @BindLayout(R.layout.fragment_movie_tv_detail)
    lateinit var binding : FragmentMovieTvDetailBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments ?: return
        val data = args.getParcelable<Parcelable>(ARGS_DETAIL) ?: return
        var postTitle: String? = null
        var postUrl: String? = null
        var overview: String? = null
        if (data is ResMoviePage.ResultsBean) {
            val result = data
            postTitle = result.title
            postUrl = result.posterPath
            overview = result.overview
        }
        if (data is ResTvPage.ResultsBean) {
            val result = data
            postTitle = result.name
            postUrl = result.posterPath
            overview = result.overview
        }
        if (data is ResTrendingPage.ResultsBean) {
            val result = data
            postTitle = if (TextUtils.isEmpty(result.title)) result.name else result.title
            postUrl = result.posterPath
            overview = result.overview
        }
        binding!!.tvNameOrTitle.text = postTitle
        binding!!.tvOverview.text = overview
        GlideApp.with(view)
                .load(IMAGE_BASE_URL +
                        IMAGE_SIZE +
                        postUrl)
                .into(binding!!.ivPostBg)
        GlideApp.with(view)
                .load(IMAGE_BASE_URL +
                        IMAGE_SIZE +
                        postUrl)
                .into(binding!!.ivPostSmall)
    }

    companion object {
        private const val ARGS_DETAIL = "MovieTvDetailFragment.DETAIL"
        fun newInstance(data: Parcelable?): MovieTvDetailFragment {
            val args = Bundle()
            args.putParcelable(ARGS_DETAIL, data)
            val fragment = MovieTvDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }
}