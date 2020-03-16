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
package literefresh.sample.ui.viewholder

import android.view.View
import androidx.fragment.app.Fragment
import literefresh.sample.R
import literefresh.sample.base.ui.BaseViewHolder
import literefresh.sample.base.ui.FragmentFactory
import literefresh.sample.base.ui.ViewBinder
import literefresh.sample.databinding.ViewHolderLiteRefreshSampleBinding
import literefresh.sample.model.LiteRefreshSample
import literefresh.sample.ui.TemplateActivity.Companion.startWith

@ViewBinder(value = R.layout.view_holder_lite_refresh_sample, dataTypes = [LiteRefreshSample::class])
class LiteRefreshSampleViewHolder(itemView: View?) : BaseViewHolder<ViewHolderLiteRefreshSampleBinding>(itemView) {
    override fun <T> onBind(data: T, position: Int) {
        if (data is LiteRefreshSample) {
            val liteRefreshSample = data as LiteRefreshSample
            binding!!.tvTitle.text = liteRefreshSample.title
            binding!!.tvDescription.text = liteRefreshSample.description
            itemView.setOnClickListener { v: View? ->
                startWith(itemView.context, 0, liteRefreshSample.fragmentClass.name,
                        object : FragmentFactory<Fragment>() {
                            override fun create(): Fragment {
                                return Fragment.instantiate(itemView.context, liteRefreshSample.fragmentClass.name)
                            }
                        })
            }
        }
    }
}