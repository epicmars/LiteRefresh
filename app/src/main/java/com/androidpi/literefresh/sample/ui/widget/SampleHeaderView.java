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
package com.androidpi.literefresh.sample.ui.widget;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.androidpi.literefresh.behavior.RefreshHeaderBehavior;
import com.androidpi.literefresh.sample.R;
import com.androidpi.literefresh.sample.common.image.GlideApp;


public class SampleHeaderView extends ConstraintLayout{
    RefreshHeaderBehavior behavior;

    public SampleHeaderView(Context context) {
        this(context, null);
    }

    public SampleHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SampleHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.view_sample_header, this);
        ImageView bg = findViewById(R.id.iv_bg);
        GlideApp.with(this)
                .load(R.mipmap.photo9)
                .placeholder(R.mipmap.photo9)
                .into(bg);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (behavior == null) {
            try {
                CoordinatorLayout.LayoutParams params = ((CoordinatorLayout.LayoutParams) getLayoutParams());
                params.setBehavior(behavior = new RefreshHeaderBehavior(getContext()));
                behavior.with(getContext())
                        .visibleHeightRatioRes(R.fraction.sample_header_visible_ratio)
                        .config();
            } catch (ClassCastException e) {

            }
        }
    }
}
