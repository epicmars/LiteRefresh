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

import android.os.Parcelable
import android.text.TextUtils
import android.view.View
import androidx.fragment.app.Fragment
import literefresh.sample.R
import literefresh.sample.base.ui.BaseViewHolder
import literefresh.sample.base.ui.FragmentFactory
import literefresh.sample.base.ui.ViewBinder
import literefresh.sample.common.image.GlideApp
import literefresh.sample.data.remote.TheMovieDbApi.Companion.IMAGE_BASE_URL
import literefresh.sample.data.remote.TheMovieDbApi.Companion.IMAGE_SIZE
import literefresh.sample.data.remote.dto.ResMoviePage
import literefresh.sample.data.remote.dto.ResTrendingPage
import literefresh.sample.data.remote.dto.ResTvPage
import literefresh.sample.databinding.ViewHolderMovieTvPostBinding
import literefresh.sample.ui.TemplateActivity.Companion.startWith
import literefresh.sample.ui.fragment.MovieTvDetailFragment

@ViewBinder(value = R.layout.view_holder_movie_tv_post, dataTypes = [ResTrendingPage.ResultsBean::class, ResTvPage.ResultsBean::class, ResMoviePage.ResultsBean::class])
class MovieTvPostViewHolder(itemView: View?) : BaseViewHolder<ViewHolderMovieTvPostBinding>(itemView) {
    override fun <T> onBind(data: T, position: Int) {
        var postTitle: String? = null
        var postUrl: String? = null
        if (data is ResMoviePage.ResultsBean) {
            val result = data as ResMoviePage.ResultsBean
            postTitle = result.title
            postUrl = result.posterPath
        }
        if (data is ResTvPage.ResultsBean) {
            val result = data as ResTvPage.ResultsBean
            postTitle = result.name
            postUrl = result.posterPath
        }
        if (data is ResTrendingPage.ResultsBean) {
            val result = data as ResTrendingPage.ResultsBean
            postTitle = if (TextUtils.isEmpty(result.title)) result.name else result.title
            postUrl = result.posterPath
        }
        binding!!.tvNameOrTitle.text = postTitle
        GlideApp.with(itemView)
                .load(IMAGE_BASE_URL +
                        IMAGE_SIZE +
                        postUrl)
                .into(binding!!.ivPost)
        itemView.setOnClickListener {
            startWith(itemView.context, 0, MovieTvDetailFragment::class.java.name, object : FragmentFactory<Fragment>() {
                override fun create(): Fragment {
                    return MovieTvDetailFragment.Companion.newInstance(data as Parcelable)
                }
            })
        }
    }
}