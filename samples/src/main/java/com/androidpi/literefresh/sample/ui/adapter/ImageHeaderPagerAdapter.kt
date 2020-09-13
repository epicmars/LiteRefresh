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
package com.androidpi.literefresh.sample.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.androidpi.literefresh.sample.data.remote.dto.ResTrendingPage
import com.androidpi.literefresh.sample.data.remote.dto.ResUnsplashPhoto
import com.androidpi.literefresh.sample.ui.fragment.ImageHeaderFragment
import com.androidpi.literefresh.sample.ui.fragment.TempFragment.Companion.newInstance
import com.androidpi.literefresh.sample.ui.fragment.TrendingImageHeaderFragment
import java.util.*

class ImageHeaderPagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm!!) {
    private val mPhotos: MutableList<Any> = ArrayList()
    override fun getItem(position: Int): Fragment {
        val photo = mPhotos[position]
        return (if (photo is ResUnsplashPhoto) {
            ImageHeaderFragment.Companion.newInstance(photo.urls?.small)
        } else if (photo is ResTrendingPage.ResultsBean) {
            TrendingImageHeaderFragment.Companion.newInstance(photo.posterPath)
        } else {
            newInstance("")
        }) as Fragment
    }

    override fun getCount(): Int {
        return mPhotos.size
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    fun setPhotos(photos: Collection<*>?) {
        if (photos == null || photos.isEmpty()) return
        mPhotos.clear()
        mPhotos.addAll(photos as Collection<Any>)
        notifyDataSetChanged()
    }
}