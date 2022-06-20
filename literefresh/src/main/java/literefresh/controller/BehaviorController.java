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

import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import literefresh.Loader;
import literefresh.OnLoadListener;
import literefresh.OnRefreshListener;
import literefresh.OnScrollListener;
import literefresh.Refresher;
import literefresh.behavior.AnimationOffsetBehavior;
import literefresh.behavior.Configuration;
import literefresh.behavior.NestedScrollingListener;

/**
 * Rather than change view's offset, the primary responsibility of behavior controller is to control
 * how behavior react to offset changes and provide API to control the behavior's states.
 */
public class BehaviorController<B extends AnimationOffsetBehavior>
        implements NestedScrollingListener, Refresher, Loader {

    protected BehaviorController proxy;
    protected B behavior;
    protected List<OnScrollListener> mScrollListeners;
    protected List<OnRefreshListener> mRefreshListeners;
    protected List<OnLoadListener> mLoadListeners;

    public BehaviorController(B behavior) {
        this.behavior = behavior;
    }

    @Override
    public void onStartScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
                              Configuration config, int type) {
        if (mScrollListeners == null) {
            return;
        }
        for (OnScrollListener l : mScrollListeners) {
            l.onStartScroll(coordinatorLayout, child, config, type);
        }
    }

    @Override
    public void onPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
                            Configuration config, int currentOffset, int type) {
        if (mScrollListeners == null)
            return;
        for (OnScrollListener l : mScrollListeners) {
            l.onPreScroll(coordinatorLayout, child, config, type);
        }
    }

    @Override
    public void onPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, Configuration config, int currentOffset, float velocityX, float velocityY) {
        if (mScrollListeners == null)
            return;
        for (OnScrollListener l : mScrollListeners) {
            //
        }
    }

    @Override
    public void onFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, Configuration config, int currentOffset, float velocityX, float velocityY) {
        if (mScrollListeners == null)
            return;
        for (OnScrollListener l : mScrollListeners) {
            //
        }
    }

    @Override
    public void onScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
                         Configuration config, int currentOffset, int delta, int type) {
        if (mScrollListeners  == null)
            return;
        for (OnScrollListener l : mScrollListeners) {
            l.onScroll(coordinatorLayout, child, config, delta, type);
        }
    }

    @Override
    public void onStopScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
                             Configuration config, int currentOffset, int type) {
        if (mScrollListeners == null) {
            return;
        }
        for (OnScrollListener l : mScrollListeners) {
            l.onStopScroll(coordinatorLayout, child, config, type);
        }
    }

    @Override
    public void refresh() {
        if (proxy == null) {
            runWithView(new Runnable() {
                @Override
                public void run() {
                    refresh();
                }
            });
        } else {
            runWithView(new Runnable() {
                @Override
                public void run() {
                    if (proxy != null) {
                        copyRemainListeners();
                        proxy.refresh();
                    }
                }
            });
        }
    }

    @Override
    public void refreshComplete() {
        runWithView(new Runnable() {
            @Override
            public void run() {
                if (proxy != null) {
                    proxy.refreshComplete();
                }
            }
        });
    }

    @Override
    public void refreshError(final Throwable throwable) {
        runWithView(new Runnable() {
            @Override
            public void run() {
                if (proxy != null) {
                    proxy.refreshError(throwable);
                }
            }
        });
    }

    @Override
    public void load() {
        if (proxy == null) {
            runWithView(new Runnable() {
                @Override
                public void run() {
                    load();
                }
            });
        } else {
            runWithView(new Runnable() {
                @Override
                public void run() {
                    if (proxy != null) {
                        copyRemainListeners();
                        proxy.load();
                    }
                }
            });
        }
    }

    @Override
    public void loadComplete() {
        runWithView(new Runnable() {
            @Override
            public void run() {
                if (proxy != null) {
                    proxy.loadComplete();
                }
            }
        });
    }

    @Override
    public void loadError(final Throwable throwable) {
        runWithView(new Runnable() {
            @Override
            public void run() {
                if (proxy != null) {
                    proxy.loadError(throwable);
                }
            }
        });
    }

    public BehaviorController getProxy() {
        return proxy;
    }

    public void setProxy(BehaviorController proxy) {
        this.proxy = proxy;
        if (proxy != null) {
            copyRemainListeners();
            behavior.executePendingActions();
        }
    }

    public void copyRemainListeners() {
        if (mRefreshListeners != null && !mRefreshListeners.isEmpty()) {
            Iterator<OnRefreshListener> iterator = mRefreshListeners.iterator();
            while (iterator.hasNext()) {
                proxy.addOnRefreshListener(iterator.next());
                iterator.remove();
            }
        }

        if (mLoadListeners != null && !mLoadListeners.isEmpty()) {
            Iterator<OnLoadListener> loadListenerIterator = mLoadListeners.iterator();
            while (loadListenerIterator.hasNext()) {
                proxy.addOnLoadListener(loadListenerIterator.next());
                loadListenerIterator.remove();
            }
        }
    }

    public B getBehavior() {
        return behavior;
    }

    public void setBehavior(B behavior) {
        this.behavior = behavior;
    }

    public Configuration getConfig() {
        return behavior.getConfig();
    }

    public void addOnScrollListener(OnScrollListener listener) {
        if (null == listener) {
            return;
        }
        if (mScrollListeners != null && mScrollListeners.contains(listener)) {
            return;
        }
        if (mScrollListeners == null) {
            mScrollListeners = new ArrayList<>();
        }
        mScrollListeners.add(listener);
    }

    public void addOnRefreshListener(final OnRefreshListener listener) {
        if (null == listener) {
            return;
        }
        if (mRefreshListeners != null && mRefreshListeners.contains(listener)) {
            return;
        }
        if (mRefreshListeners == null) {
            mRefreshListeners = new ArrayList<>();
        }
        runWithView(new Runnable() {
            @Override
            public void run() {
                if (proxy != null) {
                    proxy.addOnRefreshListener(listener);
                } else {
                    mRefreshListeners.add(listener);
                }
            }
        });
    }

    public void removeOnRefreshListeners() {
        if (mRefreshListeners != null) {
            mRefreshListeners.clear();
        }
    }

    public void removeOnLoadListeners() {
        if (mLoadListeners != null) {
            mLoadListeners.clear();
        }
    }

    public void removeOnScrollListeners() {
        if (mScrollListeners != null) {
            mScrollListeners.clear();
        }
    }

    public void recycle() {
        removeOnScrollListeners();
        removeOnRefreshListeners();
        removeOnLoadListeners();
    }

    public void addOnLoadListener(final OnLoadListener listener) {
        if (null == listener) {
            return;
        }
        if (mLoadListeners != null && mLoadListeners.contains(listener)) {
            return;
        }
        if (mLoadListeners == null) {
            mLoadListeners = new ArrayList<>();
        }
        runWithView(new Runnable() {
            @Override
            public void run() {
                if (proxy != null) {
                    proxy.addOnLoadListener(listener);
                } else {
                    mLoadListeners.add(listener);
                }
            }
        });
    }

    protected boolean hasOnLoadListeners() {
        return mLoadListeners != null && !mLoadListeners.isEmpty();
    }

    protected boolean hasOnRefreshListeners() {
        return mRefreshListeners != null && !mRefreshListeners.isEmpty();
    }

    protected void runWithView(Runnable runnable) {
        if (runnable == null) return;
        behavior.runWithView(runnable);
    }

    protected void runOnUiThread(Runnable runnable) {
        if (runnable == null) return;
        behavior.runOnUiThread(runnable);
    }
}
