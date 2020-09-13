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
package com.androidpi.literefresh.sample.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "news", indices = [Index(value = ["url"], unique = true)])
class News : Parcelable {

    @Embedded(prefix = "source_")
    @SerializedName("source")
    var source: Source? = null

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

    class Source : Parcelable {
        @SerializedName("id")
        var id: String? = null

        @SerializedName("name")
        var name: String? = null

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeString(id)
            dest.writeString(name)
        }

        constructor() {}
        protected constructor(`in`: Parcel) {
            id = `in`.readString()
            name = `in`.readString()
        }

        companion object {
            val CREATOR: Parcelable.Creator<Source?> = object : Parcelable.Creator<Source?> {
                override fun createFromParcel(source: Parcel): Source? {
                    return Source(source)
                }

                override fun newArray(size: Int): Array<Source?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(source, flags)
        dest.writeString(author)
        dest.writeString(title)
        dest.writeString(description)
        dest.writeString(url)
        dest.writeString(urlToImage)
        dest.writeString(publishedAt)
    }

    constructor() {}
    protected constructor(`in`: Parcel) {
        source = `in`.readParcelable(Source::class.java.classLoader)
        author = `in`.readString()
        title = `in`.readString()
        description = `in`.readString()
        url = `in`.readString()
        urlToImage = `in`.readString()
        publishedAt = `in`.readString()
    }

    companion object {
        val CREATOR: Parcelable.Creator<News?> = object : Parcelable.Creator<News?> {
            override fun createFromParcel(source: Parcel): News? {
                return News(source)
            }

            override fun newArray(size: Int): Array<News?> {
                return arrayOfNulls(size)
            }
        }
    }
}