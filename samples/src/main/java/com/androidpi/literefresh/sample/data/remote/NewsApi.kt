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
package com.androidpi.literefresh.sample.data.remote

import com.androidpi.literefresh.sample.data.remote.dto.ResTopHeadlines
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    companion object {
        const val BASE_URL = "https://newsapi.org"
    }

    /**
     * https://newsapi.org/docs/endpoints/top-headlines
     */
    @GET("/v2/top-headlines")
    fun topHeadlines(@Query("country") country: String = "us",
                     @Query("category") category: String? = null,
                     @Query("sources") sources: String? = null,
                     @Query("q") q: String? = null,
                     @Query("pageSize") pageSize: Int,
                     @Query("page") page: Int,
                     @Query("apiKey") apiKey: String = "5b7bf07986684b238a01fac5a5dbf19f"): Single<ResTopHeadlines>
}