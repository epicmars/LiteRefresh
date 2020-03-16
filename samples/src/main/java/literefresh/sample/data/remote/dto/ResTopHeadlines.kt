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

import literefresh.sample.model.News
import com.google.gson.annotations.SerializedName

class ResTopHeadlines {
    @SerializedName("status")
    var status: String? = null

    @SerializedName("totalResults")
    var totalResults = 0

    @SerializedName("articles")
    var articles: List<ArticlesBean>? = null

    class ArticlesBean {
        @SerializedName("source")
        var source: SourceBean? = null

        @SerializedName("author")
        var author: String? = null

        @SerializedName("title")
        var title: String? = null

        @SerializedName("description")
        var description: String? = null

        @SerializedName("url")
        var url: String? = null

        @SerializedName("urlToImage")
        var urlToImage: String? = null

        @SerializedName("publishedAt")
        var publishedAt: String? = null

        fun toNewsArticle(): News {
            val news = News()
            news.author = author
            news.description = description
            news.publishedAt = publishedAt
            news.source = source!!.toSource()
            news.title = title
            news.url = url
            news.urlToImage = urlToImage
            return news
        }

        class SourceBean {
            @SerializedName("id")
            var id: String? = null

            @SerializedName("name")
            var name: String? = null

            fun toSource(): News.Source {
                val source = News.Source()
                source.id = id
                source.name = name
                return source
            }
        }
    }
}