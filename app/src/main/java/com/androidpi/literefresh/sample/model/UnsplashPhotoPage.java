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
package com.androidpi.literefresh.sample.model;

import com.androidpi.literefresh.sample.data.remote.dto.ResUnsplashPhoto;

import java.util.List;

public class UnsplashPhotoPage {

    public static final int FIRST_PAGE = 0;

    private final int page;
    private final List<ResUnsplashPhoto> photos;

    public UnsplashPhotoPage(int page, List<ResUnsplashPhoto> photos) {
        this.page = page;
        this.photos = photos;
    }

    public boolean isFirstPage() {
        return page == FIRST_PAGE;
    }

    public List<ResUnsplashPhoto> getPhotos() {
        return photos;
    }
}