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
package literefresh.sample.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import literefresh.LiteRefresh
import literefresh.OnRefreshListener
import literefresh.OnScrollListener
import literefresh.behavior.RefreshHeaderBehavior
import literefresh.sample.R
import literefresh.sample.utils.UiUtils
import literefresh.widget.LoadingView
import literefresh.widget.RefreshHeaderLayout

class WeatherHeaderView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : RefreshHeaderLayout(context, attrs, defStyle), OnScrollListener, OnRefreshListener {
    private val loadingView: LoadingView
    private val offset: Float
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val behavior = LiteRefresh.getAttachedBehavior<RefreshHeaderBehavior<*>>(this)
        if (behavior != null) {
            behavior.addOnRefreshListener(this)
            behavior.addOnScrollListener(this)
        }
    }

    override fun onStartScroll(parent: CoordinatorLayout, view: View, initial: Int, trigger: Int, min: Int, max: Int, type: Int) {}
    override fun onPreScroll(parent: CoordinatorLayout, view: View, current: Int, initial: Int, trigger: Int, min: Int, max: Int, type: Int) {

    }

    override fun onScroll(parent: CoordinatorLayout, view: View, current: Int, delta: Int, initial: Int, trigger: Int, min: Int, max: Int, type: Int) {
        val height = height.toFloat()
        if (current > offset) {
            val progress = (current - offset) / height
            loadingView.setProgress(progress)
        }
    }

    override fun onStopScroll(parent: CoordinatorLayout, view: View, current: Int, initial: Int, trigger: Int, min: Int, max: Int, type: Int) {}
    override fun onRefreshStart() {
        loadingView.startProgress()
    }

    override fun onReleaseToRefresh() {
        loadingView.readyToLoad()
    }

    override fun onRefresh() {
        loadingView.startLoading()
    }

    override fun onRefreshEnd(throwable: Throwable?) {
        loadingView.finishLoading()
    }

    init {
        View.inflate(context, R.layout.view_weather_header, this)
        loadingView = findViewById(R.id.loading_view)
        offset = UiUtils.getStatusBarHeight(context).toFloat()
    }
}