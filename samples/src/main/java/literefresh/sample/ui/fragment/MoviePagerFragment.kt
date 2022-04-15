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
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import literefresh.LiteRefresh
import literefresh.OnRefreshListener
import literefresh.OnScrollListener
import literefresh.behavior.RefreshHeaderBehavior
import literefresh.sample.R
import literefresh.sample.base.ui.BaseFragment
import literefresh.sample.data.remote.dto.ResTrendingPage
import literefresh.sample.databinding.FragmentMoviePagerBinding
import literefresh.sample.vm.TheMovieDbViewModel
import layoutbinder.annotations.BindLayout
import literefresh.behavior.Configuration
import java.util.*

class MoviePagerFragment : BaseFragment() {

    @BindLayout(R.layout.fragment_movie_pager)
    lateinit var binding: FragmentMoviePagerBinding

    var pagerAdapter: MoviePagerAdapter? = null
    val viewModel: TheMovieDbViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pagerAdapter = MoviePagerAdapter(childFragmentManager)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.imagePagerHeader.setFragmentManager(childFragmentManager)
        binding!!.viewPager.adapter = pagerAdapter
        binding!!.pagerTabs.setupWithViewPager(binding!!.viewPager)
        val headerBehavior = LiteRefresh.getAttachedBehavior<RefreshHeaderBehavior<*>>(binding!!.viewHeader)
        viewModel!!.dayTrendingAllResults.observe(viewLifecycleOwner, Observer { resTrendingPageResource ->
            if (resTrendingPageResource == null) return@Observer
            if (resTrendingPageResource.isSuccess) {
                val results = resTrendingPageResource.data?.results
                binding!!.imagePagerHeader.setImages(selectRandomCovers(results))
                headerBehavior.refreshComplete()
            } else if (resTrendingPageResource.isError) {
                headerBehavior.refreshError(resTrendingPageResource.throwable)
            }
        })
        headerBehavior.addOnScrollListener(object : OnScrollListener {
            override fun onStartScroll(
                parent: CoordinatorLayout,
                view: View,
                config: Configuration,
                type: Int
            ) {}
            override fun onPreScroll(
                parent: CoordinatorLayout,
                view: View,
                config: Configuration,
                type: Int
            ) {
            }

            override fun onScroll(
                parent: CoordinatorLayout,
                view: View,
                config: Configuration,
                delta: Int,
                type: Int
            ) {
                val percent = (current - initial) / (trigger - initial).toFloat()
                binding!!.circleProgress.setProgress(percent)
            }

            override fun onStopScroll(
                parent: CoordinatorLayout,
                view: View,
                config: Configuration,
                type: Int
            ) {}
        })
        headerBehavior.addOnRefreshListener(object : OnRefreshListener {
            override fun onRefreshStart() {
                binding!!.circleProgress.resetStyle()
            }

            override fun onReleaseToRefresh() {
                binding!!.circleProgress.fillCircle()
            }

            override fun onRefresh() {
                binding!!.circleProgress.startLoading()
                refreshCovers()
            }

            override fun onRefreshEnd(throwable: Throwable?) {
                binding!!.circleProgress.stopLoading()
            }
        })
        if (viewModel!!.dayTrendingAllResults.value == null) {
            headerBehavior.refresh()
        }
    }

    private fun selectRandomCovers(origin: List<ResTrendingPage.ResultsBean?>?): List<ResTrendingPage.ResultsBean?>? {
        if (origin == null || origin.size <= MAX_COVER_SIZE) return origin
        val size = origin.size
        val random = Random()
        val selected: MutableList<ResTrendingPage.ResultsBean?> = ArrayList()
        while (selected.size < MAX_COVER_SIZE) {
            val result = origin[random.nextInt(size)]
            if (selected.contains(result)) continue
            selected.add(result)
        }
        return selected
    }

    private fun refreshCovers() {
        viewModel!!.getDayTrending(TheMovieDbViewModel.TRENDING_ALL, viewModel!!.dayTrendingAllResults)
    }

    class MoviePagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm!!) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> MovieTvFragment.Companion.newInstance(TheMovieDbViewModel.TRENDING_MOVIE)
                1 -> MovieTvFragment.Companion.newInstance(TheMovieDbViewModel.TRENDING_TV)
                else -> MovieTvFragment.Companion.newInstance(TheMovieDbViewModel.TRENDING_TV)
            }
        }

        override fun getCount(): Int {
            return PAGE_TITLES.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return PAGE_TITLES[position]
        }

        companion object {
            var PAGE_TITLES = arrayOf("Movie", "Tv")
        }
    }

    companion object {
        private const val MAX_COVER_SIZE = 5
    }
}