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


import androidx.annotation.Nullable;

/**
 * Refreshing controller of header behavior.
 *
 * <p>
 *     If using in multiple threads, the implementations should be thread safe.
 * </p>
 */
public interface Refresher extends RefreshController{

    /**
     * Start a refreshing.
     */
    void refresh();

    /**
     * Refreshing complete, which will reset the header behavior's refreshing state.
     */
    void refreshComplete();

    /**
     * The same as {@link #refreshComplete()}, in addition it can report exceptions to the
     * refreshing listeners.
     * @param throwable
     * @see {@link OnRefreshListener}
     */
    void refreshError(@Nullable Throwable throwable);
}
