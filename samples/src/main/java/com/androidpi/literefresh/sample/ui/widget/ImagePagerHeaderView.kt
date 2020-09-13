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

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.androidpi.literefresh.sample.R
import com.androidpi.literefresh.sample.ui.adapter.ImageHeaderPagerAdapter
import me.relex.circleindicator.CircleIndicator

class ImagePagerHeaderView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : FrameLayout(context, attrs, defStyle) {
    lateinit var viewPager: ViewPager
    private val circleIndicator: CircleIndicator
    private var pagerAdapter: ImageHeaderPagerAdapter? = null
    private var autoFlip = false
    private var autoFlipStarted = false
    val hdl = Handler()

    fun setFragmentManager(fm: FragmentManager?) {
        pagerAdapter = ImageHeaderPagerAdapter(fm)
        viewPager.adapter = pagerAdapter
        circleIndicator.setViewPager(viewPager)
        autoFlip()
    }

    fun setImages(photos: List<*>?) {
        if (pagerAdapter != null) {
            pagerAdapter!!.setPhotos(photos)
            circleIndicator.setViewPager(viewPager)
        }
    }

    private val autoFlipTask: Runnable = object : Runnable {
        override fun run() {
            if (!autoFlip) {
                return
            }
            var next = viewPager.currentItem + 1
            val total = pagerAdapter!!.count
            if (next >= total) {
                next = 0
            }
            viewPager.setCurrentItem(next, true)
            hdl.postDelayed(this, 5000L)
        }
    }

    fun autoFlip() {
        if (autoFlipStarted) return
        autoFlipStarted = true
        hdl.postDelayed(autoFlipTask, 3000L)
    }

    fun finishAutoFlip() {
        autoFlip = false
        autoFlipStarted = false
        hdl.removeCallbacks(autoFlipTask)
    }

    override fun onDetachedFromWindow() {
        finishAutoFlip()
        super.onDetachedFromWindow()
    }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ImagePagerHeaderView, 0, 0)
        autoFlip = a.getBoolean(R.styleable.ImagePagerHeaderView_auto_flip, false)
        a.recycle()
        View.inflate(context, R.layout.view_image_pager, this)
        viewPager = findViewById(R.id.image_view_pager)
        circleIndicator = findViewById(R.id.circle_indicator)
    }
}