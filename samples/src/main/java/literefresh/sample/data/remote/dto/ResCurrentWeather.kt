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
package literefresh.sample.data.remote.dto

import com.google.gson.annotations.SerializedName

class ResCurrentWeather {
    /**
     * coord : {"lon":104.08,"lat":30.67}
     * weather : [{"id":801,"main":"Clouds","description":"晴，少云","icon":"02n"}]
     * base : stations
     * main : {"temp":300.15,"pressure":1004,"humidity":83,"temp_min":300.15,"temp_max":300.15}
     * visibility : 10000
     * wind : {"speed":1.12,"deg":255.502}
     * clouds : {"all":20}
     * dt : 1534597200
     * sys : {"type":1,"id":7461,"message":0.0045,"country":"CN","sunrise":1534545120,"sunset":1534592550}
     * id : 1815286
     * name : Chengdu
     * cod : 200
     */
    @SerializedName("coord")
    var coord: CoordBean? = null

    @SerializedName("base")
    var base: String? = null

    @SerializedName("main")
    var main: MainBean? = null

    @SerializedName("visibility")
    var visibility = 0

    @SerializedName("wind")
    var wind: WindBean? = null

    @SerializedName("clouds")
    var clouds: CloudsBean? = null

    @SerializedName("dt")
    var dt: Long = 0

    @SerializedName("sys")
    var sys: SysBean? = null

    @SerializedName("id")
    var id = 0

    @SerializedName("name")
    var name: String? = null

    @SerializedName("cod")
    var cod = 0

    @SerializedName("weather")
    var weather: List<WeatherBean>? = null

    class CoordBean {
        /**
         * lon : 104.08
         * lat : 30.67
         */
        @SerializedName("lon")
        var lon = 0.0

        @SerializedName("lat")
        var lat = 0.0

    }

    class MainBean {
        /**
         * temp : 300.15
         * pressure : 1004
         * humidity : 83
         * temp_min : 300.15
         * temp_max : 300.15
         */
        @SerializedName("temp")
        var temp = 0.0

        @SerializedName("pressure")
        var pressure = 0.0

        @SerializedName("humidity")
        var humidity = 0.0

        @SerializedName("temp_min")
        var tempMin = 0.0

        @SerializedName("temp_max")
        var tempMax = 0.0

    }

    class WindBean {
        /**
         * speed : 1.12
         * deg : 255.502
         */
        @SerializedName("speed")
        var speed = 0.0

        @SerializedName("deg")
        var deg = 0.0

    }

    class CloudsBean {
        /**
         * all : 20
         */
        @SerializedName("all")
        var all = 0

    }

    class SysBean {
        /**
         * type : 1
         * id : 7461
         * message : 0.0045
         * country : CN
         * sunrise : 1534545120
         * sunset : 1534592550
         */
        @SerializedName("type")
        var type = 0

        @SerializedName("id")
        var id = 0

        @SerializedName("message")
        var message = 0.0

        @SerializedName("country")
        var country: String? = null

        @SerializedName("sunrise")
        var sunrise: Long = 0

        @SerializedName("sunset")
        var sunset: Long = 0

    }

    class WeatherBean {
        /**
         * id : 801
         * main : Clouds
         * description : 晴，少云
         * icon : 02n
         */
        @SerializedName("id")
        var id = 0

        @SerializedName("main")
        var main: String? = null

        @SerializedName("description")
        var description: String? = null

        @SerializedName("icon")
        var icon: String? = null

    }
}