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

import literefresh.sample.data.remote.dto.ResCurrentWeather
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * API docs:
 *
 * https://openweathermap.org/current
 *
 * https://openweathermap.org/weather-conditions
 *
 */
interface OpenWeatherMapApi {

    @GET("/data/2.5/weather")
    fun getCurrentWeather(@Query("lat") lat: Float,
                          @Query("lon") lon: Float,
                          @Query("lang") lang: String? = null,
                          @Query("appid") appid: String = APP_ID): Single<ResCurrentWeather>

    companion object {
        val BASE_URL = "https://api.openweathermap.org"
        val APP_ID = "9ce9f3756cec0215f881d42d0fbcad6f"
        val ICON_URL_FORMAT = "https://openweathermap.org/img/w/%s.png"
    }
}
