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

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import literefresh.behavior.Checkpoint;
import literefresh.behavior.CheckpointListener;
import literefresh.behavior.Configuration;
import literefresh.behavior.NestedScrollingListener;
import literefresh.controller.ScrollableBehaviorController;

public class EdgeStateManager implements NestedScrollingListener, CheckpointListener {

    private static final String TAG = EdgeStateManager.class.getName();
    ScrollableBehaviorController controller;

    RefreshStateManager refreshStateManager = new RefreshStateManager();
    ScrollableStateManager scrollableStateManager;

    public EdgeStateManager(ScrollableBehaviorController controller) {
        this.controller = controller;
        refreshStateManager.setRefreshStateHandler(refreshStateHandler);
        refreshStateManager.setRefreshStateListener(refreshStateListener);

        scrollableStateManager = new ScrollableStateManager();
        scrollableStateManager.setScrollableStateListener(refreshStateManager);
    }

    private RefreshStateManager.RefreshStateHandler refreshStateHandler = new RefreshStateManager.RefreshStateHandler() {
        @Override
        public void resetRefreshOffset() {
            controller.refreshHeader();
        }

        @Override
        public void resetOffset() {
            controller.reset();
        }
    };

    private RefreshStateManager.RefreshStateListener refreshStateListener = new RefreshStateManager.RefreshStateListener() {
        @Override
        public void onRefreshStateChanged(RefreshState state, Throwable throwable) {
            switch (state.getRefreshState()) {
                case RefreshStateManager.REFRESH_STATE_START:
                    controller.onRefreshStart();
                    break;
                case RefreshStateManager.REFRESH_STATE_READY:
                    controller.onReleaseToRefresh();
                    break;
                case RefreshStateManager.REFRESH_STATE_CANCELLED:
                    controller.stopScroll(false);
                    break;
                case RefreshStateManager.REFRESH_STATE_REFRESH:
                    controller.onRefresh();
                    break;
                case RefreshStateManager.REFRESH_STATE_COMPLETE:
                    controller.onRefreshComplete(throwable);
                    break;
                case RefreshStateManager.REFRESH_STATE_IDLE:
                default:
                    break;
            }
        }
    };

    ///////////////////////////////////////////////////////////////////////////
    // NestedScrollingListener
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void onStartScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, Configuration config, int type) {
        dispatchStart();
    }

    @Override
    public void onPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, Configuration config, int currentOffset, int type) {

    }

    @Override
    public void onScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, Configuration config, int currentOffset, int delta, int type) {
        Checkpoint closest = config.getTopEdgeConfig().findClosestCheckpoint(currentOffset);
        Checkpoint front = null;
        Checkpoint back = null;
        if (currentOffset >= closest.getOffsetConfig().getOffset()) {
            front = closest;
            back = closest.getMNext();
        } else {
            back = closest;
            front = closest.getMPrevious();
        }
        if (front == null || back == null) {
            Log.e(TAG, "onStopScroll front : " + front + " back: " + back);
            return;
        }

//        if (front.isAnchorPoint()) {
//            dispatchAnchorPointReached(front);
//        }
//
//        if (front.isTriggerPoint()) {
//            dispatchTriggerPointReached(front);
//        }

//        dispatchCheckpointReached(currentOffset, front, back);
    }

    @Override
    public void onStopScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, Configuration config, int currentOffset, int type) {
        Checkpoint closest = config.getTopEdgeConfig().findClosestCheckpoint(currentOffset);
        Checkpoint front = null;
        Checkpoint back = null;
        if (currentOffset >= closest.getOffsetConfig().getOffset()) {
            front = closest;
            back = closest.getMNext();
        } else {
            back = closest;
            front = closest.getMPrevious();
        }
        if (front == null || back == null) {
            Log.e(TAG, "onStopScroll front : " + front + " back: " + back);
            return;
        }

//        Checkpoint closestAnchor = closest;
//        while (closestAnchor != null && !closestAnchor.isAnchorPoint()) {
//            closestAnchor = closestAnchor.getMPrevious();
//            if (closestAnchor != null && closestAnchor.isAnchorPoint()) {
//                break;
//            }
//        }
//
//        if (closestAnchor != null && closestAnchor.isAnchorPoint()) {
//            dispatchAnchorPointStop(closestAnchor);
//        }
//
//        Checkpoint closestTrigger = front;
//        while (closestTrigger != null && !closestTrigger.isTriggerPoint()) {
//            closestTrigger = closestTrigger.getMPrevious();
//            if (closestTrigger != null && closestTrigger.isTriggerPoint()) {
//                break;
//            }
//        }
//        if (closestTrigger != null && closestTrigger.isTriggerPoint()) {
//            dispatchTriggerPointTrigger(closestTrigger);
//        }

//        dispatchStop(currentOffset, front, back);
    }

    private void dispatchStart() {
            scrollableStateManager.onStart(ScrollableBehaviorController.FLAG_EDGE_TOP_LEFT);
    }

//    private void dispatchCheckpointReached(int currentOffset, Checkpoint front, Checkpoint back) {
//            scrollableStateManager.onReached(, currentOffset, front, back);
//    }

//    private void dispatchStop(int currentOffset, Checkpoint front, Checkpoint back) {
//            scrollableStateManager.onStop(, currentOffset, front, back);
//    }


    ///////////////////////////////////////////////////////////////////////////
    // CheckpointListener
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void onStart(int edgeFlag) {

    }

    @Override
    public void onScroll(int edgeFlag, int currentOffset, Checkpoint front, Checkpoint back) {

    }

    @Override
    public void onStop(int edgeFlag, int currentOffset, Checkpoint front, Checkpoint back) {

    }
}
