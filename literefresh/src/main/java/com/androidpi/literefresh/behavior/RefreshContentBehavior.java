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
package com.androidpi.literefresh.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;

import com.androidpi.literefresh.Loader;
import com.androidpi.literefresh.OnLoadListener;
import com.androidpi.literefresh.OnRefreshListener;
import com.androidpi.literefresh.OnScrollListener;
import com.androidpi.literefresh.Refresher;
import com.androidpi.literefresh.animator.ViscousFluidInterpolator;
import com.androidpi.literefresh.controller.ContentBehaviorController;

/**
 * This class is what we use to attach to an nested scrolling view, add new scrolling features to it.
 * <p>
 * This behavior is the pivot of header and footer behavior, without it the other behaviors can not
 * work. All the offset and state changes come from it, so it can be used standalone.
 * <p>
 * For now, the nested scrolling view supported by {@link androidx.coordinatorlayout.widget.CoordinatorLayout} are
 * {@link androidx.core.widget.NestedScrollView},
 * {@link androidx.recyclerview.widget.RecyclerView}
 * which implement {@link androidx.core.view.NestedScrollingChild}.
 * <p>
 * Use other parent view to wrap these scrollable child is OK, cause the {@link android.view.ViewGroup}
 * and {@link View} already implement the nested scrolling event dispatch contract, but the nested
 * scrolling child itself must exist in the view hierarchy.
 * <p>
 * <strong>
 * The view to which this behavior is attached must be a direct child of {@link androidx.coordinatorlayout.widget.CoordinatorLayout}.
 * </strong>
 */

public class RefreshContentBehavior<V extends View> extends ScrollingContentBehavior<V>
        implements Refresher, Loader {

    {
        addScrollListener(controller = new ContentBehaviorController(this));
    }

    public RefreshContentBehavior(Context context) {
        this(context, null);
    }

    public RefreshContentBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void addOnScrollListener(OnScrollListener listener) {
        controller.addOnScrollListener(listener);
    }

    public void addOnRefreshListener(OnRefreshListener listener) {
        controller.addOnRefreshListener(listener);
    }

    public void addOnLoadListener(OnLoadListener listener) {
        controller.addOnLoadListener(listener);
    }

    @Override
    public void refresh() {
        controller.refresh();
    }

    @Override
    public void refreshComplete() {
        controller.refreshComplete();
    }

    @Override
    public void refreshError(Throwable throwable) {
        controller.refreshError(throwable);
    }

    @Override
    public void load() {
        controller.load();
    }

    @Override
    public void loadComplete() {
        controller.loadComplete();
    }

    @Override
    public void loadError(Throwable throwable) {
        controller.loadError(throwable);
    }

    private float accumulator = 0;

    private Interpolator scrollDownInterpolator = new ViscousFluidInterpolator();

    @Override
    protected float onConsumeOffset(int current, int max, int delta) {
        float consumed = delta;
        if (current >= 0 && delta > 0) {
            float y = scrollDownInterpolator.getInterpolation(current / (float) max);
            consumed = (1f - y) * delta;
            if (consumed < 0.5) {
                accumulator += 0.2;
                if (accumulator >= 1) {
                    consumed += 1;
                    accumulator = 0;
                }
            }
        }
        return consumed;
    }
}
