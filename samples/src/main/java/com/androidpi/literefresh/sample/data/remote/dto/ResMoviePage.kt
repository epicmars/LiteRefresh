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
package com.androidpi.literefresh.sample.data.remote.dto

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.util.*

class ResMoviePage {
    /**
     * page : 1
     * total_results : 523
     * total_pages : 27
     * results : [{"vote_count":16,"id":507569,"video":false,"vote_average":6.1,"title":"The Seven Deadly Sins: Prisoners of the Sky","popularity":87.283,"poster_path":"/r6pPUVUKU5eIpYj4oEzidk5ZibB.jpg","original_language":"ja","original_title":"劇場版 七つの大罪 天空の囚われ人","genre_ids":[28,12,14,16],"backdrop_path":"/uKwOX7MtKlAaGeCQe6c4jc1vZpj.jpg","adult":false,"overview":"The Seven Deadly Sins travel to a remote land in search of the phantom ingredient \"sky fish.\" Meliodas and Hawk end up at a \"Sky Palace\" that exists above the clouds, where all the residents have wings. Meliodas is mistaken for a boy who committed a crime and is thrown in prison. Meanwhile, the residents are preparing a ceremony for defense against a ferocious beast that awakens once every 3,000 years. But the Six Knights of Black, a Demon Clan army, arrives and removes the seal on the beast in order to threaten the lives of the residents of Sky Palace. Meliodas and his allies meet the Six Knights of Black in battle.","release_date":"2018-08-18"},{"vote_count":117,"id":399360,"video":false,"vote_average":5.5,"title":"Alpha","popularity":75.67,"poster_path":"/afdZAIcAQscziqVtsEoh2PwsYTW.jpg","original_language":"en","original_title":"Alpha","genre_ids":[12,18],"backdrop_path":"/nKMeTdm72LQ756Eq20uTjF1zDXu.jpg","adult":false,"overview":"After a hunting expedition goes awry, a young caveman struggles against the elements to find his way home.","release_date":"2018-08-17"},{"vote_count":1491,"id":466282,"video":false,"vote_average":8.2,"title":"To All the Boys I've Loved Before","popularity":65.078,"poster_path":"/hKHZhUbIyUAjcSrqJThFGYIR6kI.jpg","original_language":"en","original_title":"To All the Boys I've Loved Before","genre_ids":[35,10749],"backdrop_path":"/hBHxBOGQBTMX3bDmqKoAgniZ9hE.jpg","adult":false,"overview":"Lara Jean's love life goes from imaginary to out of control when her secret letters to every boy she's ever fallen for are mysteriously mailed out.","release_date":"2018-08-17"},{"vote_count":5,"id":531593,"video":false,"vote_average":4.8,"title":"Reprisal","popularity":29.556,"poster_path":"/qBsQUK5QSeZjRPLFUVgSGHxj2PI.jpg","original_language":"en","original_title":"Reprisal","genre_ids":[28,53,80],"backdrop_path":"/j1iKtApo2dl1xaMTVtZvNQ3m2yK.jpg","adult":false,"overview":"Jacob, a bank manager haunted by a violent heist that took the life of a coworker, teams up with his ex-cop neighbor, James, to bring down the assailant. While the two men work together to figure out the thief\u2019s next move, Gabriel, the highly-trained criminal, is one step ahead. When Gabriel kidnaps Jacob\u2019s wife and daughter, Jacob barrels down a path of bloodshed that initiates an explosive counterattack and brings all three men to the breaking point.","release_date":"2018-08-31"},{"vote_count":39,"id":489999,"video":false,"vote_average":7.4,"title":"Searching","popularity":28.31,"poster_path":"/9N0T3BaHZNdUCcMZQIM3yMUFwEh.jpg","original_language":"en","original_title":"Searching","genre_ids":[18,53],"backdrop_path":"/1q5AauYD1szopz5qnd6qqxnpTHG.jpg","adult":false,"overview":"After his 16-year-old daughter goes missing, a desperate father breaks into her laptop to look for clues to find her. A thriller that unfolds entirely on computer screens.","release_date":"2018-08-24"},{"vote_count":151,"id":455207,"video":false,"vote_average":7.1,"title":"Crazy Rich Asians","popularity":25.628,"poster_path":"/gnTqi4nhIi1eesT5uYMmhEPGNih.jpg","original_language":"en","original_title":"Crazy Rich Asians","genre_ids":[35,18],"backdrop_path":"/zeHB7aP46Xs3u4aFLuAq2GFeUGb.jpg","adult":false,"overview":"An American-born Chinese economics professor accompanies her boyfriend to Singapore for his best friend's wedding, only to get thrust into the lives of Asia's rich and famous.","release_date":"2018-08-15"},{"vote_count":35,"id":425505,"video":false,"vote_average":6.9,"title":"Kin","popularity":23.076,"poster_path":"/cFDcj29pWfcRAxEWbUptwQQbj9o.jpg","original_language":"en","original_title":"Kin","genre_ids":[878,28],"backdrop_path":"/dQi56qgM8GYFUHqJqQE4dgSl56y.jpg","adult":false,"overview":"Co-directors Jonathan Baker and Josh Baker\u2019s Sci-Fi action thriller features James Franco, Zoe Kravitz, and Dennis Quaid. A young boy (Myles Truitt) finds a powerful otherworldly weapon, which he uses to save his older adoptive brother (Jack Reynor) from a crew of thugs. Before long, the two of them are also pursued by federal agents and mysterious mercenaries aiming to reclaim their asset.","release_date":"2018-08-29"},{"vote_count":9,"id":531145,"video":false,"vote_average":5.7,"title":"Boarding School","popularity":18.068,"poster_path":"/lun3G1IU6AYgXlXP649amULMPpv.jpg","original_language":"en","original_title":"Boarding School","genre_ids":[27],"backdrop_path":"/yXOMFVN1RDVcnbFvD0o8EG3cUBG.jpg","adult":false,"overview":"When troubled 12-year-old Jacob Felsen is sent away to boarding school, he enters every kid's worst nightmare: A creepy old mansion, deserted except for six other teenage misfits and two menacing and mysterious teachers. As events become increasingly horrific, Jacob must conquer his fears to find the strength to survive.","release_date":"2018-08-31"},{"vote_count":2,"id":472918,"video":false,"vote_average":7.5,"title":"Wiro Sableng: 212 Warrior","popularity":14.525,"poster_path":"/fVRj9skFb7CRJgXMjDGYscTYuja.jpg","original_language":"id","original_title":"Wiro Sableng: Pendekar Kapak Maut Naga Geni 212","genre_ids":[28,35,12],"backdrop_path":null,"adult":false,"overview":"Wiro is sent by his master Sinto Gendeng to stop Mahesa Birawa from ruining the whole kingdom.","release_date":"2018-08-30"},{"vote_count":11,"id":493551,"video":false,"vote_average":7.4,"title":"Operation Finale","popularity":14.498,"poster_path":"/uitoc4tDFFotLSs1vJBon52IZIx.jpg","original_language":"en","original_title":"Operation Finale","genre_ids":[18,36],"backdrop_path":"/7diKA3XyLUB9pV6oI9FznQ5lv66.jpg","adult":false,"overview":"15 years after the end of  World War II, a team of Israeli secret agents is assigned to track down Adolf Eichmann, the infamous Nazi architect of the Holocaust, reported hiding in Argentina, and smuggle him back to Israel for trial.  A true story.","release_date":"2018-08-29"},{"vote_count":7,"id":487258,"video":false,"vote_average":5.9,"title":"Marainthirunthu Paarkum Marmam Enna","popularity":13.979,"poster_path":"/2zfyj4gY7RHkf89ssSTly2UppMC.jpg","original_language":"ta","original_title":"மறைந்திருந்து பார்க்கும் மர்மம் என்ன","genre_ids":[10751,80,10749],"backdrop_path":"/lp2fC1MPUc8bCmDxJ8uXLvecyby.jpg","adult":false,"overview":"A daring young man joins a gang that is behind chain-snatching crimes in the city. What's his game plan?","release_date":"2018-08-17"},{"vote_count":39,"id":538604,"video":false,"vote_average":6.4,"title":"The After Party","popularity":13.804,"poster_path":"/wFPGheX6X4bt1ZqP1870MPlUvGu.jpg","original_language":"en","original_title":"The After Party","genre_ids":[35,10402],"backdrop_path":"/6GyzqGMUMmSTcCdkOoEFEq2FUWD.jpg","adult":false,"overview":"When an aspiring rapper goes viral for the wrong reasons, he thinks his career is sunk. But a wild party gives him one more chance to make it right.","release_date":"2018-08-24"},{"vote_count":37,"id":412988,"video":false,"vote_average":5.4,"title":"The Happytime Murders","popularity":13.365,"poster_path":"/rWxkur51srfVnMn2QOFjE7mbq6h.jpg","original_language":"en","original_title":"The Happytime Murders","genre_ids":[28,35,80],"backdrop_path":"/nMEGEPQIyS30KIJpZETGpluFquJ.jpg","adult":false,"overview":"When the puppet cast of a '90s children's TV show begins to get murdered one by one, a disgraced LAPD detective-turned-private eye puppet takes on the case.","release_date":"2018-08-22"},{"vote_count":11,"id":456086,"video":false,"vote_average":6.5,"title":"Support the Girls","popularity":12.213,"poster_path":"/hXfwrs8g3pY4mmaO5WDAowWLqBg.jpg","original_language":"en","original_title":"Support the Girls","genre_ids":[18,35],"backdrop_path":"/nzkNBGiyHBUdxQwBQ0eR2HZwIgu.jpg","adult":false,"overview":"Lisa Conroy may not love managing the restaurant Double Whammies, but she loves her employees more than anything, not only Danyelle, and Maci, her closest friends, but also her extended family. Unfortunately, the cheap, curmudgeonly owner Ben Cubby doesn\u2019t care nearly as much, and confronts Lisa when he learns that she\u2019s using the restaurant to raise money for Shaina, an employee in legal trouble related to an abusive boyfriend. To get even, the girls decide to sabotage the restaurant on the night of a major mixed martial arts fight.","release_date":"2018-08-24"},{"vote_count":35,"id":426814,"video":false,"vote_average":6.7,"title":"The Domestics","popularity":11.296,"poster_path":"/j64I3u1wwgPct3stvvKU2ikl14w.jpg","original_language":"en","original_title":"The Domestics","genre_ids":[53,27],"backdrop_path":"/uH5igyrxp7yg01xTEC2RHWQoFHq.jpg","adult":false,"overview":"A young husband and wife must fight to return home in a post-apocalyptic mid-western landscape ravaged by gangs.","release_date":"2018-08-23"},{"vote_count":11,"id":438590,"video":false,"vote_average":3.4,"title":"A.X.L.","popularity":10.932,"poster_path":"/9kB56ZdMB6RgY5QtX9Bar45jCeI.jpg","original_language":"en","original_title":"A.X.L.","genre_ids":[878],"backdrop_path":"/l1nYo0yzKjf84atnBDbx0do16vQ.jpg","adult":false,"overview":"The life of a teenage boy is forever altered by a chance encounter with cutting edge military technology.","release_date":"2018-08-16"},{"vote_count":14,"id":490004,"video":false,"vote_average":5.9,"title":"Arizona","popularity":9.486,"poster_path":"/qzeoTO2GoOvnBsYGxE4iJq1hrc8.jpg","original_language":"en","original_title":"Arizona","genre_ids":[35,53],"backdrop_path":"/ydYBgKoi6LwXdzjaUFOgQ5kpPED.jpg","adult":false,"overview":"Set in the midst of the 2009 housing crisis, the life of Cassie Fowler, a single mother and struggling realtor, goes off the rails when she witnesses a murder.","release_date":"2018-08-24"},{"vote_count":7,"id":458344,"video":false,"vote_average":7,"title":"Juliet, Naked","popularity":9.217,"poster_path":"/tj4lbeWQBvPwGjadEAAjJdQolko.jpg","original_language":"en","original_title":"Juliet, Naked","genre_ids":[35,18,10749,10402],"backdrop_path":"/6Bm5vkfHPsNckefypVlPc7xSkpT.jpg","adult":false,"overview":"Annie is stuck in a long-term relationship with Duncan \u2013 an obsessive fan of obscure rocker Tucker Crowe. When the acoustic demo of Tucker\u2019s hit record from 25 years ago surfaces, its release leads to a life-changing encounter with the elusive rocker himself.","release_date":"2018-08-16"},{"vote_count":4,"id":340613,"video":false,"vote_average":9,"title":"The Wife","popularity":9.084,"poster_path":"/5zDB2tIYwfMSmNpgARHbDKpPgdg.jpg","original_language":"en","original_title":"The Wife","genre_ids":[18],"backdrop_path":"/eULLdgpuN9AxLKiOuAW7PO0Vjd.jpg","adult":false,"overview":"A wife questions her life choices as she travels to Stockholm with her husband, where he is slated to receive the Nobel Prize for Literature.","release_date":"2018-08-17"},{"vote_count":4,"id":434596,"video":false,"vote_average":7,"title":"An Actor Prepares","popularity":9.026,"poster_path":"/8rSc59ajnmV9EsNWdLFSjRlFfT3.jpg","original_language":"en","original_title":"An Actor Prepares","genre_ids":[35,18],"backdrop_path":"/5RtpoYfCiyDC9WLzUiIIBHxdjs1.jpg","adult":false,"overview":"After suffering a heart attack, a world-famous and hard-drinking actor is forced to drive across country with his estranged son\u2014who testified against him in his parents' divorce\u2014on one last madcap adventure.","release_date":"2018-08-31"}]
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
         * vote_count : 16
         * id : 507569
         * video : false
         * vote_average : 6.1
         * title : The Seven Deadly Sins: Prisoners of the Sky
         * popularity : 87.283
         * poster_path : /r6pPUVUKU5eIpYj4oEzidk5ZibB.jpg
         * original_language : ja
         * original_title : 劇場版 七つの大罪 天空の囚われ人
         * genre_ids : [28,12,14,16]
         * backdrop_path : /uKwOX7MtKlAaGeCQe6c4jc1vZpj.jpg
         * adult : false
         * overview : The Seven Deadly Sins travel to a remote land in search of the phantom ingredient "sky fish." Meliodas and Hawk end up at a "Sky Palace" that exists above the clouds, where all the residents have wings. Meliodas is mistaken for a boy who committed a crime and is thrown in prison. Meanwhile, the residents are preparing a ceremony for defense against a ferocious beast that awakens once every 3,000 years. But the Six Knights of Black, a Demon Clan army, arrives and removes the seal on the beast in order to threaten the lives of the residents of Sky Palace. Meliodas and his allies meet the Six Knights of Black in battle.
         * release_date : 2018-08-18
         */
        @SerializedName("vote_count")
        var voteCount = 0

        @SerializedName("id")
        var id = 0

        @SerializedName("video")
        var isVideo = false

        @SerializedName("vote_average")
        var voteAverage = 0.0

        @SerializedName("title")
        var title: String? = null

        @SerializedName("popularity")
        var popularity = 0.0

        @SerializedName("poster_path")
        var posterPath: String? = null

        @SerializedName("original_language")
        var originalLanguage: String? = null

        @SerializedName("original_title")
        var originalTitle: String? = null

        @SerializedName("backdrop_path")
        var backdropPath: String? = null

        @SerializedName("adult")
        var isAdult = false

        @SerializedName("overview")
        var overview: String? = null

        @SerializedName("release_date")
        var releaseDate: String? = null

        @SerializedName("genre_ids")
        var genreIds: List<Int?>? = null

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeInt(voteCount)
            dest.writeInt(id)
            dest.writeByte(if (isVideo) 1.toByte() else 0.toByte())
            dest.writeDouble(voteAverage)
            dest.writeString(title)
            dest.writeDouble(popularity)
            dest.writeString(posterPath)
            dest.writeString(originalLanguage)
            dest.writeString(originalTitle)
            dest.writeString(backdropPath)
            dest.writeByte(if (isAdult) 1.toByte() else 0.toByte())
            dest.writeString(overview)
            dest.writeString(releaseDate)
            dest.writeList(genreIds)
        }

        constructor() {}
        protected constructor(`in`: Parcel) {
            voteCount = `in`.readInt()
            id = `in`.readInt()
            isVideo = `in`.readByte().toInt() != 0
            voteAverage = `in`.readDouble()
            title = `in`.readString()
            popularity = `in`.readDouble()
            posterPath = `in`.readString()
            originalLanguage = `in`.readString()
            originalTitle = `in`.readString()
            backdropPath = `in`.readString()
            isAdult = `in`.readByte().toInt() != 0
            overview = `in`.readString()
            releaseDate = `in`.readString()
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