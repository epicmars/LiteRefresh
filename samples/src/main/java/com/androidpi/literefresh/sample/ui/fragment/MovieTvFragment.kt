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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidpi.literefresh.sample.R
import com.androidpi.literefresh.sample.base.model.Resource
import com.androidpi.literefresh.sample.base.ui.BaseFragment
import com.androidpi.literefresh.sample.base.ui.RecyclerAdapter
import com.androidpi.literefresh.sample.data.remote.dto.ResMoviePage
import com.androidpi.literefresh.sample.data.remote.dto.ResTrendingPage
import com.androidpi.literefresh.sample.data.remote.dto.ResTvPage
import com.androidpi.literefresh.sample.databinding.FragmentMovieTvBinding
import com.androidpi.literefresh.sample.model.ErrorItem
import com.androidpi.literefresh.sample.model.LoadingItem
import com.androidpi.literefresh.sample.ui.viewholder.ErrorViewHolder
import com.androidpi.literefresh.sample.ui.viewholder.LoadingViewHolder
import com.androidpi.literefresh.sample.ui.viewholder.MovieTvPostViewHolder
import com.androidpi.literefresh.sample.vm.TheMovieDbViewModel
import layoutbinder.annotations.BindLayout


class MovieTvFragment : BaseFragment() {

    @BindLayout(R.layout.fragment_movie_tv)
    lateinit var binding: FragmentMovieTvBinding

    private var type: String? = TheMovieDbViewModel.TRENDING_ALL
    private val viewModel: TheMovieDbViewModel by activityViewModels()
    private var recentAdapter: RecyclerAdapter? = null
    private var trendingAdapter: RecyclerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recentAdapter = RecyclerAdapter()
        recentAdapter!!.register(MovieTvPostViewHolder::class.java, ErrorViewHolder::class.java, LoadingViewHolder::class.java)
        trendingAdapter = RecyclerAdapter()
        trendingAdapter!!.register(MovieTvPostViewHolder::class.java, ErrorViewHolder::class.java, LoadingViewHolder::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recentRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recentRecyclerView.adapter = recentAdapter
        binding.recentRecyclerView.isNestedScrollingEnabled = false
        binding.trendingRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.trendingRecyclerView.adapter = trendingAdapter
        binding.trendingRecyclerView.isNestedScrollingEnabled = false
        val args = arguments
        if (args != null) {
            type = arguments?.getString(ARGS_TYPE)
        }
        val trendingObserver = Observer<Resource<ResTrendingPage>?> { resTrendingPageResource ->
            if (resTrendingPageResource == null) return@Observer
            if (resTrendingPageResource.isSuccess) {
                trendingAdapter!!.setPayloads(resTrendingPageResource.data?.results)
            } else if (resTrendingPageResource.isError) {
                trendingAdapter!!.setPayload(ErrorItem(resTrendingPageResource.throwable!!.message))
            } else if (resTrendingPageResource.isLoading) {
                trendingAdapter!!.setPayload(LoadingItem())
            }
        }
        val movieObserver: Observer<Resource<ResMoviePage>> = Observer { resMoviePageResource ->
            if (resMoviePageResource == null) return@Observer
            if (resMoviePageResource.isSuccess) {
                recentAdapter!!.setPayloads(resMoviePageResource.data?.results)
            } else if (resMoviePageResource.isError) {
                recentAdapter!!.setPayload(ErrorItem(resMoviePageResource.throwable!!.message))
            } else if (resMoviePageResource.isLoading) {
                recentAdapter!!.setPayload(LoadingItem())
            }
        }
        val tvObserver: Observer<Resource<ResTvPage>> = Observer { resTvPageResource ->
            if (resTvPageResource == null) return@Observer
            if (resTvPageResource.isSuccess) {
                recentAdapter!!.setPayloads(resTvPageResource.data?.results)
            } else if (resTvPageResource.isError) {
                recentAdapter!!.setPayload(ErrorItem(resTvPageResource.throwable!!.message))
            } else if (resTvPageResource.isLoading) {
                recentAdapter!!.setPayload(LoadingItem())
            }
        }
        val trendingResults = type?.let { getTrendingResults(it) }
        trendingResults?.observe(viewLifecycleOwner, trendingObserver)
        if (trendingResults?.value == null) {
            viewModel!!.getWeekTrending(type, trendingResults)
        }
        when (type) {
            TheMovieDbViewModel.TRENDING_TV -> {
                viewModel!!.tvWithinMonthResults.observe(viewLifecycleOwner, tvObserver)
                if (viewModel!!.tvWithinMonthResults.value == null) {
                    viewModel!!.getTvWithinMonth()
                }
            }
            else -> {
                viewModel!!.movieWithinMonthResults.observe(viewLifecycleOwner, movieObserver)
                if (viewModel!!.movieWithinMonthResults.value == null) {
                    viewModel!!.getMovieWithinMonth()
                }
            }
        }
    }

    private fun getTrendingResults(type: String): MutableLiveData<Resource<ResTrendingPage>> {
        return when (type) {
            TheMovieDbViewModel.TRENDING_MOVIE -> viewModel!!.weekTrendingMovieResults
            TheMovieDbViewModel.TRENDING_TV -> viewModel!!.weekTrendingTvResults
            TheMovieDbViewModel.TRENDING_ALL -> viewModel!!.weekTrendingAllResults
            else -> viewModel!!.weekTrendingAllResults
        }
    }

    companion object {
        private const val ARGS_TYPE = "MovieTvFragment.ARGS_TYPE"
        fun newInstance(type: String?): MovieTvFragment {
            val args = Bundle()
            args.putString(ARGS_TYPE, type)
            val fragment = MovieTvFragment()
            fragment.arguments = args
            return fragment
        }
    }
}