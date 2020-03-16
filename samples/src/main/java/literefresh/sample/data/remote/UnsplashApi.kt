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
package literefresh.sample.data.remote

import literefresh.sample.data.remote.dto.ResUnsplashPhoto

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface UnsplashApi {

    companion object {
        const val BASE_URL = "https://api.unsplash.com/"
        const val ACCESS_KEY = "d945a361e65ebf42cefda32bef69ea1fd2fa4029231cfc133d66dca73ab985b6"
    }

    /**
     * https://unsplash.com/documentation#get-a-random-photo
     */
    @GET("/photos/random")
    @Headers("Accept-Version: v1", "Authorization: Client-ID $ACCESS_KEY")
    fun randomPhotos(@Query("collections") collections: String? = null,
                     @Query("featured") featured: String? = null,
                     @Query("username") username: String? = null,
                     @Query("query") query: String? = null,
                     @Query("w") w: Long? = null,
                     @Query("h") h: Long? = null,
                     @Query("orientation") orientation: String? = null,
                     @Query("count") count: Int = 1): Single<List<ResUnsplashPhoto>>

}
