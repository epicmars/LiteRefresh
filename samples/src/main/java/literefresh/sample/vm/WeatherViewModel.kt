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
package literefresh.sample.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import literefresh.sample.base.model.Resource
import literefresh.sample.common.http.RetrofitClientFactory
import literefresh.sample.data.remote.OpenWeatherMapApi
import literefresh.sample.data.remote.dto.ResCurrentWeather
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

