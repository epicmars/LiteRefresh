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

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import literefresh.LiteRefresh
import literefresh.OnRefreshListener
import literefresh.OnScrollListener
import literefresh.behavior.RefreshContentBehavior
import literefresh.behavior.RefreshHeaderBehavior
import literefresh.sample.R
import literefresh.sample.base.model.Resource
import literefresh.sample.base.ui.BaseFragment
import literefresh.sample.databinding.FragmentCollapsibleHeaderBinding
import literefresh.sample.model.UnsplashPhotoPage
import literefresh.sample.vm.UnsplashViewModel
import layoutbinder.annotations.BindLayout

class CollapsibleHeaderFragment : BaseFragment() {
    val unsplashViewModel: UnsplashViewModel by activityViewModels()
    @BindLayout(value = R.layout.fragment_collapsible_header)
    lateinit var binding: FragmentCollapsibleHeaderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val headerBehavior = LiteRefresh.getAttachedBehavior<RefreshHeaderBehavior<*>>(binding!!.viewHeader)
        val contentBehavior = LiteRefresh.getAttachedBehavior<RefreshContentBehavior<*>>(binding!!.viewContent)
        binding!!.circleProgress.setColor(resources.getColor(R.color.colorAccent))
        binding!!.imagePagerHeader.setFragmentManager(childFragmentManager)
        val fragment = childFragmentManager.findFragmentById(R.id.fragment) as UnsplashPhotoGridFragment?
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
                headerBehavior!!.refreshComplete()
            } else if (listResource.isError) {
                if (listResource.data.isFirstPage) {
                    fragment!!.refreshError(listResource.throwable)
                }
                headerBehavior!!.refreshError(listResource.throwable)
            }
        })
        if (headerBehavior != null) {
            contentBehavior.addOnScrollListener(object : OnScrollListener {
                var drawable = ColorDrawable(Color.BLACK)
                override fun onStartScroll(parent: CoordinatorLayout, view: View, initial: Int, trigger: Int, min: Int, max: Int, type: Int) {}
                override fun onPreScroll(parent: CoordinatorLayout, view: View, current: Int, initial: Int, trigger: Int, min: Int, max: Int, type: Int) {

                }

                override fun onScroll(parent: CoordinatorLayout, view: View, current: Int, delta: Int, initial: Int, trigger: Int, min: Int, max: Int, type: Int) {
                    // set header's translation
                    if (current <= initial) {
                        val y = initial - current.toFloat()
                        binding!!.viewHeader.translationY = y / 2
                        val alpha = 1 - current.toFloat() / initial
                        drawable.alpha = (alpha * 196).toInt()
                        binding!!.viewHeader.foreground = drawable
                    }

                    // set progress
                    binding!!.circleProgress.setProgress(Math.max(0f, current.toFloat() / trigger))

                    // set appbar's translation
                    if (current >= min) {
                        val rangeMax = initial - min.toFloat()
                        val distance = current - min.toFloat()
                        val alpha = 1 - distance / rangeMax
                        binding!!.appBar.alpha = alpha
                        binding!!.appBar.translationY = alpha * min
                    }
                }

                override fun onStopScroll(parent: CoordinatorLayout, view: View, current: Int, initial: Int, trigger: Int, min: Int, max: Int, type: Int) {}
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
                    unsplashViewModel!!.firstPage()
                }

                override fun onRefreshEnd(throwable: Throwable?) {
                    binding!!.circleProgress.stopLoading()
                }
            })
        }
        if (unsplashViewModel!!.randomPhotosResult.value == null) {
            headerBehavior!!.refresh()
        }
    }

    companion object {
        fun newInstance(): CollapsibleHeaderFragment {
            val args = Bundle()
            val fragment = CollapsibleHeaderFragment()
            fragment.arguments = args
            return fragment
        }
    }
}