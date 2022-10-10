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
package literefresh.controller;

import static literefresh.state.StateMachine.STATE_CANCELLED;
import static literefresh.state.StateMachine.STATE_COMPLETE;
import static literefresh.state.StateMachine.STATE_IDLE;
import static literefresh.state.StateMachine.STATE_READY;
import static literefresh.state.StateMachine.STATE_REFRESH;
import static literefresh.state.StateMachine.STATE_START;
import static literefresh.state.StateMachine.StateHandler;

import android.util.Log;
import android.view.View;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import java.util.ArrayList;
import java.util.List;

import literefresh.OnLoadListener;
import literefresh.OnRefreshListener;
import literefresh.behavior.Checkpoint;
import literefresh.behavior.CheckpointListener;
import literefresh.behavior.Configuration;
import literefresh.behavior.ScrollableBehavior;
import literefresh.behavior.ScrollableConfiguration;
import literefresh.state.RefreshState;
import literefresh.state.RefreshStateManager;
import literefresh.state.ScrollableStateManager;

/**
 * Content behavior's controller, it has to state machines that manage header and footer view's
 * refreshing or loading state respectively.
 */
public class ScrollableBehaviorController extends BehaviorController<ScrollableBehavior>
        implements OnRefreshListener, OnLoadListener {

    private static final String TAG = ScrollableBehaviorController.class.getName();
    private static final long HOLD_ON_DURATION = 500L;
    private static final long SHOW_DURATION = 300L;
    private static final long RESET_DURATION = 300L;

    public static final int FLAG_EDGE_UNKNOWN = 0;
    public static final int FLAG_EDGE_TOP_LEFT = 1;
    public static final int FLAG_EDGE_BOTTOM_RIGHT = 2;


    public static final int SCROLL_DIRECTION_UP_LEFT = 0;
    public static final int SCROLL_DIRECTION_DOWN_RIGHT = 1;

    @IntDef({FLAG_EDGE_UNKNOWN, FLAG_EDGE_TOP_LEFT, FLAG_EDGE_BOTTOM_RIGHT})
    public @interface EdgeFlag {
    }

    @IntDef({SCROLL_DIRECTION_UP_LEFT, SCROLL_DIRECTION_DOWN_RIGHT})
    public @interface ScrollDirection {
    }


    private final StateHandler footerStateHandler = new StateHandler() {
        @Override
        public boolean isValidOffset(int currentOffset) {
//            return transform(currentOffset) > behavior.getFooterConfig().getInitialVisibleHeight();
            return true;
        }

        @Override
        public int transform(int currentOffset) {
            // The current offset here is the content's top and bottom offset.
            return -(currentOffset + behavior.getChild().getHeight())
                    + behavior.getParent().getHeight();
        }

        @Override
        public int readyRefreshOffset() {
//            return behavior.getFooterConfig().getTriggerOffset()
//                    + behavior.getFooterConfig().getInitialVisibleHeight();
            return 100;
        }

        @Override
        public boolean hasRefreshStateListeners() {
            return hasOnLoadListeners();
        }

        @Override
        public void resetRefreshOffset() {
            refreshFooter();
        }

        @Override
        public void resetOffset() {
            reset();
        }

        @Override
        public void onStateChanged(int state, Throwable throwable) {
            switch (state) {
                case STATE_START:
                    onLoadStart();
                    break;
                case STATE_READY:
                    onReleaseToLoad();
                    break;
                case STATE_CANCELLED:
                    stopScroll(false);
                    break;
                case STATE_REFRESH:
                    onLoad();
                    refreshFooter();
                    break;
                case STATE_COMPLETE:
                    stopScroll(true);
                    onLoadComplete(throwable);
                    break;
                case STATE_IDLE:
                default:
                    break;
            }
        }
    };

    private StateHandler headerStateHandler = new StateHandler() {

        @Override
        public boolean isValidOffset(int currentOffset) {
//            return transform(currentOffset) > behavior.getHeaderConfig().getInitialVisibleHeight();
            return true;
        }

        @Override
        public int transform(int currentOffset) {
            // The current offset here is the content's top and bottom offset.
            return currentOffset;
        }

        @Override
        public int readyRefreshOffset() {
//            return behavior.getHeaderConfig().getTriggerOffset()
//                    + behavior.getHeaderConfig().getInitialVisibleHeight();
            return 100;
        }

        @Override
        public boolean hasRefreshStateListeners() {
            return hasOnRefreshListeners();
        }

        @Override
        public void resetRefreshOffset() {
            refreshHeader();
        }

        @Override
        public void resetOffset() {
            reset();
        }

        @Override
        public void onStateChanged(int state, Throwable throwable) {
            switch (state) {
                case STATE_START:
                    onRefreshStart();
                    break;
                case STATE_READY:
                    onReleaseToRefresh();
                    break;
                case STATE_CANCELLED:
                    stopScroll(false);
                    break;
                case STATE_REFRESH:
                    onRefresh();
                    refreshHeader();
                    break;
                case STATE_COMPLETE:
                    stopScroll(true);
                    onRefreshComplete(throwable);
                    break;
                case STATE_IDLE:
                default:
                    break;
            }
        }
    };

    private RefreshStateManager.RefreshStateHandler topEdgeRefreshStateHandler
            = new RefreshStateManager.RefreshStateHandler() {
        @Override
        public void resetRefreshOffset() {
            refreshHeader();
        }

        @Override
        public void resetOffset() {
            reset();
        }
    };

    private RefreshStateManager.RefreshStateListener topEdgeRefreshStateListener
            = new RefreshStateManager.RefreshStateListener() {
        @Override
        public void onRefreshStateChanged(RefreshState state, Throwable throwable) {
            Log.d("topRefreshState", "onRefreshStateChanged: " + state.getRefreshState());
            switch (state.getRefreshState()) {
                case RefreshStateManager.REFRESH_STATE_START:
                    onRefreshStart();
                    break;
                case RefreshStateManager.REFRESH_STATE_READY:
                    onReleaseToRefresh();
                    break;
                case RefreshStateManager.REFRESH_STATE_CANCELLED:
                    // fixme Attempt java.lang.NullPointerException: to invoke virtual method
                    //  'int literefresh.behavior.Checkpoint.offset()' on a null object reference
                    if (topEdgeRefreshStateManager.getAnchorPoint() == null) {
                        stopScroll(false);
                    } else {
                        behavior.animateToPosition(topEdgeRefreshStateManager.getAnchorPoint().offset());
                    }
                    break;
                case RefreshStateManager.REFRESH_STATE_REFRESH:
                    onRefresh();
                    break;
                case RefreshStateManager.REFRESH_STATE_COMPLETE:
                    onRefreshComplete(throwable);
                    break;
                case RefreshStateManager.REFRESH_STATE_IDLE:
                default:
                    break;
            }
        }
    };

    private RefreshStateManager.RefreshStateHandler bottomEdgeRefreshStateHandler
            = new RefreshStateManager.RefreshStateHandler() {
        @Override
        public void resetRefreshOffset() {
            refreshFooter();
        }

        @Override
        public void resetOffset() {
            reset();
        }
    };

    private RefreshStateManager.RefreshStateListener bottomEdgeRefreshStateListener = new RefreshStateManager.RefreshStateListener() {
        @Override
        public void onRefreshStateChanged(RefreshState state, Throwable throwable) {
            Log.d("bottomRefreshState", "onRefreshStateChanged: " + state.getRefreshState());
            switch (state.getRefreshState()) {
                case RefreshStateManager.REFRESH_STATE_START:
                    onLoadStart();
                    break;
                case RefreshStateManager.REFRESH_STATE_READY:
                    onReleaseToLoad();
                    break;
                case RefreshStateManager.REFRESH_STATE_CANCELLED:
                    stopScroll(false);
                    break;
                case RefreshStateManager.REFRESH_STATE_REFRESH:
                    onLoad();
                    break;
                case RefreshStateManager.REFRESH_STATE_COMPLETE:
                    onLoadComplete(throwable);
                    break;
                case RefreshStateManager.REFRESH_STATE_IDLE:
                default:
                    break;
            }
        }
    };

    RefreshStateManager topEdgeRefreshStateManager = new RefreshStateManager();
    RefreshStateManager bottomEdgeRefreshStateManager = new RefreshStateManager();
    ScrollableStateManager topScrollableStateManager = new ScrollableStateManager();
    ScrollableStateManager bottomScrollableStateManager = new ScrollableStateManager();


    private List<CheckpointListener> checkpointListeners = new ArrayList<CheckpointListener>() {
        {
            topEdgeRefreshStateManager.setRefreshStateHandler(topEdgeRefreshStateHandler);
            topEdgeRefreshStateManager.setRefreshStateListener(topEdgeRefreshStateListener);

            topScrollableStateManager.setScrollableStateListener(topEdgeRefreshStateManager);
            add(topScrollableStateManager);

            bottomEdgeRefreshStateManager.setRefreshStateHandler(bottomEdgeRefreshStateHandler);
            bottomEdgeRefreshStateManager.setRefreshStateListener(bottomEdgeRefreshStateListener);

            bottomScrollableStateManager.setScrollableStateListener(bottomEdgeRefreshStateManager);
            add(bottomScrollableStateManager);
        }
    };

    public ScrollableBehaviorController(ScrollableBehavior behavior) {
        super(behavior);
    }

    private boolean isTopInRange(@NonNull View child, Configuration config) {
        return child.getTop() >= config.getTopEdgeConfig().getMinOffset();
    }

    private boolean isBottomInRange(@NonNull View child, Configuration config) {
        return getBehavior().getBottomPosition() < config.getBottomEdgeConfig().getMaxOffset();
    }

    @Override
    public void onStartScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
                              Configuration config, @ViewCompat.NestedScrollType int type) {
//        for (StateMachine stateMachine : stateMachines) {
//            stateMachine.onStartScroll(coordinatorLayout, child, config, type);
//        }
        Log.d(TAG, "onStartScroll: type=" + type);
        super.onStartScroll(coordinatorLayout, child, config, type);
        topScrollableStateManager.onStart(FLAG_EDGE_TOP_LEFT, type);
        bottomScrollableStateManager.onStart(FLAG_EDGE_BOTTOM_RIGHT, type);
    }

    @Override
    public void onPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
                            Configuration config, int currentOffset,
                            @ViewCompat.NestedScrollType int type) {
//        for (StateMachine stateMachine : stateMachines) {
//            stateMachine.onPreScroll(coordinatorLayout, child, config, currentOffset, type);
//        }
        super.onPreScroll(coordinatorLayout, child, config, currentOffset, type);
    }

    @Override
    public void onPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
                           Configuration config, int currentOffset, float velocityX, float velocityY) {
        super.onPreFling(coordinatorLayout, child, config, currentOffset, velocityX, velocityY);
    }

    @Override
    public void onFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
                        Configuration config, int currentOffset, float velocityX, float velocityY) {
        super.onFling(coordinatorLayout, child, config, currentOffset, velocityX, velocityY);
//        dispatchFling();
        // Scroll down
        if (velocityY < 0) {
            if (getBehavior().getTopPosition() <= getConfig().getTopEdgeConfig().getMinOffset()) {
                topScrollableStateManager.onFling(FLAG_EDGE_TOP_LEFT, coordinatorLayout, child,
                        config, currentOffset, velocityX, velocityY);
            }
        } else if (velocityY > 0) {
            if (getBehavior().getBottomPosition() >= getConfig().getBottomEdgeConfig().getMinOffset()) {
                bottomScrollableStateManager.onFling(FLAG_EDGE_BOTTOM_RIGHT, coordinatorLayout,
                        child, config, currentOffset, velocityX, velocityY);
            }
        }
    }

    @Override
    public void onScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
                         Configuration config, int currentOffset, int delta,
                         @ViewCompat.NestedScrollType int type) {
        ScrollableConfiguration cfg = getBehavior().getConfig();

        // The original StateMachine implements pull to refresh and pull to load more.
//        for (StateMachine stateMachine : stateMachines) {
//            stateMachine.onScroll(coordinatorLayout, child, config, currentOffset, delta, type);
//        }
        super.onScroll(coordinatorLayout, child, config, currentOffset, delta, type);
        Checkpoint front = null;
        Checkpoint back = null;
        if (isTopInRange(child, config)) {
            Checkpoint closest = config.getTopEdgeConfig().findClosestCheckpoint(currentOffset);
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

            topScrollableStateManager.onScroll(FLAG_EDGE_TOP_LEFT, currentOffset, delta, front, back, type);
        } else {
            int bottomOffset = currentOffset + child.getHeight();
            Checkpoint closest = config.getBottomEdgeConfig().findClosestCheckpoint(bottomOffset);
            if (bottomOffset >= closest.getOffsetConfig().getOffset()) {
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

            bottomScrollableStateManager.onScroll(FLAG_EDGE_BOTTOM_RIGHT, currentOffset, delta,
                    front, back, type);
        }
    }

    @Override
    public void onStopScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
                             Configuration config, int currentOffset, @ViewCompat.NestedScrollType
                                     int type) {
//        for (StateMachine stateMachine : stateMachines) {
//            stateMachine.onStopScroll(coordinatorLayout, child, config, currentOffset, type);
//        }
        super.onStopScroll(coordinatorLayout, child, config, currentOffset, type);
        Checkpoint front = null;
        Checkpoint back = null;
        // TODO bottom and top position conflict
        if (type == ViewCompat.TYPE_TOUCH) {
            if (isBottomInRange(child, config)) {
                int bottomOffset = currentOffset + child.getHeight();
                Checkpoint closest = config.getBottomEdgeConfig().findClosestCheckpoint(bottomOffset);
                if (bottomOffset >= closest.getOffsetConfig().getOffset()) {
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

                bottomScrollableStateManager.onStop(FLAG_EDGE_BOTTOM_RIGHT, currentOffset, front, back,
                        type);
            } else {
                Checkpoint closest = config.getTopEdgeConfig().findClosestCheckpoint(currentOffset);
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

                topScrollableStateManager.onStop(FLAG_EDGE_TOP_LEFT, currentOffset, front, back, type);
            }
        } else {
            bottomScrollableStateManager.onStop(FLAG_EDGE_BOTTOM_RIGHT, currentOffset, front, back,
                    type);
            topScrollableStateManager.onStop(FLAG_EDGE_TOP_LEFT, currentOffset, front, back, type);
        }

    }

    @Override
    public void onLoadStart() {
        if (!hasOnLoadListeners()) {
            return;
        }
        for (OnLoadListener l : mLoadListeners) {
            l.onLoadStart();
        }
    }

    @Override
    public void onReleaseToLoad() {
        if (!hasOnLoadListeners()) {
            return;
        }
        for (OnLoadListener l : mLoadListeners) {
            l.onReleaseToLoad();
        }
    }

    @Override
    public void onLoad() {
        if (!hasOnLoadListeners()) {
            return;
        }
        for (OnLoadListener l : mLoadListeners) {
            l.onLoad();
        }
    }

    @Override
    public void onLoadComplete(Throwable throwable) {
        if (!hasOnLoadListeners()) {
            return;
        }
        for (OnLoadListener l : mLoadListeners) {
            l.onLoadComplete(throwable);
        }
    }

    @Override
    public void onRefreshStart() {
        if (!hasOnRefreshListeners()) {
            return;
        }
        for (OnRefreshListener l : mRefreshListeners) {
            l.onRefreshStart();
        }
    }

    @Override
    public void onReleaseToRefresh() {
        if (!hasOnRefreshListeners()) {
            return;
        }
        for (OnRefreshListener l : mRefreshListeners) {
            l.onReleaseToRefresh();
        }
    }

    @Override
    public void onRefresh() {
        if (!hasOnRefreshListeners()) {
            return;
        }
        for (OnRefreshListener l : mRefreshListeners) {
            l.onRefresh();
        }
    }

    @Override
    public void onRefreshComplete(Throwable throwable) {
        if (!hasOnRefreshListeners()) {
            return;
        }
        for (OnRefreshListener l : mRefreshListeners) {
            l.onRefreshComplete(throwable);
        }
    }

    @Override
    public void refresh() {
        // Avoid unnecessary task queueing.
        if (topEdgeRefreshStateManager.isRefreshing())
            return;
        runWithView(new Runnable() {
            @Override
            public void run() {
                topEdgeRefreshStateManager.refresh();
            }
        });
    }

    @Override
    public void refreshComplete() {
        topEdgeRefreshStateManager.refreshComplete();
    }

    @Override
    public void refreshError(Throwable throwable) {
        topEdgeRefreshStateManager.refreshError(throwable);
    }

    @Override
    public void load() {
        // Avoid unnecessary task queueing.
        if (bottomEdgeRefreshStateManager.isRefreshing())
            return;
        runWithView(new Runnable() {
            @Override
            public void run() {
                bottomEdgeRefreshStateManager.refresh();
            }
        });
    }

    @Override
    public void loadComplete() {
        bottomEdgeRefreshStateManager.refreshComplete();
    }

    @Override
    public void loadError(Throwable throwable) {
        bottomEdgeRefreshStateManager.refreshError(throwable);
    }

    public boolean isRefreshing() {
        return topEdgeRefreshStateManager.isRefreshing();
    }

    public boolean isLoading() {
        return bottomEdgeRefreshStateManager.isRefreshing();
    }

    void showHeader() {
//        behavior.showHeader(SHOW_DURATION);
    }

    void showFooter() {
//        behavior.showFooter(SHOW_DURATION);
    }

    void refreshFooter() {
        behavior.refreshFooter(SHOW_DURATION);
    }

    public void refreshHeader() {
        behavior.refreshHeader(SHOW_DURATION);
    }

    public void reset() {
        behavior.reset(RESET_DURATION);
    }

    public void stopScroll(boolean holdOn) {
        behavior.stopScroll(holdOn);
    }

}
