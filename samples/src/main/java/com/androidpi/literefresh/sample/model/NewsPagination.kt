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

class NewsPagination() {

    companion object {
        val FIRST_PAGE = 1
        const val PAGE_SIZE = 10
    }

    var newsList: MutableList<News> = mutableListOf()

    var page: Int = 1

    constructor(page: Int, list: List<News>?): this() {
        this.page = page
        if (list != null) {
            this.newsList.addAll(list)
        }
    }

    fun nextPage(isNext: Boolean) : Int {
        page = if (isNext) page + 1 else 1
        return  page
    }

    fun isFirstPage() : Boolean {
        return page == FIRST_PAGE
    }
}