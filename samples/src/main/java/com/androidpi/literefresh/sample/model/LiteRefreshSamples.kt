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
package com.androidpi.literefresh.sample.model

import com.androidpi.literefresh.sample.ui.fragment.*
import java.util.*

object LiteRefreshSamples {

    val samples: List<LiteRefreshSample> = object : ArrayList<LiteRefreshSample>() {
        init {
            add(LiteRefreshSample("Weather", "Weather report.", WeatherFragment::class.java))
            add(LiteRefreshSample("News", "A news feed.", NewsFragment::class.java))
            add(LiteRefreshSample("Unsplash", "An gallery of photos from unsplash.", UnsplashFragment::class.java))
            add(LiteRefreshSample("Movies", "Movie data from The Movie Db.", MoviePagerFragment::class.java))
            add(LiteRefreshSample("Collapsible header.", "A scrollable list with a collapsible header.", CollapsibleHeaderFragment::class.java))
            add(LiteRefreshSample("Partial visible RecyclerView.", "A recycler view that is partial visible.", PartialVisibleListFragment::class.java))
            add(LiteRefreshSample("Partial visible header.", "A recycler view with a partial visible header.", PartialVisibleHeaderFragment::class.java))
            add(LiteRefreshSample("Header follow with content.", "Header can follow up and down with content.", HeaderFollowFragment::class.java))
            add(LiteRefreshSample("Header follow up.", "Header can follow up with content but not down.", HeaderFollowUpFragment::class.java))
            add(LiteRefreshSample("Header follow down.", "Header can follow down with content but not up", HeaderFollowDownFragment::class.java))
            add(LiteRefreshSample("Header stays still.", "Header doesn't follow down or up with content.", HeaderStillFragment::class.java))
        }
    }
}
