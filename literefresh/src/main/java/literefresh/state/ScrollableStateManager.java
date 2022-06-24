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
    public static final int STATE_SCROLL_START = 1;
    public static final int STATE_SCROLL = 2;
    public static final int STATE_SCROLL_STOP = 3;
    public static final int STATE_FLING = 4;
    public static final int STATE_FLING_START = 5;
    public static final int STATE_FLING_SCROLL = 6;
    public static final int STATE_SCROLL_STOP_AFTER_FLING = 7;
    public static final int STATE_FLING_STOP = 8;
//    public static final int STATE_STOP = 8;

    private int mState = STATE_IDLE;

    private ScrollableStateListener scrollableStateListener;

    @IntDef({STATE_IDLE, STATE_SCROLL_START, STATE_SCROLL, STATE_SCROLL_STOP, STATE_FLING,
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
                Integer.MIN_VALUE, null, null, type);
    }

    @Override
    public void onScroll(@ScrollableBehaviorController.EdgeFlag int edgeFlag, int currentOffset,
                         Checkpoint front, Checkpoint back, @ViewCompat.NestedScrollType int type) {
        moveToState(edgeFlag, type == ViewCompat.TYPE_TOUCH ? STATE_SCROLL : STATE_FLING_SCROLL,
                currentOffset, front, back, type);
    }

    @Override
    public void onFling(@ScrollableBehaviorController.EdgeFlag int edgeFlag,
                        @NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
                        Configuration config, int currentOffset, float velocityX, float velocityY) {
        moveToState(edgeFlag, STATE_FLING, Integer.MIN_VALUE, null, null,
                ViewCompat.TYPE_NON_TOUCH);
    }

    @Override
    public void onStop(@ScrollableBehaviorController.EdgeFlag int edgeFlag, int currentOffset,
                       Checkpoint front, Checkpoint back, @ViewCompat.NestedScrollType int type) {
        moveToState(edgeFlag, type == ViewCompat.TYPE_TOUCH ? STATE_SCROLL_STOP : STATE_FLING_STOP,
                currentOffset, front, back, type);
    }

    private void moveToState(@ScrollableBehaviorController.EdgeFlag int edgeFlag, int state,
                             int offset, Checkpoint front, Checkpoint back, int type) {
        Log.d(TAG, "edge: " + edgeFlag + " moveToState: " + state + " Current-state: " + mState);
        switch (state) {
            case STATE_SCROLL_START:
                if (mState == STATE_IDLE) {
                    moveToTargetState(edgeFlag, state, offset, front, back);
                }
                break;

            case STATE_SCROLL:
                if (mState == STATE_SCROLL_START || mState == STATE_SCROLL) {
                    moveToTargetState(edgeFlag, state, offset, front, back);
                }
                break;

            case STATE_FLING:
                if (mState == STATE_SCROLL) {
                    moveToTargetState(edgeFlag, state, offset, front, back);
                }
                break;
            case STATE_FLING_START:
                if (mState == STATE_FLING) {
                    moveToTargetState(edgeFlag, state, offset, front, back);
                }
                break;
            case STATE_FLING_SCROLL:
                if (mState == STATE_FLING_START || mState == STATE_SCROLL_STOP_AFTER_FLING || mState == STATE_FLING_SCROLL) {
                    moveToTargetState(edgeFlag, state, offset, front, back);
                }
                break;
            case STATE_SCROLL_STOP:
                if (mState == STATE_SCROLL) {
                    moveToTargetState(edgeFlag, state, offset, front, back);
                    setState(STATE_IDLE);
                } else if (mState == STATE_FLING || mState == STATE_FLING_START
                        || mState == STATE_FLING_SCROLL) {
                    moveToTargetState(edgeFlag, STATE_SCROLL_STOP_AFTER_FLING, offset, front, back);
                }
                break;

            case STATE_FLING_STOP:
                if (mState == STATE_FLING_SCROLL || mState == STATE_SCROLL_STOP_AFTER_FLING) {
                    moveToTargetState(edgeFlag, state, offset, front, back);
                    setState(STATE_IDLE);
                }
                break;
            default:
                break;
        }
        Log.d(TAG, "edge: " + edgeFlag + " Moved to scrollable State: " + mState);
    }

    private void moveToTargetState(@ScrollableBehaviorController.EdgeFlag int edgeFlag, int state,
                                   int offset, Checkpoint front, Checkpoint back) {
        setState(state);
        dispatchStateChanged(edgeFlag, offset, front, back);
    }

    private void dispatchStateChanged(@ScrollableBehaviorController.EdgeFlag int edgeFlag,
                                      int offset, Checkpoint front, Checkpoint back) {
        if (scrollableStateListener != null) {
            scrollableStateListener.onScrollableStateChanged(new ScrollableState(edgeFlag, mState,
                    offset), front, back);
        }
    }

    private void setState(int stateIdle) {
        mState = stateIdle;
    }

    public void setScrollableStateListener(ScrollableStateListener scrollableStateListener) {
        this.scrollableStateListener = scrollableStateListener;
    }
}
