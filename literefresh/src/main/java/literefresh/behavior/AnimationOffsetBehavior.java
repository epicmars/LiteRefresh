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
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import literefresh.R;
import literefresh.animator.OffsetAnimator;
import literefresh.animator.SpringOffsetAnimator;
import literefresh.controller.BehaviorController;

/**
 * A behavior that with offset animation feature.
 */

public abstract class AnimationOffsetBehavior<V extends View>
        extends ViewOffsetBehavior<V> implements Handler.Callback {

    public static final int TYPE_UNKNOWN = 2;

    static final long HOLD_ON_DURATION = 500L;
    static final long DEFAULT_ANIM_DURATION = 300L;
    static final long RESET_DURATION = 300L;

    private static final int MSG_VIEW_INITIATED = 1;

    public static final float GOLDEN_RATIO = 0.618F;

    private OffsetAnimator offsetAnimator;
    protected V mChild;
    protected CoordinatorLayout mParent;
    protected List<NestedScrollingListener> mListeners = new ArrayList<>();
    protected Handler handler = new Handler(this);
    private Queue<Runnable> pendingActions = new LinkedList<>();
    protected BehaviorController controller;
    private Configuration config;

    public AnimationOffsetBehavior(Context context) {
        this(context, null);
    }

    public AnimationOffsetBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (config == null) {
            config = newConfigBuilder().build();
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.OffsetBehavior,
                0, 0);
//        boolean hasMaxOffsetRatioOfParent = a.hasValue(R.styleable.OffsetBehavior_lr_maxOffsetRatioOfParent);
//        boolean hasMaxOffset = a.hasValue(R.styleable.OffsetBehavior_lr_maxOffset);
//        if (hasMaxOffsetRatioOfParent) {
//            config.setMaxDownOffsetRatioOfParent(a.getFraction(
//                    R.styleable.OffsetBehavior_lr_maxOffsetRatioOfParent, 1, 2, 0f));
//        }
//        if (hasMaxOffset) {
//            config.setMaxDownOffset(a.getDimensionPixelOffset(
//                    R.styleable.OffsetBehavior_lr_maxOffset, 0));
//        }
//        // If maxOffset and maxOffsetRatio is not set then use default.
//        config.setUseDefaultMaxDownOffset(!hasMaxOffsetRatioOfParent && !hasMaxOffset);
//        config.setDefaultTriggerOffset(context.getResources().getDimensionPixelOffset(
//                R.dimen.lr_default_trigger_offset));
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
        // If child view's height have changed.
        if (config.getHeight() != child.getHeight()) {
            config.setHeight(child.getHeight());
            config.setSettled(false);
        }
        if (mParent == null) {
            mParent = parent;
        }
        if (mChild == null) {
            mChild = child;
        }
        // Execute pending actions which need view to be initialized.
        handler.sendEmptyMessage(MSG_VIEW_INITIATED);
        getConfig().onLayout(parent, child, layoutDirection);
        return handled;
    }

    @Override
    public void onAttachedToLayoutParams(@NonNull CoordinatorLayout.LayoutParams params) {
        super.onAttachedToLayoutParams(params);
        config.setTopMargin(params.topMargin);
        config.setBottomMargin(params.bottomMargin);
        // Configuration has changed.
        config.setSettled(false);
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

    public void cancelAnimation() {
        if (offsetAnimator != null && offsetAnimator.isRunning()) {
            offsetAnimator.cancel();
        }
    }

    protected void animateOffsetDeltaWithDuration(CoordinatorLayout parent,
                                                  V child,
                                                  ScrollableConfiguration config,
                                                  int offsetDelta,
                                                  long duration,
                                                  int type) {
        animateOffsetWithDuration(parent, child, offsetDelta, duration, type);
    }

    protected void animateOffsetWithDuration(final CoordinatorLayout parent, final V child,
                                             int offsetDelta, long duration, final int type) {
        final int current = getTopAndBottomOffset();
        int destOffset = current + offsetDelta;
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
                    private int last = current;

                    @Override
                    public void onAnimationUpdate(int value) {
                        boolean offsetChanged = setTopAndBottomOffset(value);
                        if (!offsetChanged) {
                            parent.dispatchDependentViewsChanged(child);
                        }
                        dispatchOnScroll(parent, child, value, type, offsetDelta);
                        last = value;
                    }

                    @Override
                    public void onAnimationEnd() {

                    }
                });
    }

    public int getBottomPosition() {
//        return getChild().getBottom() + getConfig().getBottomMargin();
        return getChild().getBottom();
    }

    public int getTopPosition() {
//        return getChild().getTop() - getConfig().getTopMargin();
        return getChild().getTop();
    }

    public CoordinatorLayout getParent() {
        return mParent;
    }

    public V getChild() {
        return mChild;
    }

    protected void addScrollListener(NestedScrollingListener listener) {
        if (null == listener)
            return;
        mListeners.add(listener);
    }

    protected void removeScrollListener(NestedScrollingListener listener) {
        if (null == listener) {
            return;
        }
        mListeners.remove(listener);
    }

    public void runWithView(Runnable action) {
        if (action == null) return;
        if (getParent() == null || getChild() == null) {
            enqueuePendingActions(action);
        } else {
            runOnUiThread(action);
        }
    }

    public void runOnUiThread(Runnable action) {
        if (action == null) return;
        if (handler == null) {
            handler = new Handler(this);
        }
        handler.post(action);
    }

    public void enqueuePendingActions(Runnable action) {
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

    public void executePendingActions() {
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

    public BehaviorController getController() {
        return controller;
    }

    protected void setConfig(Configuration config) {
        if (!config.isSettled()) {
            this.config = config;
            requestLayout();
        }
    }

    protected void dispatchOnStartScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, int type) {
        for (NestedScrollingListener l : mListeners) {
            l.onStartScroll(coordinatorLayout, child, getConfig(), type);
        }
    }

    protected void dispatchOnPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, int currentOffset, int type) {
        for (NestedScrollingListener l : mListeners) {
            l.onPreScroll(coordinatorLayout, child, getConfig(), currentOffset, type);
        }
    }

    protected void dispatchOnPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, int currentOffset, float velocityX, float velocityY) {
        for (NestedScrollingListener l : mListeners) {
            l.onPreFling(coordinatorLayout, child, getConfig(), currentOffset, velocityX, velocityY);
        }
    }

    protected void dispatchOnFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, int currentOffset, float velocityX, float velocityY) {
        for (NestedScrollingListener l : mListeners) {
            l.onFling(coordinatorLayout, child, getConfig(), currentOffset, velocityX, velocityY);
        }
    }

    protected void dispatchOnScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, int currentOffset, int offsetDelta, int type) {
        Log.d("ScrollableBehavior", "dispatchOnScroll currentOffset: " + currentOffset + " offsetDelta: " + offsetDelta);
        for (NestedScrollingListener l : mListeners) {
            l.onScroll(coordinatorLayout, child, getConfig(), currentOffset, offsetDelta, type);
        }
    }

    protected void dispatchOnStopScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, int currentOffset, int type) {
        for (NestedScrollingListener l : mListeners) {
            l.onStopScroll(coordinatorLayout, child, getConfig(), currentOffset, type);
        }
    }

    public Configuration getConfig() {
        return config;
    }

    abstract Configuration.Builder newConfigBuilder();

    public abstract Configuration.Builder with(@NonNull Context context);
}
