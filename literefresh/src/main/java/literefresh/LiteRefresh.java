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
package literefresh;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import literefresh.behavior.AnimationOffsetBehavior;
import literefresh.behavior.RefreshContentBehavior;
import literefresh.behavior.RefreshFooterBehavior;
import literefresh.behavior.RefreshHeaderBehavior;

/**
 * Some convenient util method.
 */
public class LiteRefresh {

    /**
     * Get the behavior attached to the specified view.
     *
     * @param view the view which has a behavior attached to it
     * @param <T> type of the behavior
     * @return
     */
    @NonNull
    public static <T extends AnimationOffsetBehavior> T getAttachedBehavior(View view) {
        try {
            CoordinatorLayout.LayoutParams params =
                    (CoordinatorLayout.LayoutParams) view.getLayoutParams();
            if (params != null && params.getBehavior() != null) {
                return (T) params.getBehavior();
            } else if (view instanceof CoordinatorLayout.AttachedBehavior) {
                return (T) ((CoordinatorLayout.AttachedBehavior) view).getBehavior();
            }
            throw new IllegalArgumentException("No behavior has been attached to the view.");
        } catch (ClassCastException e) {
            throw e;
        }
    }

    /**
     * Get the {@link RefreshContentBehavior} attached to the specified view.
     *
     * @param view
     * @return
     */
    @NonNull
    public static RefreshContentBehavior getContentBehavior(View view) {
        return getAttachedBehavior(view);
    }

    /**
     * Get the {@link RefreshHeaderBehavior} attached to the specified view.
     * @param view
     * @return
     */
    @NonNull
    public static RefreshHeaderBehavior getHeaderBehavior(View view) {
        return getAttachedBehavior(view);
    }

    /**
     * Get the {@link RefreshFooterBehavior} attached to the specified view.
     * @param view
     * @return
     */
    @NonNull
    public static RefreshFooterBehavior getFooterBehavior(View view) {
        return getAttachedBehavior(view);
    }
}
