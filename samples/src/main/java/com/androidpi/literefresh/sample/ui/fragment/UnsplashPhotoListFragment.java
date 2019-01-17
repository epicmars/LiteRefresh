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
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.androidpi.literefresh.sample.R;
import com.androidpi.literefresh.sample.base.ui.BaseFragment;
import com.androidpi.literefresh.sample.base.ui.BindLayout;
import com.androidpi.literefresh.sample.base.ui.RecyclerAdapter;
import com.androidpi.literefresh.sample.databinding.FragmentUnsplashPhotoListBinding;
import com.androidpi.literefresh.sample.model.ErrorItem;
import com.androidpi.literefresh.sample.ui.viewholder.ErrorViewHolder;
import com.androidpi.literefresh.sample.ui.viewholder.UnsplashPhotoListViewHolder;

import java.util.Collection;

@BindLayout(R.layout.fragment_unsplash_photo_list)
public class UnsplashPhotoListFragment extends BaseFragment<FragmentUnsplashPhotoListBinding> {

    private RecyclerAdapter adapter;

    public static UnsplashPhotoListFragment newInstance() {

        Bundle args = new Bundle();

        UnsplashPhotoListFragment fragment = new UnsplashPhotoListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new RecyclerAdapter();
        adapter.register(UnsplashPhotoListViewHolder.class, ErrorViewHolder.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    public void refreshError(Throwable throwable) {
        adapter.setPayloads(new ErrorItem(throwable.getMessage()));
    }

    public void setPayloads(Collection<?> payloads) {
        adapter.setPayloads(payloads);
    }

    public void addPayloads(Collection<?> payloads) {
        adapter.addPayloads(payloads);
    }
}
