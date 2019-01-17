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

import android.view.View;

import com.androidpi.literefresh.sample.R;
import com.androidpi.literefresh.sample.base.ui.BaseViewHolder;
import com.androidpi.literefresh.sample.base.ui.BindLayout;
import com.androidpi.literefresh.sample.databinding.ViewHolderLoadingBinding;
import com.androidpi.literefresh.sample.model.LoadingItem;

@BindLayout(value = R.layout.view_holder_loading, dataTypes = LoadingItem.class)
public class LoadingViewHolder extends BaseViewHolder<ViewHolderLoadingBinding> {

    public LoadingViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBind(Object data, int position) {

    }
}
