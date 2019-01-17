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
package com.androidpi.literefresh.sample.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.androidpi.literefresh.sample.R;
import com.androidpi.literefresh.sample.base.ui.BaseFragment;
import com.androidpi.literefresh.sample.base.ui.BindLayout;
import com.androidpi.literefresh.sample.base.ui.RecyclerAdapter;
import com.androidpi.literefresh.sample.databinding.FragmentUnsplashPhotoGridBinding;
import com.androidpi.literefresh.sample.model.ErrorItem;
import com.androidpi.literefresh.sample.ui.viewholder.ErrorViewHolder;
import com.androidpi.literefresh.sample.ui.viewholder.UnsplashPhotoGridViewHolder;

import java.util.Collection;

@BindLayout(R.layout.fragment_unsplash_photo_grid)
public class UnsplashPhotoGridFragment extends BaseFragment<FragmentUnsplashPhotoGridBinding> {

    RecyclerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new RecyclerAdapter();
        adapter.register(UnsplashPhotoGridViewHolder.class, ErrorViewHolder.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(adapter);
    }

    public void setPayloads(Collection<?> payloads) {
        adapter.setPayloads(payloads);
    }

    public void addPayloads(Collection<?> payloads) {
        adapter.addPayloads(payloads);
    }

    public void refreshError(Throwable throwable) {
        adapter.setPayloads(new ErrorItem(throwable.getMessage()));
    }
}
