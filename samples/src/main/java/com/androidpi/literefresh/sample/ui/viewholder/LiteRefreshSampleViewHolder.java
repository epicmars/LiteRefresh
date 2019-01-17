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
package com.androidpi.literefresh.sample.ui.viewholder;

import android.support.v4.app.Fragment;
import android.view.View;

import com.androidpi.literefresh.sample.R;
import com.androidpi.literefresh.sample.base.ui.BaseViewHolder;
import com.androidpi.literefresh.sample.base.ui.BindLayout;
import com.androidpi.literefresh.sample.base.ui.FragmentFactory;
import com.androidpi.literefresh.sample.databinding.ViewHolderLiteRefreshSampleBinding;
import com.androidpi.literefresh.sample.model.LiteRefreshSample;
import com.androidpi.literefresh.sample.ui.TemplateActivity;

@BindLayout(value = R.layout.view_holder_lite_refresh_sample, dataTypes = LiteRefreshSample.class)
public class LiteRefreshSampleViewHolder extends BaseViewHolder<ViewHolderLiteRefreshSampleBinding> {

    public LiteRefreshSampleViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public <T> void onBind(T data, int position) {
        if (data instanceof LiteRefreshSample) {
            LiteRefreshSample liteRefreshSample = (LiteRefreshSample) data;
            binding.tvTitle.setText(liteRefreshSample.getTitle());
            binding.tvDescription.setText(liteRefreshSample.getDescription());

            itemView.setOnClickListener(v -> {
                TemplateActivity.Companion.startWith(itemView.getContext(), 0, liteRefreshSample.getFragmentClass().getName(),
                        new FragmentFactory<Fragment>() {
                            @Override
                            public Fragment create() {
                                return Fragment.instantiate(itemView.getContext(), liteRefreshSample.getFragmentClass().getName());
                            }
                        });
            });
        }
    }
}
