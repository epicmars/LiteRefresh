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

import static androidx.core.view.ViewCompat.TYPE_NON_TOUCH;
import static androidx.core.view.ViewCompat.TYPE_TOUCH;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.math.MathUtils;
import androidx.core.view.ViewCompat;
import androidx.customview.view.AbsSavedState;

import literefresh.R;
import literefresh.controller.ScrollableBehaviorController;

/**
 * A behavior for nested scrollable child of {@link CoordinatorLayout}.
 * <p>
 * It's attach to the nested scrolling target view, such as
 * {@link android.widget.ListView}
 * {@link androidx.core.widget.NestedScrollView},
 * {@link androidx.recyclerview.widget.RecyclerView}
 * which implement nested scrolling operation of {@link android.view.ViewParent}.
 * <p>
 * View with this behavior must be a direct child of {@link CoordinatorLayout}.
 * <p>
 */
public class ScrollableBehavior<V extends View> extends AnimationOffsetBehavior<V> {

    private static final String TAG = ScrollableBehavior.class.getName();
    private static final int INVALID_OFFSET = Integer.MIN_VALUE;
    private int savedOffset = INVALID_OFFSET;

    private static final int ORIENTATION_VERTICAL = 1;
    private static final int ORIENTATION_HORIZON = 2;
    private static final int ORIENTATION_VERTICAL_HORIZON = 3;

    private @ScrollingContentOrientation int orientation = ORIENTATION_VERTICAL;

    @IntDef({ORIENTATION_VERTICAL, ORIENTATION_HORIZON, ORIENTATION_VERTICAL_HORIZON})
    public @interface ScrollingContentOrientation {}

    public ScrollableBehavior(Context context) {
        this(context, null);
    }

    public ScrollableBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ContentBehavior,
                0, 0);
//        boolean hasMinOffset = a.hasValue(R.styleable.ContentBehavior_lr_minOffset);
//        int minOffset = a.getDimensionPixelOffset(R.styleable.ContentBehavior_lr_minOffset, 0);
//        boolean hasMinOffsetRatio = a.hasValue(R.styleable.ContentBehavior_lr_minOffsetRatio);
//        float minOffsetRatio = a.getFraction(R.styleable.ContentBehavior_lr_minOffsetRatio, 1, 1, 0f);
//        float minOffsetRatioOfParent = a.getFraction(R.styleable.ContentBehavior_lr_minOffsetRatio, 1, 2, 0f);
//
//        if (hasMinOffset) {
//            getConfig().setMinOffset(minOffset);
//            getConfig().setCachedMinOffset(minOffset);
//        }
//
//        if (hasMinOffsetRatio) {
//            getConfig().setMinOffsetRatio(minOffsetRatio);
//            getConfig().setMinOffsetRatioOfParent(minOffsetRatioOfParent);
//        }
//        if (!hasMinOffset && !hasMinOffsetRatio) {
//            getConfig().setUseDefaultMinOffset(true);
//        }

        a.recycle();
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
        // Header and footer's configurations must be performed before the content's,
        // and header's configuration before footer.
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
                restoreTopAndBottomOffset(parent, child, savedOffset, TYPE_NON_TOUCH);
            } else {
                int offset = getConfig().getTopEdgeConfig().getMinOffset() - getViewOffsetHelper().getLayoutTop();
                setTopAndBottomOffset(offset);
            }
        }
        return handled;
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull CoordinatorLayout parent, @NonNull V child, @NonNull MotionEvent ev) {
//        if (getController().isRefreshing()) {
//            return true;
//        }
        return super.onInterceptTouchEvent(parent, child, ev);
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
            dispatchOnStartScroll(coordinatorLayout, child, type);
        }
        return start;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child,
                                  @NonNull View target, int dx, int dy, @NonNull int[] consumed,
                                  int type) {
        if (dy > 0) {
            onNestedPreScrollUp(coordinatorLayout, child, dy, consumed, type);
        } else if (dy < 0) {
            onNestedPreScrollDown(coordinatorLayout, child, dy, consumed, type);
        }
    }


    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child,
                               @NonNull View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, type, consumed);
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
        dispatchOnStopScroll(coordinatorLayout, child, getTopAndBottomOffset(), type);
    }

    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child,
                                    @NonNull View target, float velocityX, float velocityY) {
//        int top = getTopPosition();
//        int bottom = getBottomPosition();
//        // todo: to make fling more nature when header can scroll up or footer can scroll down
//        if (top > getConfig().getTopEdgeConfig().getMinOffset() || bottom > getConfig().getBottomEdgeConfig().getMinOffset()) {
//            return true;
//        }
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }

    @Override
    public boolean onNestedFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, float velocityX, float velocityY, boolean consumed) {
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }


    private void onNestedPreScrollDown(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, int dy, @NonNull int [] consumed, int type) {
        int bottom = getBottomPosition();
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

    private void onNestedPreScrollUp(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, int dy, @NonNull int[] consumed, int type) {
        // When scrolling up, compute the top offset which content can reach.
        int top = getTopPosition();
        // If already reach minimum offset.
        if (top <= getConfig().getTopEdgeConfig().getMinOffset())
            return;
        int offset = MathUtils.clamp(-dy, getConfig().getTopEdgeConfig().getMinOffset()
                - top, 0);
        if (offset != 0) {
            consumeOffsetDelta(coordinatorLayout, child, offset, type, true);
            consumed[1] = -offset;
        }
    }

    public void onNestedScrollDown(CoordinatorLayout coordinatorLayout, V child, int offsetDelta, int type) {
        // Scrolling down.
        final int top = getTopPosition();
        final int maxOffset = getConfig().getTopEdgeConfig().getMaxOffset();
        Log.d(TAG, "top: " + top + " maxOffset: " + maxOffset);
        // Top position of child can not scroll exceed maximum offset.
        if (top >= maxOffset) {
            return;
        }
        int offset = MathUtils.clamp(offsetDelta, 0, maxOffset - top);
        if (offset != 0) {
            if (top >= getConfig().getTopEdgeConfig().getMinOffset()) {
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
                        getConfig().getTopEdgeConfig().getMinOffset() - top);
                consumeOffsetDelta(coordinatorLayout, child, offset, type, true);
            }
        }
    }

    public void onNestedScrollUp(CoordinatorLayout coordinatorLayout, V child, int offsetDelta, int type) {
        final int bottom = getBottomPosition();
        final int bottomMinOffset = getConfig().getBottomEdgeConfig().getMinOffset();
        // Can not scroll up exceed bottom offset.
        if (bottom <= bottomMinOffset) {
            return;
        }
        int offset = MathUtils.clamp(offsetDelta, bottomMinOffset - bottom, 0);
        if (offset != 0) {
            if (bottom <= getConfig().getBottomEdgeConfig().getMaxOffset()) {
                // If footer's hidden part is visible, ignore fling too.
                if (type != TYPE_TOUCH)
                    return;
                consumeOffsetDelta(coordinatorLayout, child, offset, type, false);
            } else {
                // Footer's hidden part is not visible yet.
                // Recompute it, so that bottom doesn't exceed bottom minimum offset
                // no matter what type of touch event is.
                offset = MathUtils.clamp(offsetDelta,
                        getConfig().getBottomEdgeConfig().getMinOffset() - bottom, 0);
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
        dispatchOnPreScroll(coordinatorLayout, child, currentOffset, type);
        float consumed = consumeRawOffset
                ? offsetDelta
                : onConsumeOffset(currentOffset, offsetDelta);
        currentOffset = Math.round(currentOffset + consumed);
        setTopAndBottomOffset(currentOffset);
        // In CoordinatorLayout the onChildViewsChanged() will be called after calling behavior's
        // onNestedScroll(). The content view itself can make some transformation by setTranslationY()
        // that may keep it's drawing rectangle unchanged while it's offset has changed. In this
        // case CoordinatorLayout will not call onDependentViewChanged(). So We need to call
        // onDependentViewChanged() manually.
        coordinatorLayout.dispatchDependentViewsChanged(child);
        dispatchOnScroll(coordinatorLayout, child, currentOffset, offsetDelta, type);
        return currentOffset;
    }

    protected float onConsumeOffset(int current, int delta) {
        return delta;
    }

    public void restoreTopAndBottomOffset(CoordinatorLayout coordinatorLayout, V child, int offset, int type) {
        int currentOffset = getTopAndBottomOffset();
        int offsetDelta = offset - currentOffset;
        setTopAndBottomOffset(offset);
        coordinatorLayout.dispatchDependentViewsChanged(child);
        dispatchOnScroll(coordinatorLayout, child, offset, offsetDelta, type);
    }

    private Runnable offsetCallback;

    /**
     * If view has scroll to a invalid position, reset it, otherwise do nothing.
     *
     * @param holdOn
     */
    public void stopScroll(boolean holdOn) {
        // There is an issue that when a refresh complete immediately, the header or footer
        // showing animation may be just started, need to be cancelled.
        cancelAnimation();
        // If content offset is larger than header's visible height or smaller than minimum offset,
        // which means content has scrolled to a insignificant or invalid position.
        // We need to reset it.
        if (null == getChild() || getParent() == null) return;
        if (getTopPosition() > getConfig().getTopEdgeConfig().getMinOffset()
                || getTopPosition() < getConfig().getTopEdgeConfig().getMinOffset()
                || getBottomPosition() < getParent().getHeight()) {
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

    public void stopAtPosition(int offset) {
        Log.d(TAG, "stopAtOffset: " + offset);
        // There is an issue that when a refresh complete immediately, the header or footer
        // showing animation may be just started, need to be cancelled.
        cancelAnimation();
        // If content offset is larger than header's visible height or smaller than minimum offset,
        // which means content has scrolled to a insignificant or invalid position.
        // We need to reset it.
        if (null == getChild() || getParent() == null) return;
        if (offset != getTopAndBottomOffset()) {
            // Remove previous pending callback.
            handler.removeCallbacks(offsetCallback);
            offsetCallback = new Runnable() {
                @Override
                public void run() {
                    animateOffsetDelta(DEFAULT_ANIM_DURATION, offset - getTopPosition());
                }
            };
            handler.post(offsetCallback);
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
        if (-bottom + getParent().getHeight() > 0) {
            offset = getParent().getHeight() - bottom;
        } else {
            offset = getConfig().getTopEdgeConfig().getMinOffset() - top;
        }
        animateOffsetDelta(animateDuration, offset);
    }

    private void animateOffsetDelta(long animateDuration, int offset) {
        animateOffsetDeltaWithDuration(getParent(), getChild(), getConfig(),
                offset,
                animateDuration,
                TYPE_NON_TOUCH);
    }

    public void refreshHeader(long animateDuration) {
//        if (null == getChild() || null == getParent()) return;
//        int topOffset = getTopOffset(getChild());
//        int offset = 0;
//        // Show up to the trigger range.
//        offset = getConfig().getMinOffset() + getConfig().getTriggerOffset() - top;
//        animateOffset(animateDuration, offset);
    }



    /**
     * Make the header visible with a specific visible height.
     */
    public void showHeader(long animateDuration, int headerHeight) {
        if (null == getChild() || null == getParent()) return;
        int offset = headerHeight - getChild().getTop();
        animateOffsetDelta(animateDuration, offset);
    }

    /**
     * Make the footer entirely visible.
     *
     * @param animationDuration
     */
    public void showFooter(long animationDuration, int footerHeight) {
        if (null == getChild() || getParent() == null) return;
        CoordinatorLayout.LayoutParams lp = ((CoordinatorLayout.LayoutParams) getChild().getLayoutParams());
        int bottom = getChild().getBottom() + lp.bottomMargin;
        int offset = getParent().getHeight() - footerHeight - bottom;
        animateOffsetDelta(animationDuration, offset);
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
        animateOffsetDelta(animationDuration, offset);
    }

    public boolean isTopMinOffsetReached() {
        return getTopPosition() <= getConfig().getTopEdgeConfig().getMinOffset();
    }

    public boolean isBottomMinOffsetReached() {
        return getBottomPosition() <= getConfig().getBottomEdgeConfig().getMinOffset();
    }

    @Override
    public ScrollableBehaviorController getController() {
        return (ScrollableBehaviorController) super.getController();
    }

    @Override
    public ScrollableConfiguration getConfig() {
        return (ScrollableConfiguration) super.getConfig();
    }

    @Override
    ScrollableConfiguration.Builder newConfigBuilder() {
        return new ScrollableConfiguration.Builder(getConfig());
    }

    @Override
    public ScrollableConfiguration.Builder with(@NonNull Context context) {
        return new ScrollableConfiguration.Builder(context, this,  getConfig());
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
