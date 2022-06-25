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

import androidx.annotation.IntDef;

import literefresh.behavior.Checkpoint;
import literefresh.behavior.CheckpointRange;

public class RefreshStateManager implements ScrollableStateManager.ScrollableStateListener {

    private static final String TAG = RefreshStateManager.class.getName();

    public static final int REFRESH_STATE_IDLE = 0;
    public static final int REFRESH_STATE_START = 1;
    public static final int REFRESH_STATE_READY = 2;
    public static final int REFRESH_STATE_REFRESH = 3;
    public static final int REFRESH_STATE_COMPLETE = 4;
    public static final int REFRESH_STATE_CANCELLED = 5;

    private int mState;

    @IntDef({REFRESH_STATE_IDLE, REFRESH_STATE_START, REFRESH_STATE_READY,
            REFRESH_STATE_REFRESH, REFRESH_STATE_COMPLETE, REFRESH_STATE_CANCELLED})
    public @interface RefreshStateFlag {
    }

    private RefreshStateListener refreshStateListener;
    private RefreshStateHandler refreshStateHandler;
    private CheckpointRange currentRange = new CheckpointRange();
    private Checkpoint anchorPoint;
    private Checkpoint triggerPoint;
    private boolean flingAsCommand = true;

    public interface RefreshStateListener {
        /**
         * A callback method when refresh state has changed.
         *
         * @param state     current state
         * @param throwable throwable when a state has changed if exists
         */
        void onRefreshStateChanged(RefreshState state, Throwable throwable);
    }

    public interface RefreshStateHandler {
        /**
         * Reset offset when in refreshing state.
         */
        void resetRefreshOffset();

        /**
         * Reset offset.
         */
        void resetOffset();
    }

    @Override
    public void onScrollableStateChanged(ScrollableState scrollableState, Checkpoint front, Checkpoint back) {
        CheckpointRange range = new CheckpointRange(front, back);
        final boolean isRangeChanged = isCurrentRangeChanged(range);
        Log.d(TAG, "onScrollableStateChanged: Scrollable-State:" + scrollableState.getState() + " isRangeChanged: " + isRangeChanged + " front-back:"
                + ((front != null && back != null) ? front.offset() + "-" + back.offset() : "null") + " Current-refresh-State: " + mState);
        switch (scrollableState.getState()) {
            case ScrollableStateManager.STATE_SCROLL_START:
                moveToRefreshState(REFRESH_STATE_START);
                break;
            case ScrollableStateManager.STATE_SCROLL:
            case ScrollableStateManager.STATE_FLING_START:
            case ScrollableStateManager.STATE_FLING_SCROLL:
                if (isRangeChanged) {
                    // Checkpoint range has changed.
                    if (scrollableState.isTopLeftEdge()) {
                        if (front.isTriggerPoint()) {
                            triggerPoint = front;
                            moveToRefreshState(REFRESH_STATE_READY);
                        } else {
                            triggerPoint = null;
                            moveToRefreshState(REFRESH_STATE_START);
                        }
                    } else if (scrollableState.isBottomRightEdge()) {
                        if (back.isTriggerPoint()) {
                            triggerPoint = back;
                            moveToRefreshState(REFRESH_STATE_READY);
                        } else {
                            triggerPoint = null;
                            moveToRefreshState(REFRESH_STATE_START);
                        }
                    }
                }
                break;

            case ScrollableStateManager.STATE_SCROLL_STOP_AFTER_FLING:
                if (flingAsCommand && scrollableState.getDirection() != ScrollableStateManager.SCROLL_DIRECTION_UNKNOWN) {
                    if (scrollableState.getDirection() == ScrollableStateManager.SCROLL_DIRECTION_UP) {
                        Checkpoint nextAnchor = front;
                        while (nextAnchor != null) {
                            if (nextAnchor.isAnchorPoint()) {
                                break;
                            }
                            if (nextAnchor.getMPrevious() == null) {
                                break;
                            }
                            nextAnchor = nextAnchor.getMPrevious();
                        }
                        anchorPoint = nextAnchor;
                    } else if (scrollableState.getDirection() == ScrollableStateManager.SCROLL_DIRECTION_DOWN) {
                        Checkpoint nextAnchor = back;
                        while (nextAnchor != null) {
                            if (nextAnchor.isAnchorPoint()) {
                                break;
                            }
                            if (nextAnchor.getMNext() == null) {
                                break;
                            }
                            nextAnchor = nextAnchor.getMNext();
                        }
                        anchorPoint = nextAnchor;
                    }
                    if (!moveToRefreshState(REFRESH_STATE_REFRESH)) {
                        // Another case is that we are still refreshing, no need to change the state.
                        // But need to reset the refreshing indicator's offset.
                        if (mState == REFRESH_STATE_REFRESH) {
                            if (refreshStateHandler != null) {
                                refreshStateHandler.resetRefreshOffset();
                            }
                        } else {
                            moveToRefreshState(REFRESH_STATE_CANCELLED);
                        }
                    }
                    break;
                }
            case ScrollableStateManager.STATE_SCROLL_STOP:
                // For the sake of we get a STATE_COMPLETE here.
                // It may happen when the next scroll started before the refresh complete.
                // So it will miss the onStartScroll() callback and the STATE_COMPLETE can
                // not be set to STATE_IDLE.
                if (scrollableState.isTopLeftEdge()) {
                    Checkpoint closestAnchor = back;
                    if (Math.abs(scrollableState.getOffset() - front.offset()) <= Math.abs(back.offset() - scrollableState.getOffset())) {
                        closestAnchor = front;
                    }
                    while (closestAnchor != null && !closestAnchor.isAnchorPoint()) {
                        closestAnchor = closestAnchor.getMPrevious();
                        if (closestAnchor != null && closestAnchor.isAnchorPoint()) {
                            break;
                        }
                    }

                    if (closestAnchor != null && closestAnchor.isAnchorPoint()) {
                        anchorPoint = closestAnchor;
                    }
                } else if (scrollableState.isBottomRightEdge()) {
                    Checkpoint closestAnchor = front;
                    if (Math.abs(scrollableState.getOffset() - back.offset()) <= Math.abs(front.offset() - scrollableState.getOffset())) {
                        closestAnchor = back;
                    }
                    while (closestAnchor != null && !closestAnchor.isAnchorPoint()) {
                        closestAnchor = closestAnchor.getMNext();
                        if (closestAnchor != null && closestAnchor.isAnchorPoint()) {
                            break;
                        }
                    }
                    if (closestAnchor != null && closestAnchor.isAnchorPoint()) {
                        anchorPoint = closestAnchor;
                    }
                }

                if (!moveToRefreshState(REFRESH_STATE_REFRESH)) {
                    // Another case is that we are still refreshing, no need to change the state.
                    // But need to reset the refreshing indicator's offset.
                    if (mState == REFRESH_STATE_REFRESH) {
                        if (refreshStateHandler != null) {
                            refreshStateHandler.resetRefreshOffset();
                        }
                    } else {
                        moveToRefreshState(REFRESH_STATE_CANCELLED);
                    }
                }
                break;
            default:
                break;
        }
        Log.d(TAG, "onScrollableStateChanged: Current-refresh-State: " + mState);
        currentRange.update(front, back);
    }

    public boolean isCurrentRangeChanged(CheckpointRange range) {
        return currentRange.isNull() || (!range.isNull() && currentRange.compareTo(range) != 0);
    }

    /**
     * Try to move to another state.
     *
     * @param newState the new state to which we are trying to move
     * @return true if state change succeed, false otherwise
     */
    protected boolean moveToRefreshState(int newState) {
        return moveToRefreshState(newState, null);
    }

    protected boolean moveToRefreshState(int newState, Throwable throwable) {
        switch (newState) {
            case REFRESH_STATE_IDLE:
                if (mState == REFRESH_STATE_COMPLETE
                        || mState == REFRESH_STATE_CANCELLED) {
                    setState(newState);
                    dispatchRefreshStateChanged(mState, throwable);
                    return true;
                }
                return false;
            case REFRESH_STATE_START:
                if (mState == REFRESH_STATE_IDLE
                        || mState == REFRESH_STATE_READY
                        || mState == REFRESH_STATE_COMPLETE
                        || mState == REFRESH_STATE_CANCELLED) {
                    setState(newState);
                    dispatchRefreshStateChanged(mState, throwable);
                    return true;
                }
                return false;

            case REFRESH_STATE_CANCELLED:
                if (mState == REFRESH_STATE_IDLE
                        || mState == REFRESH_STATE_START
                        || mState == REFRESH_STATE_COMPLETE) {
                    setState(newState);
                    dispatchRefreshStateChanged(mState, throwable);
                    return true;
                }
                return false;
            case REFRESH_STATE_READY:
                if (mState == REFRESH_STATE_START) {
                    setState(newState);
                    dispatchRefreshStateChanged(mState, throwable);
                    return true;
                }
                return false;
            case REFRESH_STATE_REFRESH:
                if (mState == REFRESH_STATE_IDLE
                        || mState == REFRESH_STATE_READY) {
                    setState(newState);
                    dispatchRefreshStateChanged(mState, throwable);
                    return true;
                }
                return false;
            case REFRESH_STATE_COMPLETE:
                if (mState == REFRESH_STATE_REFRESH) {
                    setState(newState);
                    dispatchRefreshStateChanged(mState, throwable);
                    return true;
                }
                return false;
            default:
                return false;
        }
    }

    private void setState(int newState) {
        mState = newState;
    }

    void dispatchRefreshStateChanged(int state, Throwable throwable) {
        if (refreshStateListener != null) {
            refreshStateListener.onRefreshStateChanged(new RefreshState(state, currentRange, anchorPoint, triggerPoint), throwable);
        }
    }

    public void setRefreshStateListener(RefreshStateListener refreshStateListener) {
        this.refreshStateListener = refreshStateListener;
    }

    public void setRefreshStateHandler(RefreshStateHandler refreshStateHandler) {
        this.refreshStateHandler = refreshStateHandler;
    }

    public boolean isRefreshing() {
        return mState == REFRESH_STATE_REFRESH;
    }

    public void refresh() {
        moveToRefreshState(REFRESH_STATE_REFRESH);
    }


    public void refreshComplete() {
        refreshCompleted(null);
    }


    public void refreshError(Throwable throwable) {
        refreshCompleted(throwable);
    }

    private void refreshCompleted(Throwable throwable) {
        moveToRefreshState(REFRESH_STATE_COMPLETE, throwable);
    }

    public CheckpointRange getCurrentRange() {
        return currentRange;
    }

    public Checkpoint getAnchorPoint() {
        return anchorPoint;
    }

    public Checkpoint getTriggerPoint() {
        return triggerPoint;
    }
}
