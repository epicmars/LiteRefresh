package com.androidpi.literefresh.sample.demos.weather.vm

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.androidpi.literefresh.sample.base.model.Resource
import com.androidpi.literefresh.sample.common.http.RetrofitClientFactory
import com.androidpi.literefresh.sample.demos.weather.data.remote.OpenWeatherMapApi
import com.androidpi.literefresh.sample.demos.weather.data.remote.dto.ResCurrentWeather
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class WeatherViewModel : ViewModel() {

    private var api: OpenWeatherMapApi
    val weatherResult = MutableLiveData<Resource<ResCurrentWeather>>()

    init {
        api = RetrofitClientFactory.newRetrofit(OpenWeatherMapApi.BASE_URL).create(OpenWeatherMapApi::class.java)
    }

    fun getCurrentWeather(lat: Float, lon: Float) {
        api.getCurrentWeather(lat, lon)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<ResCurrentWeather> {
                    override fun onSuccess(t: ResCurrentWeather) {
                        weatherResult.value = Resource.success(t)
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onError(e: Throwable) {
                        weatherResult.value = Resource.error(e)
                    }
                })
    }

}
