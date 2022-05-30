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

import androidx.annotation.IntDef;

import literefresh.behavior.Checkpoint;
import literefresh.behavior.CheckpointListener;
import literefresh.controller.ScrollableBehaviorController;


public class ScrollableStateManager implements CheckpointListener {

    private static final String TAG = ScrollableStateManager.class.getName();

    public static final int STATE_IDLE = 0;
    public static final int STATE_START = 1;
    public static final int STATE_SCROLL = 2;
    public static final int STATE_STOP = 3;

    private int mState = STATE_IDLE;

    private ScrollableStateListener scrollableStateListener;

    @IntDef({STATE_IDLE, STATE_START, STATE_SCROLL, STATE_STOP})
    public @interface ScrollableStateFlag {
    }

    public interface ScrollableStateListener {
        void onScrollableStateChanged(ScrollableState scrollableState, Checkpoint front, Checkpoint back);
    }

    @Override
    public void onStart(@ScrollableBehaviorController.EdgeFlag int edgeFlag) {
        moveToState(edgeFlag, STATE_START, Integer.MIN_VALUE, null, null);
    }


    @Override
    public void onScroll(@ScrollableBehaviorController.EdgeFlag int edgeFlag, int currentOffset,
                         Checkpoint front, Checkpoint back) {
        moveToState(edgeFlag, STATE_SCROLL, currentOffset, front, back);
    }

    @Override
    public void onStop(@ScrollableBehaviorController.EdgeFlag int edgeFlag, int currentOffset,
                       Checkpoint front, Checkpoint back) {
        moveToState(edgeFlag, STATE_STOP, currentOffset, front, back);
        setState(STATE_IDLE);
    }

    private void moveToState(@ScrollableBehaviorController.EdgeFlag int edgeFlag, int state,
                             int offset, Checkpoint front, Checkpoint back) {
        switch (state) {
            case STATE_START:
                if (mState == STATE_IDLE) {
                    setState(state);
                    dispatchStateChanged(edgeFlag, offset, front, back);
                }
                break;

            case STATE_SCROLL:
                if (mState == STATE_START || mState == STATE_SCROLL) {
                    setState(state);
                    dispatchStateChanged(edgeFlag, offset, front, back);
                }
                break;

            case STATE_STOP:
                if (mState == STATE_SCROLL) {
                    setState(state);
                    dispatchStateChanged(edgeFlag, offset, front, back);
                }
                break;
            default:
                break;
        }
    }

    private void dispatchStateChanged(@ScrollableBehaviorController.EdgeFlag int edgeFlag,
                                      int offset, Checkpoint front, Checkpoint back) {
        if (scrollableStateListener != null) {
            scrollableStateListener.onScrollableStateChanged(new ScrollableState(edgeFlag, mState, offset), front, back);
        }
    }

    private void setState(int stateIdle) {
        mState = stateIdle;
    }

    public void setScrollableStateListener(ScrollableStateListener scrollableStateListener) {
        this.scrollableStateListener = scrollableStateListener;
    }
}
