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

import literefresh.OnRefreshListener;
import literefresh.OnScrollListener;
import literefresh.R;
import literefresh.Refresher;
import literefresh.controller.HeaderBehaviorController;

import static literefresh.behavior.IndicatorConfiguration.MODE_FOLLOW;

/**
 * This class is used to attach to a header view and add nested scrolling features to it.
 *
 * Note that this behavior can not work standalone, the header view to which this behavior is
 * attached must work with a nested scrolling content view that is attached with an
 * {@link RefreshContentBehavior}, otherwise it'll not work.
 *
 * <strong>
 *     The view to which this behavior is attached must be a direct child of {@link CoordinatorLayout}.
 * </strong>
 */

public class RefreshHeaderBehavior<V extends View>
        extends VerticalIndicatorBehavior<V> implements Refresher {

    {
        addScrollListener(controller = new HeaderBehaviorController(this));
        runWithView(new Runnable() {
            @Override
            public void run() {
                ScrollingContentBehavior contentBehavior = getContentBehavior(getParent(), getChild());
                if (contentBehavior != null) {
                    controller.setProxy(contentBehavior.getController());
                }
            }
        });
    }

    public RefreshHeaderBehavior(Context context) {
        this(context, null);
    }

    public RefreshHeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.IndicatorBehavior, 0, 0);
        if (a.hasValue(R.styleable.IndicatorBehavior_lr_mode)) {
            int mode = a.getInt(
                    R.styleable.IndicatorBehavior_lr_mode, MODE_FOLLOW);
            getConfig().setFollowMode(mode);
        }
        a.recycle();
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        boolean handled = super.onLayoutChild(parent, child, layoutDirection);
        CoordinatorLayout.LayoutParams lp =
                ((CoordinatorLayout.LayoutParams) child.getLayoutParams());
        final int initialVisibleHeight = getInitialVisibleHeight();
        getConfig().setInitialVisibleHeight(initialVisibleHeight);
        // If initial visible height is non-positive, to make header visible when the trigger offset
        // has been reached, we have scrolled over the header's bottom margin at least.
        int triggerOffset = getConfig().getTriggerOffset();
        if (initialVisibleHeight <= 0) {
            triggerOffset += lp.bottomMargin;
        }
        getConfig().setTriggerOffset(triggerOffset);
        // Config maximum offset.
        configMaxOffset(parent, child, initialVisibleHeight, triggerOffset);
        ScrollingContentBehavior contentBehavior = getContentBehavior(parent, child);
        if (!getConfig().isSettled() || !contentBehavior.getConfig().isSettled()) {
            getConfig().setSettled(true);
            // If we have set a minimum offset for content, but header's initial visible height is smaller,
            // than use it as the minimum offset.
            if (contentBehavior != null) {
                contentBehavior.configTopMinOffset(getConfig().getInitialVisibleHeight());
                requestLayout();
            }
            // The header's height may have changed, it can occur in such a situation when you set
            // adjustViewBound to true in a image view's layout attributes and then load image async.
//            setTopAndBottomOffset(-getConfiguration().getInvisibleHeight());ContentBehavior
        }
        return handled;
    }

    private void configMaxOffset(CoordinatorLayout parent, V child,
                                 int initialVisibleHeight, int triggerOffset) {
        int currentMaxOffset = getConfig().getMaxOffset();
        if (getConfig().isUseDefaultMaxOffset()) {
            // We want child can be fully visible by default.
            currentMaxOffset = (int) Math.max(GOLDEN_RATIO * parent.getHeight(),
                    child.getHeight());
        } else {
            currentMaxOffset = (int) Math.max(currentMaxOffset,
                    getConfig().getMaxOffsetRatioOfParent() * parent.getHeight());
        }
        currentMaxOffset = Math.max(currentMaxOffset, initialVisibleHeight + triggerOffset);
        getConfig().setMaxOffset(currentMaxOffset);
    }

    public void addOnScrollListener(OnScrollListener listener) {
        controller.addOnScrollListener(listener);
    }

    public void addOnRefreshListener(OnRefreshListener listener) {
        controller.addOnRefreshListener(listener);
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
    protected int getInitialOffset(@NonNull CoordinatorLayout parent, @NonNull View child) {
        return getConfig().getVisibleHeight();
    }

    @Override
    protected int getRefreshTriggerOffset(@NonNull CoordinatorLayout parent, @NonNull View child) {
        return getConfig().getVisibleHeight() + getConfig().getTriggerOffset();
    }

    @Override
    protected int getMinOffset(@NonNull CoordinatorLayout parent, @NonNull View child) {
        ContentConfiguration contentConfig = getContentBehavior(parent, child).getConfig();
        return contentConfig.getMinOffset() - getConfig().getBottomMargin();
    }

    @Override
    protected int getMaxOffset(@NonNull CoordinatorLayout parent, @NonNull View child) {
        ContentConfiguration contentConfig = getContentBehavior(parent, child).getConfig();
        return contentConfig.getMaxOffset() -
                (contentConfig.getMaxOffset() > getConfig().getBottomMargin()
                        ? getConfig().getBottomMargin()
                        : 0);
    }

    /**
     * The initial visible height is origin visible height with vertical margins included.
     * It's used as a initial offset by content view to lay itself out and compute
     * offsets when needed.
     *
     * @return header view's initial visible height.
     */
    private int getInitialVisibleHeight() {
        int initialVisibleHeight;
        if (getConfig().getHeight() <= 0 || getConfig().getVisibleHeight() <= 0) {
            initialVisibleHeight = getConfig().getVisibleHeight();
        } else if (getConfig().getVisibleHeight() >= getConfig().getHeight()) {
            initialVisibleHeight = getConfig().getVisibleHeight() + getConfig().getTopMargin()
                    + getConfig().getBottomMargin();
        } else {
            initialVisibleHeight = getConfig().getVisibleHeight() + getConfig().getBottomMargin();
        }
        return initialVisibleHeight;
    }

}
