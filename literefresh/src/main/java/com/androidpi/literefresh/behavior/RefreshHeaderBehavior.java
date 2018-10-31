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
package com.androidpi.literefresh.behavior;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.androidpi.literefresh.OnRefreshListener;
import com.androidpi.literefresh.OnScrollListener;
import com.androidpi.literefresh.R;
import com.androidpi.literefresh.Refresher;
import com.androidpi.literefresh.controller.HeaderBehaviorController;

/**
 * This class is used to attach to a header view and add nested scrolling features to it.
 *
 * Note that this behavior can not work standalone, the header view to which this behavior is
 * attached must work with a nested scrolling content view that is attached with an
 * {@link RefreshContentBehavior}, otherwise it'll not work.
 *
 * <strong>
 *     The view to which this behavior is attached must be a direct child of {@link CoordinatorLayout}.
 * </strong>
 */

public class RefreshHeaderBehavior<V extends View>
        extends VerticalIndicatorBehavior<V> implements Refresher {

    {
        addScrollListener(controller = new HeaderBehaviorController(this));
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

    public RefreshHeaderBehavior(Context context) {
        this(context, null);
    }

    public RefreshHeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.IndicatorBehavior, 0, 0);
        if (a.hasValue(R.styleable.IndicatorBehavior_lr_mode)) {
            int mode = a.getInt(
                    R.styleable.IndicatorBehavior_lr_mode, HeaderBehaviorController.MODE_FOLLOW);
            getController().setMode(mode);
        }
        a.recycle();
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        boolean handled = super.onLayoutChild(parent, child, layoutDirection);
        CoordinatorLayout.LayoutParams lp =
                ((CoordinatorLayout.LayoutParams) child.getLayoutParams());
        final int initialVisibleHeight = getInitialVisibleHeight();
        getConfiguration().setInitialVisibleHeight(initialVisibleHeight);
        // If initial visible height is non-positive, add the bottom margin to refresh trigger range.
        int triggerOffset = getConfiguration().getTriggerOffset();
        if (initialVisibleHeight <= 0) {
            triggerOffset += lp.bottomMargin;
        }
        getConfiguration().setTriggerOffset(triggerOffset);
        // Config maximum offset.
        configMaxOffset(parent, child, initialVisibleHeight, triggerOffset);
        if (!configuration.isSettled()) {
            configuration.setSettled(true);
            ScrollingContentBehavior contentBehavior = getContentBehavior(parent, child);
            if (contentBehavior != null) {
                contentBehavior.setHeaderConfig(getConfiguration());
            }
            // The header's height may have changed, it can occur in such a situation when you set
            // adjustViewBound to true in a image view's layout attributes and then load image async.
            setTopAndBottomOffset(-getConfiguration().getInvisibleHeight());
        }
        return handled;
    }

    private void configMaxOffset(CoordinatorLayout parent, V child,
                                 int initialVisibleHeight, int triggerOffset) {
        int currentMaxOffset = configuration.getMaxOffset();
        if (configuration.isUseDefaultMaxOffset()) {
            // We want child can be fully visible by default.
            currentMaxOffset = (int) Math.max(GOLDEN_RATIO * parent.getHeight(),
                    child.getHeight());
        } else {
            currentMaxOffset = (int) Math.max(currentMaxOffset,
                    configuration.getMaxOffsetRatioOfParent()
                            > configuration.getMaxOffsetRatio()
                            ? configuration.getMaxOffsetRatio() * parent.getHeight()
                            : configuration.getMaxOffsetRatio() * child.getHeight());
        }
        currentMaxOffset = Math.max(currentMaxOffset, initialVisibleHeight + triggerOffset);
        getConfiguration().setMaxOffset(currentMaxOffset);
    }

    public void addOnScrollListener(OnScrollListener listener) {
        controller.addOnScrollListener(listener);
    }

    public void addOnRefreshListener(OnRefreshListener listener) {
        controller.addOnRefreshListener(listener);
    }

    @Override
    public void refresh() {
        controller.refresh();
    }

    @Override
    public void refreshComplete() {
        controller.refreshComplete();
    }

    @Override
    public void refreshError(Throwable throwable) {
        controller.refreshError(throwable);
    }

    @Override
    protected int getInitialOffset(@NonNull CoordinatorLayout parent, @NonNull View child) {
        return getConfiguration().getVisibleHeight();
    }

    @Override
    protected int getRefreshTriggerOffset(@NonNull CoordinatorLayout parent, @NonNull View child) {
        return getConfiguration().getVisibleHeight() + getConfiguration().getTriggerOffset();
    }

    @Override
    protected int getMinOffset(@NonNull CoordinatorLayout parent, @NonNull View child) {
        ContentConfiguration contentConfig = getContentBehavior(parent, child).getConfiguration();
        return contentConfig.getMinOffset() - configuration.getBottomMargin();
    }

    @Override
    protected int getMaxOffset(@NonNull CoordinatorLayout parent, @NonNull View child) {
        ContentConfiguration contentConfig = getContentBehavior(parent, child).getConfiguration();
        return contentConfig.getMaxOffset() -
                (contentConfig.getMaxOffset() > configuration.getBottomMargin()
                        ? configuration.getBottomMargin()
                        : 0);
    }

    /**
     * The initial visible height is original visible height with vertical margins included.
     * Primarily, it's used as a initial offset by content view to lay itself out and compute
     * some offsets when needed.
     *
     * @return header view's initial visible height.
     */
    private int getInitialVisibleHeight() {
        int initialVisibleHeight;
        if (configuration.getHeight() <= 0 || getConfiguration().getVisibleHeight() <= 0) {
            initialVisibleHeight = getConfiguration().getVisibleHeight();
        } else if (getConfiguration().getVisibleHeight() >= configuration.getHeight()) {
            initialVisibleHeight = getConfiguration().getVisibleHeight() + configuration.getTopMargin()
                    + configuration.getBottomMargin();
        } else {
            initialVisibleHeight = getConfiguration().getVisibleHeight() + configuration.getBottomMargin();
        }
        return initialVisibleHeight;
    }

}
