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
import android.util.TypedValue
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import literefresh.LiteRefresh
import literefresh.OnRefreshListener
import literefresh.OnScrollListener
import literefresh.behavior.RefreshContentBehavior
import literefresh.sample.R
import literefresh.sample.base.model.Resource
import literefresh.sample.base.ui.BaseFragment
import literefresh.sample.base.ui.RecyclerAdapter
import literefresh.sample.databinding.FragmentPartialVisibleListBinding
import literefresh.sample.model.ErrorItem
import literefresh.sample.model.HeaderUnsplashPhoto
import literefresh.sample.model.UnsplashPhotoPage
import literefresh.sample.ui.viewholder.ErrorViewHolder
import literefresh.sample.ui.viewholder.LoadingViewHolder
import literefresh.sample.ui.viewholder.UnsplashPhotoHeaderViewHolder
import literefresh.sample.ui.viewholder.UnsplashPhotoListViewHolder
import literefresh.sample.vm.UnsplashViewModel
import layoutbinder.annotations.BindLayout
import literefresh.behavior.Configuration


class PartialVisibleListFragment : BaseFragment() {

    @BindLayout(R.layout.fragment_partial_visible_list)
    lateinit var binding: FragmentPartialVisibleListBinding

    private val unsplashViewModel: UnsplashViewModel by activityViewModels()
    private var adapter: RecyclerAdapter? = null
    private val headerUnsplashPhoto = HeaderUnsplashPhoto(null)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = RecyclerAdapter()
        adapter!!.register(UnsplashPhotoHeaderViewHolder::class.java,
                UnsplashPhotoListViewHolder::class.java,
                ErrorViewHolder::class.java,
                LoadingViewHolder::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val behavior = LiteRefresh.getAttachedBehavior<RefreshContentBehavior<*>>(binding!!.recyclerView)
        binding!!.recyclerView.layoutManager = LinearLayoutManager(context)
        binding!!.recyclerView.adapter = adapter
        unsplashViewModel!!.randomPhotosResult.observe(viewLifecycleOwner, Observer<Resource<UnsplashPhotoPage>> { listResource ->
            if (listResource == null) return@Observer
            val page = listResource.data ?: return@Observer
            if (listResource.isSuccess) {
                behavior!!.refreshComplete()
                if (page.isFirstPage) {
                    adapter!!.setPayload(headerUnsplashPhoto)
                    adapter!!.addPayloads(page.photos)
                } else {
                    adapter!!.addPayloads(page.photos)
                }
            } else if (listResource.isError) {
                if (page.isFirstPage) {
                    behavior!!.refreshError(listResource.throwable)
                    adapter!!.setPayload(ErrorItem(listResource.throwable!!.message))
                }
            }
        })
        val translationDistance = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 128f, view.resources.displayMetrics)
        binding!!.circleProgress.resetStyle()
        binding!!.circleProgress.setProgress(1f)
        binding!!.circleProgress.translationY = -translationDistance
        if (behavior != null) {
            behavior.addOnScrollListener(object : OnScrollListener {
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
                ) {}
                override fun onStopScroll(
                    parent: CoordinatorLayout,
                    view: View,
                    config: Configuration,
                    type: Int
                ) {
                    if (type == ViewCompat.TYPE_TOUCH && !behavior.controller.isRefreshing) {
                        binding!!.circleProgress.setProgress(1f)
                        binding!!.circleProgress.animate().translationY(-translationDistance)
                    }
                }
            })
            behavior.addOnRefreshListener(object : OnRefreshListener {
                override fun onRefreshStart() {
//                    Timber.d("onRefreshStart");
                    binding!!.circleProgress.resetStyle()
                    binding!!.circleProgress.setProgress(1f)
                }

                override fun onReleaseToRefresh() {
//                    Timber.d("onReleaseToRefresh");
                    binding!!.circleProgress.fillCircle()
                    binding!!.circleProgress.animate().translationY(0f)
                }

                override fun onRefresh() {
//                    Timber.d("onRefresh");
                    if (binding!!.circleProgress.translationY != 0f) {
                        binding!!.circleProgress.translationY = 0f
                    }
                    binding!!.circleProgress.startLoading()
                    unsplashViewModel!!.firstPage()
                }

                override fun onRefreshEnd(throwable: Throwable?) {
//                    Timber.d("onRefreshEnd");
                    binding!!.circleProgress.stopLoading()
                    binding!!.circleProgress.animate().translationY(-translationDistance)
                }
            })
        }
        if (unsplashViewModel!!.randomPhotosResult.value == null) {
            behavior!!.refresh()
        }
    }

    companion object {
        fun newInstance(): UnsplashPhotoListFragment {
            val args = Bundle()
            val fragment = UnsplashPhotoListFragment()
            fragment.arguments = args
            return fragment
        }
    }
}