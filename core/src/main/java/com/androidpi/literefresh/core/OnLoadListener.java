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

import android.support.annotation.Nullable;

/**
 * Implementing this interface to listener to the refreshing state of footer behavior or content
 * behavior's logical footer.
 */
public interface OnLoadListener {

    /**
     * The start of a loading lifecycle.
     */
    void onLoadStart();

    /**
     * The loading came to a critical state when user release their finger from screen,
     * which means a touch event or scrolling is over, it will trigger the loading to happen.
     */
    void onReleaseToLoad();

    /**
     * The loading is happening now.
     */
    void onLoad();

    /**
     * The end of loading.
     *
     * @param throwable the exception that was reported by a loader.
     */
    void onLoadEnd(@Nullable Throwable throwable);
}
