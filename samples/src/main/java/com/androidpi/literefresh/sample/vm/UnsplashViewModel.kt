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
import com.androidpi.literefresh.sample.common.http.RetrofitClientFactory
import com.androidpi.literefresh.sample.data.remote.UnsplashApi
import com.androidpi.literefresh.sample.data.remote.dto.ResUnsplashPhoto
import com.androidpi.literefresh.sample.model.UnsplashPhotoPage
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class UnsplashViewModel : ViewModel() {

    companion object {
        const val PAGE_SIZE = 12
    }

    val randomPhotosResult = MutableLiveData<Resource<UnsplashPhotoPage>>()

    private val api: UnsplashApi

    var page: Int = UnsplashPhotoPage.FIRST_PAGE

    init {
        api = RetrofitClientFactory.newRetrofit(UnsplashApi.BASE_URL).create(UnsplashApi::class.java)
    }

    fun getRandomPhotos(count: Int = PAGE_SIZE, page: Int = 0) {
        this.page = page
        api.randomPhotos(count = count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<List<ResUnsplashPhoto>> {
                    override fun onSuccess(t: List<ResUnsplashPhoto>) {
                        randomPhotosResult.value = Resource.success(UnsplashPhotoPage(page, t))
                    }

                    override fun onSubscribe(d: Disposable) {
                        randomPhotosResult.value = Resource.loading(UnsplashPhotoPage(page, null))
                    }

                    override fun onError(e: Throwable) {
                        randomPhotosResult.value = Resource.error(null, UnsplashPhotoPage(page, null), e)
                    }
                })
    }

    fun firstPage() {
        getRandomPhotos(page = UnsplashPhotoPage.FIRST_PAGE)
    }

    fun nextPage() {
        getRandomPhotos(page = page + 1)
    }
}
