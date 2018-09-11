package com.androidpi.literefresh.sample.demos.weather.data.remote.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jastrelax on 2018/8/18.
 */
public class ResCurrentWeather {

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
    private CoordBean coord;
    @SerializedName("base")
    private String base;
    @SerializedName("main")
    private MainBean main;
    @SerializedName("visibility")
    private int visibility;
    @SerializedName("wind")
    private WindBean wind;
    @SerializedName("clouds")
    private CloudsBean clouds;
    @SerializedName("dt")
    private long dt;
    @SerializedName("sys")
    private SysBean sys;
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("cod")
    private int cod;
    @SerializedName("weather")
    private List<WeatherBean> weather;

    public CoordBean getCoord() {
        return coord;
    }

    public void setCoord(CoordBean coord) {
        this.coord = coord;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public MainBean getMain() {
        return main;
    }

    public void setMain(MainBean main) {
        this.main = main;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public WindBean getWind() {
        return wind;
    }

    public void setWind(WindBean wind) {
        this.wind = wind;
    }

    public CloudsBean getClouds() {
        return clouds;
    }

    public void setClouds(CloudsBean clouds) {
        this.clouds = clouds;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public SysBean getSys() {
        return sys;
    }

    public void setSys(SysBean sys) {
        this.sys = sys;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public List<WeatherBean> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherBean> weather) {
        this.weather = weather;
    }

    public static class CoordBean {
        /**
         * lon : 104.08
         * lat : 30.67
         */

        @SerializedName("lon")
        private double lon;
        @SerializedName("lat")
        private double lat;

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }
    }

    public static class MainBean {
        /**
         * temp : 300.15
         * pressure : 1004
         * humidity : 83
         * temp_min : 300.15
         * temp_max : 300.15
         */

        @SerializedName("temp")
        private double temp;
        @SerializedName("pressure")
        private double pressure;
        @SerializedName("humidity")
        private double humidity;
        @SerializedName("temp_min")
        private double tempMin;
        @SerializedName("temp_max")
        private double tempMax;

        public double getTemp() {
            return temp;
        }

        public void setTemp(double temp) {
            this.temp = temp;
        }

        public double getPressure() {
            return pressure;
        }

        public void setPressure(double pressure) {
            this.pressure = pressure;
        }

        public double getHumidity() {
            return humidity;
        }

        public void setHumidity(double humidity) {
            this.humidity = humidity;
        }

        public double getTempMin() {
            return tempMin;
        }

        public void setTempMin(double tempMin) {
            this.tempMin = tempMin;
        }

        public double getTempMax() {
            return tempMax;
        }

        public void setTempMax(double tempMax) {
            this.tempMax = tempMax;
        }
    }

    public static class WindBean {
        /**
         * speed : 1.12
         * deg : 255.502
         */

        @SerializedName("speed")
        private double speed;
        @SerializedName("deg")
        private double deg;

        public double getSpeed() {
            return speed;
        }

        public void setSpeed(double speed) {
            this.speed = speed;
        }

        public double getDeg() {
            return deg;
        }

        public void setDeg(double deg) {
            this.deg = deg;
        }
    }

    public static class CloudsBean {
        /**
         * all : 20
         */

        @SerializedName("all")
        private int all;

        public int getAll() {
            return all;
        }

        public void setAll(int all) {
            this.all = all;
        }
    }

    public static class SysBean {
        /**
         * type : 1
         * id : 7461
         * message : 0.0045
         * country : CN
         * sunrise : 1534545120
         * sunset : 1534592550
         */

        @SerializedName("type")
        private int type;
        @SerializedName("id")
        private int id;
        @SerializedName("message")
        private double message;
        @SerializedName("country")
        private String country;
        @SerializedName("sunrise")
        private long sunrise;
        @SerializedName("sunset")
        private long sunset;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getMessage() {
            return message;
        }

        public void setMessage(double message) {
            this.message = message;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public long getSunrise() {
            return sunrise;
        }

        public void setSunrise(long sunrise) {
            this.sunrise = sunrise;
        }

        public long getSunset() {
            return sunset;
        }

        public void setSunset(long sunset) {
            this.sunset = sunset;
        }
    }

    public static class WeatherBean {
        /**
         * id : 801
         * main : Clouds
         * description : 晴，少云
         * icon : 02n
         */

        @SerializedName("id")
        private int id;
        @SerializedName("main")
        private String main;
        @SerializedName("description")
        private String description;
        @SerializedName("icon")
        private String icon;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}
