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
package literefresh.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.ImageViewCompat;

import literefresh.LiteRefresh;
import literefresh.OnRefreshListener;
import literefresh.OnScrollListener;
import literefresh.behavior.Configuration;
import literefresh.behavior.HeaderRefreshBehavior;

public class RefreshingHeaderView extends RefreshHeaderLayout implements OnScrollListener, OnRefreshListener {

    private TextView tvState;
    private LoadingView loadingView;
    private ImageView ivArrow;
    private ObjectAnimator rotateUpAnimator;
    private ObjectAnimator rotateDownAnimator;
    private int gravity = Gravity.CENTER;

    public RefreshingHeaderView(Context context) {
        this(context, null);
    }

    public RefreshingHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshingHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.view_refreshing_header, this);

        tvState = findViewById(R.id.tv_state);
        ivArrow = findViewById(R.id.iv_arrow);
        ImageViewCompat.setImageTintList(ivArrow, ColorStateList.valueOf(getResources().getColor(R.color.lr_color_gray)));
        loadingView = findViewById(R.id.loading_view);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        HeaderRefreshBehavior behavior = LiteRefresh.getAttachedBehavior(this);
        if (behavior != null) {
            behavior.addOnRefreshListener(this);
            behavior.addOnScrollListener(this);
        }
    }

    @Override
    public void onStartScroll(CoordinatorLayout parent, View view, Configuration config, int type) {

    }

    @Override
    public void onPreScroll(@NonNull CoordinatorLayout parent, @NonNull View view, Configuration config, int type) {

    }

    @Override
    public void onScroll(CoordinatorLayout parent, View view, Configuration config, int delta, int type) {

    }

    @Override
    public void onStopScroll(CoordinatorLayout parent, View view, Configuration config, int type) {

    }

    @Override
    public void onRefreshStart() {
        loadingView.setVisibility(GONE);
        ivArrow.setVisibility(VISIBLE);
        rotateDown();
        tvState.setText(R.string.refresh_start);
    }

    @Override
    public void onReleaseToRefresh() {
        loadingView.setVisibility(GONE);
        ivArrow.setVisibility(VISIBLE);
        rotateUp();
        tvState.setText(R.string.release_to_refresh);
    }

    @Override
    public void onRefresh() {
        loadingView.setVisibility(VISIBLE);
        ivArrow.setVisibility(GONE);
        tvState.setText(R.string.refreshing);
    }

    @Override
    public void onRefreshComplete(Throwable throwable) {
        tvState.setText(R.string.refresh_complete);
    }

    private void rotateUp() {
        if (rotateUpAnimator == null) {
            rotateUpAnimator = ObjectAnimator.ofFloat(ivArrow, "rotation", 0, 180);
            rotateUpAnimator.setDuration(100L);
        }
        rotateUpAnimator.start();
    }

    private void rotateDown() {
        if (rotateDownAnimator == null) {
            rotateDownAnimator = ObjectAnimator.ofFloat(ivArrow, "rotation", 180, 0);
            rotateDownAnimator.setDuration(100L);
        }
        rotateDownAnimator.start();
    }
}
