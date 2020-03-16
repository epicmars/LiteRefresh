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
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import literefresh.behavior.AnimationOffsetBehavior
import literefresh.behavior.IndicatorConfiguration
import literefresh.behavior.RefreshHeaderBehavior
import literefresh.sample.R
import literefresh.sample.common.image.GlideApp

class SampleHeaderView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyle: Int = 0) : ConstraintLayout(context, attrs, defStyle) {
    var behavior: RefreshHeaderBehavior<*>? = null
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (behavior == null) {
            try {
                val params = layoutParams as CoordinatorLayout.LayoutParams
                params.behavior = RefreshHeaderBehavior<View>(context).also { behavior = it }
                behavior!!.with(context)
                        .maxOffsetRatio(1.0f)
                        .visibleHeightRatioRes(R.fraction.percent_50)
                        .setFollowMode(IndicatorConfiguration.MODE_FOLLOW_DOWN)
                        .config<AnimationOffsetBehavior<*>>()
            } catch (e: ClassCastException) {
                // ignore
            }
        }
    }

    init {
        View.inflate(context, R.layout.view_sample_header, this)
        val bg = findViewById<ImageView>(R.id.iv_bg)
        GlideApp.with(this)
                .load(R.mipmap.photo9)
                .placeholder(R.mipmap.photo9)
                .into(bg)
    }
}