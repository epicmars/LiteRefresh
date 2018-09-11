package com.androidpi.literefresh.sample.demos.weather.ui;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.androidpi.literefresh.core.LiteRefreshHelper;
import com.androidpi.literefresh.core.OnRefreshListener;
import com.androidpi.literefresh.core.OnScrollListener;
import com.androidpi.literefresh.core.behavior.RefreshHeaderBehavior;
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
