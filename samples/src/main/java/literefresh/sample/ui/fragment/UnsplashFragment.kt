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
import androidx.core.math.MathUtils
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import literefresh.LiteRefresh
import literefresh.OnRefreshListener
import literefresh.OnScrollListener
import literefresh.behavior.RefreshHeaderBehavior
import literefresh.sample.R
import literefresh.sample.base.model.Resource
import literefresh.sample.base.ui.BaseFragment
import literefresh.sample.databinding.FragmentUnsplashBinding
import literefresh.sample.model.UnsplashPhotoPage
import literefresh.sample.vm.UnsplashViewModel
import layoutbinder.annotations.BindLayout
import literefresh.behavior.Configuration

class UnsplashFragment : BaseFragment() {

    @BindLayout(value = R.layout.fragment_unsplash)
    lateinit var binding : FragmentUnsplashBinding

    private val unsplashViewModel: UnsplashViewModel by activityViewModels()

    var triggerOffset = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imagePagerHeader.setFragmentManager(childFragmentManager)
        val fragment = childFragmentManager.findFragmentById(R.id.fragment) as UnsplashPhotoListFragment?
        val headerBehavior = LiteRefresh.getHeaderBehavior(binding!!.scaleableHeader)
                .with(requireContext())
                .maxOffsetRatio(R.fraction.percent_100p.toFloat())
                .triggerOffsetRes(R.dimen.unsplash_fragment_trigger_offset)
                .visibleHeightRatioRes(R.fraction.percent_100)
                .config<RefreshHeaderBehavior<*>>()
        unsplashViewModel!!.randomPhotosResult.observe(viewLifecycleOwner, Observer<Resource<UnsplashPhotoPage>> { listResource ->
            if (null == listResource) return@Observer
            if (listResource.data == null) return@Observer
            if (listResource.isSuccess) {
                if (listResource.data.isFirstPage) {
                    val photos = listResource.data.photos
                    if (photos == null || photos.isEmpty()) {
                        fragment!!.refreshError(Exception("Empty data."))
                    } else {
                        if (photos.size > 3) {
                            binding!!.imagePagerHeader.setImages(photos.subList(0, 3))
                            fragment!!.setPayloads(photos.subList(3, photos.size))
                        } else {
                            binding!!.imagePagerHeader.setImages(photos.subList(0, 1))
                            if (photos.size > 1) {
                                fragment!!.setPayloads(photos.subList(1, photos.size))
                            }
                        }
                    }
                } else {
                    fragment!!.addPayloads(listResource.data.photos)
                }
                headerBehavior?.refreshComplete()
            } else if (listResource.isError) {
                if (listResource.data.isFirstPage) {
                    fragment!!.refreshError(listResource.throwable)
                }
                headerBehavior?.refreshError(listResource.throwable)
            }
        })
        headerBehavior?.addOnScrollListener(object : OnScrollListener {
            override fun onStartScroll(
                parent: CoordinatorLayout,
                view: View,
                config: Configuration,
                type: Int
            ) {
                triggerOffset = trigger - initial
            }

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
                val offset = current - initial.toFloat()
                val triggerRange = trigger - initial.toFloat()
                val progress = offset / (trigger - initial)
                binding!!.loadingView.setProgress(progress)
                binding!!.loadingView.translationY = MathUtils.clamp(offset, 0f, triggerRange)
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
                binding!!.loadingView.startProgress()
            }

            override fun onReleaseToRefresh() {
                binding!!.loadingView.translationY = triggerOffset.toFloat()
                binding!!.loadingView.readyToLoad()
            }

            override fun onRefresh() {
                binding!!.loadingView.startLoading()
                firstPage()
            }

            override fun onRefreshEnd(throwable: Throwable?) {
                binding!!.loadingView.finishLoading()
            }
        })
        if (unsplashViewModel!!.randomPhotosResult.value == null) {
            headerBehavior.refresh()
        }
    }

    private fun firstPage() {
        unsplashViewModel!!.firstPage()
    }

    companion object {
        fun newInstance(): UnsplashFragment {
            val args = Bundle()
            val fragment = UnsplashFragment()
            fragment.arguments = args
            return fragment
        }
    }
}