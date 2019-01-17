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

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.androidpi.literefresh.sample.base.model.Resource
import com.androidpi.literefresh.sample.common.http.RetrofitClientFactory
import com.androidpi.literefresh.sample.data.remote.NewsApi
import com.androidpi.literefresh.sample.data.remote.dto.ResTopHeadlines
import com.androidpi.literefresh.sample.model.News
import com.androidpi.literefresh.sample.model.NewsPagination
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class NewsViewModel : ViewModel() {

    val newsApi: NewsApi = RetrofitClientFactory.newRetrofit(NewsApi.BASE_URL).create(NewsApi::class.java)

    val mNews: MutableLiveData<Resource<NewsPagination>> = MutableLiveData()

    var mCategory: String? = null

    var currentPage = NewsPagination.FIRST_PAGE

    fun getLatestNews(isNext: Boolean, count: Int = NewsPagination.PAGE_SIZE) {
        val page = if (isNext) this.currentPage + 1 else NewsPagination.FIRST_PAGE
        this.currentPage = page
        Timber.d("getLatestedNews $page")
        refreshNews(page, count, mCategory)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<List<News>> {

                    override fun onSuccess(t: List<News>) {
                        if (t.isEmpty() && isNext) {
                            currentPage--
                        }
                        mNews.value = Resource.success(NewsPagination(page, t))
                    }

                    override fun onSubscribe(d: Disposable) {
                        mNews.value = Resource.loading()
                    }

                    override fun onError(e: Throwable) {
                        mNews.value = Resource.error(null, NewsPagination(page, null), e)
                    }
                })
    }

    fun refreshNews(page: Int = 1, count: Int = NewsPagination.PAGE_SIZE, category: String? = "general"): Single<List<News>> {
        return newsApi.topHeadlines(pageSize = count, page = page, category = category)
                .toObservable()
                .flatMap { resNews -> Observable.fromIterable(resNews.articles) }
                .map { t: ResTopHeadlines.ArticlesBean -> t.toNewsArticle() }
                .toList()
    }

    fun refreshPage() {
        getLatestNews(false)
    }

    fun nextPage() {
        getLatestNews(true)
    }
}
