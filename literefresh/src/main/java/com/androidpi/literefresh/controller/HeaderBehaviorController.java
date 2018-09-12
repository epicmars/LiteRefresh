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

import android.support.design.widget.CoordinatorLayout;
import android.view.View;

import com.androidpi.literefresh.behavior.VerticalIndicatorBehavior;
import com.androidpi.literefresh.behavior.ScrollingContentBehavior;

/**
 * The header behavior's controller, it controls header behavior how to consume the offset changes.
 */
public class HeaderBehaviorController extends VerticalIndicatorBehaviorController {

    public HeaderBehaviorController(VerticalIndicatorBehavior behavior) {
        super(behavior);
    }

    @Override
    public int computeOffsetDeltaOnDependentViewChanged(CoordinatorLayout parent,
                                                        View child,
                                                        View dependency,
                                                        VerticalIndicatorBehavior behavior,
                                                        ScrollingContentBehavior contentBehavior) {
        // For now we don't care about invisible changes.
        // And when content has reached minimum offset, we should not changed with it.
        // If content has reach it's minimum offset, header may have not changed yet.
        if (contentBehavior.isMinOffsetReached()
                && child.getBottom() + behavior.getConfiguration().getBottomMargin()
                <= contentBehavior.getConfiguration().getMinOffset()) {
            return 0;
        }
        CoordinatorLayout.LayoutParams dependencyLp = ((CoordinatorLayout.LayoutParams) dependency.getLayoutParams());
        CoordinatorLayout.LayoutParams lp = ((CoordinatorLayout.LayoutParams) child.getLayoutParams());
        return dependency.getTop() - dependencyLp.topMargin - (child.getBottom() + lp.bottomMargin);
    }

    @Override
    public int transformOffsetCoordinate(CoordinatorLayout parent, View child,
                                         VerticalIndicatorBehavior behavior, int currentOffset) {
        return currentOffset + child.getHeight();
    }

    @Override
    public float consumeOffsetOnDependentViewChanged(CoordinatorLayout parent, View child,
                                                     VerticalIndicatorBehavior behavior,
                                                     ScrollingContentBehavior contentBehavior,
                                                     int currentOffset, int offsetDelta) {
        switch (mode) {
            case MODE_STILL:
                // If child has reached it's initial position then don't move again.
                if (child.getBottom() + behavior.getConfiguration().getBottomMargin()
                        == behavior.getConfiguration().getInitialVisibleHeight()) {
                    return 0;
                } else {
                    return offsetDelta;
                }
            case MODE_FOLLOW_DOWN:
                // if scroll up and has reached initial visible height.
                if (offsetDelta < 0
                        && child.getBottom() + behavior.getConfiguration().getBottomMargin()
                        <= behavior.getConfiguration().getInitialVisibleHeight()) {
                    return 0;
                } else {
                    return offsetDelta;
                }
            case MODE_FOLLOW_UP:
                // If scroll down and reached initial visible height.
                if ((offsetDelta > 0
                        && child.getBottom() + behavior.getConfiguration().getBottomMargin()
                        >= behavior.getConfiguration().getInitialVisibleHeight())
                        // or if scroll up and it has reached the content's minimum offset.
                        || (offsetDelta < 0
                        && child.getBottom() + behavior.getConfiguration().getBottomMargin()
                        <= contentBehavior.getConfiguration().getMinOffset())) {
                    return 0;
                } else {
                    return offsetDelta;
                }
            case MODE_FOLLOW:
            default:
                return offsetDelta;
        }
    }

    /**
     * Tell if the hidden part of header view is visible.
     * If invisible height is zero, which means visible height equals to view's height,
     * in that case it's considered to be invisible.
     *
     * @return true if hidden part of header view is visible,
     * otherwise return false.
     */
    @Override
    public boolean isHiddenPartVisible(CoordinatorLayout parent, View child,
                                       VerticalIndicatorBehavior behavior) {
        return child.getBottom() + behavior.getConfiguration().getBottomMargin()
                > behavior.getConfiguration().getInitialVisibleHeight();
    }
}
