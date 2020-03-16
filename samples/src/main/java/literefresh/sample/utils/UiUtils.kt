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
package literefresh.sample.utils

import android.content.Context
import literefresh.sample.R

object UiUtils {
    fun getStatusBarHeight(context: Context): Int {
        val statusBarHeight: Int
        val resources = context.resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        statusBarHeight = if (resourceId != 0) {
            resources.getDimensionPixelSize(resourceId)
        } else {
            resources.getDimensionPixelSize(R.dimen.app_status_bar_height)
        }
        return statusBarHeight
    }
}