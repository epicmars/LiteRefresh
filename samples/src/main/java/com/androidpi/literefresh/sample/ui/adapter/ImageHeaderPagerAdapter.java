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
package com.androidpi.literefresh.sample.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.androidpi.literefresh.sample.data.remote.dto.ResTrendingPage;
import com.androidpi.literefresh.sample.data.remote.dto.ResUnsplashPhoto;
import com.androidpi.literefresh.sample.ui.fragment.ImageHeaderFragment;
import com.androidpi.literefresh.sample.ui.fragment.TempFragment;
import com.androidpi.literefresh.sample.ui.fragment.TrendingImageHeaderFragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ImageHeaderPagerAdapter extends FragmentStatePagerAdapter {

        private List<Object> mPhotos = new ArrayList<>();

        public ImageHeaderPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Object photo = mPhotos.get(position);
            if (photo instanceof ResUnsplashPhoto) {
                return ImageHeaderFragment.newInstance(((ResUnsplashPhoto) photo).getUrls().getSmall());
            } else if (photo instanceof ResTrendingPage.ResultsBean) {
                return TrendingImageHeaderFragment.newInstance(((ResTrendingPage.ResultsBean) photo).getPosterPath());
            } else {
                return TempFragment.Companion.newInstance("");
            }
        }

        @Override
        public int getCount() {
            return mPhotos.size();
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        public void setPhotos(Collection<?> photos) {
            if (photos == null || photos.isEmpty())
                return;
            mPhotos.clear();
            mPhotos.addAll(photos);
            notifyDataSetChanged();
        }
    }