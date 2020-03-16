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
package literefresh.behavior;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import literefresh.R;
import literefresh.controller.VerticalIndicatorBehaviorController;

import java.util.List;

import static androidx.core.view.ViewCompat.TYPE_TOUCH;

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
public abstract class VerticalIndicatorBehavior<V extends View>
        extends IndicatorBehavior<V> {
    private final String TAG = "VerticalIndicator";
    private final int defaultMinTriggerOffset;
    private ScrollingContentBehavior scrollingContentBehavior;
    private View dependency;

    public VerticalIndicatorBehavior(Context context) {
        this(context, null);
    }

    public VerticalIndicatorBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.OffsetBehavior, 0, 0);
        if (a.hasValue(R.styleable.OffsetBehavior_lr_visibleHeight)) {
            getConfig().setVisibleHeight(Math.round(a.getDimension(
                    R.styleable.OffsetBehavior_lr_visibleHeight, 0)));
        }
        if (a.hasValue(R.styleable.OffsetBehavior_lr_visibleHeightRatio)) {
            getConfig().setVisibleHeightRatio(a.getFraction(
                    R.styleable.OffsetBehavior_lr_visibleHeightRatio, 1, 1, 0f));
            getConfig().setVisibleHeightRatioOfParent(a.getFraction(
                    R.styleable.OffsetBehavior_lr_visibleHeightRatio, 1, 2, 0f));
        }
        if (a.hasValue(R.styleable.OffsetBehavior_lr_showUpWhenRefresh)) {
            getConfig().setShowUpWhenRefresh(a.getBoolean(
                    R.styleable.OffsetBehavior_lr_showUpWhenRefresh, false));
        }

        getConfig().setUseDefinedTriggerOffset(a.hasValue(
                R.styleable.OffsetBehavior_lr_triggerOffset));
        getConfig().setTriggerOffset(a.getDimensionPixelOffset(
                R.styleable.OffsetBehavior_lr_triggerOffset, 0));
        a.recycle();
        defaultMinTriggerOffset = context.getResources().getDimensionPixelOffset(
                R.dimen.lr_default_min_trigger_offset);
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
        // Compute visible height of child.
        final int visibleHeight = (int) Math.max((float) getConfig().getVisibleHeight(),
                getConfig().getVisibleHeightRatioOfParent()
                        > getConfig().getVisibleHeightRatio()
                        ? getConfig().getVisibleHeightRatio() * parent.getHeight()
                        : getConfig().getVisibleHeightRatio() * child.getHeight());
        final int invisibleHeight = child.getHeight() - visibleHeight;
        getConfig().setVisibleHeight(visibleHeight);
        getConfig().setInvisibleHeight(invisibleHeight);

        // Compute refresh trigger offset.
        if (getConfig().getTriggerOffset() < 0) {
            // User define a invalid trigger offset, use the default.
            getConfig().setTriggerOffset(getConfig().getDefaultTriggerOffset());
        } else if (!getConfig().isUseDefinedTriggerOffset()) {
            // User doesn't predefined one, we need to ensure the refreshing is triggered when
            // indicator is totally visible, no matter whether child height is zero or not.
            // If child is already visible, the invisible height will be non-positive, in this
            // case we use the default .
            if (defaultMinTriggerOffset > 0 && invisibleHeight >= defaultMinTriggerOffset
                    && invisibleHeight <= getConfig().getDefaultTriggerOffset()) {
                getConfig().setTriggerOffset(invisibleHeight);
            } else {
                getConfig().setTriggerOffset(Math.max(invisibleHeight,
                        getConfig().getDefaultTriggerOffset()));
            }
        } // Otherwise we use predefined trigger offset.
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
            l.onStopScroll(coordinatorLayout, child, getController().transformOffsetCoordinate(
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
    public boolean onDependentViewChanged(final CoordinatorLayout parent, final V child, final View dependency) {
        CoordinatorLayout.LayoutParams lp =
                (CoordinatorLayout.LayoutParams) dependency.getLayoutParams();
        CoordinatorLayout.Behavior behavior = lp.getBehavior();
        if (behavior instanceof ScrollingContentBehavior) {
            final ScrollingContentBehavior contentBehavior = (ScrollingContentBehavior) behavior;
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
        final int offsetDelta = getController().computeOffsetDeltaOnDependentViewChanged(parent, child,
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
            l.onPreScroll(coordinatorLayout, child, getController().transformOffsetCoordinate(
                    coordinatorLayout, child, this, currentOffset
                    ),
                    getInitialOffset(coordinatorLayout, child),
                    getRefreshTriggerOffset(coordinatorLayout, child),
                    getMinOffset(coordinatorLayout, child), height, type);
        }
        float consumed = getController().consumeOffsetOnDependentViewChanged(coordinatorLayout, child,
                this, contentBehavior, currentOffset, offsetDelta);
        currentOffset = Math.round(currentOffset + consumed);
        // If the offset is already at the top don't reset it again.
        setTopAndBottomOffset(currentOffset);
        for (ScrollingListener l : mListeners) {
            l.onScroll(coordinatorLayout, child, getController().transformOffsetCoordinate(
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

    @Override
    public IndicatorConfiguration getConfig() {
        return (IndicatorConfiguration) super.getConfig();
    }

    @Override
    IndicatorConfiguration.Builder newConfigBuilder() {
        return new IndicatorConfiguration.Builder(getConfig());
    }

    @Override
    public IndicatorConfiguration.Builder with(@NonNull Context context) {
        return new IndicatorConfiguration.Builder(context, this, getConfig());
    }

    @Override
    public VerticalIndicatorBehaviorController getController() {
        return (VerticalIndicatorBehaviorController) super.getController();
    }

    @Override
    public int getCurrentTopBottomOffset() {
        if (ensureScrollingContentBehavior()) {
            return scrollingContentBehavior.getTopAndBottomOffset();
        }
        return 0;
    }

    @Override
    public int getMinTopBottomOffset() {
        if (ensureScrollingContentBehavior()) {
            return scrollingContentBehavior.getConfig().getMinOffset();
        }
        return 0;
    }

    @Override
    public int getMaxTopBottomOffset() {
        if (ensureScrollingContentBehavior()) {
            return scrollingContentBehavior.getConfig().getMaxOffset();
        }
        return 0;
    }

    @Override
    public void updateTopBottomOffset(CoordinatorLayout parent, View child, int offset, int delta) {
        scrollingContentBehavior.updateTopAndBottomOffset(offset, delta, TYPE_TOUCH);
    }

    @Override
    public void onScrollStart(CoordinatorLayout parent, V layout) {
        if (ensureScrollingContentBehavior() && ensureDependencyView()) {
            scrollingContentBehavior.dispatchStartScroll(parent, dependency, TYPE_TOUCH);
        }
    }

    @Override
    public void onScrollStop(CoordinatorLayout parent, V layout) {
        if (ensureScrollingContentBehavior() && ensureDependencyView()) {
            scrollingContentBehavior.dispatchStopScroll(parent, dependency, TYPE_TOUCH);
        }
    }

    @Override
    public void onFlingStart(CoordinatorLayout parent, V layout) {
        if (ensureScrollingContentBehavior() && ensureDependencyView()) {
            scrollingContentBehavior.dispatchStartScroll(parent, dependency, TYPE_TOUCH);
        }
    }

    @Override
    public void onFlingFinished(CoordinatorLayout parent, V layout) {
        if (ensureScrollingContentBehavior() && ensureDependencyView()) {
            scrollingContentBehavior.dispatchStopScroll(parent, dependency, TYPE_TOUCH);
        }
    }

    public boolean ensureScrollingContentBehavior() {
        if (scrollingContentBehavior == null) {
            scrollingContentBehavior = findDependencyBehavior(getParent(), getChild());
        }
        return scrollingContentBehavior != null;
    }

    public boolean ensureDependencyView() {
        if (dependency == null) {
            dependency = findDependencyChild(getParent(), getChild());
        }
        return dependency != null;
    }

    protected abstract int getInitialOffset(@NonNull CoordinatorLayout parent, @NonNull View child);

    protected abstract int getRefreshTriggerOffset(@NonNull CoordinatorLayout parent, @NonNull View child);

    protected abstract int getMinOffset(@NonNull CoordinatorLayout parent, @NonNull View child);

    protected abstract int getMaxOffset(@NonNull CoordinatorLayout parent, @NonNull View child);
}
