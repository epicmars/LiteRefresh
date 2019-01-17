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

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidpi.literefresh.OnLoadListener
import com.androidpi.literefresh.OnRefreshListener
import com.androidpi.literefresh.OnScrollListener
import com.androidpi.literefresh.behavior.RefreshContentBehavior
import com.androidpi.literefresh.behavior.RefreshFooterBehavior
import com.androidpi.literefresh.behavior.RefreshHeaderBehavior
import com.androidpi.literefresh.sample.R
import com.androidpi.literefresh.sample.base.model.Resource
import com.androidpi.literefresh.sample.base.ui.BaseFragment
import com.androidpi.literefresh.sample.base.ui.BindLayout
import com.androidpi.literefresh.sample.base.ui.RecyclerAdapter
import com.androidpi.literefresh.sample.databinding.FragmentNewsBinding
import com.androidpi.literefresh.sample.model.ErrorItem
import com.androidpi.literefresh.sample.model.News
import com.androidpi.literefresh.sample.model.NewsPagination
import com.androidpi.literefresh.sample.model.NewsPagination.Companion.PAGE_SIZE
import com.androidpi.literefresh.sample.ui.viewholder.ErrorViewHolder
import com.androidpi.literefresh.sample.ui.viewholder.NewsViewHolder
import com.androidpi.literefresh.sample.vm.NewsViewModel


@BindLayout(R.layout.fragment_news)
class NewsFragment : BaseFragment<FragmentNewsBinding>() {

    lateinit var mNewsModel: NewsViewModel

    lateinit var mAdapter: RecyclerAdapter

    var mNewsCategory: String? = null

    lateinit var headerBehavior: RefreshHeaderBehavior<View>

    lateinit var footerBehavior: RefreshFooterBehavior<View>


    companion object {

        val KEY_CATEGORY = "NewsFragment.KEY_PORTAL"

        fun newInstance(): NewsFragment {
            return NewsFragment()
        }

        fun newInstance(category: String?): NewsFragment {
            val args = Bundle()
            args.putString(KEY_CATEGORY, category)
            val fragment = NewsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // If retainInstance is set to be true, on configuration change,
        // onCreate will not be called, therefore [NewsViewModel] will not be recreated.
        mNewsModel = getViewModelOfActivity(NewsViewModel::class.java)
        mNewsCategory = arguments?.getString(KEY_CATEGORY)
        mNewsCategory = if (mNewsCategory == null) "general" else mNewsCategory
        mNewsModel.mCategory = mNewsCategory

        mAdapter = RecyclerAdapter()
        mAdapter.setFragmentManager(childFragmentManager)
        mAdapter.register(NewsViewHolder::class.java,
                ErrorViewHolder::class.java)

        mNewsModel.mNews.observe(this, Observer { t ->
            if (t == null) return@Observer
            val pagination: NewsPagination? = t.data
            if (t.isSuccess) {
                if (pagination == null) {
                    mAdapter.setPayloads(ErrorItem("Empty data"))
                    return@Observer
                }
                if (pagination.isFirstPage()) {
                    refreshFinished()
                    if (pagination.newsList.isNotEmpty()) {
                        mAdapter.setPayloads(pagination.newsList)
                    }
                } else {
                    if (pagination.newsList.isEmpty()) {
                        loadFinished(Exception(getString(R.string.no_more_date)))
                    } else {
                        loadFinished(null)
                        mAdapter.addPayloads(pagination.newsList)
                    }
                }
            } else if (t.isError) {
                if (pagination == null) {
                    mAdapter.setPayloads(ErrorItem("Empty data"))
                    return@Observer
                }
                if (pagination.isFirstPage()) {
                    refreshFinished(t.throwable)
                    mAdapter.setPayloads(ErrorItem("Loading failed"))
                } else {
                    loadFinished(t.throwable as Exception)
                }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // If retainInstance is set to be true, on configuration change,
        // View state will be restored,
        // thus the referenced obsolete activity context should not be used any more,
        // otherwise a memory leak may occur.
        binding.recyclerNews.layoutManager = LinearLayoutManager(context)
        binding.recyclerNews.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        binding.recyclerNews.adapter = mAdapter

        //
        val headerParams = binding.scrollHeader.layoutParams as CoordinatorLayout.LayoutParams
        headerBehavior = RefreshHeaderBehavior<View>(context)
        headerBehavior.addOnScrollListener(object : OnScrollListener {

            override fun onStartScroll(parent: CoordinatorLayout, view: View, initial: Int, trigger: Int, min: Int, max: Int, type: Int) {
                binding.headerProgress.max = max
            }

            override fun onScroll(parent: CoordinatorLayout, view: View, current: Int, delta: Int, initial: Int, trigger: Int, min: Int, max: Int, type: Int) {
                binding.headerProgress.progress = current
            }

            override fun onStopScroll(parent: CoordinatorLayout, view: View, current: Int, initial: Int, trigger: Int, min: Int, max: Int, type: Int) {
                binding.headerProgress.progress = current
            }

        })

        headerBehavior.addOnRefreshListener(object : OnRefreshListener {

            override fun onRefreshStart() {
            }

            override fun onReleaseToRefresh() {

            }

            override fun onRefresh() {
                loadFirstPage()
            }

            override fun onRefreshEnd(throwable: Throwable?) {
            }
        })

        headerParams.behavior = headerBehavior
        binding.scrollHeader.layoutParams = headerParams

        // Set footer behavior.
        val footerParams = binding.scrollFooter.layoutParams as CoordinatorLayout.LayoutParams
        footerBehavior = RefreshFooterBehavior(context)
        footerBehavior.addOnLoadListener(object : OnLoadListener {
            override fun onLoadStart() {

            }

            override fun onReleaseToLoad() {
            }

            override fun onLoad() {
                loadNextPage()
            }

            override fun onLoadEnd(throwable: Throwable?) {
            }
        })


        footerBehavior.addOnScrollListener(object : OnScrollListener {
            override fun onScroll(parent: CoordinatorLayout, view: View, current: Int, delta: Int, initial: Int, trigger: Int, min: Int, max: Int, type: Int) {
            }

            override fun onStartScroll(parent: CoordinatorLayout, view: View, initial: Int, trigger: Int, min: Int, max: Int, type: Int) {

            }

            override fun onStopScroll(parent: CoordinatorLayout, view: View, current: Int, initial: Int, trigger: Int, min: Int, max: Int, type: Int) {
            }
        })

        footerParams.behavior = footerBehavior
        binding.scrollFooter.layoutParams = footerParams

        // Set content behavior.
        val contentParams = binding.recyclerNews.layoutParams as CoordinatorLayout.LayoutParams
        val contentBehavior = RefreshContentBehavior<View>(context)
        contentParams.behavior = contentBehavior

        contentBehavior.addOnScrollListener(object : OnScrollListener {
            override fun onStartScroll(parent: CoordinatorLayout, view: View, initial: Int, trigger: Int, min: Int, max: Int, type: Int) {
                binding.contentProgress.max = max
            }

            override fun onScroll(parent: CoordinatorLayout, view: View, current: Int, delta: Int, initial: Int, trigger: Int, min: Int, max: Int, type: Int) {
                binding.contentProgress.progress = current
            }

            override fun onStopScroll(parent: CoordinatorLayout, view: View, current: Int, initial: Int, trigger: Int, min: Int, max: Int, type: Int) {
                binding.contentProgress.progress = current
            }
        })

        binding.recyclerNews.layoutParams = contentParams

//        // pull up to load more
        binding.recyclerNews.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            val THRESHOULD = PAGE_SIZE / 2

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy == 0)
                    return
                val lastVisibleItem = (recyclerView?.layoutManager as LinearLayoutManager)
                        .findLastVisibleItemPosition()
                val totalItemCount = recyclerView.layoutManager.itemCount
                if (totalItemCount <= lastVisibleItem + THRESHOULD) {
//                    footerBehavior.refresh()
                }
            }
        })

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (mNewsModel.mNews.value == null)
            return
        var newsResource: Resource<NewsPagination> = mNewsModel.mNews.value as Resource<NewsPagination>
        newsResource.data?.newsList?.clear()
        newsResource.data?.newsList?.addAll(mAdapter.payloads as List<News>)
        mNewsModel.mNews.value = newsResource
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (mNewsModel.mNews.value == null) {
            headerBehavior.refresh()
        }
    }

    fun refreshFinished(exception: Throwable? = null) {
        if (exception == null) {
            headerBehavior.refreshComplete()
        } else {
            headerBehavior.refreshError(exception)
        }
    }

    fun loadFinished(exception: Throwable?) {
        if (exception == null) {
            footerBehavior.loadComplete()
        } else {
            footerBehavior.loadError(exception)
        }
    }

    fun loadFirstPage() {
        mNewsModel.refreshPage()
    }

    fun loadNextPage() {
        mNewsModel.nextPage()
    }
}