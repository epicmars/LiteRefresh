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

import static literefresh.behavior.IndicatorConfiguration.MODE_FOLLOW;

import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import literefresh.behavior.ScrollableBehavior;
import literefresh.behavior.VerticalIndicatorBehavior;

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
                                                        ScrollableBehavior scrollableBehavior) {
        // For now we don't care about invisible changes.
        // And when content has reached minimum offset, we should not changed with it.
        // If content has reach it's minimum offset, header may have not changed yet.
        if (scrollableBehavior.isTopMinOffsetReached()
                && behavior.getBottomPosition()
                <= scrollableBehavior.getConfig().getTopEdgeConfig().getMinOffset()) {
            return 0;
        }
        // After scroll stop, the header top position plus the distance from header min-offset to content min-offset
        // should equal to the content top position.
        return scrollableBehavior.getTopPosition() - (behavior.getTopPosition()
                + scrollableBehavior.getConfig().getTopEdgeConfig().getMinOffset()
                - behavior.getConfig().getTopEdgeConfig().getMinOffset());
    }

    @Override
    public int transformOffsetCoordinate(CoordinatorLayout parent, View child,
                                         VerticalIndicatorBehavior behavior, int currentOffset) {
        return currentOffset + child.getHeight();
    }

    @Override
    public float consumedOffsetOnDependentViewChanged(CoordinatorLayout parent, View child,
                                                      VerticalIndicatorBehavior behavior,
                                                      ScrollableBehavior contentBehavior,
                                                      int currentOffset, int offsetDelta) {
        switch (behavior.getConfig().getFollowMode()) {
//            case MODE_STILL:
//                // If child has reached it's initial position then don't move again.
//                if (child.getBottom() + behavior.getConfig().getBottomMargin()
//                        == behavior.getConfig().getInitialVisibleHeight()) {
//                    return 0;
//                } else {
//                    return offsetDelta;
//                }
//            case MODE_FOLLOW_DOWN:
//                // if scroll up and has reached initial visible height.
//                if (offsetDelta < 0
//                        && child.getBottom() + behavior.getConfig().getBottomMargin()
//                        <= behavior.getConfig().getInitialVisibleHeight()) {
//                    return 0;
//                } else {
//                    return offsetDelta;
//                }
//            case MODE_FOLLOW_UP:
//                // If scroll down and reached initial visible height.
//                if ((offsetDelta > 0
//                        && child.getBottom() + behavior.getConfig().getBottomMargin()
//                        >= behavior.getConfig().getInitialVisibleHeight())
//                        // or if scroll up and it has reached the content's minimum offset.
//                        || (offsetDelta < 0
//                        && child.getBottom() + behavior.getConfig().getBottomMargin()
//                        <= contentBehavior.getConfig().getMinOffset())) {
//                    return 0;
//                } else {
//                    return offsetDelta;
//                }
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
//        return child.getBottom() + behavior.getConfig().getBottomMargin()
//                > behavior.getConfig().getInitialVisibleHeight();
        return false;
    }
}
