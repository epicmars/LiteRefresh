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

import com.google.gson.annotations.SerializedName

class ResUnsplashPhoto {
    /**
     * id : Qdr4mpifNQQ
     * created_at : 2018-03-19T07:45:24-04:00
     * updated_at : 2018-05-09T05:15:55-04:00
     * width : 5472
     * height : 3648
     * color : #F3E3D2
     * description : null
     * urls : {"raw":"https://images.unsplash.com/photo-1521459893400-80c3a8926f5b?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjMyMDE3fQ&s=ec5ba2979d9ac3b4f372062c91ffff29","full":"https://images.unsplash.com/photo-1521459893400-80c3a8926f5b?ixlib=rb-0.3.5&q=85&fm=jpg&crop=entropy&cs=srgb&ixid=eyJhcHBfaWQiOjMyMDE3fQ&s=b340b9e974abc2e62ef0f5423f7f3727","regular":"https://images.unsplash.com/photo-1521459893400-80c3a8926f5b?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjMyMDE3fQ&s=600db1f8b9761decda8152d8eb1e20a2","small":"https://images.unsplash.com/photo-1521459893400-80c3a8926f5b?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max&ixid=eyJhcHBfaWQiOjMyMDE3fQ&s=9441e5151b8a210c7312cd8feba8f824","thumb":"https://images.unsplash.com/photo-1521459893400-80c3a8926f5b?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&ixid=eyJhcHBfaWQiOjMyMDE3fQ&s=089fb3b7813b395520826fc4700cc652"}
     * links : {"self":"https://api.unsplash.com/photos/Qdr4mpifNQQ","html":"https://unsplash.com/photos/Qdr4mpifNQQ","download":"https://unsplash.com/photos/Qdr4mpifNQQ/download","download_location":"https://api.unsplash.com/photos/Qdr4mpifNQQ/download"}
     * categories : []
     * sponsored : false
     * likes : 4
     * liked_by_user : false
     * current_user_collections : []
     * slug : null
     * user : {"id":"MOoBF6DIHSs","updated_at":"2018-06-27T11:18:30-04:00","username":"jamesbold","name":"James Bold","first_name":"James","last_name":"Bold","twitter_username":"iamjamesbold","portfolio_url":"https://smartphotocourses.com","bio":"Photography, writer, creator.","location":null,"links":{"self":"https://api.unsplash.com/users/jamesbold","html":"https://unsplash.com/@jamesbold","photos":"https://api.unsplash.com/users/jamesbold/photos","likes":"https://api.unsplash.com/users/jamesbold/likes","portfolio":"https://api.unsplash.com/users/jamesbold/portfolio","following":"https://api.unsplash.com/users/jamesbold/following","followers":"https://api.unsplash.com/users/jamesbold/followers"},"profile_image":{"small":"https://images.unsplash.com/profile-1492679404723-7d73c381e4a5?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=32&w=32&s=1e71bf8fbb0228779557cc76d074126a","medium":"https://images.unsplash.com/profile-1492679404723-7d73c381e4a5?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=64&w=64&s=274a3a90072b1a95ec7e241832abb4e8","large":"https://images.unsplash.com/profile-1492679404723-7d73c381e4a5?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=128&w=128&s=3d5f7851f2af9c02b21ca37fc40d2c5f"},"instagram_username":"michaelbold1","total_collections":0,"total_likes":5,"total_photos":64}
     * exif : {"make":null,"model":null,"exposure_time":null,"aperture":null,"focal_length":null,"iso":null}
     * views : 351967
     * downloads : 784
     */
    @SerializedName("id")
    var id: String? = null

    @SerializedName("created_at")
    var createdAt: String? = null

    @SerializedName("updated_at")
    var updatedAt: String? = null

    @SerializedName("width")
    var width = 0

    @SerializedName("height")
    var height = 0

    @SerializedName("color")
    var color: String? = null

    @SerializedName("description")
    var description: Any? = null

    @SerializedName("urls")
    var urls: UrlsBean? = null

    @SerializedName("links")
    var links: LinksBean? = null

    @SerializedName("sponsored")
    var isSponsored = false

    @SerializedName("likes")
    var likes = 0

    @SerializedName("liked_by_user")
    var isLikedByUser = false

    @SerializedName("slug")
    var slug: Any? = null

    @SerializedName("user")
    var user: UserBean? = null

    @SerializedName("exif")
    var exif: ExifBean? = null

    @SerializedName("views")
    var views = 0

    @SerializedName("downloads")
    var downloads = 0

    @SerializedName("categories")
    var categories: List<*>? = null

    @SerializedName("current_user_collections")
    var currentUserCollections: List<*>? = null

    class UrlsBean {
        /**
         * raw : https://images.unsplash.com/photo-1521459893400-80c3a8926f5b?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjMyMDE3fQ&s=ec5ba2979d9ac3b4f372062c91ffff29
         * full : https://images.unsplash.com/photo-1521459893400-80c3a8926f5b?ixlib=rb-0.3.5&q=85&fm=jpg&crop=entropy&cs=srgb&ixid=eyJhcHBfaWQiOjMyMDE3fQ&s=b340b9e974abc2e62ef0f5423f7f3727
         * regular : https://images.unsplash.com/photo-1521459893400-80c3a8926f5b?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjMyMDE3fQ&s=600db1f8b9761decda8152d8eb1e20a2
         * small : https://images.unsplash.com/photo-1521459893400-80c3a8926f5b?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max&ixid=eyJhcHBfaWQiOjMyMDE3fQ&s=9441e5151b8a210c7312cd8feba8f824
         * thumb : https://images.unsplash.com/photo-1521459893400-80c3a8926f5b?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&ixid=eyJhcHBfaWQiOjMyMDE3fQ&s=089fb3b7813b395520826fc4700cc652
         */
        @SerializedName("raw")
        var raw: String? = null

        @SerializedName("full")
        var full: String? = null

        @SerializedName("regular")
        var regular: String? = null

        @SerializedName("small")
        var small: String? = null

        @SerializedName("thumb")
        var thumb: String? = null

        override fun equals(o: Any?): Boolean {
            if (this === o) return true
            if (o == null || javaClass != o.javaClass) return false
            val urlsBean = o as UrlsBean
            if (if (raw != null) raw != urlsBean.raw else urlsBean.raw != null) return false
            if (if (full != null) full != urlsBean.full else urlsBean.full != null) return false
            if (if (regular != null) regular != urlsBean.regular else urlsBean.regular != null) return false
            if (if (small != null) small != urlsBean.small else urlsBean.small != null) return false
            return if (thumb != null) thumb == urlsBean.thumb else urlsBean.thumb == null
        }

        override fun hashCode(): Int {
            var result = if (raw != null) raw.hashCode() else 0
            result = 31 * result + if (full != null) full.hashCode() else 0
            result = 31 * result + if (regular != null) regular.hashCode() else 0
            result = 31 * result + if (small != null) small.hashCode() else 0
            result = 31 * result + if (thumb != null) thumb.hashCode() else 0
            return result
        }
    }

    class LinksBean {
        /**
         * self : https://api.unsplash.com/photos/Qdr4mpifNQQ
         * html : https://unsplash.com/photos/Qdr4mpifNQQ
         * download : https://unsplash.com/photos/Qdr4mpifNQQ/download
         * download_location : https://api.unsplash.com/photos/Qdr4mpifNQQ/download
         */
        @SerializedName("self")
        var self: String? = null

        @SerializedName("html")
        var html: String? = null

        @SerializedName("download")
        var download: String? = null

        @SerializedName("download_location")
        var downloadLocation: String? = null

        override fun equals(o: Any?): Boolean {
            if (this === o) return true
            if (o == null || javaClass != o.javaClass) return false
            val linksBean = o as LinksBean
            if (if (self != null) self != linksBean.self else linksBean.self != null) return false
            if (if (html != null) html != linksBean.html else linksBean.html != null) return false
            if (if (download != null) download != linksBean.download else linksBean.download != null) return false
            return if (downloadLocation != null) downloadLocation == linksBean.downloadLocation else linksBean.downloadLocation == null
        }

        override fun hashCode(): Int {
            var result = if (self != null) self.hashCode() else 0
            result = 31 * result + if (html != null) html.hashCode() else 0
            result = 31 * result + if (download != null) download.hashCode() else 0
            result = 31 * result + if (downloadLocation != null) downloadLocation.hashCode() else 0
            return result
        }
    }

    class UserBean {
        /**
         * id : MOoBF6DIHSs
         * updated_at : 2018-06-27T11:18:30-04:00
         * username : jamesbold
         * name : James Bold
         * first_name : James
         * last_name : Bold
         * twitter_username : iamjamesbold
         * portfolio_url : https://smartphotocourses.com
         * bio : Photography, writer, creator.
         * location : null
         * links : {"self":"https://api.unsplash.com/users/jamesbold","html":"https://unsplash.com/@jamesbold","photos":"https://api.unsplash.com/users/jamesbold/photos","likes":"https://api.unsplash.com/users/jamesbold/likes","portfolio":"https://api.unsplash.com/users/jamesbold/portfolio","following":"https://api.unsplash.com/users/jamesbold/following","followers":"https://api.unsplash.com/users/jamesbold/followers"}
         * profile_image : {"small":"https://images.unsplash.com/profile-1492679404723-7d73c381e4a5?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=32&w=32&s=1e71bf8fbb0228779557cc76d074126a","medium":"https://images.unsplash.com/profile-1492679404723-7d73c381e4a5?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=64&w=64&s=274a3a90072b1a95ec7e241832abb4e8","large":"https://images.unsplash.com/profile-1492679404723-7d73c381e4a5?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=128&w=128&s=3d5f7851f2af9c02b21ca37fc40d2c5f"}
         * instagram_username : michaelbold1
         * total_collections : 0
         * total_likes : 5
         * total_photos : 64
         */
        @SerializedName("id")
        var id: String? = null

        @SerializedName("updated_at")
        var updatedAt: String? = null

        @SerializedName("username")
        var username: String? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("first_name")
        var firstName: String? = null

        @SerializedName("last_name")
        var lastName: String? = null

        @SerializedName("twitter_username")
        var twitterUsername: String? = null

        @SerializedName("portfolio_url")
        var portfolioUrl: String? = null

        @SerializedName("bio")
        var bio: String? = null

        @SerializedName("location")
        var location: Any? = null

        @SerializedName("links")
        var links: LinksBeanX? = null

        @SerializedName("profile_image")
        var profileImage: ProfileImageBean? = null

        @SerializedName("instagram_username")
        var instagramUsername: String? = null

        @SerializedName("total_collections")
        var totalCollections = 0

        @SerializedName("total_likes")
        var totalLikes = 0

        @SerializedName("total_photos")
        var totalPhotos = 0

        class LinksBeanX {
            /**
             * self : https://api.unsplash.com/users/jamesbold
             * html : https://unsplash.com/@jamesbold
             * photos : https://api.unsplash.com/users/jamesbold/photos
             * likes : https://api.unsplash.com/users/jamesbold/likes
             * portfolio : https://api.unsplash.com/users/jamesbold/portfolio
             * following : https://api.unsplash.com/users/jamesbold/following
             * followers : https://api.unsplash.com/users/jamesbold/followers
             */
            @SerializedName("self")
            var self: String? = null

            @SerializedName("html")
            var html: String? = null

            @SerializedName("photos")
            var photos: String? = null

            @SerializedName("likes")
            var likes: String? = null

            @SerializedName("portfolio")
            var portfolio: String? = null

            @SerializedName("following")
            var following: String? = null

            @SerializedName("followers")
            var followers: String? = null

            override fun equals(o: Any?): Boolean {
                if (this === o) return true
                if (o == null || javaClass != o.javaClass) return false
                val that = o as LinksBeanX
                if (if (self != null) self != that.self else that.self != null) return false
                if (if (html != null) html != that.html else that.html != null) return false
                if (if (photos != null) photos != that.photos else that.photos != null) return false
                if (if (likes != null) likes != that.likes else that.likes != null) return false
                if (if (portfolio != null) portfolio != that.portfolio else that.portfolio != null) return false
                if (if (following != null) following != that.following else that.following != null) return false
                return if (followers != null) followers == that.followers else that.followers == null
            }

            override fun hashCode(): Int {
                var result = if (self != null) self.hashCode() else 0
                result = 31 * result + if (html != null) html.hashCode() else 0
                result = 31 * result + if (photos != null) photos.hashCode() else 0
                result = 31 * result + if (likes != null) likes.hashCode() else 0
                result = 31 * result + if (portfolio != null) portfolio.hashCode() else 0
                result = 31 * result + if (following != null) following.hashCode() else 0
                result = 31 * result + if (followers != null) followers.hashCode() else 0
                return result
            }
        }

        class ProfileImageBean {
            /**
             * small : https://images.unsplash.com/profile-1492679404723-7d73c381e4a5?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=32&w=32&s=1e71bf8fbb0228779557cc76d074126a
             * medium : https://images.unsplash.com/profile-1492679404723-7d73c381e4a5?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=64&w=64&s=274a3a90072b1a95ec7e241832abb4e8
             * large : https://images.unsplash.com/profile-1492679404723-7d73c381e4a5?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=128&w=128&s=3d5f7851f2af9c02b21ca37fc40d2c5f
             */
            @SerializedName("small")
            var small: String? = null

            @SerializedName("medium")
            var medium: String? = null

            @SerializedName("large")
            var large: String? = null

            override fun equals(o: Any?): Boolean {
                if (this === o) return true
                if (o == null || javaClass != o.javaClass) return false
                val that = o as ProfileImageBean
                if (if (small != null) small != that.small else that.small != null) return false
                if (if (medium != null) medium != that.medium else that.medium != null) return false
                return if (large != null) large == that.large else that.large == null
            }

            override fun hashCode(): Int {
                var result = if (small != null) small.hashCode() else 0
                result = 31 * result + if (medium != null) medium.hashCode() else 0
                result = 31 * result + if (large != null) large.hashCode() else 0
                return result
            }
        }

        override fun equals(o: Any?): Boolean {
            if (this === o) return true
            if (o == null || javaClass != o.javaClass) return false
            val userBean = o as UserBean
            if (totalCollections != userBean.totalCollections) return false
            if (totalLikes != userBean.totalLikes) return false
            if (totalPhotos != userBean.totalPhotos) return false
            if (if (id != null) id != userBean.id else userBean.id != null) return false
            if (if (updatedAt != null) updatedAt != userBean.updatedAt else userBean.updatedAt != null) return false
            if (if (username != null) username != userBean.username else userBean.username != null) return false
            if (if (name != null) name != userBean.name else userBean.name != null) return false
            if (if (firstName != null) firstName != userBean.firstName else userBean.firstName != null) return false
            if (if (lastName != null) lastName != userBean.lastName else userBean.lastName != null) return false
            if (if (twitterUsername != null) twitterUsername != userBean.twitterUsername else userBean.twitterUsername != null) return false
            if (if (portfolioUrl != null) portfolioUrl != userBean.portfolioUrl else userBean.portfolioUrl != null) return false
            if (if (bio != null) bio != userBean.bio else userBean.bio != null) return false
            if (if (location != null) location != userBean.location else userBean.location != null) return false
            if (if (links != null) links != userBean.links else userBean.links != null) return false
            if (if (profileImage != null) profileImage != userBean.profileImage else userBean.profileImage != null) return false
            return if (instagramUsername != null) instagramUsername == userBean.instagramUsername else userBean.instagramUsername == null
        }

        override fun hashCode(): Int {
            var result = if (id != null) id.hashCode() else 0
            result = 31 * result + if (updatedAt != null) updatedAt.hashCode() else 0
            result = 31 * result + if (username != null) username.hashCode() else 0
            result = 31 * result + if (name != null) name.hashCode() else 0
            result = 31 * result + if (firstName != null) firstName.hashCode() else 0
            result = 31 * result + if (lastName != null) lastName.hashCode() else 0
            result = 31 * result + if (twitterUsername != null) twitterUsername.hashCode() else 0
            result = 31 * result + if (portfolioUrl != null) portfolioUrl.hashCode() else 0
            result = 31 * result + if (bio != null) bio.hashCode() else 0
            result = 31 * result + if (location != null) location.hashCode() else 0
            result = 31 * result + if (links != null) links.hashCode() else 0
            result = 31 * result + if (profileImage != null) profileImage.hashCode() else 0
            result = 31 * result + if (instagramUsername != null) instagramUsername.hashCode() else 0
            result = 31 * result + totalCollections
            result = 31 * result + totalLikes
            result = 31 * result + totalPhotos
            return result
        }
    }

    class ExifBean {
        /**
         * make : null
         * model : null
         * exposure_time : null
         * aperture : null
         * focal_length : null
         * iso : null
         */
        @SerializedName("make")
        var make: Any? = null

        @SerializedName("model")
        var model: Any? = null

        @SerializedName("exposure_time")
        var exposureTime: Any? = null

        @SerializedName("aperture")
        var aperture: Any? = null

        @SerializedName("focal_length")
        var focalLength: Any? = null

        @SerializedName("iso")
        var iso: Any? = null

        override fun equals(o: Any?): Boolean {
            if (this === o) return true
            if (o == null || javaClass != o.javaClass) return false
            val exifBean = o as ExifBean
            if (if (make != null) make != exifBean.make else exifBean.make != null) return false
            if (if (model != null) model != exifBean.model else exifBean.model != null) return false
            if (if (exposureTime != null) exposureTime != exifBean.exposureTime else exifBean.exposureTime != null) return false
            if (if (aperture != null) aperture != exifBean.aperture else exifBean.aperture != null) return false
            if (if (focalLength != null) focalLength != exifBean.focalLength else exifBean.focalLength != null) return false
            return if (iso != null) iso == exifBean.iso else exifBean.iso == null
        }

        override fun hashCode(): Int {
            var result = if (make != null) make.hashCode() else 0
            result = 31 * result + if (model != null) model.hashCode() else 0
            result = 31 * result + if (exposureTime != null) exposureTime.hashCode() else 0
            result = 31 * result + if (aperture != null) aperture.hashCode() else 0
            result = 31 * result + if (focalLength != null) focalLength.hashCode() else 0
            result = 31 * result + if (iso != null) iso.hashCode() else 0
            return result
        }
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as ResUnsplashPhoto
        if (width != that.width) return false
        if (height != that.height) return false
        if (isSponsored != that.isSponsored) return false
        if (likes != that.likes) return false
        if (isLikedByUser != that.isLikedByUser) return false
        if (views != that.views) return false
        if (downloads != that.downloads) return false
        if (if (id != null) id != that.id else that.id != null) return false
        if (if (createdAt != null) createdAt != that.createdAt else that.createdAt != null) return false
        if (if (updatedAt != null) updatedAt != that.updatedAt else that.updatedAt != null) return false
        if (if (color != null) color != that.color else that.color != null) return false
        if (if (description != null) description != that.description else that.description != null) return false
        if (if (urls != null) urls != that.urls else that.urls != null) return false
        if (if (links != null) links != that.links else that.links != null) return false
        if (if (slug != null) slug != that.slug else that.slug != null) return false
        if (if (user != null) user != that.user else that.user != null) return false
        if (if (exif != null) exif != that.exif else that.exif != null) return false
        if (if (categories != null) categories != that.categories else that.categories != null) return false
        return if (currentUserCollections != null) currentUserCollections == that.currentUserCollections else that.currentUserCollections == null
    }

    override fun hashCode(): Int {
        var result = if (id != null) id.hashCode() else 0
        result = 31 * result + if (createdAt != null) createdAt.hashCode() else 0
        result = 31 * result + if (updatedAt != null) updatedAt.hashCode() else 0
        result = 31 * result + width
        result = 31 * result + height
        result = 31 * result + if (color != null) color.hashCode() else 0
        result = 31 * result + if (description != null) description.hashCode() else 0
        result = 31 * result + if (urls != null) urls.hashCode() else 0
        result = 31 * result + if (links != null) links.hashCode() else 0
        result = 31 * result + if (isSponsored) 1 else 0
        result = 31 * result + likes
        result = 31 * result + if (isLikedByUser) 1 else 0
        result = 31 * result + if (slug != null) slug.hashCode() else 0
        result = 31 * result + if (user != null) user.hashCode() else 0
        result = 31 * result + if (exif != null) exif.hashCode() else 0
        result = 31 * result + views
        result = 31 * result + downloads
        result = 31 * result + if (categories != null) categories.hashCode() else 0
        result = 31 * result + if (currentUserCollections != null) currentUserCollections.hashCode() else 0
        return result
    }
}