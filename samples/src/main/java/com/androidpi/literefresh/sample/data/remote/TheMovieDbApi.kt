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

import com.androidpi.literefresh.sample.data.remote.dto.ResMoviePage
import com.androidpi.literefresh.sample.data.remote.dto.ResTrendingPage
import com.androidpi.literefresh.sample.data.remote.dto.ResTvPage
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDbApi {

    companion object {

        val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/"
        val IMAGE_SIZE = "w500"

        val BASE_URL_V3 = "https://api.themoviedb.org/3/"
        val API_KEY_V3 = "a03feee7a52470501a8df012ac49fc5b"
    }

    @GET("discover/movie")
    fun discoverMovie(@Query("primary_release_date.gte") primaryReleaseDateGte: String,
                      @Query("primary_release_date.lte") primaryReleaseDateLte: String,
                      @Query("api_key") apiKey: String = API_KEY_V3): Single<ResMoviePage>

    @GET("discover/tv")
    fun discoverTv(@Query("air_date.gte") airDateGte: String,
                   @Query("air_date.lte") airDateLte: String,
                   @Query("api_key") apiKey: String = API_KEY_V3): Single<ResTvPage>

    @GET("trending/{media_type}/{time_window}")
    fun trending(@Path("media_type") mediaType: String?,
                 @Path("time_window") timeWindow: String,
                 @Query("api_key") apiKey: String = API_KEY_V3): Single<ResTrendingPage>

}
