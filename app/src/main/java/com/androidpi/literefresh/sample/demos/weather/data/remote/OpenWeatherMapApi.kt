package com.androidpi.literefresh.sample.demos.weather.data.remote

import com.androidpi.literefresh.sample.demos.weather.data.remote.dto.ResCurrentWeather
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
 * Created by jastrelax on 2018/8/18.
 */
interface OpenWeatherMapApi {

    @GET("/data/2.5/weather")
    fun getCurrentWeather(@Query("lat") lat: Float,
                          @Query("lon") lon: Float,
                          @Query("lang") lang: String? = null,
                          @Query("appid") appid: String = APP_ID): Single<ResCurrentWeather>

    companion object {
        val BASE_URL = "http://api.openweathermap.org"
        val APP_ID = "d1dfb0110bec1a4281900b8c1ac8f759"
        val ICON_URL_FORMAT = "http://openweathermap.org/img/w/%s.png"
    }
}
