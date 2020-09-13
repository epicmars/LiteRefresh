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
package com.androidpi.literefresh.sample.ui.viewholder

import android.content.Intent
import android.net.Uri
import android.view.View
import com.androidpi.literefresh.sample.R
import com.androidpi.literefresh.sample.base.ui.BaseViewHolder
import com.androidpi.literefresh.sample.base.ui.ViewBinder
import com.androidpi.literefresh.sample.common.image.GlideApp
import com.androidpi.literefresh.sample.databinding.ViewHolderNewsBinding
import com.androidpi.literefresh.sample.model.News
import com.androidpi.literefresh.sample.ui.HtmlActivity
import layoutbinder.annotations.BindLayout


@ViewBinder(value = R.layout.view_holder_news, dataTypes = [News::class])
class NewsViewHolder(itemView: View) : BaseViewHolder<ViewHolderNewsBinding>(itemView) {

    override fun <T : Any?> onBind(data: T, position: Int) {
        val news = data as? News
        binding?.tvTitle?.text = news?.title
        binding?.tvPublishTime?.text = news?.publishedAt

        if (news != null && news.urlToImage != null) {
            binding?.ivImage?.visibility = View.VISIBLE
            binding?.ivImage?.let { GlideApp.with(itemView).load(news.urlToImage).into(it) }
        }

        itemView.setOnClickListener {
            v: View? ->
            val action = Intent(HtmlActivity.ACTION_VIEW, Uri.parse(news?.url))
            if (action.resolveActivity(itemView.context.packageManager) != null) {
                v?.context?.startActivity(action)
            }
        }
    }

    override fun onViewRecycled() {
        super.onViewRecycled()
        itemView.setOnClickListener(null)
        binding?.ivImage?.visibility = View.GONE
        binding?.ivImage?.let { GlideApp.with(itemView).clear(it) }
    }
}