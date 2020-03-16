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

class ResTrendingPage {
    /**
     * page : 1
     * results : [{"adult":false,"backdrop_path":"/bOGkgRGdhrBYJSLpXaxhXVstddV.jpg","genre_ids":[28,12,14,878],"id":299536,"original_language":"en","original_title":"Avengers: Infinity War","overview":"As the Avengers and their allies have continued to protect the world from threats too large for any one hero to handle, a new danger has emerged from the cosmic shadows: Thanos. A despot of intergalactic infamy, his goal is to collect all six Infinity Stones, artifacts of unimaginable power, and use them to inflict his twisted will on all of reality. Everything the Avengers have fought for has led up to this moment - the fate of Earth and existence itself has never been more uncertain.","poster_path":"/7WsyChQLEftFiDOVTGkv3hFpyyt.jpg","release_date":"2018-04-25","title":"Avengers: Infinity War","video":false,"vote_average":8.3,"vote_count":6937,"popularity":358.799},{"adult":false,"backdrop_path":"/3P52oz9HPQWxcwHOwxtyrVV1LKi.jpg","genre_ids":[28,35,878],"id":383498,"original_language":"en","original_title":"Deadpool 2","overview":"Wisecracking mercenary Deadpool battles the evil and powerful Cable and other bad guys to save a boy's life.","poster_path":"/to0spRl1CMDvyUbOnbb4fTk3VAd.jpg","release_date":"2018-05-15","title":"Deadpool 2","video":false,"vote_average":7.6,"vote_count":3938,"popularity":223.011},{"adult":false,"backdrop_path":"/22cUd4Yg5euCxIwWzXrL4m4otkU.jpg","genre_ids":[28,878,53],"id":500664,"original_language":"en","original_title":"Upgrade","overview":"A brutal mugging leaves Grey Trace paralyzed in the hospital and his beloved wife dead. A billionaire inventor soon offers Trace a cure \u2014 an artificial intelligence implant called STEM that will enhance his body. Now able to walk, Grey finds that he also has superhuman strength and agility \u2014 skills he uses to seek revenge against the thugs who destroyed his life.","poster_path":"/adOzdWS35KAo21r9R4BuFCkLer6.jpg","release_date":"2018-06-01","title":"Upgrade","video":false,"vote_average":7.6,"vote_count":138,"popularity":32.969},{"adult":false,"backdrop_path":"/uZTtVdOEIwPA6vwVRI3217DoPM.jpg","genre_ids":[35,10749],"id":466282,"original_language":"en","original_title":"To All the Boys I've Loved Before","overview":"Lara Jean's love life goes from imaginary to out of control when her secret letters to every boy she's ever fallen for are mysteriously mailed out.","poster_path":"/hKHZhUbIyUAjcSrqJThFGYIR6kI.jpg","release_date":"2018-08-17","title":"To All the Boys I've Loved Before","video":false,"vote_average":8.4,"vote_count":349,"popularity":31.76},{"adult":false,"backdrop_path":"/yRXzrwLfB5tDTIA3lSU9S3N9RUK.jpg","genre_ids":[35,18],"id":455980,"original_language":"en","original_title":"Tag","overview":"For one month every year, five highly competitive friends hit the ground running in a no-holds-barred game of tag they\u2019ve been playing since the first grade. This year, the game coincides with the wedding of their only undefeated player, which should finally make him an easy target. But he knows they\u2019re coming...and he\u2019s ready.","poster_path":"/eXXpuW2xaq5Aen9N5prFlARVIvr.jpg","release_date":"2018-06-14","title":"Tag","video":false,"vote_average":7,"vote_count":285,"popularity":87.194},{"backdrop_path":"/hHEqDPbO6z4Xje5tOf3Wm1mdMtI.jpg","first_air_date":"2018-08-17","genre_ids":[16,35,10765],"id":73021,"name":"Disenchantment","origin_country":["US"],"original_language":"en","original_name":"Disenchantment","overview":"Set in a ruined medieval city called Dreamland, Disenchantment follows the grubby adventures of a hard-drinking princess, her feisty elf companion and her personal demon.","poster_path":"/c3cUb0b3qHlWaawbLRC9DSsJwEr.jpg","vote_average":7.8,"vote_count":8,"popularity":19.929},{"adult":false,"backdrop_path":"/3ccBOsbVpgwN9K5whd2UB9ACebG.jpg","genre_ids":[80,18],"id":489931,"original_language":"en","original_title":"American Animals","overview":"Four young men mistake their lives for a movie and attempt one of the most audacious heists in U.S. history.","poster_path":"/aLbdKxgxuOPvs6CTlmzoOQ4Yg3j.jpg","release_date":"2018-06-01","title":"American Animals","video":false,"vote_average":7,"vote_count":38,"popularity":16.876},{"adult":false,"backdrop_path":"/tmpY6f0Lf7Dnx6inByjvHby4AYf.jpg","genre_ids":[35],"id":454283,"original_language":"en","original_title":"Action Point","overview":"A daredevil designs and operates his own theme park with his friends.","poster_path":"/5lqJx0uNKrD1cEKgaqF1LBsLAoi.jpg","release_date":"2018-06-01","title":"Action Point","video":false,"vote_average":5.3,"vote_count":31,"popularity":33.909},{"adult":false,"backdrop_path":"/cS6S6OcvcAjx0aBzvHPy1Sm4Snj.jpg","genre_ids":[18,14,27,53],"id":421792,"original_language":"en","original_title":"Down a Dark Hall","overview":"Kitt Gordy, a new student at the exclusive Blackwood Boarding School, confronts the institution's supernatural occurrences and dark powers of its headmistress.","poster_path":"/wErHaJrD1QZ2FEVneH6w0GZUz2L.jpg","release_date":"2018-08-01","title":"Down a Dark Hall","video":false,"vote_average":5.5,"vote_count":30,"popularity":11.162},{"adult":false,"backdrop_path":"/64jAqTJvrzEwncD3ARZdqYLcqbc.jpg","genre_ids":[12,53,10749],"id":429300,"original_language":"en","original_title":"Adrift","overview":"A true story of survival, as a young couple's chance encounter leads them first to love, and then on the adventure of a lifetime as they face one of the most catastrophic hurricanes in recorded history.","poster_path":"/5gLDeADaETvwQlQow5szlyuhLbj.jpg","release_date":"2018-05-31","title":"Adrift","video":false,"vote_average":6.4,"vote_count":170,"popularity":49.661},{"adult":false,"backdrop_path":"/gRtLcCQOpYUI9ThdVzi4VUP8QO3.jpg","genre_ids":[18,36,10752],"id":857,"original_language":"en","original_title":"Saving Private Ryan","overview":"As U.S. troops storm the beaches of Normandy, three brothers lie dead on the battlefield, with a fourth trapped behind enemy lines. Ranger captain John Miller and seven men are tasked with penetrating German-held territory and bringing the boy home.","poster_path":"/miDoEMlYDJhOCvxlzI0wZqBs9Yt.jpg","release_date":"1998-07-24","title":"Saving Private Ryan","video":false,"vote_average":8,"vote_count":6840,"popularity":15.153},{"adult":false,"backdrop_path":"/aOQjLmHGuFy3hsY26QDIctxjMol.jpg","genre_ids":[18,53],"id":470918,"original_language":"en","original_title":"Beast","overview":"A troubled woman living in an isolated community finds herself pulled between the control of her oppressive family and the allure of a secretive outsider suspected of a series of brutal murders.","poster_path":"/kZdncyp1IKhEqwv5zdmUpK5Dc7S.jpg","release_date":"2018-04-18","title":"Beast","video":false,"vote_average":6.9,"vote_count":19,"popularity":2.492},{"id":353081,"video":false,"vote_count":952,"vote_average":7.5,"title":"Mission: Impossible - Fallout","release_date":"2018-07-25","original_language":"en","original_title":"Mission: Impossible - Fallout","genre_ids":[28,12,53],"backdrop_path":"/5qxePyMYDisLe8rJiBYX8HKEyv2.jpg","adult":false,"overview":"When an IMF mission ends badly, the world is faced with dire consequences. As Ethan Hunt takes it upon himself to fulfil his original briefing, the CIA begin to question his loyalty and his motives. The IMF team find themselves in a race against time, hunted by assassins while trying to prevent a global catastrophe.","poster_path":"/AkJQpZp9WoNdj7pLYSj1L0RcMMN.jpg","popularity":139.023},{"adult":false,"backdrop_path":"/kNAzo7icHdFkF43JQa18mPEUtvf.jpg","genre_ids":[12,16,14],"id":271706,"original_language":"zh","original_title":"大魚海棠","overview":"Beyond the human realm, there is a magical race of beings who control the tides and the changing of the seasons. One of these beings, a young girl named Chun, seeks something more\u2014she wants to experience the human world! At sixteen, she finally gets her chance and transforms into a dolphin in order to explore the world that has her fascinated. But she soon discovers that it\u2019s a dangerous place and nearly gets killed in a vortex. Luckily, her life is spared when a young boy sacrifices himself to save her. Moved by his kindness and courage, she uses magic to bring him back to life only to learn that this power comes at a serious price. On a new adventure, she\u2019ll have to make her own sacrifices in order to protect his soul until it is ready to return to the human world.","poster_path":"/fRCdXh9MZutj1JJPZlUXMex6AuB.jpg","release_date":"2016-07-08","title":"Big Fish & Begonia","video":false,"vote_average":6.9,"vote_count":30,"popularity":7.424},{"original_name":"Game of Thrones","id":1399,"name":"Game of Thrones","vote_count":4772,"vote_average":8.2,"first_air_date":"2011-04-17","poster_path":"/gwPSoYUHAKmdyVywgLpKKA4BjRr.jpg","genre_ids":[18,10759,10765],"original_language":"en","backdrop_path":"/gX8SYlnL9ZznfZwEH4KJUePBFUM.jpg","overview":"Seven noble families fight for control of the mythical land of Westeros. Friction between the houses leads to full-scale war. All while a very ancient evil awakens in the farthest north. Amidst the war, a neglected military order of misfits, the Night's Watch, is all that stands between the realms of men and icy horrors beyond.","origin_country":["US"],"popularity":61.91},{"adult":false,"backdrop_path":"/5a7lMDn3nAj2ByO0X1fg6BhUphR.jpg","genre_ids":[12,14,878],"id":333339,"original_language":"en","original_title":"Ready Player One","overview":"When the creator of a popular video game system dies, a virtual contest is created to compete for his fortune.","poster_path":"/pU1ULUq8D3iRxl1fdX2lZIzdHuI.jpg","release_date":"2018-03-28","title":"Ready Player One","video":false,"vote_average":7.7,"vote_count":3673,"popularity":68.153},{"adult":false,"backdrop_path":"/wWoCid7YUxiLhq3ZZT6CtFEDPXw.jpg","genre_ids":[28],"id":347375,"original_language":"en","original_title":"Mile 22","overview":"A CIA field officer and an Indonesian police officer are forced to work together in confronting political corruption. An informant must be moved twenty-two miles to safety.","poster_path":"/2L8ehd95eSW9x7KINYtZmRkAlrZ.jpg","release_date":"2018-08-10","title":"Mile 22","video":false,"vote_average":6,"vote_count":8,"popularity":30.064},{"backdrop_path":"/okhLwP26UXHJ4KYGVsERQqp3129.jpg","first_air_date":"2015-08-23","genre_ids":[18,27],"id":62286,"name":"Fear the Walking Dead","origin_country":["US"],"original_language":"en","original_name":"Fear the Walking Dead","overview":"What did the world look like as it was transforming into the horrifying apocalypse depicted in \"The Walking Dead\"? This spin-off set in Los Angeles, following new characters as they face the beginning of the end of the world, will answer that question.","poster_path":"/gAEZitvNudXr9kphSd4XOlOkjPX.jpg","vote_average":6.4,"vote_count":791,"popularity":44.477},{"adult":false,"backdrop_path":"/bLJTjfbZ1c5zSNiAvGYs1Uc82ir.jpg","genre_ids":[28,12,14],"id":338970,"original_language":"en","original_title":"Tomb Raider","overview":"Lara Croft, the fiercely independent daughter of a missing adventurer, must push herself beyond her limits when she finds herself on the island where her father disappeared.","poster_path":"/3zrC5tUiR35rTz9stuIxnU1nUS5.jpg","release_date":"2018-03-05","title":"Tomb Raider","video":false,"vote_average":6.3,"vote_count":2530,"popularity":44.164},{"id":345940,"video":false,"vote_count":310,"vote_average":6.3,"title":"The Meg","release_date":"2018-08-09","original_language":"en","original_title":"The Meg","genre_ids":[28,27,878,53],"backdrop_path":"/ibKeXahq4JD63z6uWQphqoJLvNw.jpg","adult":false,"overview":"A deep sea submersible pilot revisits his past fears in the Mariana Trench, and accidentally unleashes the seventy foot ancestor of the Great White Shark believed to be extinct.","poster_path":"/xqECHNvzbDL5I3iiOVUkVPJMSbc.jpg","popularity":198.941}]
     * total_pages : 792
     * total_results : 15831
     */
    @SerializedName("page")
    var page = 0

    @SerializedName("total_pages")
    var totalPages = 0

    @SerializedName("total_results")
    var totalResults = 0

    @SerializedName("results")
    var results: List<ResultsBean>? = null

    class ResultsBean : Parcelable {
        /**
         * adult : false
         * backdrop_path : /bOGkgRGdhrBYJSLpXaxhXVstddV.jpg
         * genre_ids : [28,12,14,878]
         * id : 299536
         * original_language : en
         * original_title : Avengers: Infinity War
         * overview : As the Avengers and their allies have continued to protect the world from threats too large for any one hero to handle, a new danger has emerged from the cosmic shadows: Thanos. A despot of intergalactic infamy, his goal is to collect all six Infinity Stones, artifacts of unimaginable power, and use them to inflict his twisted will on all of reality. Everything the Avengers have fought for has led up to this moment - the fate of Earth and existence itself has never been more uncertain.
         * poster_path : /7WsyChQLEftFiDOVTGkv3hFpyyt.jpg
         * release_date : 2018-04-25
         * title : Avengers: Infinity War
         * video : false
         * vote_average : 8.3
         * vote_count : 6937
         * popularity : 358.799
         * first_air_date : 2018-08-17
         * name : Disenchantment
         * origin_country : ["US"]
         * original_name : Disenchantment
         */
        @SerializedName("adult")
        var isAdult = false

        @SerializedName("backdrop_path")
        var backdropPath: String? = null

        @SerializedName("id")
        var id = 0

        @SerializedName("original_language")
        var originalLanguage: String? = null

        @SerializedName("original_title")
        var originalTitle: String? = null

        @SerializedName("overview")
        var overview: String? = null

        @SerializedName("poster_path")
        var posterPath: String? = null

        @SerializedName("release_date")
        var releaseDate: String? = null

        @SerializedName("title")
        var title: String? = null

        @SerializedName("video")
        var isVideo = false

        @SerializedName("vote_average")
        var voteAverage = 0.0

        @SerializedName("vote_count")
        var voteCount = 0

        @SerializedName("popularity")
        var popularity = 0.0

        @SerializedName("first_air_date")
        var firstAirDate: String? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("original_name")
        var originalName: String? = null

        @SerializedName("genre_ids")
        var genreIds: List<Int?>? = null

        @SerializedName("origin_country")
        var originCountry: List<String>? = null

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeByte(if (isAdult) 1.toByte() else 0.toByte())
            dest.writeString(backdropPath)
            dest.writeInt(id)
            dest.writeString(originalLanguage)
            dest.writeString(originalTitle)
            dest.writeString(overview)
            dest.writeString(posterPath)
            dest.writeString(releaseDate)
            dest.writeString(title)
            dest.writeByte(if (isVideo) 1.toByte() else 0.toByte())
            dest.writeDouble(voteAverage)
            dest.writeInt(voteCount)
            dest.writeDouble(popularity)
            dest.writeString(firstAirDate)
            dest.writeString(name)
            dest.writeString(originalName)
            dest.writeList(genreIds)
            dest.writeStringList(originCountry)
        }

        constructor() {}
        protected constructor(`in`: Parcel) {
            isAdult = `in`.readByte().toInt() != 0
            backdropPath = `in`.readString()
            id = `in`.readInt()
            originalLanguage = `in`.readString()
            originalTitle = `in`.readString()
            overview = `in`.readString()
            posterPath = `in`.readString()
            releaseDate = `in`.readString()
            title = `in`.readString()
            isVideo = `in`.readByte().toInt() != 0
            voteAverage = `in`.readDouble()
            voteCount = `in`.readInt()
            popularity = `in`.readDouble()
            firstAirDate = `in`.readString()
            name = `in`.readString()
            originalName = `in`.readString()
            genreIds = ArrayList()
            `in`.readList(genreIds, Int::class.java.classLoader)
            originCountry = `in`.createStringArrayList()
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