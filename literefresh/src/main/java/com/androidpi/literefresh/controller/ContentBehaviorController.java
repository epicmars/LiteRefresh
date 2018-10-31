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
package com.androidpi.literefresh.controller;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;

import com.androidpi.literefresh.OnLoadListener;
import com.androidpi.literefresh.OnRefreshListener;
import com.androidpi.literefresh.behavior.ScrollingContentBehavior;
import com.androidpi.literefresh.state.StateMachine;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.androidpi.literefresh.state.StateMachine.*;

/**
 * Content behavior's controller, it has to state machines that manage header and footer view's
 * refreshing or loading state respectively.
 */
public class ContentBehaviorController extends BehaviorController<ScrollingContentBehavior>
        implements OnRefreshListener, OnLoadListener {

    private static final long HOLD_ON_DURATION = 500L;
    private static final long SHOW_DURATION = 300L;
    private static final long RESET_DURATION = 300L;

    private final StateHandler footerStateHandler = new StateHandler() {
        @Override
        public boolean isValidOffset(int currentOffset) {
            return transform(currentOffset) > behavior.getFooterConfig().getInitialVisibleHeight();
        }

        @Override
        public int transform(int currentOffset) {
            // The current offset here is the content's top and bottom offset.
            return -(currentOffset + behavior.getChild().getHeight())
                    + behavior.getParent().getHeight();
        }

        @Override
        public int readyRefreshOffset() {
            return behavior.getFooterConfig().getTriggerOffset()
                    + behavior.getFooterConfig().getInitialVisibleHeight();
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
                    onLoadEnd(throwable);
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
            return transform(currentOffset) > behavior.getHeaderConfig().getInitialVisibleHeight();
        }

        @Override
        public int transform(int currentOffset) {
            // The current offset here is the content's top and bottom offset.
            return currentOffset;
        }

        @Override
        public int readyRefreshOffset() {
            return behavior.getHeaderConfig().getTriggerOffset()
                    + behavior.getHeaderConfig().getInitialVisibleHeight();
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
                    onRefreshEnd(throwable);
                    break;
                case STATE_IDLE:
                default:
                    break;
            }
        }
    };

    private StateMachine headerStateMachine = new StateMachine(headerStateHandler);
    private StateMachine footerStateMachine = new StateMachine(footerStateHandler);
    private Set<StateMachine> stateMachines = new LinkedHashSet<StateMachine>(2) {
        {
            add(headerStateMachine);
            add(footerStateMachine);
        }
    };

    public ContentBehaviorController(ScrollingContentBehavior behavior) {
        super(behavior);
    }

    @Override
    public void onStartScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
                              int initial, int trigger, int min, int max, int type) {
        for (StateMachine stateMachine : stateMachines) {
            stateMachine.onStartScroll(coordinatorLayout, child, initial, trigger, min, max, type);
        }
        super.onStartScroll(coordinatorLayout, child, initial, trigger, min, max, type);
    }

    @Override
    public void onPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
                            int current, int initial, int trigger, int min, int max, int type) {
        for (StateMachine stateMachine : stateMachines) {
            stateMachine.onPreScroll(coordinatorLayout, child, current, initial, trigger, min, max, type);
        }
        super.onPreScroll(coordinatorLayout, child, current, initial, trigger, min, max, type);
    }

    @Override
    public void onScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
                         int current, int delta, int initial, int trigger, int min, int max, int type) {
        for (StateMachine stateMachine : stateMachines) {
            stateMachine.onScroll(coordinatorLayout, child, current, delta, initial, trigger, min , max, type);
        }
        super.onScroll(coordinatorLayout, child, current, delta, initial, trigger, min, max, type);
    }

    @Override
    public void onStopScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
                             int current, int initial, int trigger, int min, int max, int type) {
        for (StateMachine stateMachine : stateMachines) {
            stateMachine.onStopScroll(coordinatorLayout, child, current, initial, trigger, min, max, type);
        }
        super.onStopScroll(coordinatorLayout, child, current, initial, trigger, min, max, type);
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
    public void onLoadEnd(Throwable throwable) {
        if (!hasOnLoadListeners()) {
            return;
        }
        for (OnLoadListener l : mLoadListeners) {
            l.onLoadEnd(throwable);
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
    public void onRefreshEnd(Throwable throwable) {
        if (!hasOnRefreshListeners()) {
            return;
        }
        for (OnRefreshListener l : mRefreshListeners) {
            l.onRefreshEnd(throwable);
        }
    }

    @Override
    public void refresh() {
        // Avoid unnecessary task queueing.
        if (headerStateMachine.isRefreshing())
            return;
        runWithView(new Runnable() {
            @Override
            public void run() {
                headerStateMachine.refresh();
            }
        });
    }

    @Override
    public void refreshComplete() {
        headerStateMachine.refreshComplete();
    }

    @Override
    public void refreshError(Throwable throwable) {
        headerStateMachine.refreshError(throwable);
    }

    @Override
    public void load() {
        // Avoid unnecessary task queueing.
        if (footerStateMachine.isRefreshing())
            return;
        runWithView(new Runnable() {
            @Override
            public void run() {
                footerStateMachine.refresh();
            }
        });
    }

    @Override
    public void loadComplete() {
        footerStateMachine.refreshComplete();
    }

    @Override
    public void loadError(Throwable throwable) {
        footerStateMachine.refreshError(throwable);
    }

    public boolean isRefreshing() {
        return headerStateMachine.isRefreshing();
    }

    public boolean isLoading() {
        return footerStateMachine.isRefreshing();
    }

    void showHeader() {
        behavior.showHeader(SHOW_DURATION);
    }

    void showFooter() {
        behavior.showFooter(SHOW_DURATION);
    }

    void refreshFooter() {
        behavior.refreshFooter(SHOW_DURATION);
    }

    void refreshHeader() {
        behavior.refreshHeader(SHOW_DURATION);
    }

    void reset() {
        behavior.reset(RESET_DURATION);
    }

    void stopScroll(boolean holdOn) {
        behavior.stopScroll(holdOn);
    }

}
