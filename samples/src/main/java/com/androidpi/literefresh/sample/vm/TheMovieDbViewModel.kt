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
package com.androidpi.literefresh.sample.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidpi.literefresh.sample.base.model.Resource
import com.androidpi.literefresh.sample.common.datetime.DateTimeUtils
import com.androidpi.literefresh.sample.common.http.RetrofitClientFactory
import com.androidpi.literefresh.sample.data.remote.TheMovieDbApi
import com.androidpi.literefresh.sample.data.remote.dto.ResMoviePage
import com.androidpi.literefresh.sample.data.remote.dto.ResTrendingPage
import com.androidpi.literefresh.sample.data.remote.dto.ResTvPage
import com.androidpi.literefresh.sample.utils.RxUtils
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class TheMovieDbViewModel : ViewModel() {

    companion object {
        const val TRENDING_ALL = "all"
        const val TRENDING_MOVIE = "movie"
        const val TRENDING_TV = "tv"

        const val TRENDING_TIMEWINDOW_WEEK = "week"
        const val TRENDING_TIMEWINDOW_DAY = "day"
    }

    internal var api = RetrofitClientFactory
            .newRetrofit(TheMovieDbApi.BASE_URL_V3)
            .create(TheMovieDbApi::class.java)

    val movieWithinMonthResults = MutableLiveData<Resource<ResMoviePage>>()

    val tvWithinMonthResults = MutableLiveData<Resource<ResTvPage>>()

    val weekTrendingAllResults = MutableLiveData<Resource<ResTrendingPage>>()

    val weekTrendingMovieResults = MutableLiveData<Resource<ResTrendingPage>>()

    val weekTrendingTvResults = MutableLiveData<Resource<ResTrendingPage>>()

    val dayTrendingAllResults = MutableLiveData<Resource<ResTrendingPage>>()

    fun getMovieWithinMonth() {
        val now = Date()
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -1)
        val start = calendar.time

        api.discoverMovie(DateTimeUtils.FORMAT_DATE.format(start),
                DateTimeUtils.FORMAT_DATE.format(now))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<ResMoviePage> {
                    override fun onSuccess(t: ResMoviePage) {
                        movieWithinMonthResults.value = Resource.success(t)
                    }

                    override fun onSubscribe(d: Disposable) {
                        movieWithinMonthResults.value = Resource.loading();
                    }

                    override fun onError(e: Throwable) {
                        movieWithinMonthResults.value = Resource.error(e)
                    }
                })
    }

    fun getTvWithinMonth() {
        val now = Date()
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -1)
        val start = calendar.time

        api.discoverTv(DateTimeUtils.FORMAT_DATE.format(start),
                DateTimeUtils.FORMAT_DATE.format(now))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<ResTvPage> {
                    override fun onSuccess(t: ResTvPage) {
                        tvWithinMonthResults.value = Resource.success(t)
                    }

                    override fun onSubscribe(d: Disposable) {
                        tvWithinMonthResults.value = Resource.loading()
                    }

                    override fun onError(e: Throwable) {
                        tvWithinMonthResults.value = Resource.error(e)
                    }
                })
    }

    fun getWeekTrending(type: String?, data: MutableLiveData<Resource<ResTrendingPage>>?) {
        getTrending(type, TRENDING_TIMEWINDOW_WEEK, data)
    }

    fun getDayTrending(type: String?, data: MutableLiveData<Resource<ResTrendingPage>>?) {
        getTrending(type, TRENDING_TIMEWINDOW_DAY, data)
    }

    fun getTrending(type: String?, timeWindow: String, data: MutableLiveData<Resource<ResTrendingPage>>?) {
        api.trending(type, timeWindow)
                .compose(RxUtils.networkIO())
                .subscribe(object : SingleObserver<ResTrendingPage> {
                    override fun onSuccess(t: ResTrendingPage) {
                        data?.value = Resource.success(t)
                    }

                    override fun onSubscribe(d: Disposable) {
                        data?.value = Resource.loading()
                    }

                    override fun onError(e: Throwable) {
                        data?.value = Resource.error(e)
                    }
                })
    }
}
