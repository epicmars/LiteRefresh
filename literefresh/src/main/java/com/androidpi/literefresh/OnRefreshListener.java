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
package com.androidpi.literefresh;

import android.support.annotation.Nullable;

/**
 * Implementing this interface to listener to the refreshing state of header behavior or content
 * behavior's logical header.
 */

public interface OnRefreshListener {

    /**
     * The start of a refreshing lifecycle.
     */
    void onRefreshStart();

    /**
     * The refreshing came to a critical state when user release their finger from screen,
     * which means a touch event or scrolling is over, it will trigger the refreshing to happen.
     */
    void onReleaseToRefresh();

    /**
     * The refreshing is happening now.
     */
    void onRefresh();

    /**
     * The end of refreshing.
     * @param throwable the exception that was reported by a refresher.
     */
    void onRefreshEnd(@Nullable Throwable throwable);
}
