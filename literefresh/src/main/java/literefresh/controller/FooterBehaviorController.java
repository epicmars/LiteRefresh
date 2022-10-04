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
 * The footer behavior's controller, it controls footer behavior how to consume the offset changes.
 */
public class FooterBehaviorController extends VerticalIndicatorBehaviorController {

    public FooterBehaviorController(VerticalIndicatorBehavior behavior) {
        super(behavior);
    }

    @Override
    public int computeOffsetDeltaOnDependentViewChanged(CoordinatorLayout parent, View child,
                                                        View dependency,
                                                        VerticalIndicatorBehavior behavior,
                                                        ScrollableBehavior scrollableBehavior) {
        if (scrollableBehavior.isBottomMinOffsetReached()
                && behavior.getTopPosition()
                <= scrollableBehavior.getConfig().getBottomEdgeConfig().getMinOffset()) {
            return 0;
        }
        return scrollableBehavior.getBottomPosition() - behavior.getTopPosition();
    }

    @Override
    public float consumeOffsetOnDependentViewChanged(CoordinatorLayout parent, View child,
                                                     VerticalIndicatorBehavior behavior,
                                                     ScrollableBehavior contentBehavior,
                                                     int currentOffset, int offsetDelta) {
        switch (behavior.getConfig().getFollowMode()) {
//            case MODE_STILL:
//                if (child.getTop() - behavior.getConfig().getTopMargin() ==
//                        -behavior.getConfig().getInitialVisibleHeight() + parent.getHeight()) {
//                    return 0;
//                } else {
//                    return offsetDelta;
//                }
//            case MODE_FOLLOW_DOWN:
//                // If scrolling up and current offset has reach the initial visible height, don't follow.
//                if (offsetDelta < 0
//                        && child.getTop() - behavior.getConfig().getTopMargin()
//                        <= -behavior.getConfig().getInitialVisibleHeight() + parent.getHeight()) {
//                    return 0;
//                } else {
//                    return offsetDelta;
//                }
//            case MODE_FOLLOW_UP:
//                // If scrolling down and current offset has reach the initial visible height, don't follow..
//                if (offsetDelta > 0
//                        && child.getTop() - behavior.getConfig().getTopMargin()
//                        >= -behavior.getConfig().getInitialVisibleHeight() + parent.getHeight()) {
//                    return 0;
//                } else {
//                    return offsetDelta;
//                }
            case MODE_FOLLOW:
            default:
                return offsetDelta;
        }
    }

    @Override
    public int transformOffsetCoordinate(CoordinatorLayout parent, View child,
                                         VerticalIndicatorBehavior behavior,
                                         int currentOffset) {
        // The current offset is the footer's top and bottom offset.
        return -currentOffset + parent.getHeight();
    }

    @Override
    public boolean isHiddenPartVisible(CoordinatorLayout parent, View child,
                                       VerticalIndicatorBehavior behavior) {
//        return -(child.getTop() - behavior.getConfig().getTopMargin()) + behavior.getParent().getHeight()
//                > behavior.getConfig().getInitialVisibleHeight();
        return false;
    }
}
