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
package com.androidpi.literefresh.sample.ui.widget

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.androidpi.literefresh.sample.R
import com.google.android.material.appbar.AppBarLayout

class SampleToolbar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : AppBarLayout(context, attrs) {
    var toolbar: Toolbar?

    private fun getActivity(context: Context?): Activity? {
        var c: Context? = context
        while (c is ContextWrapper) {
            if (c is Activity) {
                return c
            }
            c = c.baseContext
        }
        return null
    }

    init {
        View.inflate(context, R.layout.sample_toolbar, this)
        val a = context.obtainStyledAttributes(attrs, R.styleable.SampleToolbar, 0, 0)
        val bgColor = a.getColor(R.styleable.SampleToolbar_toolbar_background, resources.getColor(R.color.transparent))
        val title = a.getString(R.styleable.SampleToolbar_toolbar_title)
        a.recycle()
        toolbar = findViewById(R.id.toolbar)
        setBackgroundColor(bgColor)
        toolbar?.title = title
        toolbar?.setNavigationOnClickListener {
            val activity = getActivity(context)
            if (activity is Activity) {
                activity.finish()
            } else {
                Toast.makeText(activity, "Navigation button clicked.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}