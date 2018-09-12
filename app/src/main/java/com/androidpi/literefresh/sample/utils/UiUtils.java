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
package com.androidpi.literefresh.sample.utils;

import android.content.Context;
import android.content.res.Resources;

import com.androidpi.literefresh.sample.R;

public class UiUtils {

    public static int getStatusBarHeight(Context context) {
        final int statusBarHeight;
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId != 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId);
        } else {
            statusBarHeight = resources.getDimensionPixelSize(R.dimen.app_status_bar_height);
        }
        return statusBarHeight;
    }
}