/*
 * Copyright 2022 yinpinjiu@gmail.com
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
package literefresh.state;

import android.util.Log;
import android.view.View;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import literefresh.behavior.Checkpoint;
import literefresh.behavior.CheckpointListener;
import literefresh.behavior.Configuration;
import literefresh.controller.ScrollableBehaviorController;


public class ScrollableStateManager implements CheckpointListener {

    private static final String TAG = ScrollableStateManager.class.getName();

    public static final int STATE_IDLE = 0;
    /**
     * [TOUCH] Scroll start
     */
    public static final int STATE_SCROLL_START = 1;
    /**
     * [TOUCH] Scrolling
     */
    public static final int STATE_SCROLL = 2;
    /**
     * [TOUCH] Scroll stop,
     * If it happens after a fling has started and not finished.
     * It will be translate to {@link ScrollableStateManager#STATE_SCROLL_STOP_AFTER_FLING_STARTED}
     */
    public static final int STATE_SCROLL_STOP = 3;
    /**
     * [Fling] Fling start
     */
    public static final int STATE_FLING_START = 4;
    /**
     * [Fling] Scroll start initiated by a fling
     */
    public static final int STATE_FLING_SCROLL = 5;
    /**
     * [Fling] Scroll stop event initiated by a scroll happened after fling has started
     */
    public static final int STATE_SCROLL_STOP_AFTER_FLING_STARTED = 6;
    /**
     * [Fling] Scroll stop event initiated by a fling happened。
      */
    public static final int STATE_FLING_STOP = 7;

    public static final int SCROLL_DIRECTION_UNKNOWN = 0;
    public static final int SCROLL_DIRECTION_DOWN = 1;
    public static final int SCROLL_DIRECTION_UP = 2;

    private int mState = STATE_IDLE;
    private int mDirection = SCROLL_DIRECTION_UNKNOWN;

    private ScrollableStateListener scrollableStateListener;

    @IntDef({STATE_IDLE, STATE_SCROLL_START, STATE_SCROLL, STATE_SCROLL_STOP,
            STATE_FLING_START, STATE_FLING_SCROLL, STATE_FLING_STOP})
    public @interface ScrollableStateFlag {
    }


    public interface ScrollableStateListener {
        void onScrollableStateChanged(ScrollableState scrollableState, Checkpoint front,
                                      Checkpoint back);
    }

    @Override
    public void onStart(@ScrollableBehaviorController.EdgeFlag int edgeFlag,
                        @ViewCompat.NestedScrollType int type) {
        moveToState(edgeFlag, type == ViewCompat.TYPE_TOUCH
                        ? STATE_SCROLL_START : STATE_FLING_START,
                Integer.MIN_VALUE, 0, null, null, type);
    }

    @Override
    public void onScroll(@ScrollableBehaviorController.EdgeFlag int edgeFlag, int currentOffset,
                         int offsetDelta, Checkpoint front, Checkpoint back, @ViewCompat.NestedScrollType int type) {
        moveToState(edgeFlag, type == ViewCompat.TYPE_TOUCH ? STATE_SCROLL : STATE_FLING_SCROLL,
                currentOffset, offsetDelta, front, back, type);
    }


    @Override
    public void onFling(@ScrollableBehaviorController.EdgeFlag int edgeFlag,
                        @NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
                        Configuration config, int currentOffset, float velocityX, float velocityY) {
        moveToState(edgeFlag, STATE_FLING_START, Integer.MIN_VALUE, 0, null,
                null, ViewCompat.TYPE_NON_TOUCH);
    }

    @Override
    public void onStop(@ScrollableBehaviorController.EdgeFlag int edgeFlag, int currentOffset,
                       Checkpoint front, Checkpoint back, @ViewCompat.NestedScrollType int type) {
        moveToState(edgeFlag, type == ViewCompat.TYPE_TOUCH ? STATE_SCROLL_STOP : STATE_FLING_STOP,
                currentOffset, 0, front, back, type);
    }

    private void moveToState(@ScrollableBehaviorController.EdgeFlag int edgeFlag, int state,
                             int offset, int delta, Checkpoint front, Checkpoint back, int type) {
        Log.d(TAG, "edge: " + edgeFlag + " moveToState: " + state + " Current-state: " + mState + " delta: " + delta);
        switch (state) {
            case STATE_SCROLL_START:
                if (mState == STATE_IDLE) {
                    moveToTargetState(edgeFlag, state, offset, delta, front, back);
                }
                break;

            case STATE_SCROLL:
                if (mState == STATE_SCROLL_START || mState == STATE_SCROLL) {
                    moveToTargetState(edgeFlag, state, offset, delta, front, back);
                }
                break;
            case STATE_FLING_START:
                if (mState == STATE_SCROLL) {
                    moveToTargetState(edgeFlag, state, offset, delta, front, back);
                }
                break;
            case STATE_FLING_SCROLL:
                if (mState == STATE_FLING_START || mState == STATE_SCROLL_STOP_AFTER_FLING_STARTED || mState == STATE_FLING_SCROLL) {
                    moveToTargetState(edgeFlag, state, offset, delta, front, back);
                }
                break;
            case STATE_SCROLL_STOP:
                if (mState == STATE_SCROLL) {
                    moveToTargetState(edgeFlag, state, offset, delta, front, back);
                    setState(STATE_IDLE);
                } else if (mState == STATE_FLING_START || mState == STATE_FLING_SCROLL) {
                    moveToTargetState(edgeFlag, STATE_SCROLL_STOP_AFTER_FLING_STARTED, offset, delta, front, back);
                }
                break;

            case STATE_FLING_STOP:
                if (mState == STATE_FLING_SCROLL || mState == STATE_SCROLL_STOP_AFTER_FLING_STARTED) {
                    moveToTargetState(edgeFlag, state, offset, delta, front, back);
                    setState(STATE_IDLE);
                }
                break;
            default:
                break;
        }
        Log.d(TAG, "edge: " + edgeFlag + " Moved to scrollable State: " + mState);
    }

    private void moveToTargetState(@ScrollableBehaviorController.EdgeFlag int edgeFlag, int state,
                                   int offset, int delta, Checkpoint front, Checkpoint back) {
        setState(state);
        if (delta < 0) {
            mDirection = SCROLL_DIRECTION_UP;
        } else if (delta > 0) {
            mDirection = SCROLL_DIRECTION_DOWN;
        }
        dispatchStateChanged(edgeFlag, offset, mDirection, front, back);
    }

    private void dispatchStateChanged(@ScrollableBehaviorController.EdgeFlag int edgeFlag,
                                      int offset, int direction, Checkpoint front, Checkpoint back) {
        if (scrollableStateListener != null) {
            ScrollableState scrollableState = new ScrollableState(edgeFlag, mState,
                    offset);
            scrollableState.setDirection(direction);
            scrollableStateListener.onScrollableStateChanged(scrollableState, front, back);
        }
    }

    private void setState(int stateIdle) {
        mState = stateIdle;
    }

    public void setScrollableStateListener(ScrollableStateListener scrollableStateListener) {
        this.scrollableStateListener = scrollableStateListener;
    }
}
