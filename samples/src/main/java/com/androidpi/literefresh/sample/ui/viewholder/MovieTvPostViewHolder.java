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
package com.androidpi.literefresh.sample.ui.viewholder;

import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.androidpi.literefresh.sample.R;
import com.androidpi.literefresh.sample.base.ui.BaseViewHolder;
import com.androidpi.literefresh.sample.base.ui.BindLayout;
import com.androidpi.literefresh.sample.base.ui.FragmentFactory;
import com.androidpi.literefresh.sample.common.image.GlideApp;
import com.androidpi.literefresh.sample.data.remote.TheMovieDbApi;
import com.androidpi.literefresh.sample.data.remote.dto.ResMoviePage;
import com.androidpi.literefresh.sample.data.remote.dto.ResTrendingPage;
import com.androidpi.literefresh.sample.data.remote.dto.ResTvPage;
import com.androidpi.literefresh.sample.databinding.ViewHolderMovieTvPostBinding;
import com.androidpi.literefresh.sample.ui.TemplateActivity;
import com.androidpi.literefresh.sample.ui.fragment.MovieTvDetailFragment;

@BindLayout(value = R.layout.view_holder_movie_tv_post, dataTypes = {
        ResTrendingPage.ResultsBean.class,
        ResTvPage.ResultsBean.class,
        ResMoviePage.ResultsBean.class
})
public class MovieTvPostViewHolder extends BaseViewHolder<ViewHolderMovieTvPostBinding> {

    public MovieTvPostViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public <T> void onBind(T data, int position) {
        String postTitle = null;
        String postUrl = null;

        if (data instanceof ResMoviePage.ResultsBean) {
            ResMoviePage.ResultsBean result = ((ResMoviePage.ResultsBean) data);
            postTitle = result.getTitle();
            postUrl = result.getPosterPath();
        }

        if (data instanceof ResTvPage.ResultsBean) {
            ResTvPage.ResultsBean result = ((ResTvPage.ResultsBean) data);
            postTitle = result.getName();
            postUrl = result.getPosterPath();
        }

        if (data instanceof ResTrendingPage.ResultsBean) {
            ResTrendingPage.ResultsBean result = ((ResTrendingPage.ResultsBean) data);
            postTitle = TextUtils.isEmpty(result.getTitle()) ? result.getName() : result.getTitle();
            postUrl = result.getPosterPath();
        }
        binding.tvNameOrTitle.setText(postTitle);
        GlideApp.with(itemView)
                .load(TheMovieDbApi.Companion.getIMAGE_BASE_URL() +
                        TheMovieDbApi.Companion.getIMAGE_SIZE() +
                        postUrl)
                .into(binding.ivPost);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TemplateActivity.Companion.startWith(itemView.getContext(), 0, MovieTvDetailFragment.class.getName(), new FragmentFactory<Fragment>() {
                    @Override
                    public Fragment create() {
                        return MovieTvDetailFragment.newInstance((Parcelable) data);
                    }
                });
            }
        });
    }
}
