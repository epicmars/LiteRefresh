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
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.androidpi.literefresh.core.R;
import com.androidpi.literefresh.core.controller.VerticalIndicatorBehaviorController;

import java.util.List;

/**
 * Super class of header and footer behavior.
 * <p>
 * The header and footer behaviors are almost the same, the primary difference is that
 * they have different coordinate system when we trace the bottom and top position
 * of the view to which they attached respectively.
 * <p>
 * As we have record the bottom and top offset of the view within the view's default coordinate
 * system, whose original point is the left top point of the parent view.
 * <p>
 * Now we need to trace how much the header has scroll from the top of the parent view.
 * We need to transform the bottom position of the header view from coordinate system of the parent
 * view to another one. This would be a affine matrix transformation below:
 * <p>
 * <pre>
 *      |1 0 height||x|   |         x|
 *      |0 1 height||y| = |y + height|
 *      |0 0      1||1|   |         1|
 * </pre>
 * <p>
 * We also need to trace how much the footer view has scrolled from the bottom of the parent
 * view, we use top position of the view as the traced position, the coordinate system would be a
 * affine transformation below:
 * <p>
 * <pre>
 *      |1   0              0||x|   |                x|
 *      |0   -1  parentHeight||y| = |-y + parentHeight|
 *      |0   0              1||1|   |                1|
 * </pre>
 * <p>
 */
public abstract class VerticalIndicatorBehavior<V extends View, CTR extends VerticalIndicatorBehaviorController>
        extends AnimationOffsetBehavior<V, CTR> {

    private final int defaultMinTriggerRange;

    public VerticalIndicatorBehavior(Context context) {
        this(context, null);
    }

    public VerticalIndicatorBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.OffsetBehavior, 0, 0);
        if (a.hasValue(R.styleable.OffsetBehavior_lr_visibleHeight)) {
            configuration.setVisibleHeight(Math.round(a.getDimension(
                    R.styleable.OffsetBehavior_lr_visibleHeight, 0)));
        }
        if (a.hasValue(R.styleable.OffsetBehavior_lr_visibleHeightRatio)) {
            configuration.setVisibleHeightRatio(a.getFraction(
                    R.styleable.OffsetBehavior_lr_visibleHeightRatio, 1, 1, 0f));
            configuration.setVisibleHeightParentRatio(a.getFraction(
                    R.styleable.OffsetBehavior_lr_visibleHeightRatio, 1, 2, 0f));
        }
        if (a.hasValue(R.styleable.OffsetBehavior_lr_showUpWhenRefresh)) {
            configuration.setShowUpWhenRefresh(a.getBoolean(
                    R.styleable.OffsetBehavior_lr_showUpWhenRefresh, false));
        }

        configuration.setUseDefinedRefreshTriggerRange(a.hasValue(
                R.styleable.OffsetBehavior_lr_triggerOffset));
        configuration.setRefreshTriggerRange(a.getDimensionPixelOffset(
                R.styleable.OffsetBehavior_lr_triggerOffset, 0));
        a.recycle();
        defaultMinTriggerRange = context.getResources().getDimensionPixelOffset(
                R.dimen.defaultMinTriggerRange);
    }

    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, V child, int parentWidthMeasureSpec,
                                  int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        boolean handled = super.onMeasureChild(parent, child, parentWidthMeasureSpec, widthUsed,
                parentHeightMeasureSpec, heightUsed);
        return handled;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        boolean handled = super.onLayoutChild(parent, child, layoutDirection);
        if (configuration.getHeight() != child.getHeight()) {
            configuration.setHeight(child.getHeight());
            configuration.setSettled(false);
        }
        // Compute visible height of child.
        int visibleHeight = (int) Math.max((float) configuration.getVisibleHeight(),
                configuration.getVisibleHeightParentRatio()
                        > configuration.getVisibleHeightRatio()
                        ? configuration.getVisibleHeightRatio() * parent.getHeight()
                        : configuration.getVisibleHeightRatio() * child.getHeight());
        int invisibleHeight = child.getHeight() - visibleHeight;
        // Compute refresh trigger range.
        if (configuration.getRefreshTriggerRange() < 0) {
            // User define a invalid trigger range, use the default.
            configuration.setRefreshTriggerRange(configuration.getDefaultRefreshTriggerRange());
        } else if (!configuration.isUseDefinedRefreshTriggerRange()) {
            // User doesn't predefined one, we need to ensure the refreshing is triggered when
            // indicator is totally visible, no matter whether child height is zero or not.
            // If child is already visible, the invisible height will be non-positive, in this
            // case we use the default .
            if (defaultMinTriggerRange > 0 && invisibleHeight >= defaultMinTriggerRange
                    && invisibleHeight <= configuration.getDefaultRefreshTriggerRange()) {
                configuration.setRefreshTriggerRange(invisibleHeight);
            } else {
                configuration.setRefreshTriggerRange(Math.max(invisibleHeight,
                        configuration.getDefaultRefreshTriggerRange()));
            }
        } // Otherwise we use predefined trigger range.
        configuration.setVisibleHeight(visibleHeight);
        configuration.setInvisibleHeight(invisibleHeight);
        return handled;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull V child, @NonNull View directTargetChild,
                                       @NonNull View target, int axes, int type) {
        boolean start = (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        if (start) {
            for (ScrollingListener l : mListeners) {
                l.onStartScroll(coordinatorLayout, child,
                        getInitialOffset(coordinatorLayout, child),
                        getRefreshTriggerOffset(coordinatorLayout, child),
                        getMinOffset(coordinatorLayout, child), getMaxOffset(coordinatorLayout, child), type);
            }
        }
        return start;
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child,
                                   @NonNull View target, int type) {
        for (ScrollingListener l : mListeners) {
            l.onStopScroll(coordinatorLayout, child, controller.transformOffsetCoordinate(
                    coordinatorLayout, child, this, getTopAndBottomOffset()),
                    getInitialOffset(coordinatorLayout, child),
                    getRefreshTriggerOffset(coordinatorLayout, child),
                    getMinOffset(coordinatorLayout, child), getMaxOffset(coordinatorLayout, child), type);
        }
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, V child, View dependency) {
        CoordinatorLayout.LayoutParams lp =
                (CoordinatorLayout.LayoutParams) dependency.getLayoutParams();
        if (null != lp) {
            CoordinatorLayout.Behavior behavior = lp.getBehavior();
            return behavior instanceof ScrollingContentBehavior;
        }
        return false;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, V child, View dependency) {
        CoordinatorLayout.LayoutParams lp =
                (CoordinatorLayout.LayoutParams) dependency.getLayoutParams();
        CoordinatorLayout.Behavior behavior = lp.getBehavior();
        if (behavior instanceof ScrollingContentBehavior) {
            ScrollingContentBehavior contentBehavior = (ScrollingContentBehavior) behavior;
            if (isLayedOut()) {
                return onDependenceOffsetChanged(parent, child, dependency, contentBehavior,
                        this);
            } else {
                runWithView(new Runnable() {
                    @Override
                    public void run() {
                        onDependenceOffsetChanged(parent, child, dependency, contentBehavior,
                                VerticalIndicatorBehavior.this);
                    }
                });
            }
        }
        return false;
    }

    private boolean isLayedOut() {
        return getParent() != null && getChild() != null;
    }

    private boolean onDependenceOffsetChanged(CoordinatorLayout parent, V child, View dependency,
                                              ScrollingContentBehavior contentBehavior,
                                              VerticalIndicatorBehavior behavior) {
        final int offsetDelta = controller.computeOffsetDeltaOnDependentViewChanged(parent, child,
                dependency, this, contentBehavior);
        if (offsetDelta != 0) {
            consumeOffsetOnDependentViewChanged(parent, child, contentBehavior, offsetDelta,
                    TYPE_UNKNOWN);
            return true;
        }
        return false;
    }

    private void consumeOffsetOnDependentViewChanged(CoordinatorLayout coordinatorLayout,
                                                     View child,
                                                     ScrollingContentBehavior contentBehavior,
                                                     int offsetDelta, int type) {
        int currentOffset = getTopAndBottomOffset();
        int height = child.getHeight();
        // Before child consume the offset.
        for (ScrollingListener l : mListeners) {
            l.onPreScroll(coordinatorLayout, child, controller.transformOffsetCoordinate(
                    coordinatorLayout, child, this, currentOffset
                    ),
                    getInitialOffset(coordinatorLayout, child),
                    getRefreshTriggerOffset(coordinatorLayout, child),
                    getMinOffset(coordinatorLayout, child), height, type);
        }
        float consumed = controller.consumeOffsetOnDependentViewChanged(coordinatorLayout, child,
                this, contentBehavior, currentOffset, offsetDelta);
        currentOffset = Math.round(currentOffset + consumed);
        // If the offset is already at the top don't reset it again.
        setTopAndBottomOffset(currentOffset);
        for (ScrollingListener l : mListeners) {
            l.onScroll(coordinatorLayout, child, controller.transformOffsetCoordinate(
                    coordinatorLayout, child, this, currentOffset
                    ),
                    offsetDelta, getInitialOffset(coordinatorLayout, child),
                    getRefreshTriggerOffset(coordinatorLayout, child),
                    getMinOffset(coordinatorLayout, child),
                    getMaxOffset(coordinatorLayout, child),
                    type);
        }
    }

    private View findDependencyChild(CoordinatorLayout parent, View child) {
        if (parent == null || child == null)
            return null;
        List<View> dependencies = parent.getDependencies(child);
        if (dependencies.isEmpty())
            return null;
        for (View v : dependencies) {
            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) v.getLayoutParams();
            if (p.getBehavior() instanceof ScrollingContentBehavior) {
                return v;
            }
        }
        return null;
    }

    private ScrollingContentBehavior findDependencyBehavior(CoordinatorLayout parent, View child) {
        if (parent == null || child == null)
            return null;
        List<View> dependencies = parent.getDependencies(child);
        if (dependencies.isEmpty())
            return null;
        for (View v : dependencies) {
            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) v.getLayoutParams();
            if (p.getBehavior() instanceof ScrollingContentBehavior) {
                return (ScrollingContentBehavior) p.getBehavior();
            }
        }
        return null;
    }

    protected ScrollingContentBehavior getContentBehavior(CoordinatorLayout parent, View child) {
        return findDependencyBehavior(parent, child);
    }

    protected abstract int getInitialOffset(@NonNull CoordinatorLayout parent, @NonNull View child);

    protected abstract int getRefreshTriggerOffset(@NonNull CoordinatorLayout parent, @NonNull View child);

    protected abstract int getMinOffset(@NonNull CoordinatorLayout parent, @NonNull View child);

    protected abstract int getMaxOffset(@NonNull CoordinatorLayout parent, @NonNull View child);
}
