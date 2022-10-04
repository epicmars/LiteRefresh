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
package literefresh.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Interpolator
import literefresh.*
import literefresh.animator.ViscousFluidInterpolator
import literefresh.controller.ScrollableBehaviorController

/**
 * This class is what we use to attach to an nested scrolling view, add new scrolling features to it.
 *
 *
 * This behavior is the pivot of header and footer behavior, without it the other behaviors can not
 * work. All the offset and state changes come from it, so it can be used standalone.
 *
 *
 * For now, the nested scrolling view supported by
 * [androidx.coordinatorlayout.widget.CoordinatorLayout] are
 * [androidx.core.widget.NestedScrollView],
 * [androidx.recyclerview.widget.RecyclerView]
 * which implement [androidx.core.view.NestedScrollingChild].
 *
 *
 * Use other parent view to wrap these scrollable child is OK, cause the
 * [android.view.ViewGroup] and [View] already implement the nested scrolling event
 * dispatch contract, but the nested scrolling child itself must exist in the view hierarchy.
 *
 *
 * **
 * The view to which this behavior is attached must be a direct child of
 * [androidx.coordinatorlayout.widget.CoordinatorLayout].
 ** *
 */
class ContentScrollableBehavior<V : View?> @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null
) : ScrollableBehavior<V>(context, attrs), Refresher, Loader {
    fun addOnScrollListener(listener: OnScrollListener?) {
        controller.addOnScrollListener(listener)
    }

    fun addOnRefreshListener(listener: OnRefreshListener?) {
        controller.addOnRefreshListener(listener)
    }

    fun addOnLoadListener(listener: OnLoadListener?) {
        controller.addOnLoadListener(listener)
    }

    fun recycle() {
        controller.recycle()
    }

    override fun refresh() {
        controller.refresh()
    }

    override fun refreshComplete() {
        controller.refreshComplete()
    }

    override fun refreshError(throwable: Throwable?) {
        controller.refreshError(throwable)
    }

    override fun load() {
        controller.load()
    }

    override fun loadComplete() {
        controller.loadComplete()
    }

    override fun loadError(throwable: Throwable) {
        controller.loadError(throwable)
    }

    private var accumulator = 0f
    private val scrollDownInterpolator: Interpolator = ViscousFluidInterpolator()
    override fun onConsumeOffset(current: Int, delta: Int): Float {
        var consumed = delta.toFloat()
//        if (current >= 0 && delta > 0) {
//            val y = scrollDownInterpolator.getInterpolation(
//                current / config.topEdgeConfig.getMaxOffset()!!
//                    .toFloat()
//            )
//            consumed = (1f - y) * delta
//            if (consumed < 0.5) {
//                accumulator += 0.2.toFloat()
//                if (accumulator >= 1) {
//                    consumed += 1f
//                    accumulator = 0f
//                }
//            }
//        }
        return consumed
    }

    init {
        addScrollListener(ScrollableBehaviorController(this).also { controller = it })
    }
}