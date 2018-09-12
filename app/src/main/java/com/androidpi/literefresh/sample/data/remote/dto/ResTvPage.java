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
package com.androidpi.literefresh.sample.data.remote.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ResTvPage {


    /**
     * page : 1
     * results : [{"poster_path":"/dDfjzRicTeVaiysRTwx56aM8bC3.jpg","popularity":5.4,"id":61889,"backdrop_path":null,"vote_average":7.74,"overview":"Lawyer-by-day Matt Murdock uses his heightened senses from being blinded as a young boy to fight crime at night on the streets of Hell\u2019s Kitchen as Daredevil.......","first_air_date":"2015-04-10","origin_country":["US"],"genre_ids":[28],"original_language":"en","vote_count":19,"name":"Marvel's Daredevil","original_name":"Marvel's Daredevil"}]
     * total_results : 61470
     * total_pages : 3074
     */

    @SerializedName("page")
    private int page;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;
    @SerializedName("results")
    private List<ResultsBean> results;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean implements Parcelable{
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
        private String posterPath;
        @SerializedName("popularity")
        private double popularity;
        @SerializedName("id")
        private int id;
        @SerializedName("backdrop_path")
        private String backdropPath;
        @SerializedName("vote_average")
        private double voteAverage;
        @SerializedName("overview")
        private String overview;
        @SerializedName("first_air_date")
        private String firstAirDate;
        @SerializedName("original_language")
        private String originalLanguage;
        @SerializedName("vote_count")
        private int voteCount;
        @SerializedName("name")
        private String name;
        @SerializedName("original_name")
        private String originalName;
        @SerializedName("origin_country")
        private List<String> originCountry;
        @SerializedName("genre_ids")
        private List<Integer> genreIds;

        public String getPosterPath() {
            return posterPath;
        }

        public void setPosterPath(String posterPath) {
            this.posterPath = posterPath;
        }

        public double getPopularity() {
            return popularity;
        }

        public void setPopularity(double popularity) {
            this.popularity = popularity;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getBackdropPath() {
            return backdropPath;
        }

        public void setBackdropPath(String backdropPath) {
            this.backdropPath = backdropPath;
        }

        public double getVoteAverage() {
            return voteAverage;
        }

        public void setVoteAverage(double voteAverage) {
            this.voteAverage = voteAverage;
        }

        public String getOverview() {
            return overview;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public String getFirstAirDate() {
            return firstAirDate;
        }

        public void setFirstAirDate(String firstAirDate) {
            this.firstAirDate = firstAirDate;
        }

        public String getOriginalLanguage() {
            return originalLanguage;
        }

        public void setOriginalLanguage(String originalLanguage) {
            this.originalLanguage = originalLanguage;
        }

        public int getVoteCount() {
            return voteCount;
        }

        public void setVoteCount(int voteCount) {
            this.voteCount = voteCount;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOriginalName() {
            return originalName;
        }

        public void setOriginalName(String originalName) {
            this.originalName = originalName;
        }

        public List<String> getOriginCountry() {
            return originCountry;
        }

        public void setOriginCountry(List<String> originCountry) {
            this.originCountry = originCountry;
        }

        public List<Integer> getGenreIds() {
            return genreIds;
        }

        public void setGenreIds(List<Integer> genreIds) {
            this.genreIds = genreIds;
        }


        public ResultsBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.posterPath);
            dest.writeDouble(this.popularity);
            dest.writeInt(this.id);
            dest.writeString(this.backdropPath);
            dest.writeDouble(this.voteAverage);
            dest.writeString(this.overview);
            dest.writeString(this.firstAirDate);
            dest.writeString(this.originalLanguage);
            dest.writeInt(this.voteCount);
            dest.writeString(this.name);
            dest.writeString(this.originalName);
            dest.writeStringList(this.originCountry);
            dest.writeList(this.genreIds);
        }

        protected ResultsBean(Parcel in) {
            this.posterPath = in.readString();
            this.popularity = in.readDouble();
            this.id = in.readInt();
            this.backdropPath = in.readString();
            this.voteAverage = in.readDouble();
            this.overview = in.readString();
            this.firstAirDate = in.readString();
            this.originalLanguage = in.readString();
            this.voteCount = in.readInt();
            this.name = in.readString();
            this.originalName = in.readString();
            this.originCountry = in.createStringArrayList();
            this.genreIds = new ArrayList<Integer>();
            in.readList(this.genreIds, Integer.class.getClassLoader());
        }

        public static final Creator<ResultsBean> CREATOR = new Creator<ResultsBean>() {
            @Override
            public ResultsBean createFromParcel(Parcel source) {
                return new ResultsBean(source);
            }

            @Override
            public ResultsBean[] newArray(int size) {
                return new ResultsBean[size];
            }
        };
    }
}
