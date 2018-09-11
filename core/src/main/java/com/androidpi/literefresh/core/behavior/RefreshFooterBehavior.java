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
package com.androidpi.literefresh.core.behavior;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.androidpi.literefresh.core.R;
import com.androidpi.literefresh.core.controller.HeaderBehaviorController;
import com.androidpi.literefresh.core.controller.FooterBehaviorController;
import com.androidpi.literefresh.core.Loader;
import com.androidpi.literefresh.core.OnLoadListener;
import com.androidpi.literefresh.core.OnScrollListener;


/**
 * This class is what we use to attach to a so called footer view, and add some nested scrolling
 * features to it.
 *
 * Note that the footer behavior can not work standalone, the footer view to which this behavior is
 * attached must work with a nested scrolling content view that is attached with an
 * {@link RefreshContentBehavior}, otherwise it'll not work.
 *
 * <strong>
 *     The view to which this behavior is attached must be a direct child of {@link CoordinatorLayout}.
 * </strong>
 */

public class RefreshFooterBehavior<V extends View>
        extends VerticalIndicatorBehavior<V, FooterBehaviorController> implements Loader {

    {
        controller = new FooterBehaviorController(this);
        addScrollListener(controller);
        runWithView(new Runnable() {
            @Override
            public void run() {
                ScrollingContentBehavior contentBehavior = getContentBehavior(getParent(), getChild());
                if (contentBehavior != null) {
                    controller.setProxy(contentBehavior.getController());
                }
            }
        });
    }

    public RefreshFooterBehavior(Context context) {
        this(context, null);
    }

    public RefreshFooterBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IndicatorBehavior,
                0, 0);
        if (a.hasValue(R.styleable.IndicatorBehavior_lr_mode)) {
            int mode = a.getInt(R.styleable.IndicatorBehavior_lr_mode, HeaderBehaviorController.MODE_FOLLOW);
            controller.setMode(mode);
        }
        a.recycle();
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        boolean handled = super.onLayoutChild(parent, child, layoutDirection);
        CoordinatorLayout.LayoutParams lp = ((CoordinatorLayout.LayoutParams) child.getLayoutParams());
        // Compute max offset, it will not exceed parent height.
        if (configuration.isUseDefaultMaxOffset()) {
            // We want footer can be just fully visible by default.
            configuration.setMaxOffset(child.getHeight());
        } else {
            configuration.setMaxOffset((int) Math.max(configuration.getMaxOffset(),
                    configuration.getMaxOffsetRatioOfParent()
                            > configuration.getMaxOffsetRatio()
                            ? configuration.getMaxOffsetRatio() * parent.getHeight()
                            : configuration.getMaxOffsetRatio() * child.getHeight()));
        }
        // The height of content may have changed, so does the footer's initial visible height.
        final int lastInitialVisibleHeight = configuration.getInitialVisibleHeight();
        final int currentInitialVisibleHeight = getInitialVisibleHeight(parent, child);
        if (lastInitialVisibleHeight != currentInitialVisibleHeight) {
            configuration.setSettled(false);
        }
        configuration.setInitialVisibleHeight(currentInitialVisibleHeight);
        if (configuration.getInitialVisibleHeight() <= 0) {
            // If initial visible height is non-positive, add the top margin to refresh trigger range.
            configuration.setRefreshTriggerRange(configuration.getRefreshTriggerRange() + lp.topMargin);
        }
        // Maximum offset should not be less than initial visible height.
        configuration.setMaxOffset(Math.max(configuration.getMaxOffset(),
                configuration.getInitialVisibleHeight() + configuration.getRefreshTriggerRange()));
        if (!configuration.isSettled()) {
            configuration.setSettled(true);
            ScrollingContentBehavior contentBehavior = getContentBehavior(parent, child);
            if (contentBehavior != null) {
                contentBehavior.setFooterConfig(configuration);
            }
            setTopAndBottomOffset(-configuration.getVisibleHeight() + parent.getHeight());
        }
        return handled;
    }

    public void addOnScrollListener(OnScrollListener listener) {
        controller.addOnScrollListener(listener);
    }

    @Override
    public void load() {
        controller.load();
    }

    @Override
    public void loadComplete() {
        controller.loadComplete();
    }

    @Override
    public void loadError(Throwable throwable) {
        controller.loadError(throwable);
    }

    public void addOnLoadListener(OnLoadListener listener) {
        controller.addOnLoadListener(listener);
    }

    @Override
    public FooterBehaviorController getController() {
        return super.getController();
    }


    @Override
    protected int getInitialOffset(@NonNull CoordinatorLayout parent, @NonNull View child) {
        return configuration.getVisibleHeight();
    }

    @Override
    protected int getRefreshTriggerOffset(@NonNull CoordinatorLayout parent, @NonNull View child) {
        return configuration.getVisibleHeight() + configuration.getRefreshTriggerRange();
    }

    @Override
    protected int getMinOffset(@NonNull CoordinatorLayout parent, @NonNull View child) {
        return -configuration.getTopMargin();
    }

    @Override
    protected int getMaxOffset(@NonNull CoordinatorLayout parent, @NonNull View child) {
        ScrollingContentBehavior contentBehavior = getContentBehavior(parent, child);
        return contentBehavior == null
                ? 0
                : contentBehavior.getFooterConfig().getMaxOffset() - configuration.getTopMargin();
    }

    /**
     * The initial visible height is original visible height with vertical margins included.
     * Primarily, it's used as a initial offset by content view to lay itself out and compute
     * some offsets when needed.
     * <p>
     * Notice that there's some differences with the header's initial visible height, that's
     * because we need to adapter some short content views which may make the footer view entirely
     * visible all the time. In that case the footer's refresh state will not work as usual,
     * so we recompute the initial visible height with the header's initial visible height included.
     * </p>
     * This also means that the header should be layout before footer. So we make the header view as
     * a dependency of footer view.
     *
     * @return footer view's initial visible height.
     */
    private int getInitialVisibleHeight(@NonNull CoordinatorLayout parent, @NonNull View child) {
        int initialVisibleHeight;
        if (configuration.getHeight() <= 0 || configuration.getVisibleHeight() <= 0) {
            initialVisibleHeight = configuration.getVisibleHeight();
        } else if (configuration.getVisibleHeight() >= child.getHeight()) {
            initialVisibleHeight = configuration.getVisibleHeight()
                    + configuration.getTopMargin() + configuration.getBottomMargin();
        } else {
            initialVisibleHeight = configuration.getVisibleHeight() + configuration.getTopMargin();
        }
        // If header configuration is not settled when footer is in layout, we would see
        // header's initial visible height is zero then we get footer's initial visible height that
        // fill the parent, after that when we compute a right initial visible height that is smaller,
        // it will not be set.
        ScrollingContentBehavior contentBehavior = getContentBehavior(parent, child);
        // If content is too short, there may be extra space left.
        if (contentBehavior == null
                || getParent().getHeight() == 0
                || contentBehavior.getConfiguration().getHeight() == 0) {
            return initialVisibleHeight;
        } else {
            return Math.max(initialVisibleHeight, getParent().getHeight()
                    - getParent().getPaddingTop()
                    - getParent().getPaddingBottom()
                    - contentBehavior.getConfiguration().getHeight()
                    - contentBehavior.getConfiguration().getTopMargin()
                    - contentBehavior.getConfiguration().getBottomMargin()
                    - contentBehavior.getHeaderConfig().getInitialVisibleHeight());
        }
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, V child, View dependency) {
        // We want footer layout after content and header.
        CoordinatorLayout.LayoutParams lp =
                (CoordinatorLayout.LayoutParams) dependency.getLayoutParams();
        if (null != lp) {
            CoordinatorLayout.Behavior behavior = lp.getBehavior();
            return behavior instanceof ScrollingContentBehavior || behavior instanceof RefreshHeaderBehavior;
        }
        return false;
    }
}
