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
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.androidpi.literefresh.R;
import com.androidpi.literefresh.animator.OffsetAnimator;
import com.androidpi.literefresh.animator.SpringOffsetAnimator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * A behavior that with offset animation feature.
 */

public abstract class AnimationOffsetBehavior<V extends View, CTR extends BehaviorController>
        extends ViewOffsetBehavior<V> implements Handler.Callback {

    public static final int TYPE_UNKNOWN = 2;

    static final long HOLD_ON_DURATION = 500L;
    static final long SHOW_DURATION = 300L;
    static final long RESET_DURATION = 300L;

    public interface ScrollingListener {

        void onStartScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int initial, int trigger, int min, int max, int type);

        void onPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int initial, int trigger, int min, int max, int type);

        void onScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int delta, int initial, int trigger, int min, int max, int type);

        void onStopScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, int current, int initial, int trigger, int min, int max, int type);
    }

    private static final int MSG_VIEW_INITIATED = 1;

    public static final float GOLDEN_RATIO = 0.618F;

    protected V mChild;
    protected CoordinatorLayout mParent;
    private OffsetAnimator offsetAnimator;
    protected int progressBase = 0;
    protected List<ScrollingListener> mListeners = new ArrayList<>();
    protected Handler handler = new Handler(this);
    private Queue<Runnable> pendingActions = new LinkedList<>();
    protected CTR controller;
    protected BehaviorConfiguration configuration;

    public AnimationOffsetBehavior(Context context) {
        this(context, null);
    }

    public AnimationOffsetBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (configuration == null) {
            configuration = new BehaviorConfiguration();
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.OffsetBehavior,
                0, 0);
        boolean hasMaxOffsetRatio = a.hasValue(R.styleable.OffsetBehavior_lr_maxOffsetRatio);
        boolean hasMaxOffset = a.hasValue(R.styleable.OffsetBehavior_lr_maxOffset);
        if (hasMaxOffsetRatio) {
            configuration.setMaxOffsetRatio(a.getFraction(
                    R.styleable.OffsetBehavior_lr_maxOffsetRatio, 1, 1, 0f));
            configuration.setMaxOffsetRatioOfParent(a.getFraction(
                    R.styleable.OffsetBehavior_lr_maxOffsetRatio, 1, 2, 0f));
        }
        if (hasMaxOffset) {
            configuration.setMaxOffset(a.getDimensionPixelOffset(
                    R.styleable.OffsetBehavior_lr_maxOffset, 0));
        }
        // If maxOffset and maxOffsetRatio is not set then use default.
        configuration.setUseDefaultMaxOffset(!hasMaxOffsetRatio && !hasMaxOffset);
        configuration.setDefaultRefreshTriggerRange(context.getResources().getDimensionPixelOffset(
                R.dimen.lr_default_trigger_range));
        a.recycle();
    }

    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, V child, int parentWidthMeasureSpec,
                                  int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        // Let parent measure it first.
        parent.onMeasureChild(child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec,
                heightUsed);
        return true;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        boolean handled = super.onLayoutChild(parent, child, layoutDirection);

        if (mParent == null) {
            mParent = parent;
        }
        if (mChild == null) {
            mChild = child;
        }
        // Execute pending actions which need view to be initialized.
        handler.sendEmptyMessage(MSG_VIEW_INITIATED);
        return handled;
    }

    @Override
    public void onAttachedToLayoutParams(@NonNull CoordinatorLayout.LayoutParams params) {
        super.onAttachedToLayoutParams(params);
        configuration.setTopMargin(params.topMargin);
        configuration.setBottomMargin(params.bottomMargin);
        // Configuration has changed.
        configuration.setSettled(false);
        if (handler == null) {
            handler = new Handler(this);
        }
    }

    @Override
    public void onDetachedFromLayoutParams() {
        super.onDetachedFromLayoutParams();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        pendingActions.clear();
        mListeners.clear();
        cancelAnimation();
    }

    protected void cancelAnimation() {
        if (offsetAnimator != null && offsetAnimator.isRunning()) {
            offsetAnimator.cancel();
        }
    }

    protected void animateOffsetDeltaWithDuration(CoordinatorLayout parent, View child,
                                                  int offsetDelta, int initialOffset,
                                                  int triggerOffset, int minOffset, int maxOffset,
                                                  long duration, int type) {
        animateOffsetWithDuration(parent, child, getTopAndBottomOffset() + offsetDelta,
                initialOffset, triggerOffset, minOffset, maxOffset, duration, type);
    }

    protected void animateOffsetWithDuration(final CoordinatorLayout parent, final View child,
                                             int destOffset, final int initialOffset,
                                             final int triggerOffset, final int minOffset,
                                             final int maxOffset, long duration, final int type) {
        int current = getTopAndBottomOffset();
        // No need to change offset.
        if (destOffset == current) {
            if (offsetAnimator != null && offsetAnimator.isRunning()) {
                offsetAnimator.cancel();
            }
            return;
        }
        if (offsetAnimator == null) {
            offsetAnimator = new SpringOffsetAnimator();
        } else {
            offsetAnimator.cancel();
        }
        offsetAnimator.animateOffsetWithDuration(current, destOffset, duration,
                new OffsetAnimator.AnimationUpdateListener() {
                    private int last;

                    @Override
                    public void onAnimationUpdate(int value) {
                        boolean offsetChanged = setTopAndBottomOffset(value);
                        if (!offsetChanged) {
                            parent.dispatchDependentViewsChanged(child);
                        }
                        for (ScrollingListener l : mListeners) {
                            l.onScroll(getParent(), getChild(), value,
                                    value - last, initialOffset, triggerOffset, minOffset, maxOffset, type);
                        }
                        last = value;
                    }

                    @Override
                    public void onAnimationEnd() {

                    }
                });
    }

    public CoordinatorLayout getParent() {
        return mParent;
    }

    public V getChild() {
        return mChild;
    }

    protected void addScrollListener(ScrollingListener listener) {
        if (null == listener)
            return;
        mListeners.add(listener);
    }

    protected void removeScrollListener(ScrollingListener listener) {
        if (null == listener) {
            return;
        }
        mListeners.remove(listener);
    }

    protected void runWithView(Runnable action) {
        if (action == null) return;
        if (getParent() == null || getChild() == null) {
            enqueuePendingActions(action);
        } else {
            runOnUiThread(action);
        }
    }

    protected void runOnUiThread(Runnable action) {
        if (action == null) return;
        if (handler == null) {
            handler = new Handler(this);
        }
        handler.post(action);
    }

    protected void enqueuePendingActions(Runnable action) {
        pendingActions.offer(action);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_VIEW_INITIATED:
                executePendingActions();
                break;
            default:
                break;
        }
        return true;
    }

    protected void executePendingActions() {
        Runnable action;
        while ((action = pendingActions.poll()) != null) {
            runOnUiThread(action);
        }
    }

    public void requestLayout() {
        runWithView(new Runnable() {
            @Override
            public void run() {
                getChild().requestLayout();
            }
        });
    }

    public CTR getController() {
        return controller;
    }

    public BehaviorConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(BehaviorConfiguration configuration) {
        this.configuration = configuration;
        requestLayout();
    }
}
