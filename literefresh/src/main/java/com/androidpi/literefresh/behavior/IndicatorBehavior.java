/*
 * Copyright (C) 2015 The Android Open Source Project
 * Copyright 2020 yinpinjiu@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.androidpi.literefresh.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.math.MathUtils;
import androidx.core.view.ViewCompat;

/**
 * @see com.google.android.material.appbar.HeaderBehavior
 * @param <V>
 */
public abstract class IndicatorBehavior<V extends View>
        extends AnimationOffsetBehavior<V> {

    private final String TAG = "IndicatorBehavior";
    private static final int INVALID_POINTER = -1;

    @Nullable
    private Runnable flingRunnable;
    OverScroller scroller;

    private boolean isBeingDragged = false;
    private int touchSlop = -1;
    private int activePointerId = INVALID_POINTER;
    private int lastMotionY;
    private VelocityTracker velocityTracker;

    private boolean isDraggable = true;
    private boolean isFling = false;

    public IndicatorBehavior(Context context) {
        super(context);
    }

    public IndicatorBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull CoordinatorLayout parent, @NonNull V child, @NonNull MotionEvent ev) {
        if (!isDraggable)
            return super.onInterceptTouchEvent(parent, child, ev);
        if (touchSlop < 0) {
            touchSlop = ViewConfiguration.get(parent.getContext()).getScaledTouchSlop();
        }
        final int action = ev.getAction();
        if (action == MotionEvent.ACTION_MOVE && isBeingDragged) {
            Log.d(TAG, "intercept already being dragged");
            return true;
        }

        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                Log.d(TAG, "intercept down");
                isBeingDragged = false;
                final int x = (int) ev.getX();
                final int y = (int) ev.getY();
                if (isPointInChildBounds(parent, child, x, y)) {
                    Log.d(TAG, "intercept down is in bounds");
                    lastMotionY = y;
                    this.activePointerId = ev.getPointerId(0);
                    ensureVelocityTracker();
                }
            }
            break;

            case MotionEvent.ACTION_MOVE: {
                Log.d(TAG, "intercept move");
                final int activePointerId = this.activePointerId;
                if (activePointerId == INVALID_POINTER) {
                    break;
                }
                final int pointerIndex = ev.findPointerIndex(activePointerId);
                if (pointerIndex == -1) {
                    break;
                }
                logPointer("Intercept-move", pointerIndex, activePointerId);
                final int y = (int) ev.getY(pointerIndex);
                final int yDiff = Math.abs(y - lastMotionY);
                if (yDiff > touchSlop) {
                    Log.d(TAG, "intercept is being dragged");
                    isBeingDragged = true;
                    lastMotionY = y;
                }
            }
            break;

            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "intercept cancel");

            case MotionEvent.ACTION_UP:
                Log.d(TAG, "intercept up");

                isBeingDragged = false;
                this.activePointerId = INVALID_POINTER;
                if (velocityTracker != null) {
                    velocityTracker.recycle();
                    velocityTracker = null;
                }
                break;
        }

        if (velocityTracker != null) {
            velocityTracker.addMovement(ev);
        }
        Log.d(TAG, String.format("intercept %b", isBeingDragged));

        return isBeingDragged;
    }

    @Override
    public boolean onTouchEvent(@NonNull CoordinatorLayout parent, @NonNull V child, @NonNull MotionEvent ev) {
        if (!isDraggable)
            return super.onTouchEvent(parent, child, ev);
        int action = ev.getAction();
        int actionIndex = ev.getActionIndex();
        int pointerId = ev.getPointerId(actionIndex);

        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            {
                final int x = (int) ev.getX();
                final int y = (int) ev.getY();
                if (isPointInChildBounds(parent, child, x, y)) {
                    lastMotionY = y;
                    activePointerId = ev.getPointerId(0);
                    ensureVelocityTracker();
                } else {
                    return false;
                }
            }
            return true;
            case MotionEvent.ACTION_POINTER_DOWN:
                logPointer("down_pointer", actionIndex, pointerId);
                break;
            case MotionEvent.ACTION_MOVE:
            {
                Log.d(TAG, "touch move");
                final int activePointerIndex = ev.findPointerIndex(activePointerId);
                if (activePointerIndex == -1) {
                    return false;
                }
                final int y = (int) ev.getY(activePointerIndex);
                int dy = lastMotionY - y;
                if (!isBeingDragged && Math.abs(dy) > touchSlop) {
                    isBeingDragged = true;
                    if (dy > 0) {
                        // scroll up
                        dy -= touchSlop;
                    } else {
                        // scroll down
                        dy += touchSlop;
                    }
                }

                if (isBeingDragged) {
                    lastMotionY = y;
                    Log.d(TAG, "onScrollStart");
                    onScrollStart(parent, child);
                    scroll(parent, child, dy);
                }
            }
                break;

            case MotionEvent.ACTION_UP:
                logPointer("up", actionIndex, pointerId);
                if (velocityTracker != null) {
                    velocityTracker.addMovement(ev);
                    velocityTracker.computeCurrentVelocity(1000);
                    float yvel = velocityTracker.getYVelocity(activePointerId);
                    Log.d(TAG, "onFlingStart");
                    onFlingStart(parent, child);
                    fling(parent, child, yvel);
                }
            case MotionEvent.ACTION_CANCEL:
                logPointer("cancel", actionIndex, pointerId);
            {
                if (isBeingDragged && !isFling) {
                    Log.d(TAG, "onScrollStop");
                    onScrollStop(parent, child);
                }
                isBeingDragged = false;
                activePointerId = INVALID_POINTER;
                if (velocityTracker != null) {
                    velocityTracker.recycle();
                    velocityTracker = null;
                }
            }
                break;
        }

        if (velocityTracker != null) {
            velocityTracker.addMovement(ev);
        }
        return true;
    }

    private void ensureVelocityTracker() {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
    }

    final void scroll(
            CoordinatorLayout coordinatorLayout, V header, int dy) {
        updateTopBottomOffset(
                coordinatorLayout,
                header,
                getCurrentTopBottomOffset() - dy, -dy);
    }

    final boolean fling(
            CoordinatorLayout coordinatorLayout,
            @NonNull V layout,
            float velocityY) {

        if (flingRunnable != null) {
            layout.removeCallbacks(flingRunnable);
            flingRunnable = null;
        }

        if (scroller == null) {
            scroller = new OverScroller(layout.getContext());
        }

        // begin fling
        isFling = true;
        scroller.fling(0,
                getCurrentTopBottomOffset(), // curr
                0,
                Math.round(velocityY), // velocity.
                0,
                0, // x
                getMinTopBottomOffset(),
                getMaxTopBottomOffset());

        if (scroller.computeScrollOffset()) {
            flingRunnable = new FlingRunnable(coordinatorLayout, layout);
            ViewCompat.postOnAnimation(layout, flingRunnable);
            return true;
        } else {
            Log.d(TAG, "onFlingFinished");
            isFling = false;
            onFlingFinished(coordinatorLayout, layout);
            return false;
        }
    }

    int setContentTopBottomOffset(CoordinatorLayout parent, V header, int newOffset) {
        return setContentTopBottomOffset(
                parent, header, newOffset, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    int setContentTopBottomOffset(
            CoordinatorLayout parent, V header, int newOffset, int minOffset, int maxOffset) {
        final int curOffset = getTopAndBottomOffset();
        int consumed = 0;

        if (minOffset != 0 && curOffset >= minOffset && curOffset <= maxOffset) {
            // If we have some scrolling range, and we're currently within the min and max
            // offsets, calculate a new offset
            newOffset = MathUtils.clamp(newOffset, minOffset, maxOffset);

            if (curOffset != newOffset) {
                setTopAndBottomOffset(newOffset);
                // Update how much dy we have consumed
                consumed = curOffset - newOffset;
            }
        }

        return consumed;
    }

    int getTopBottomOffsetForScrollingSibling() {
        return getTopAndBottomOffset();
    }

    private void logPointer(String action, int actionIndex, int pointerId) {
        Log.d(TAG, String.format(action + ": %d, %d", actionIndex, pointerId));
    }

    private boolean isPointInChildBounds(CoordinatorLayout parent, View child, int x, int y) {
        int count = parent.getChildCount();
        if (count == 0)
            return false;
        for (int i = count - 1; i >= 0; i--) {
            View v = parent.getChildAt(i);
            if (parent.isPointInChildBounds(v, x, y)) {
                return v == child;
            }
        }
        return false;
    }

    @Override
    public IndicatorConfiguration getConfiguration() {
        return (IndicatorConfiguration) super.getConfiguration();
    }

    @Override
    IndicatorConfiguration.Builder newConfigBuilder() {
        return new IndicatorConfiguration.Builder(getConfiguration());
    }

    @Override
    public IndicatorConfiguration.Builder with(@NonNull Context context) {
        return new IndicatorConfiguration.Builder(context, this, getConfiguration());
    }

    private class FlingRunnable implements Runnable {

        private final CoordinatorLayout parent;
        private final V child;

        FlingRunnable(CoordinatorLayout parent, V layout) {
            this.parent = parent;
            this.child = layout;
        }

        @Override
        public void run() {
            if (child != null && scroller != null) {
                int last = getCurrentTopBottomOffset();
                if (scroller.computeScrollOffset()) {
                    int current = scroller.getCurrY();
                    updateTopBottomOffset(parent, child, current, current - last);
                    ViewCompat.postOnAnimation(child, this);
                } else {
                    Log.d(TAG, "onFlingFinished");
                    isFling = false;
                    onFlingFinished(parent, child);
                }
            }
        }
    }

    public abstract int getCurrentTopBottomOffset();
    public abstract int getMinTopBottomOffset();
    public abstract int getMaxTopBottomOffset();
    public abstract void updateTopBottomOffset(CoordinatorLayout parent, View child, int offset, int delta);
    public abstract void onScrollStart(CoordinatorLayout parent, V layout);
    public abstract void onScrollStop(CoordinatorLayout parent, V layout);
    public abstract void onFlingStart(CoordinatorLayout parent, V layout);
    /**
     * Called when a fling has finished, or the fling was initiated but there wasn't enough velocity
     * to start it.
     */
    public abstract void onFlingFinished(CoordinatorLayout parent, V layout);
}
