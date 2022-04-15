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
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import literefresh.LiteRefresh
import literefresh.OnLoadListener
import literefresh.OnRefreshListener
import literefresh.OnScrollListener
import literefresh.behavior.RefreshFooterBehavior
import literefresh.behavior.RefreshHeaderBehavior
import literefresh.sample.R
import literefresh.sample.base.model.Resource
import literefresh.sample.base.ui.BaseFragment
import literefresh.sample.base.ui.FragmentFactory
import literefresh.sample.common.image.GlideApp
import literefresh.sample.databinding.FragmentPartialVisibleHeaderBinding
import literefresh.sample.model.UnsplashPhotoPage
import literefresh.sample.ui.TemplateActivity.Companion.startWith
import literefresh.sample.vm.UnsplashViewModel
import layoutbinder.annotations.BindLayout
import literefresh.behavior.Configuration

class PartialVisibleHeaderFragment : BaseFragment() {

    @BindLayout(R.layout.fragment_partial_visible_header)
    lateinit var binding: FragmentPartialVisibleHeaderBinding
    val unsplashViewModel: UnsplashViewModel by activityViewModels()
    var photoListFragment: UnsplashPhotoListFragment? = null
    private var isLaunched = false
    var headerBehavior: RefreshHeaderBehavior<*>? = null
    var footerBehavior: RefreshFooterBehavior<*>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GlideApp.with(view).load(R.mipmap.photo4).into(binding.ivPhoto)
        headerBehavior = LiteRefresh.getAttachedBehavior(binding.viewHeader)
        footerBehavior = LiteRefresh.getAttachedBehavior(binding.viewFooter)
        photoListFragment = childFragmentManager.findFragmentById(R.id.fragment_list) as UnsplashPhotoListFragment?
        unsplashViewModel.randomPhotosResult.observe(viewLifecycleOwner, Observer<Resource<UnsplashPhotoPage>> { listResource ->
            if (listResource == null) return@Observer
            val page = listResource.data ?: return@Observer
            if (listResource.isSuccess) {
                headerBehavior?.refreshComplete()
                if (page.isFirstPage) {
                    photoListFragment!!.setPayloads(page.photos)
                } else {
                    photoListFragment!!.addPayloads(page.photos)
                }
            } else if (listResource.isError) {
                headerBehavior?.refreshError(listResource.throwable)
                if (page.isFirstPage) {
                    photoListFragment!!.refreshError(listResource.throwable)
                }
            }
        })
        if (headerBehavior != null) {
            headerBehavior?.addOnScrollListener(object : OnScrollListener {
                override fun onStartScroll(
                    parent: CoordinatorLayout,
                    view: View,
                    config: Configuration,
                    type: Int
                ) {
//                    binding.circleProgress.setVisibility(View.VISIBLE);
//                    circularProgressDrawable.start();
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
                    if (current >= view.height * 0.8f) {
                        if (!isLaunched) {
                            isLaunched = true
                            view.postDelayed({
                                val sharedElementName = resources.getString(R.string.transition_header)
                                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!, view, sharedElementName)
                                startWith(options, context!!, 0, ImageFragment::class.java.name, object : FragmentFactory<Fragment>() {
                                    override fun create(): Fragment {
                                        return ImageFragment.Companion.newInstance(null as String?)
                                    }
                                })
                            }, 300L)
                        }
                        return
                    }
                    if (current >= headerBehavior!!.getConfig()!!.visibleHeight) {
                        val distance = current - initial.toFloat()
                        val triggerRange = trigger - initial.toFloat()
                        binding.circleProgress.setProgress(distance / triggerRange)
                    } else {
                        binding.circleProgress.setProgress(0f)
                    }
                }

                override fun onStopScroll(
                    parent: CoordinatorLayout,
                    view: View,
                    config: Configuration,
                    type: Int
                ) {}
            })
            headerBehavior?.addOnRefreshListener(object : OnRefreshListener {
                override fun onRefreshStart() {
                    binding.circleProgress.visibility = View.VISIBLE
                    binding.circleProgress.resetStyle()
                }

                override fun onReleaseToRefresh() {
                    binding.circleProgress.fillCircle()
                }

                override fun onRefresh() {
                    binding.circleProgress.startLoading()
                    if (!isLaunched) {
                        unsplashViewModel!!.firstPage()
                    } else {
                        headerBehavior?.refreshComplete()
                    }
                }

                override fun onRefreshEnd(throwable: Throwable?) {
                    binding.circleProgress.stopLoading()
                    binding.circleProgress.visibility = View.GONE
                }
            })
        }
        if (footerBehavior != null) {
            footerBehavior?.addOnScrollListener(object : OnScrollListener {
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
                    binding.footerCircleProgress.setProgress(Math.max(0f, current.toFloat() / trigger))
                }

                override fun onStopScroll(
                    parent: CoordinatorLayout,
                    view: View,
                    config: Configuration,
                    type: Int
                ) {}
            })
            footerBehavior?.addOnLoadListener(object : OnLoadListener {
                override fun onLoadStart() {
                    binding.footerCircleProgress.visibility = View.VISIBLE
                    binding.footerTvMessage.visibility = View.GONE
                    binding.footerCircleProgress.resetStyle()
                }

                override fun onReleaseToLoad() {
                    binding.footerCircleProgress.fillCircle()
                }

                override fun onLoad() {
                    binding.footerCircleProgress.startLoading()
                    binding.footerCircleProgress.postDelayed({ footerBehavior?.loadComplete() }, 2000L)
                }

                override fun onLoadComplete(throwable: Throwable?) {
                    binding.footerCircleProgress.stopLoading()
                    binding.footerCircleProgress.visibility = View.GONE
                    binding.footerTvMessage.visibility = View.VISIBLE
                    binding.footerTvMessage.text = "Mocked loading complete."
                }
            })
        }
        if (unsplashViewModel!!.randomPhotosResult.value == null) {
            headerBehavior?.refresh()
        }
    }

    private fun firstPage() {
        unsplashViewModel!!.firstPage()
    }

    override fun onResume() {
        super.onResume()
        isLaunched = false
    }
}