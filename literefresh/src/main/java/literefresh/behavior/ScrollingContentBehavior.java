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
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.math.MathUtils;
import androidx.core.view.ViewCompat;
import androidx.customview.view.AbsSavedState;

import literefresh.R;
import literefresh.controller.ContentBehaviorController;

import static androidx.core.view.ViewCompat.TYPE_NON_TOUCH;
import static androidx.core.view.ViewCompat.TYPE_TOUCH;

/**
 * A behavior for nested scrollable child of {@link CoordinatorLayout}.
 * <p>
 * It's attach to the nested scrolling target view, such as
 * {@link androidx.core.widget.NestedScrollView},
 * {@link androidx.recyclerview.widget.RecyclerView}
 * which implement {@link androidx.core.view.NestedScrollingChild}.
 * <p>
 * View with this behavior must be a direct child of {@link CoordinatorLayout}.
 * <p>
 */
public class ScrollingContentBehavior<V extends View> extends AnimationOffsetBehavior<V> {

    private static final int INVALID_OFFSET = Integer.MIN_VALUE;
    private Configuration headerConfig;
    private Configuration footerConfig;
    private int savedOffset = INVALID_OFFSET;

    private static final int ORIENTATION_VERTICAL = 1;
    private static final int ORIENTATION_HORIZON = 2;
    private static final int ORIENTATION_VERTICAL_HORIZON = 3;

    private @ScrollingContentOrientation int orientation = ORIENTATION_VERTICAL;

    @IntDef({ORIENTATION_VERTICAL, ORIENTATION_HORIZON, ORIENTATION_VERTICAL_HORIZON})
    public @interface ScrollingContentOrientation {}

    public ScrollingContentBehavior(Context context) {
        this(context, null);
    }

    public ScrollingContentBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ContentBehavior,
                0, 0);
        boolean hasMinOffset = a.hasValue(R.styleable.ContentBehavior_lr_minOffset);
        if (hasMinOffset) {
            getConfig().setMinOffset(a.getDimensionPixelOffset(
                    R.styleable.ContentBehavior_lr_minOffset, 0));
            getConfig().setCachedMinOffset(getConfig().getMinOffset());
        }

        boolean hasMinOffsetRatio = a.hasValue(R.styleable.ContentBehavior_lr_minOffsetRatio);
        if (hasMinOffsetRatio) {
            getConfig().setMinOffsetRatio(a.getFraction(
                    R.styleable.ContentBehavior_lr_minOffsetRatio, 1, 1, 0f));
            getConfig().setMinOffsetRatioOfParent(a.getFraction(
                    R.styleable.ContentBehavior_lr_minOffsetRatio, 1, 2, 0f));
        }
        if (!hasMinOffset && !hasMinOffsetRatio) {
            getConfig().setUseDefaultMinOffset(true);
        }
        a.recycle();
    }

    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, V child, int parentWidthMeasureSpec,
                                  int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        boolean handled = super.onMeasureChild(parent, child, parentWidthMeasureSpec, widthUsed,
                parentHeightMeasureSpec, heightUsed);
        int heightSize = View.MeasureSpec.getSize(parentHeightMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(parentHeightMeasureSpec);
        // If required height is zero.
        if (heightSize == 0) {
            return handled;
        }
        // The minimum offset is used to limit the content view's scrolling offset.
        // Besides, the minimum offset and header's visible height are used together to reset
        // content's position.
        // We must make sure after resetting, either it's top reaches the minimum offset or
        // header visible height, it depends on which one is larger. When do the layout, we set
        // minimum offset to be always less than or equal to header visible height.
        int height = heightSize - getConfig().getMinOffset();
        int heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
        if (height >= 0) {
            parent.onMeasureChild(child, parentWidthMeasureSpec, widthUsed, heightSpec, heightUsed);
        }
        return handled;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        boolean handled = super.onLayoutChild(parent, child, layoutDirection);
        // Header and footer's configurations must be performed before the content's,
        // and header's configuration before footer.

        // Config footer.
        configFooter(parent);

        // Compute max offset, it will not exceed parent height.
        configMaxOffset(parent, child);

        // Compute content view's minimum offset.
        configMinOffset(parent, child);

        if (!getConfig().isSettled()) {
            getConfig().setSettled(true);
            cancelAnimation();
            if (savedOffset != INVALID_OFFSET) {
                // fixme: If screen orientation changed, the saved offset may be larger than initial visible height.
                //if (headerConfig.getInitialVisibleHeight() > 0
                //        && initialOffset > headerConfig.getInitialVisibleHeight()) {
                //        initialOffset = MathUtils.clamp(initialOffset, getConfiguration().getMinOffset(),
                //        headerConfig.getInitialVisibleHeight());
                //}
                restoreContentTopAndBottomOffset(parent, child, savedOffset, TYPE_NON_TOUCH);
            } else {
                setTopAndBottomOffset(getConfig().getMinOffset());
            }
        }
        return handled;
    }

    private void configMaxOffset(CoordinatorLayout parent, V child) {
        int currentMaxOffset = getConfig().getMaxOffset();
        if (getConfig().isUseDefaultMaxOffset()) {
            currentMaxOffset = (int) Math.max(currentMaxOffset,
                    GOLDEN_RATIO * parent.getHeight());
        } else {
            currentMaxOffset = (int) Math.max(currentMaxOffset,
                    getConfig().getMaxOffsetRatioOfParent() * parent.getHeight());
        }
        // Content's max offset should be larger than header's, otherwise the refresh trigger
        // range may not be reached.
        getConfig().setMaxOffset(currentMaxOffset);
    }

    private void configMinOffset(CoordinatorLayout parent, V child) {
        int currentMinOffset = getConfig().getMinOffset();
        // If not use default minimum offset.
        if (!getConfig().isUseDefaultMinOffset()) {
            // If we have set a minimum offset ratio.
            currentMinOffset = (int) Math.max(currentMinOffset,
                    getConfig().getMinOffsetRatioOfParent()
                            > getConfig().getMinOffsetRatio()
                            ? getConfig().getMinOffsetRatio() * parent.getHeight()
                            : getConfig().getMinOffsetRatio() * child.getHeight());
            getConfig().setMinOffset(currentMinOffset);
            // Cache new minimum offset.
            getConfig().setCachedMinOffset(currentMinOffset);
        }
    }

    /**
     * Configure minimum offset with header initial visible height.
     * @param headerInitialVisibleHeight header's initial visible height.
     */
    void configTopMinOffset(int headerInitialVisibleHeight) {

    }

//    private void configFooter(CoordinatorLayout parent) {
//        // If footer's max offset is not settled yet.
//        if (footerConfig.isUseDefaultMaxOffset() && footerConfig.getMaxOffset() <= 0) {
//            footerConfig.setMaxOffset((int) ((1 - GOLDEN_RATIO) * parent.getHeight()));
//        }
//    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull V child, @NonNull View directTargetChild,
                                       @NonNull View target, int axes, int type) {
        // If scrolling started by touch event we need to invalidate restored initial offset.
        if (savedOffset != INVALID_OFFSET && type == TYPE_TOUCH) {
            savedOffset = INVALID_OFFSET;
        }
        boolean start = (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        if (start) {
            dispatchStartScroll(coordinatorLayout, child, type);
        }
        return start;
    }

    public void dispatchStartScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, int type) {
        for (ScrollingListener l : mListeners) {
            l.onStartScroll(coordinatorLayout, child, getConfig().getMinOffset(),
                    getConfig().getMinOffset()
                            + getConfig().getDefaultTriggerOffset(),
                    getConfig().getMinOffset(), getConfig().getMaxOffset(), type);
        }
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child,
                                  @NonNull View target, int dx, int dy, @NonNull int[] consumed,
                                  int type) {
        if (dy > 0) {
            // When scrolling up, compute the top offset which content can reach.
            int top = child.getTop() - getConfig().getTopMargin();
            // If already reach minimum offset.
            if (top <= getConfig().getMinOffset())
                return;
            int offset = MathUtils.clamp(-dy, getConfig().getMinOffset() - top, 0);
            if (offset != 0) {
                consumeOffsetDelta(coordinatorLayout, child, offset, type, true);
                consumed[1] = -offset;
            }
        } else if (dy < 0) {
            int bottom = child.getBottom() + getConfig().getBottomMargin();
            // If already reach the bottom of parent view.
            if (bottom >= coordinatorLayout.getHeight()) {
                return;
            }
            // When scrolling down, if footer is still visible.
            int offset = MathUtils.clamp(-dy, 0, coordinatorLayout.getHeight() - bottom);
            if (offset != 0) {
                consumeOffsetDelta(coordinatorLayout, child, offset, type, true);
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
            onNestedScrollDown(coordinatorLayout, child, -dyUnconsumed, type);
        } else if (dyUnconsumed > 0) {
            // Scrolling up.
            onNestedScrollUp(coordinatorLayout, child, -dyUnconsumed, type);
        }
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child,
                                   @NonNull View target, int type) {
        dispatchStopScroll(coordinatorLayout, child, type);
    }

    public void dispatchStopScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, int type) {
        for (ScrollingListener l : mListeners) {
            l.onStopScroll(coordinatorLayout, child, getTopAndBottomOffset(),
                    getConfig().getMinOffset(),
                    getConfig().getMinOffset()
                            + getConfig().getTriggerOffset(),
                    getConfig().getMinOffset(),
                    getConfig().getMaxOffset(), type);
        }
    }

    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child,
                                    @NonNull View target, float velocityX, float velocityY) {
//        int top = child.getTop() - getConfiguration().getTopMargin();
//        int bottom = child.getBottom() + getConfiguration().getBottomMargin();
//        // todo: to make fling more nature when header can scroll up or footer can scroll down
//        if (top > getConfiguration().getMinOffset()) {
//            return true;
//        }
//        if (top > headerConfig.getInitialVisibleHeight()
//                || (-bottom + getParent().getHeight()) > footerConfig.getInitialVisibleHeight())
//            return true;
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }

    @Override
    public boolean onNestedFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, float velocityX, float velocityY, boolean consumed) {
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

    /**
     * Nested scroll down.
     * @param coordinatorLayout
     * @param child
     * @param offsetDelta
     * @param type
     */
    public void onNestedScrollDown(CoordinatorLayout coordinatorLayout, V child, int offsetDelta, int type) {
        // Scrolling down.
        final int top = child.getTop() - getConfig().getTopMargin();
        final int maxOffset = getConfig().getMaxOffset();
        // Top position of child can not scroll exceed maximum offset.
        if (top >= maxOffset)
            return;
        int offset = MathUtils.clamp(offsetDelta, 0, maxOffset - top);
        if (offset != 0) {
            if (top >= getConfig().getMinOffset()) {
                // When header's hidden part is visible, do not consume none touch scroll,
                // content can scroll to the maximum offset with the touch event only.
                if (type != TYPE_TOUCH)
                    return;
                consumeOffsetDelta(coordinatorLayout, child, offset, type, false);
            } else {
                // Header's hidden part is not visible yet.
                // Recompute the offset so that the top does not exceed header's initial
                // visible height no matter what type of touch event is.
                // todo: add overshot feature to make scrolling motion more nature.
                offset = MathUtils.clamp(offsetDelta, 0,
                        getConfig().getMinOffset() - top);
                consumeOffsetDelta(coordinatorLayout, child, offset, type, true);
            }
        }
    }

    public void onNestedScrollUp(CoordinatorLayout coordinatorLayout, V child, int offsetDelta, int type) {
        final int bottom = child.getBottom() + getConfig().getBottomMargin();
        final int footerMaxOffset = coordinatorLayout.getHeight() - footerConfig.getMaxOffset();
        // Can not scroll exceed footer maximum offset.
        if (bottom <= footerMaxOffset)
            return;
        int offset = MathUtils.clamp(offsetDelta, footerMaxOffset - bottom, 0);
        if (offset != 0) {
            if (coordinatorLayout.getHeight() - bottom >= footerConfig.getInitialVisibleHeight()) {
                // If footer's hidden part is visible, ignore fling too.
                if (type != TYPE_TOUCH)
                    return;
                consumeOffsetDelta(coordinatorLayout, child, offset, type, false);
            } else {
                // Footer's hidden part is not visible yet.
                // Recompute it, so that bottom doesn't exceed footer's initial visible height
                // no matter what type of touch event is.
                offset = MathUtils.clamp(offsetDelta,
                        -(footerConfig.getInitialVisibleHeight()
                                - coordinatorLayout.getHeight() + bottom), 0);
                consumeOffsetDelta(coordinatorLayout, child, offset, type, true);
            }
        }
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
    private int consumeOffsetDelta(CoordinatorLayout coordinatorLayout, V child, int offsetDelta,
                                   int type, boolean consumeRawOffset) {
        int currentOffset = getTopAndBottomOffset();
        // Before child consume the offset.
        for (ScrollingListener l : mListeners) {
            l.onPreScroll(coordinatorLayout, child, currentOffset,
                    getConfig().getMinOffset(),
                    getConfig().getMinOffset()
                            + getConfig().getTriggerOffset(),
                    getConfig().getMinOffset(), getConfig().getMaxOffset(), type);
        }
        float consumed = consumeRawOffset
                ? offsetDelta
                : onConsumeOffset(currentOffset, getConfig().getMaxOffset(), offsetDelta);
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
                    getConfig().getMinOffset(),
                    getConfig().getMinOffset()
                            + getConfig().getTriggerOffset(),
                    getConfig().getMinOffset(),
                    getConfig().getMaxOffset(), type);
        }
        return currentOffset;
    }

    protected float onConsumeOffset(int current, int max, int delta) {
        return delta;
    }

    public void restoreContentTopAndBottomOffset(CoordinatorLayout coordinatorLayout, V child, int offset, int type) {
        int currentOffset = getTopAndBottomOffset();
        int offsetDelta = offset - currentOffset;
        setTopAndBottomOffset(offset);
        coordinatorLayout.dispatchDependentViewsChanged(child);
        for (ScrollingListener l : mListeners) {
            l.onScroll(coordinatorLayout, child, currentOffset, offsetDelta,
                    getConfig().getMinOffset(),
                    getConfig().getMinOffset()
                            + getConfig().getTriggerOffset(),
                    getConfig().getMinOffset(),
                    getConfig().getMaxOffset(), type);
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
        if (getChild().getTop() - getConfig().getTopMargin() > getConfig().getMinOffset()
                || getChild().getTop() - getConfig().getTopMargin() < getConfig().getMinOffset()
                || getChild().getBottom() + getConfig().getBottomMargin()
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
     * todo delegated to onNestedScroll
     * @param offset
     * @param delta
     * @param type
     */
    public void updateTopAndBottomOffset(int offset, int delta, int type) {
        if (delta > 0) {
            onNestedScrollDown(getParent(), getChild(), delta, type);
        } else {
            onNestedScrollUp(getParent(), getChild(), delta, type);
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
        // Based on a strong contract that header visible height is a distance from parent top.
        int offset = 0;
//        if (-bottom + getParent().getHeight() > 0) {
//            offset = -footerConfig.getInitialVisibleHeight() + getParent().getHeight() - bottom;
//        } else {
            offset = getConfig().getMinOffset() - top;
//        }
        animateOffsetDeltaWithDuration(getParent(), getChild(), offset,
                getConfig().getMinOffset(),
                getConfig().getMinOffset()
                        + getConfig().getTriggerOffset(),
                getConfig().getMinOffset(),
                getConfig().getMaxOffset(), animateDuration, TYPE_NON_TOUCH);
    }

    public void refreshHeader(long animateDuration) {
        if (null == getChild() || null == getParent()) return;
        int top = getChild().getTop() - getConfig().getTopMargin();
        int offset = 0;
        // Show up to the trigger range.
        offset = getConfig().getMinOffset() + getConfig().getTriggerOffset() - top;
        animateOffsetDeltaWithDuration(getParent(), getChild(), offset,
                getConfig().getMinOffset(),
                getConfig().getMinOffset()
                        + getConfig().getTriggerOffset(),
                getConfig().getMinOffset(),
                getConfig().getMaxOffset(), animateDuration, TYPE_NON_TOUCH);
    }


    /**
     * Make the header view entirely visible.
     */
    public void showHeader(long animateDuration, int headerHeight, int headerBottomMargin) {
        if (null == getChild() || null == getParent()) return;
        int offset = headerHeight + headerBottomMargin
                - (getChild().getTop() - getConfig().getTopMargin());
        animateOffsetDeltaWithDuration(getParent(), getChild(), offset,
                getConfig().getMinOffset(),
                getConfig().getMinOffset()
                        + getConfig().getTriggerOffset(),
                getConfig().getMinOffset(),
                getConfig().getMaxOffset(), animateDuration, TYPE_NON_TOUCH);
    }

    public void refreshFooter(long animationDuration) {
        if (null == getChild() || getParent() == null) return;
        CoordinatorLayout.LayoutParams lp = ((CoordinatorLayout.LayoutParams) getChild().getLayoutParams());
        int bottom = getChild().getBottom() + lp.bottomMargin;
        int offset = 0;
//        if (footerConfig.getShowUpWhenRefresh() == null) {
//            offset = -(footerConfig.getInitialVisibleHeight()
//                    + footerConfig.getTriggerOffset())
//                    + getParent().getHeight() - bottom;
//        } else {
//            if (footerConfig.getShowUpWhenRefresh()) {
//                offset = getParent().getHeight() - footerConfig.getHeight() - bottom;
//            } else {
//                offset = -footerConfig.getInitialVisibleHeight() + getParent().getHeight() - bottom;
//            }
//        }
        animateOffsetDeltaWithDuration(getParent(), getChild(), offset,
                getConfig().getMinOffset(),
                getConfig().getMinOffset()
                        + getConfig().getTriggerOffset(),
                getConfig().getMinOffset(),
                getConfig().getMaxOffset(),
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
        int offset = 0;
//        int offset = getParent().getHeight() - footerConfig.getHeight()
//                - footerConfig.getTopMargin() - bottom;
        animateOffsetDeltaWithDuration(getParent(), getChild(), offset,
                getConfig().getMinOffset(),
                getConfig().getMinOffset()
                        + getConfig().getTriggerOffset(),
                getConfig().getMinOffset(),
                getConfig().getMaxOffset(),
                animationDuration, TYPE_NON_TOUCH);
    }


    public boolean isMinOffsetReached() {
        int top = getChild().getTop() - getConfig().getTopMargin();
        return top <= getConfig().getMinOffset();
    }

//    /**
//     * Get an initial visible height for the sake of we don't have a header view actually,
//     * i.e. this behavior is used by a standalone content view without any header view.
//     *
//     * @return
//     */
//    private int getHeaderInitialVisibleHeight(int visibleHeight) {
//        int initialVisibleHeight;
//        if (headerConfig.getHeight() <= 0 || visibleHeight <= 0) {
//            initialVisibleHeight = visibleHeight;
//        } else if (headerConfig.getVisibleHeight() >= headerConfig.getHeight()) {
//            initialVisibleHeight = visibleHeight
//                    + headerConfig.getTopMargin() + headerConfig.getBottomMargin();
//        } else {
//            initialVisibleHeight = visibleHeight + headerConfig.getBottomMargin();
//        }
//        return initialVisibleHeight;
//    }

//    public IndicatorConfiguration.Builder newHeaderConfigBuilder() {
//        if (headerConfig == null) {
//            return new IndicatorConfiguration.Builder()
//                    .setDefaultTriggerOffset(getConfiguration().getDefaultTriggerOffset())
//                    .triggerOffset(getConfiguration().getDefaultTriggerOffset());
//        } else {
//            return new IndicatorConfiguration.Builder(headerConfig);
//        }
//    }
//
//    public IndicatorConfiguration.Builder newFooterConfigBuilder() {
//        if (footerConfig == null) {
//            return new IndicatorConfiguration.Builder()
//                    .setDefaultTriggerOffset(getConfiguration().getDefaultTriggerOffset())
//                    .triggerOffset(getConfiguration().getDefaultTriggerOffset())
//                    .setUseDefaultMaxOffset(true);
//        } else {
//            return new IndicatorConfiguration.Builder(footerConfig);
//        }
//    }

//    void setHeaderConfig(IndicatorConfiguration headerConfig) {
//        this.headerConfig = new IndicatorConfiguration.Builder(headerConfig)
//                .setSettled(false).build();
//        // headerConfig.getInitialVisibleHeight() may be zero when created for the first time and
//        // cause the minimum offset set to zero, we use the cached one to set it right.
//        getConfiguration().setMinOffset(Math.min(getConfiguration().getCachedMinOffset(),
//                headerConfig.getInitialVisibleHeight()));
//        requestLayout();
//    }

//    void setFooterConfig(IndicatorConfiguration footerConfig) {
//        this.footerConfig = new IndicatorConfiguration.Builder(footerConfig)
//                .setSettled(false).build();
//        requestLayout();
//    }

//    public IndicatorConfiguration getHeaderConfig() {
//        return headerConfig;
//    }
//
//    public IndicatorConfiguration getFooterConfig() {
//        return footerConfig;
//    }

    @Override
    public ContentBehaviorController getController() {
        return (ContentBehaviorController) super.getController();
    }

    @Override
    public ContentConfiguration getConfig() {
        return (ContentConfiguration) super.getConfig();
    }

    @Override
    ContentConfiguration.Builder newConfigBuilder() {
        return new ContentConfiguration.Builder(getConfig());
    }

    @Override
    public ContentConfiguration.Builder with(@NonNull Context context) {
        return new ContentConfiguration.Builder(context, this, getConfig());
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
            savedOffset = ss.topAndBottomOffset;
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
