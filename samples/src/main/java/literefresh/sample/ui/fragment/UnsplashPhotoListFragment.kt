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
import androidx.recyclerview.widget.LinearLayoutManager
import literefresh.sample.R
import literefresh.sample.base.ui.BaseFragment
import literefresh.sample.base.ui.RecyclerAdapter
import literefresh.sample.databinding.FragmentUnsplashPhotoListBinding
import literefresh.sample.model.ErrorItem
import literefresh.sample.ui.viewholder.ErrorViewHolder
import literefresh.sample.ui.viewholder.UnsplashPhotoListViewHolder
import layoutbinder.annotations.BindLayout

class UnsplashPhotoListFragment : BaseFragment() {

    @BindLayout(R.layout.fragment_unsplash_photo_list)
    lateinit var binding: FragmentUnsplashPhotoListBinding
    private var adapter: RecyclerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = RecyclerAdapter()
        adapter!!.register(UnsplashPhotoListViewHolder::class.java, ErrorViewHolder::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.recyclerView.layoutManager = LinearLayoutManager(context)
        binding!!.recyclerView.adapter = adapter
    }

    fun refreshError(throwable: Throwable?) {
        adapter!!.setPayload(ErrorItem(throwable!!.message))
    }

    fun setPayloads(payloads: Collection<Any>?) {
        adapter!!.setPayloads(payloads)
    }

    fun addPayloads(payloads: Collection<Any>?) {
        adapter!!.addPayloads(payloads)
    }

    companion object {
        fun newInstance(): UnsplashPhotoListFragment {
            val args = Bundle()
            val fragment = UnsplashPhotoListFragment()
            fragment.arguments = args
            return fragment
        }
    }
}