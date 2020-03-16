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
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import literefresh.sample.R

class CircularLoadingView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : AppCompatImageView(context, attrs, defStyle) {
    private val drawable: CircularProgressDrawable
    private var isStyleReset = true
    fun setProgress(progress: Float) {
        drawable.setStartEndTrim(progress, 0f)
    }

    fun resetStyle() {
        drawable.setStyle(CircularProgressDrawable.DEFAULT)
        isStyleReset = true
    }

    fun fillCircle() {
        setProgress(1f)
        drawable.strokeWidth = drawable.strokeWidth + drawable.centerRadius
        drawable.centerRadius = 0.1f
        isStyleReset = false
    }

    fun startLoading() {
        if (!isStyleReset) {
            resetStyle()
        }
        drawable.start()
    }

    fun stopLoading() {
        drawable.stop()
        if (!isStyleReset) {
            resetStyle()
        }
        setProgress(1f)
    }

    fun setColor(color: Int) {
        drawable.setColorSchemeColors(color)
    }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.CircularLoadingView, 0, 0)
        val color = a.getColor(R.styleable.CircularLoadingView_lr_circle_color, Color.WHITE)
        a.recycle()
        drawable = CircularProgressDrawable(getContext())
        drawable.setStyle(CircularProgressDrawable.DEFAULT)
        drawable.setColorSchemeColors(color)
        setImageDrawable(drawable)
    }
}