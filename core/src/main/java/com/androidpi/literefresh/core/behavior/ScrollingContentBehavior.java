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
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.math.MathUtils;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.androidpi.literefresh.core.R;
import com.androidpi.literefresh.core.controller.ContentBehaviorController;

import static android.support.v4.view.ViewCompat.TYPE_NON_TOUCH;
import static android.support.v4.view.ViewCompat.TYPE_TOUCH;

/**
 * A behavior for nested scrollable child of {@link CoordinatorLayout}.
 * <p>
 * It's attach to the nested scrolling target view, such as
 * {@link android.support.v4.widget.NestedScrollView}, {@link android.support.v7.widget.RecyclerView}
 * which implement {@link android.support.v4.view.NestedScrollingChild}.
 *
 * <p>
 * View with this behavior must be a direct child of {@link CoordinatorLayout}.
 * <p>
 */
public class ScrollingContentBehavior<V extends View> extends AnimationOffsetBehavior<V, ContentBehaviorController> {

    private static final int INVALID_OFFSET = Integer.MIN_VALUE;
    private BehaviorConfiguration headerConfig;
    private BehaviorConfiguration footerConfig;
    private int initialOffset = INVALID_OFFSET;

    {
        controller = new ContentBehaviorController(this);
        addScrollListener(controller);
    }

    public ScrollingContentBehavior(Context context) {
        this(context, null);
    }

    public ScrollingContentBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (headerConfig == null) {
            headerConfig = createIndicatorConfig();
        }
        if (footerConfig == null) {
            footerConfig = createIndicatorConfig();
            footerConfig.setUseDefaultMaxOffset(true);
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ContentBehavior,
                0, 0);
        boolean hasMinOffset = a.hasValue(R.styleable.ContentBehavior_lr_minOffset);
        if (hasMinOffset) {
            configuration.setMinOffset(a.getDimensionPixelOffset(
                    R.styleable.ContentBehavior_lr_minOffset, 0));
            configuration.setCachedMinOffset(configuration.getMinOffset());
        }

        boolean hasMinOffsetRatio = a.hasValue(R.styleable.ContentBehavior_lr_minOffsetRatio);
        if (hasMinOffsetRatio) {
            configuration.setMinOffsetRatio(a.getFraction(
                    R.styleable.ContentBehavior_lr_minOffsetRatio, 1, 1, 0f));
            configuration.setMinOffsetRatioOfParent(a.getFraction(
                    R.styleable.ContentBehavior_lr_minOffsetRatio, 1, 2, 0f));
        }
        if (!hasMinOffset && !hasMinOffsetRatio) {
            configuration.setUseDefaultMinOffset(true);
        }
        if (a.hasValue(R.styleable.ContentBehavior_lr_headerVisibleHeight)) {
            headerConfig.setVisibleHeight(a.getDimensionPixelOffset(
                    R.styleable.ContentBehavior_lr_headerVisibleHeight, 0));
            headerConfig.setInitialVisibleHeight(getHeaderInitialVisibleHeight());
        }
        a.recycle();
    }

    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, V child, int parentWidthMeasureSpec,
                                  int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        boolean handled = super.onMeasureChild(parent, child, parentWidthMeasureSpec, widthUsed,
                parentHeightMeasureSpec, heightUsed);
        // The minimum offset is used to limit the content view offset.
        // Besides, the minimum offset and header's visible height are used to reset content.
        // We must make sure after resetting, either it's top reaches the minimum offset or
        // header visible height, it depends on which one is larger. When do the layout, we set
        // minimum offset to be always less than or equal to header visible height.
        int height = parent.getMeasuredHeight() - configuration.getMinOffset();
        int heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
        parent.onMeasureChild(child, parentWidthMeasureSpec, widthUsed, heightSpec, heightUsed);
        return handled;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        boolean handled = super.onLayoutChild(parent, child, layoutDirection);
        // If child view's height have changed.
        if (configuration.getHeight() != child.getHeight()) {
            configuration.setHeight(child.getHeight());
            configuration.setSettled(false);
        }
        // Header and footer's configurations must be performed before the content's,
        // and header's configuration before footer.

        // Config footer.
        configFooter(parent);

        // Compute max offset, it will not exceed parent height.
        configMaxOffset(parent, child);

        // Compute content view's minimum offset.
        configMinOffset(parent, child);

        if (!configuration.isSettled() || !headerConfig.isSettled() || !footerConfig.isSettled()) {
            // If we have set a minimum offset but header's initial visible height is smaller,
            // than use it as the minimum offset.
            configuration.setMinOffset(Math.min(configuration.getMinOffset(),
                    headerConfig.getInitialVisibleHeight()));
            // We have set a minimum offset now, relayout.
            child.requestLayout();

            configuration.setSettled(true);
            headerConfig.setSettled(true);
            footerConfig.setSettled(true);
            cancelAnimation();
            if (initialOffset != INVALID_OFFSET) {
                setContentTopAndBottomOffset(parent, child, initialOffset, TYPE_NON_TOUCH);
            } else {
                setTopAndBottomOffset(headerConfig.getInitialVisibleHeight());
            }
        }
        return handled;
    }

    private void configMaxOffset(CoordinatorLayout parent, V child) {
        if (configuration.isUseDefaultMaxOffset()) {
            configuration.setMaxOffset(
                    (int) Math.max(configuration.getMaxOffset(),
                            GOLDEN_RATIO * parent.getHeight()));
            // If header has set a maximum offset and it's positive, then use it.
            if (!headerConfig.isUseDefaultMaxOffset() && headerConfig.getMaxOffset() > 0) {
                configuration.setMaxOffset(headerConfig.getMaxOffset());
            }
        } else {
            configuration.setMaxOffset(
                    (int) Math.max(configuration.getMaxOffset(),
                            configuration.getMaxOffsetRatioOfParent()
                                    > configuration.getMaxOffsetRatio()
                                    ? configuration.getMaxOffsetRatio() * parent.getHeight()
                                    : configuration.getMaxOffsetRatio() * child.getHeight()));
        }
        // Content's max offset should be larger than header's, otherwise the refresh trigger
        // range may not be reached.
        configuration.setMaxOffset(
                Math.max(configuration.getMaxOffset(), headerConfig.getMaxOffset()));
    }

    private void configMinOffset(CoordinatorLayout parent, V child) {
        // Default minimum offset is zero.
        if (!configuration.isUseDefaultMinOffset()) {
            // If we have set a minimum offset ratio.
            if (configuration.getMinOffsetRatio() != null
                    && configuration.getMinOffsetRatioOfParent() != null) {
                configuration.setMinOffset((int) Math.max(configuration.getMinOffset(),
                        configuration.getMinOffsetRatioOfParent()
                                > configuration.getMinOffsetRatio()
                                ? configuration.getMinOffsetRatio() * parent.getHeight()
                                : configuration.getMinOffsetRatio() * child.getHeight()));
                // Cache new minimum offset.
                configuration.setCachedMinOffset(configuration.getMinOffset());
            }
        }

    }

    private void configFooter(CoordinatorLayout parent) {
        // If footer's max offset is not settled yet.
        if (footerConfig.isUseDefaultMaxOffset() && footerConfig.getMaxOffset() <= 0) {
            footerConfig.setMaxOffset((int) ((1 - GOLDEN_RATIO) * parent.getHeight()));
        }
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull V child, @NonNull View directTargetChild,
                                       @NonNull View target, int axes, int type) {
        // If scrolling started by touch event we need to invalidate restored initial offset.
        if (initialOffset != INVALID_OFFSET && type == TYPE_TOUCH) {
            initialOffset = INVALID_OFFSET;
        }
        boolean start = (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        if (start) {
            for (ScrollingListener l : mListeners) {
                l.onStartScroll(coordinatorLayout, child, headerConfig.getInitialVisibleHeight(),
                        headerConfig.getInitialVisibleHeight()
                                + headerConfig.getRefreshTriggerRange(),
                        configuration.getMinOffset(), configuration.getMaxOffset(), type);
            }
        }
        return start;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child,
                                  @NonNull View target, int dx, int dy, @NonNull int[] consumed,
                                  int type) {
        if (dy > 0) {
            // When scrolling up, compute the top offset which content can reach.
            int top = child.getTop() - configuration.getTopMargin();
            // If already reach minimum offset.
            if (top <= configuration.getMinOffset())
                return;
            int offset = MathUtils.clamp(-dy, configuration.getMinOffset() - top, 0);
            if (offset != 0) {
                consumeOffset(coordinatorLayout, child, offset, type, true);
                consumed[1] = -offset;
            }
        } else if (dy < 0) {
            int bottom = child.getBottom() + configuration.getBottomMargin();
            // If already reach the bottom of parent view.
            if (bottom >= coordinatorLayout.getHeight()) {
                return;
            }
            // When scrolling down, if footer is still visible.
            int offset = MathUtils.clamp(-dy, 0, coordinatorLayout.getHeight() - bottom);
            if (offset != 0) {
                consumeOffset(coordinatorLayout, child, offset, type, true);
                consumed[1] = -offset;
            }
        }
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child,
                               @NonNull View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed, int type) {
        // If there is unconsumed pixels.
        if (dyUnconsumed < 0) {
            // Scrolling down.
            final int top = child.getTop() - configuration.getTopMargin();
            final int maxOffset = configuration.getMaxOffset();
            // Top position of child can not scroll exceed maximum offset.
            if (top >= maxOffset)
                return;
            int offset = MathUtils.clamp(-dyUnconsumed, 0, maxOffset - top);
            if (offset != 0) {
                if (top >= headerConfig.getInitialVisibleHeight()) {
                    // When header's hidden part is visible, do not consume none touch scroll,
                    // content can scroll to the maximum offset with the touch event only.
                    if (type != TYPE_TOUCH)
                        return;
                    consumeOffset(coordinatorLayout, child, offset, type, false);
                } else {
                    // Header's hidden part is not visible yet.
                    // Recompute the offset so that the top does not exceed header's initial
                    // visible height no matter what type of touch event is.
                    // todo: add overshot feature to make scrolling motion more nature.
                    offset = MathUtils.clamp(-dyUnconsumed, 0,
                            headerConfig.getInitialVisibleHeight() - top);
                    consumeOffset(coordinatorLayout, child, offset, type, true);
                }
            }
        } else if (dyUnconsumed > 0) {
            // Scrolling up.
            final int bottom = child.getBottom() + configuration.getBottomMargin();
            final int footerMaxOffset = coordinatorLayout.getHeight() - footerConfig.getMaxOffset();
            // Can not scroll exceed footer maximum offset.
            if (bottom <= footerMaxOffset)
                return;
            int offset = MathUtils.clamp(-dyUnconsumed, footerMaxOffset - bottom, 0);
            if (offset != 0) {
                if (coordinatorLayout.getHeight() - bottom >= footerConfig.getInitialVisibleHeight()) {
                    // If footer's hidden part is visible, ignore fling too.
                    if (type != TYPE_TOUCH)
                        return;
                    consumeOffset(coordinatorLayout, child, offset, type, false);
                } else {
                    // Footer's hidden part is not visible yet.
                    // Recompute it, so that bottom doesn't exceed footer's initial visible height
                    // no matter what type of touch event is.
                    offset = MathUtils.clamp(-dyUnconsumed,
                            -(footerConfig.getInitialVisibleHeight()
                                    - coordinatorLayout.getHeight() + bottom), 0);
                    consumeOffset(coordinatorLayout, child, offset, type, true);
                }
            }
        }
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child,
                                   @NonNull View target, int type) {
        for (ScrollingListener l : mListeners) {
            l.onStopScroll(coordinatorLayout, child, getTopAndBottomOffset(),
                    headerConfig.getInitialVisibleHeight(),
                    headerConfig.getInitialVisibleHeight()
                            + headerConfig.getRefreshTriggerRange(),
                    configuration.getMinOffset(),
                    configuration.getMaxOffset(), type);
        }
    }

    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child,
                                    @NonNull View target, float velocityX, float velocityY) {
        int top = child.getTop() - configuration.getTopMargin();
        int bottom = child.getBottom() + configuration.getBottomMargin();
        // If header or footer's hidden part is visible, do not fling.
        if (top > headerConfig.getInitialVisibleHeight()
                || (-bottom + getParent().getHeight()) > footerConfig.getInitialVisibleHeight())
            return true;
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }

    /**
     * @param coordinatorLayout
     * @param child
     * @param offsetDelta
     * @param type
     * @param consumeRawOffset  consume raw offset or not, eg. for a smooth fling action we may
     *                          not just keep it.
     * @return
     */
    private int consumeOffset(CoordinatorLayout coordinatorLayout, V child, int offsetDelta,
                              int type, boolean consumeRawOffset) {
        int currentOffset = getTopAndBottomOffset();
        // Before child consume the offset.
        for (ScrollingListener l : mListeners) {
            l.onPreScroll(coordinatorLayout, child, currentOffset,
                    headerConfig.getInitialVisibleHeight(),
                    headerConfig.getInitialVisibleHeight()
                            + headerConfig.getRefreshTriggerRange(),
                    configuration.getMinOffset(), configuration.getMaxOffset(), type);
        }
        float consumed = consumeRawOffset
                ? offsetDelta
                : onConsumeOffset(currentOffset, configuration.getMaxOffset(), offsetDelta);
        currentOffset = Math.round(currentOffset + consumed);
        setTopAndBottomOffset(currentOffset);
        // In CoordinatorLayout the onChildViewsChanged() will be called after calling behavior's
        // onNestedScroll(). The content view itself can make some transformation by setTranslationY()
        // that may keep it's drawing rectangle unchanged while it's offset has changed. In this
        // case CoordinatorLayout will not call onDependentViewChanged(). So We need to call
        // onDependentViewChanged() manually.
        coordinatorLayout.dispatchDependentViewsChanged(child);
        for (ScrollingListener l : mListeners) {
            l.onScroll(coordinatorLayout, child, currentOffset, offsetDelta,
                    headerConfig.getInitialVisibleHeight(),
                    headerConfig.getInitialVisibleHeight()
                            + headerConfig.getRefreshTriggerRange(),
                    configuration.getMinOffset(),
                    configuration.getMaxOffset(), type);
        }
        return currentOffset;
    }

    protected float onConsumeOffset(int current, int max, int delta) {
        return delta;
    }

    public void setContentTopAndBottomOffset(CoordinatorLayout coordinatorLayout, V child, int offset, int type) {
        int currentOffset = getTopAndBottomOffset();
        int offsetDelta = offset - currentOffset;
        for (ScrollingListener l : mListeners) {
            l.onPreScroll(coordinatorLayout, child, currentOffset,
                    headerConfig.getInitialVisibleHeight(),
                    headerConfig.getInitialVisibleHeight()
                            + headerConfig.getRefreshTriggerRange(),
                    configuration.getMinOffset(), configuration.getMaxOffset(), type);
        }
        setTopAndBottomOffset(offset);
        coordinatorLayout.dispatchDependentViewsChanged(child);
        for (ScrollingListener l : mListeners) {
            l.onScroll(coordinatorLayout, child, currentOffset, offsetDelta,
                    headerConfig.getInitialVisibleHeight(),
                    headerConfig.getInitialVisibleHeight()
                            + headerConfig.getRefreshTriggerRange(),
                    configuration.getMinOffset(),
                    configuration.getMaxOffset(), type);
        }
    }

    private Runnable offsetCallback;

    /**
     * If view has scroll to a invalid position, reset it, otherwise do nothing.
     *
     * @param holdOn
     */
    public void stopScroll(boolean holdOn) {
        // There is an issue that when a refresh complete immediately, the show header or footer
        // animation may be just started, need to be cancelled.
        cancelAnimation();
        // If content offset is larger than header's visible height or smaller than minimum offset,
        // which means content has scrolled to a insignificant or invalid position.
        // We need to reset it.
        if (null == getChild() || getParent() == null) return;
        if (getChild().getTop() - configuration.getTopMargin() > headerConfig.getInitialVisibleHeight()
                || getChild().getTop() - configuration.getTopMargin() < configuration.getMinOffset()
                || getChild().getBottom() + configuration.getBottomMargin()
                < -footerConfig.getInitialVisibleHeight() + getParent().getHeight()) {
            // Remove previous pending callback.
            handler.removeCallbacks(offsetCallback);
            offsetCallback = new Runnable() {
                @Override
                public void run() {
                    reset(RESET_DURATION);
                }
            };
            handler.postDelayed(offsetCallback, holdOn ? HOLD_ON_DURATION : 0L);
        }
    }

    /**
     * This will reset the header or footer view to it's original position when it's laid out for the first time.
     */
    public void reset(long animateDuration) {
        if (null == getChild() || getParent() == null) return;
        CoordinatorLayout.LayoutParams lp = ((CoordinatorLayout.LayoutParams) getChild().getLayoutParams());
        int top = getChild().getTop() - lp.topMargin;
        int bottom = getChild().getBottom() + lp.bottomMargin;
        // Reset footer first, then consider header.
        // Based on a strong contract that headerVisibleHeight is a distance from parent top.
        int offset = 0;
        if (-bottom + getParent().getHeight() > 0) {
            offset = -footerConfig.getInitialVisibleHeight() + getParent().getHeight() - bottom;
        } else {
            offset = headerConfig.getInitialVisibleHeight() - top;
        }
        animateOffsetDeltaWithDuration(getParent(), getChild(), offset,
                headerConfig.getInitialVisibleHeight(),
                headerConfig.getInitialVisibleHeight()
                        + headerConfig.getRefreshTriggerRange(),
                configuration.getMinOffset(),
                configuration.getMaxOffset(), animateDuration, TYPE_NON_TOUCH);
    }

    public void refreshHeader(long animateDuration) {
        if (null == getChild() || null == getParent()) return;
        int top = getChild().getTop() - configuration.getTopMargin();
        int offset = 0;
        if (headerConfig.getShowUpWhenRefresh() == null) {
            // Show up to the trigger range.
            offset = headerConfig.getInitialVisibleHeight() + headerConfig.getRefreshTriggerRange() - top;
        } else {
            if (headerConfig.getShowUpWhenRefresh()) {
                // Show entire header.
                offset = headerConfig.getHeight() - top;
            } else {
                // Show the visible part only.
                offset = headerConfig.getInitialVisibleHeight() - top;
            }
        }
        animateOffsetDeltaWithDuration(getParent(), getChild(), offset,
                headerConfig.getInitialVisibleHeight(),
                headerConfig.getInitialVisibleHeight()
                        + headerConfig.getRefreshTriggerRange(),
                configuration.getMinOffset(),
                configuration.getMaxOffset(), animateDuration, TYPE_NON_TOUCH);
    }


    /**
     * Make the header view entirely visible.
     */
    public void showHeader(long animateDuration) {
        if (null == getChild() || null == getParent()) return;
        int offset = headerConfig.getHeight() + headerConfig.getBottomMargin()
                - (getChild().getTop() - configuration.getTopMargin());
        animateOffsetDeltaWithDuration(getParent(), getChild(), offset,
                headerConfig.getInitialVisibleHeight(),
                headerConfig.getInitialVisibleHeight()
                        + headerConfig.getRefreshTriggerRange(),
                configuration.getMinOffset(),
                configuration.getMaxOffset(), animateDuration, TYPE_NON_TOUCH);
    }


    public void refreshFooter(long animationDuration) {
        if (null == getChild() || getParent() == null) return;
        CoordinatorLayout.LayoutParams lp = ((CoordinatorLayout.LayoutParams) getChild().getLayoutParams());
        int bottom = getChild().getBottom() + lp.bottomMargin;
        int offset = 0;
        if (footerConfig.getShowUpWhenRefresh() == null) {
            offset = -(footerConfig.getInitialVisibleHeight()
                    + footerConfig.getRefreshTriggerRange())
                    + getParent().getHeight() - bottom;
        } else {
            if (footerConfig.getShowUpWhenRefresh()) {
                offset = getParent().getHeight() - footerConfig.getHeight() - bottom;
            } else {
                offset = -footerConfig.getInitialVisibleHeight() + getParent().getHeight() - bottom;
            }
        }
        animateOffsetDeltaWithDuration(getParent(), getChild(), offset,
                headerConfig.getInitialVisibleHeight(),
                headerConfig.getInitialVisibleHeight()
                        + headerConfig.getRefreshTriggerRange(),
                configuration.getMinOffset(),
                configuration.getMaxOffset(),
                animationDuration, TYPE_NON_TOUCH);
    }

    /**
     * Make the footer entirely visible.
     *
     * @param animationDuration
     */
    public void showFooter(long animationDuration) {
        if (null == getChild() || getParent() == null) return;
        CoordinatorLayout.LayoutParams lp = ((CoordinatorLayout.LayoutParams) getChild().getLayoutParams());
        int bottom = getChild().getBottom() + lp.bottomMargin;
        int offset = getParent().getHeight() - footerConfig.getHeight()
                - footerConfig.getTopMargin() - bottom;
        animateOffsetDeltaWithDuration(getParent(), getChild(), offset,
                headerConfig.getInitialVisibleHeight(),
                headerConfig.getInitialVisibleHeight()
                        + headerConfig.getRefreshTriggerRange(),
                configuration.getMinOffset(), configuration.getMaxOffset(), animationDuration, TYPE_NON_TOUCH);
    }


    public boolean isMinOffsetReached() {
        int top = getChild().getTop() - configuration.getTopMargin();
        return top <= configuration.getMinOffset();
    }

    public BehaviorConfiguration createIndicatorConfig() {
        return new BehaviorConfiguration.Builder()
                .setDefaultRefreshTriggerRange(configuration.getDefaultRefreshTriggerRange())
                .setRefreshTriggerRange(configuration.getDefaultRefreshTriggerRange())
                .build();
    }

    public void setHeaderConfig(BehaviorConfiguration headerConfig) {
        this.headerConfig = new BehaviorConfiguration.Builder(headerConfig).setSettled(false).build();
        // headerConfig.getInitialVisibleHeight may be zero when created for the first time and
        // cause the minimum offset set to zero, we use the cached one to set it right.
        configuration.setMinOffset(Math.min(configuration.getCachedMinOffset(),
                headerConfig.getInitialVisibleHeight()));
        requestLayout();
    }

    public void setFooterConfig(BehaviorConfiguration footerConfig) {
        this.footerConfig = new BehaviorConfiguration.Builder(footerConfig).setSettled(false).build();
        requestLayout();
    }

    /**
     * Get an initial visible height for the sake of we don't have a header view actually,
     * i.e. this behavior is used by a standalone content view without any header view.
     *
     * @return
     */
    private int getHeaderInitialVisibleHeight() {
        int initialVisibleHeight;
        if (headerConfig.getHeight() <= 0 || headerConfig.getVisibleHeight() <= 0) {
            initialVisibleHeight = headerConfig.getVisibleHeight();
        } else if (headerConfig.getVisibleHeight() >= headerConfig.getHeight()) {
            initialVisibleHeight = headerConfig.getVisibleHeight()
                    + headerConfig.getTopMargin() + headerConfig.getBottomMargin();
        } else {
            initialVisibleHeight = headerConfig.getVisibleHeight() + headerConfig.getBottomMargin();
        }
        return initialVisibleHeight;
    }

    @Override
    public ContentBehaviorController getController() {
        return super.getController();
    }

    public BehaviorConfiguration getHeaderConfig() {
        return headerConfig;
    }

    public BehaviorConfiguration getFooterConfig() {
        return footerConfig;
    }

    @Override
    public Parcelable onSaveInstanceState(CoordinatorLayout parent, V child) {
        final Parcelable superState = super.onSaveInstanceState(parent, child);
        final int topAndBottomOffset = getTopAndBottomOffset();
        SavedState ss = new SavedState(superState);
        ss.topAndBottomOffset = topAndBottomOffset;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(CoordinatorLayout parent, V child, Parcelable state) {
        if (state instanceof SavedState) {
            super.onRestoreInstanceState(parent, child, ((SavedState) state).getSuperState());
            final SavedState ss = ((SavedState) state);
            initialOffset = ss.topAndBottomOffset;
        } else {
            super.onRestoreInstanceState(parent, child, state);
        }
    }

    protected static class SavedState extends AbsSavedState {
        private int topAndBottomOffset;

        public SavedState(@NonNull Parcelable superState) {
            super(superState);
        }

        public SavedState(@NonNull Parcel source, @Nullable ClassLoader loader) {
            super(source, loader);
            topAndBottomOffset = source.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(topAndBottomOffset);
        }

        public static final Creator<SavedState> CREATOR = new ClassLoaderCreator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source, ClassLoader loader) {
                return new SavedState(source, loader);
            }

            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source, null);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
