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
package com.androidpi.literefresh.sample.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.androidpi.literefresh.sample.R
import com.androidpi.literefresh.sample.base.ui.BaseFragment
import com.androidpi.literefresh.sample.base.ui.RecyclerAdapter
import com.androidpi.literefresh.sample.databinding.FragmentUnsplashPhotoGridBinding
import com.androidpi.literefresh.sample.model.ErrorItem
import com.androidpi.literefresh.sample.ui.viewholder.ErrorViewHolder
import com.androidpi.literefresh.sample.ui.viewholder.UnsplashPhotoGridViewHolder
import layoutbinder.annotations.BindLayout

class UnsplashPhotoGridFragment : BaseFragment() {

    @BindLayout(R.layout.fragment_unsplash_photo_grid)
    lateinit var binding: FragmentUnsplashPhotoGridBinding

    var adapter: RecyclerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = RecyclerAdapter()
        adapter!!.register(UnsplashPhotoGridViewHolder::class.java, ErrorViewHolder::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding!!.recyclerView.layoutManager = layoutManager
        binding!!.recyclerView.adapter = adapter
    }

    fun setPayloads(payloads: Collection<Any>?) {
        adapter!!.setPayloads(payloads)
    }

    fun addPayloads(payloads: Collection<Any>?) {
        adapter!!.addPayloads(payloads)
    }

    fun refreshError(throwable: Throwable?) {
        adapter!!.setPayload(ErrorItem(throwable!!.message))
    }
}