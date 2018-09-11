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
package com.androidpi.literefresh.core;

import android.support.design.widget.CoordinatorLayout;
import android.view.View;

/**
 * Implementing this interface and register it with a behavior, then you can observe the scrolling
 * event of the view that the behavior is attached.
 * <p>
 * To make the api more close to human visual instinct, the offset used in the callback method of
 * this interface is transformed. As we are scrolling vertically, we only care about offset in the
 * vertical axis, i.e. axis y.
 * <p>
 * For header view we use the bottom of the view along the axis y as the coordinate.
 * for content view we use the top the the view along axis y as the coordinate, their coordinate
 * system is the same, and the same as the parent view's original touch event coordinate system.
 * <p>
 * <pre>
 *
 *                      o---------------------------------> x
 *                      |                      |
 *                      |                      |
 *                      |                      |
 *                      |                      |
 *                      |                      |
 *                      | CoordinatorLayout    |
 *                      |                      |
 *                      |                      |
 *                      |                      |
 *                      |                      |
 *                      |                      |
 *                      |footer_o              |
 *                      |-----------------------
 *                      |
 *                      |
 *                      |
 *                      v
 *
 *                      y (header_bottom_y, content_top_y)
 *
 *                      header_offset = header_bottom_y
 *                      content_offset = content_top_y
 *
 * </pre>
 * <p>
 * Fot the footer view we use the top of the view along the axis y as the coordinate to compute
 * offset, it's coordinate system's original point is at the left-bottom corner of parent view,
 * as show in the figure below.
 * <p>
 * <pre>
 *                      y (footer_top_y)
 *                      ^
 *                      |
 *                      |
 *                      |-----------------------
 *                      |                      |
 *                      |                      |
 *                      |                      |
 *                      |                      |
 *                      |                      |
 *                      | CoordinatorLayout    |
 *                      |                      |
 *                      |                      |
 *                      |                      |
 *                      |                      |
 *                      |                      |
 *                      |                      |
 *                      o---------------------------------> x
 *
 *                      footer_offset = footer_top_y
 *
 * </pre>
 * <p>
 */

public interface OnScrollListener {

    /**
     * The view that a behavior is attached is starting to scroll, when implementing this method,
     * you should be careful with which type of touch event causes the scroll to happen. So does
     * the {@link #onStopScroll(CoordinatorLayout, View, int, int, int, int, int, int)} method of
     * this interface.
     * <p>
     * The reason is that after a normal touch scroll is end, it may be followed by a fling motion
     * immediately which can cause this method be invoked again and start another start-scroll-stop
     * round.
     *
     * @param parent  the view's parent view, it must be CoordinatorLayout
     * @param view    the view to which the behavior is attached
     * @param initial the initial offset of the view
     * @param trigger the trigger offset of the view related to the refreshing state changing
     * @param min     the minimum offset of the view, the view can not scroll out of the range
     *                limited by minimum and maximum offset
     * @param max     the maximum offset of the view the view can not scroll out of the range
     *                limited by minimum and maximum offset
     * @param type    the type of touch event that cause the scrolling to happen
     */
    void onStartScroll(CoordinatorLayout parent, View view, int initial, int trigger, int min, int max, int type);

    /**
     * The view that a behavior is attached is scrolling now, you can care less about which type of
     * touch event type now, because no matter what the touch event is, it just scrolls.
     * <p>
     * <strong>
     * Note: When compute a progress percentage, because all the number values are integers, you may
     * need to do some number type conversion to make things right.
     * </strong>
     *
     * @param parent  the view's parent view, it must be CoordinatorLayout
     * @param view    the view to which the behavior is attached
     * @param current the current offset of the view
     * @param delta   the offset delta
     * @param initial the initial offset of the view
     * @param trigger the trigger offset of the view related to the refreshing state changing
     * @param min     the minimum offset of the view, the view can not scroll out of the range
     *                limited by minimum and maximum offset
     * @param max     the maximum offset of the view the view can not scroll out of the range
     *                limited by minimum and maximum offset
     * @param type    the type of touch event that cause the scrolling to happen
     */
    void onScroll(CoordinatorLayout parent, View view, int current, int delta, int initial, int trigger, int min, int max, int type);

    /**
     * The view that a behavior is attached has stopped to scroll, when implementing this method,
     * you should be careful with which type of touch event causes the scrolling.
     * <p>
     * The reason is the same as the {@link #onStartScroll(CoordinatorLayout, View, int, int,
     * int, int, int)} method.
     *
     * @param parent  the view's parent view, it must be CoordinatorLayout
     * @param view    the view to which the behavior is attached
     * @param current the current offset of the view
     * @param initial the initial offset of the view
     * @param trigger the trigger offset of the view related to the refreshing state changing
     * @param min     the minimum offset of the view, the view can not scroll out of the range
     *                limited by minimum and maximum offset
     * @param max     the maximum offset of the view the view can not scroll out of the range
     *                limited by minimum and maximum offset
     * @param type    the type of touch event that cause the scrolling to happen
     * @see #onStartScroll(CoordinatorLayout, View, int, int, int, int, int)
     */
    void onStopScroll(CoordinatorLayout parent, View view, int current, int initial, int trigger, int min, int max, int type);
}
