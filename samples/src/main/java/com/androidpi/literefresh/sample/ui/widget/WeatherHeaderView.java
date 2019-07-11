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
import android.util.AttributeSet;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.androidpi.literefresh.LiteRefreshHelper;
import com.androidpi.literefresh.OnRefreshListener;
import com.androidpi.literefresh.OnScrollListener;
import com.androidpi.literefresh.behavior.RefreshHeaderBehavior;
import com.androidpi.literefresh.sample.R;
import com.androidpi.literefresh.sample.utils.UiUtils;
import com.androidpi.literefresh.widget.LoadingView;
import com.androidpi.literefresh.widget.RefreshHeaderLayout;

public class WeatherHeaderView extends RefreshHeaderLayout implements OnScrollListener, OnRefreshListener {

    private LoadingView loadingView;
    private float offset;

    public WeatherHeaderView(Context context) {
        this(context, null);
    }

    public WeatherHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeatherHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.view_weather_header, this);
        loadingView = findViewById(R.id.loading_view);
        offset = UiUtils.getStatusBarHeight(context);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        RefreshHeaderBehavior behavior = LiteRefreshHelper.getAttachedBehavior(this);
        if (behavior != null) {
            behavior.addOnRefreshListener(this);
            behavior.addOnScrollListener(this);
        }
    }

    @Override
    public void onStartScroll(CoordinatorLayout parent, View view, int initial, int trigger, int min, int max, int type) {

    }

    @Override
    public void onScroll(CoordinatorLayout parent, View view, int current, int delta, int initial, int trigger, int min, int max, int type) {
        float height = getHeight();
        if (current > offset) {
            float progress = (current - offset) / height;
            loadingView.setProgress(progress);
        }
    }

    @Override
    public void onStopScroll(CoordinatorLayout parent, View view, int current, int initial, int trigger, int min, int max, int type) {

    }

    @Override
    public void onRefreshStart() {
        loadingView.startProgress();
    }

    @Override
    public void onReleaseToRefresh() {
        loadingView.readyToLoad();
    }

    @Override
    public void onRefresh() {
        loadingView.startLoading();
    }

    @Override
    public void onRefreshEnd(Throwable throwable) {
        loadingView.finishLoading();
    }
}
