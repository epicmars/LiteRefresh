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

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.util.*

class ResTvPage {
    /**
     * page : 1
     * results : [{"poster_path":"/dDfjzRicTeVaiysRTwx56aM8bC3.jpg","popularity":5.4,"id":61889,"backdrop_path":null,"vote_average":7.74,"overview":"Lawyer-by-day Matt Murdock uses his heightened senses from being blinded as a young boy to fight crime at night on the streets of Hell\u2019s Kitchen as Daredevil.......","first_air_date":"2015-04-10","origin_country":["US"],"genre_ids":[28],"original_language":"en","vote_count":19,"name":"Marvel's Daredevil","original_name":"Marvel's Daredevil"}]
     * total_results : 61470
     * total_pages : 3074
     */
    @SerializedName("page")
    var page = 0

    @SerializedName("total_results")
    var totalResults = 0

    @SerializedName("total_pages")
    var totalPages = 0

    @SerializedName("results")
    var results: List<ResultsBean>? = null

    class ResultsBean : Parcelable {
        /**
         * poster_path : /dDfjzRicTeVaiysRTwx56aM8bC3.jpg
         * popularity : 5.4
         * id : 61889
         * backdrop_path : null
         * vote_average : 7.74
         * overview : Lawyer-by-day Matt Murdock uses his heightened senses from being blinded as a young boy to fight crime at night on the streets of Hell’s Kitchen as Daredevil.......
         * first_air_date : 2015-04-10
         * origin_country : ["US"]
         * genre_ids : [28]
         * original_language : en
         * vote_count : 19
         * name : Marvel's Daredevil
         * original_name : Marvel's Daredevil
         */
        @SerializedName("poster_path")
        var posterPath: String? = null

        @SerializedName("popularity")
        var popularity = 0.0

        @SerializedName("id")
        var id = 0

        @SerializedName("backdrop_path")
        private var backdropPath: String? = null

        @SerializedName("vote_average")
        var voteAverage = 0.0

        @SerializedName("overview")
        var overview: String? = null

        @SerializedName("first_air_date")
        var firstAirDate: String? = null

        @SerializedName("original_language")
        var originalLanguage: String? = null

        @SerializedName("vote_count")
        var voteCount = 0

        @SerializedName("name")
        var name: String? = null

        @SerializedName("original_name")
        var originalName: String? = null

        @SerializedName("origin_country")
        var originCountry: List<String>? = null

        @SerializedName("genre_ids")
        var genreIds: List<Int?>? = null

        fun getBackdropPath(): Any? {
            return backdropPath
        }

        fun setBackdropPath(backdropPath: String?) {
            this.backdropPath = backdropPath
        }

        constructor() {}

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeString(posterPath)
            dest.writeDouble(popularity)
            dest.writeInt(id)
            dest.writeString(backdropPath)
            dest.writeDouble(voteAverage)
            dest.writeString(overview)
            dest.writeString(firstAirDate)
            dest.writeString(originalLanguage)
            dest.writeInt(voteCount)
            dest.writeString(name)
            dest.writeString(originalName)
            dest.writeStringList(originCountry)
            dest.writeList(genreIds)
        }

        protected constructor(`in`: Parcel) {
            posterPath = `in`.readString()
            popularity = `in`.readDouble()
            id = `in`.readInt()
            backdropPath = `in`.readString()
            voteAverage = `in`.readDouble()
            overview = `in`.readString()
            firstAirDate = `in`.readString()
            originalLanguage = `in`.readString()
            voteCount = `in`.readInt()
            name = `in`.readString()
            originalName = `in`.readString()
            originCountry = `in`.createStringArrayList()
            genreIds = ArrayList()
            `in`.readList(genreIds, Int::class.java.classLoader)
        }

        companion object {
            val CREATOR: Parcelable.Creator<ResultsBean?> = object : Parcelable.Creator<ResultsBean?> {
                override fun createFromParcel(source: Parcel): ResultsBean? {
                    return ResultsBean(source)
                }

                override fun newArray(size: Int): Array<ResultsBean?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }
}