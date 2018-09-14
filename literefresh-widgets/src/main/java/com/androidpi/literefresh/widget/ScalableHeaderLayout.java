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
package com.androidpi.literefresh.widget;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.androidpi.literefresh.OnScrollListener;
import com.androidpi.literefresh.behavior.RefreshHeaderBehavior;

/**
 * A simple scalable header layout.
 *
 * Generally you should use this layout as a parent view.
 *
 * If you implement a custom view that extends this view directly
 * and add a {@link OnScrollListener} to it's
 * attached behavior, the order of the added listeners may be out of order.
 * In that case when you make scale or translation to your extended view, it may be conflict with
 * the view property changes that have been made here.
 *
 */
public class ScalableHeaderLayout extends RefreshHeaderLayout implements OnScrollListener {

    public ScalableHeaderLayout(Context context) {
        this(context, null);
    }

    public ScalableHeaderLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScalableHeaderLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        behavior.addOnScrollListener(this);
    }

    @Override
    public void onStartScroll(CoordinatorLayout parent, View view, int initial, int trigger, int min, int max, int type) {

    }

    @Override
    public void onScroll(CoordinatorLayout parent, View view, int current, int delta, int initial, int trigger, int min, int max, int type) {
        int height = getHeight();
        if (current <= height) {
            // Because the view can scroll down and then back. And it will not always reach a position
            // where current offset equals to height exactly so that the scale and translation can be reset.
            // Therefore we need to reset it to original scale and translation manually, especially when scroll back.
            setScaleX(1f);
            setScaleY(1f);
            setTranslationY(0f);
            return;
        }
        if (current > height) {
            float scale = Math.max(current / (float) height, 1f);
            setScaleX(scale);
            setScaleY(scale);
            setTranslationY(-(scale - 1f) * height / 2f);
        }
    }

    @Override
    public void onStopScroll(CoordinatorLayout parent, View view, int current, int initial, int trigger, int min, int max, int type) {

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) getLayoutParams();
        RefreshHeaderBehavior behavior = (RefreshHeaderBehavior) params.getBehavior();
        if (behavior != null) {
            // todo : remove scroll listener
        }
    }
}
