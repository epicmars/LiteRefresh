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
package com.androidpi.literefresh.state;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;

import com.androidpi.literefresh.Refresher;
import com.androidpi.literefresh.behavior.AnimationOffsetBehavior;

/**
 * The content behavior's refreshing or loading state machine, it update states based on the
 * behavior's offset changes.
 */
public class StateMachine implements AnimationOffsetBehavior.ScrollingListener, Refresher {

    public interface StateHandler {

        /**
         * Offset that can makes an indicator's hidden part visible.
         *
         * @param currentOffset the current offset of content
         * @return true if current offset can make the indicator's hidden part visible, false otherwise
         */
        boolean isValidOffset(int currentOffset);

        /**
         * Transform content's offset coordinate to indicator's coordinate system.
         *
         * @param currentOffset current vertical offset of content view.
         * @return transformed offset in indicator's coordinate system.
         */
        int transform(int currentOffset);

        /**
         * A positive offset in content's coordinate system, if content's vertical
         * offset has reach this point then it can move to a ready or releasing to refresh state.
         */
        int readyRefreshOffset();

        /**
         * If a controller has any refresh state listeners.
         *
         * @return true if the controller has refresh listeners set, false otherwise
         */
        boolean hasRefreshStateListeners();

        /**
         * A callback method when refresh state has changed.
         *
         * @param state     current state
         * @param throwable throwable when a state has changed if exists
         */
        void onStateChanged(int state, Throwable throwable);

        /**
         * Reset offset when in refreshing state.
         */
        void resetRefreshOffset();

        /**
         * Reset offset.
         */
        void resetOffset();
    }

    public static final int STATE_IDLE = 0;
    public static final int STATE_START = 1;
    public static final int STATE_CANCELLED = 2;
    public static final int STATE_READY = 3;
    public static final int STATE_REFRESH = 4;
    public static final int STATE_COMPLETE = 5;

    protected int mState = STATE_IDLE;

    private StateHandler mStateHandler;

    public StateMachine(StateHandler stateHandler) {
        this.mStateHandler = stateHandler;
    }

    @Override
    public void onStartScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
                              int initial, int trigger, int min, int max, int type) {
        // If current state is ready, when touch event is MotionEvent.ACTION_UP, may be followed a
        // fling that start another scroll immediately.
        moveToState(STATE_IDLE);
    }

    @Override
    public void onPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
                            int current, int initial, int trigger, int min, int max, int type) {
        // Only allow STATE_IDLE to translate to STATE_START here.
        if (mState == STATE_IDLE) {
            moveToState(STATE_START);
        }
    }

    @Override
    public void onScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
                         int current, int delta, int initial, int trigger, int min, int max, int type) {
        if (!mStateHandler.hasRefreshStateListeners() || !mStateHandler.isValidOffset(current)) {
            return;
        }
        if (mStateHandler.transform(current) >= mStateHandler.readyRefreshOffset()) {
            moveToState(STATE_READY);
        } else {
            moveToState(STATE_START);
        }
    }

    @Override
    public void onStopScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
                             int current, int initial, int trigger, int min, int max, int type) {
        // When child start dispatching touch events, the MotionEvent.DOWN event may cause
        // a defensive clean up for new gesture.
        if (!mStateHandler.isValidOffset(current)) {
            return;
        }
        if (!mStateHandler.hasRefreshStateListeners()) {
            moveToState(STATE_CANCELLED);
            return;
        }
        if (mStateHandler.transform(current) >= mStateHandler.readyRefreshOffset()) {
            // For the sake of we get a STATE_COMPLETE here.
            // It may happen when the next scroll started before the refresh complete.
            // So it will miss the onStartScroll() callback and the STATE_COMPLETE can
            // not be set to STATE_IDLE.
            if (!moveToState(STATE_REFRESH)) {
                // Another case is that we are still refreshing, no need to change the state.
                // But need to reset the refreshing indicator's offset.
                if (mState == STATE_REFRESH) {
                    mStateHandler.resetRefreshOffset();
                } else {
                    moveToState(STATE_CANCELLED);
                }
            }
        } else {
            moveToState(STATE_CANCELLED);
        }
    }

    protected boolean moveToState(int state, Throwable throwable) {
        switch (state) {
            case STATE_IDLE:
                if (mState == STATE_COMPLETE || mState == STATE_CANCELLED) {
                    mState = state;
                    onStateChanged(mState, throwable);
                    return true;
                }
                return false;
            case STATE_START:
                if (mState == STATE_IDLE || mState == STATE_READY) {
                    mState = state;
                    onStateChanged(mState, throwable);
                    return true;
                }
                return false;
            case STATE_CANCELLED:
                if (mState == STATE_START || mState == STATE_COMPLETE) {
                    mState = state;
                    onStateChanged(mState, throwable);
                    return true;
                }
                return false;
            case STATE_READY:
                if (mState == STATE_START) {
                    mState = state;
                    onStateChanged(mState, throwable);
                    return true;
                }
                return false;
            case STATE_REFRESH:
                if (mState == STATE_IDLE || mState == STATE_READY) {
                    mState = state;
                    onStateChanged(mState, throwable);
                    return true;
                }
                return false;
            case STATE_COMPLETE:
                if (mState == STATE_REFRESH) {
                    mState = state;
                    onStateChanged(mState, throwable);
                    return true;
                }
                return false;
            default:
                return false;
        }
    }

    /**
     * Try to move to another state.
     *
     * @param state the new state to which we are trying to move
     * @return true if state change succeed, false otherwise
     */
    protected boolean moveToState(int state) {
        return moveToState(state, null);
    }

    void onStateChanged(int state, Throwable throwable) {
        if (mStateHandler == null) return;
        mStateHandler.onStateChanged(state, throwable);
    }

    public boolean isRefreshing() {
        return mState == STATE_REFRESH;
    }

    @Override
    public void refresh() {
        moveToState(STATE_REFRESH);
    }

    @Override
    public void refreshComplete() {
        refreshCompleted(null);
    }

    @Override
    public void refreshError(Throwable throwable) {
        refreshCompleted(throwable);
    }

    private void refreshCompleted(Throwable throwable) {
        moveToState(STATE_COMPLETE, throwable);
    }

}
