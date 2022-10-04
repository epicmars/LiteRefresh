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
package literefresh.behavior;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import literefresh.Loader;
import literefresh.OnLoadListener;
import literefresh.OnScrollListener;
import literefresh.R;
import literefresh.controller.FooterBehaviorController;

import static literefresh.behavior.IndicatorConfiguration.MODE_FOLLOW;


/**
 * This class is what we use to attach to a so called footer view, and add some nested scrolling
 * features to it.
 * <p>
 * Note that the footer behavior can not work standalone, the footer view to which this behavior is
 * attached must work with a nested scrolling content view that is attached with an
 * {@link ContentScrollableBehavior}, otherwise it'll not work.
 * <p>
 * <strong>
 * The view to which this behavior is attached must be a direct child of {@link CoordinatorLayout}.
 * </strong>
 */

public class FooterLoaderBehavior<V extends View>
        extends VerticalIndicatorBehavior<V> implements Loader {

    {
        addScrollListener(controller = new FooterBehaviorController(this));
        runWithView(new Runnable() {
            @Override
            public void run() {
                ScrollableBehavior contentBehavior = getContentBehavior(getParent(), getChild());
                if (contentBehavior != null) {
                    controller.setProxy(contentBehavior.getController());
                }
            }
        });
    }

    public FooterLoaderBehavior(Context context) {
        this(context, null);
    }

    public FooterLoaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IndicatorBehavior,
                0, 0);
        if (a.hasValue(R.styleable.IndicatorBehavior_lr_mode)) {
            int mode = a.getInt(R.styleable.IndicatorBehavior_lr_mode, MODE_FOLLOW);
            getConfig().setFollowMode(mode);
        }
        a.recycle();
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        boolean handled = super.onLayoutChild(parent, child, layoutDirection);
        CoordinatorLayout.LayoutParams lp = ((CoordinatorLayout.LayoutParams) child.getLayoutParams());
        // The height of content may have changed, so does the footer's initial visible height.
//        final int lastInitialVisibleHeight = getConfig().getInitialVisibleHeight();
//        final int currentInitialVisibleHeight = getInitialVisibleHeight(parent, child);
//        if (lastInitialVisibleHeight != currentInitialVisibleHeight) {
//            getConfig().setSettled(false);
//        }
//        getConfig().setInitialVisibleHeight(currentInitialVisibleHeight);
//        // If initial visible height is non-positive, add the top margin to refresh trigger range.
//        int triggerOffset = getConfig().getTriggerOffset();
//        if (currentInitialVisibleHeight <= 0) {
//            triggerOffset += lp.topMargin;
//        }
//        getConfig().setTriggerOffset(triggerOffset);
//        // Config maximum offset.
//        configMaxOffset(parent, child, currentInitialVisibleHeight, triggerOffset);
//        if (!getConfig().isSettled()) {
//            getConfig().setSettled(true);
//            ScrollableBehavior contentBehavior = getContentBehavior(parent, child);
//            if (contentBehavior != null) {
//                contentBehavior.setFooterConfig(getConfig());
//            }
//            setTopAndBottomOffset(-getConfig().getVisibleHeight() + parent.getHeight());
//        }
        return handled;
    }

    private void configMaxOffset(CoordinatorLayout parent, V child,
                                 int initialVisibleHeight, int triggerOffset) {
//        int currentMaxOffset = getConfig().getMaxDownOffset();
//        if (getConfig().isUseDefaultMaxDownOffset()) {
//            // We want footer can be just fully visible by default.
//            currentMaxOffset = child.getHeight();
//        } else {
//            currentMaxOffset = (int) Math.max(currentMaxOffset, getConfig().getMaxDownOffsetRatioOfParent() * parent.getHeight());
//        }
//        // Maximum offset should not be less than initial visible height.
//        currentMaxOffset = Math.max(currentMaxOffset,
//                initialVisibleHeight + triggerOffset);
//        getConfig().setMaxDownOffset(currentMaxOffset);
    }

    public void addOnScrollListener(OnScrollListener listener) {
        controller.addOnScrollListener(listener);
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

    public void addOnLoadListener(OnLoadListener listener) {
        controller.addOnLoadListener(listener);
    }

    @Override
    protected int getInitialOffset(@NonNull CoordinatorLayout parent, @NonNull View child) {
//        return getConfig().getVisibleHeight();
        return 0;
    }

    @Override
    protected int getRefreshTriggerOffset(@NonNull CoordinatorLayout parent, @NonNull View child) {
//        return getConfig().getVisibleHeight() + getConfig().getTriggerOffset();
        return 50;
    }

    @Override
    protected int getMinOffset(@NonNull CoordinatorLayout parent, @NonNull View child) {
        return -getConfig().getTopMargin();
    }

    @Override
    protected int getMaxOffset(@NonNull CoordinatorLayout parent, @NonNull View child) {
//        ScrollableBehavior contentBehavior = getContentBehavior(parent, child);
//        return contentBehavior == null
//                ? 0
//                : contentBehavior.getFooterConfig().getMaxOffset() - getConfig().getTopMargin();
        return 100;
    }

    /**
     * The initial visible height is original visible height with vertical margins included.
     * Primarily, it's used as a initial offset by content view to lay itself out and compute
     * some offsets when needed.
     * <p>
     * Notice that there's some differences with the header's initial visible height, that's
     * because we need to adapter some short content views which may make the footer view entirely
     * visible all the time. In that case the footer's refresh state will not work as usual,
     * so we recompute the initial visible height with the header's initial visible height included.
     * </p>
     * This also means that the header should be layout before footer. So we make the header view as
     * a dependency of footer view.
     *
     * @return footer view's initial visible height.
     */
    private int getInitialVisibleHeight(@NonNull CoordinatorLayout parent, @NonNull View child) {
//        int initialVisibleHeight;
//        if (getConfig().getHeight() <= 0 || getConfig().getVisibleHeight() <= 0) {
//            initialVisibleHeight = getConfig().getVisibleHeight();
//        } else if (getConfig().getVisibleHeight() >= child.getHeight()) {
//            initialVisibleHeight = getConfig().getVisibleHeight()
//                    + getConfig().getTopMargin() + getConfig().getBottomMargin();
//        } else {
//            initialVisibleHeight = getConfig().getVisibleHeight() + getConfig().getTopMargin();
//        }
//        // If header configuration is not settled when footer is in layout, we would see
//        // header's initial visible height is zero then we get footer's initial visible height that
//        // fill the parent, after that when we compute a right initial visible height that is smaller,
//        // it will not be set.
//        ScrollableBehavior contentBehavior = getContentBehavior(parent, child);
//        // If content is too short, there may be extra space left.
//        if (contentBehavior == null
//                || getParent().getHeight() == 0
//                || contentBehavior.getConfig().getHeight() == 0) {
//            return initialVisibleHeight;
//        } else {
//            return Math.max(initialVisibleHeight, getParent().getHeight()
//                    - getParent().getPaddingTop()
//                    - getParent().getPaddingBottom()
//                    - contentBehavior.getConfig().getHeight()
//                    - contentBehavior.getConfig().getTopMargin()
//                    - contentBehavior.getConfig().getBottomMargin()
//                    - contentBehavior.getHeaderConfig().getInitialVisibleHeight());
//        }
        return 20;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, V child, View dependency) {
        // We want footer layout after content and header.
        CoordinatorLayout.LayoutParams lp =
                (CoordinatorLayout.LayoutParams) dependency.getLayoutParams();
        if (null != lp) {
            CoordinatorLayout.Behavior behavior = lp.getBehavior();
            return behavior instanceof ScrollableBehavior || behavior instanceof HeaderRefreshBehavior;
        }
        return false;
    }
}
