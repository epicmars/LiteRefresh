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
import literefresh.sample.R
import literefresh.sample.base.ui.BaseViewHolder
import literefresh.sample.base.ui.ViewBinder
import literefresh.sample.databinding.ViewHolderErrorBinding
import literefresh.sample.model.ErrorItem


@ViewBinder(value = R.layout.view_holder_error, dataTypes = [ErrorItem::class])
class ErrorViewHolder(itemView: View) : BaseViewHolder<ViewHolderErrorBinding>(itemView) {

    override fun <T : Any?> onBind(data: T, position: Int) {
        if (data is ErrorItem) {
            binding?.tvMessage?.text = data.message
        }
    }
}