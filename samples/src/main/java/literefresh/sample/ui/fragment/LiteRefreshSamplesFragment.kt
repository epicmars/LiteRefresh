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
import literefresh.sample.LiteRefreshSamples.samples
import literefresh.sample.R
import literefresh.sample.base.ui.BaseFragment
import literefresh.sample.base.ui.RecyclerAdapter
import literefresh.sample.databinding.FragmentLiteRefreshSamplesBinding
import literefresh.sample.ui.viewholder.LiteRefreshSampleViewHolder
import layoutbinder.annotations.BindLayout

class LiteRefreshSamplesFragment : BaseFragment() {

    @BindLayout(R.layout.fragment_lite_refresh_samples)
    lateinit var binding : FragmentLiteRefreshSamplesBinding

    private var adapter: RecyclerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = RecyclerAdapter()
        adapter!!.register(LiteRefreshSampleViewHolder::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.recyclerView.adapter = adapter
        adapter!!.setPayloads(samples)
    }

    companion object {
        fun newInstance(): LiteRefreshSamplesFragment {
            val args = Bundle()
            val fragment = LiteRefreshSamplesFragment()
            fragment.arguments = args
            return fragment
        }
    }
}