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
package com.androidpi.literefresh.sample.ui.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.androidpi.literefresh.sample.R;
import com.androidpi.literefresh.sample.base.ui.RecyclerAdapter;
import com.androidpi.literefresh.sample.model.SampleUnsplashPhoto;
import com.androidpi.literefresh.sample.ui.viewholder.SampleUnsplashPhotoViewHolder;

import java.util.ArrayList;
import java.util.List;

public class SampleContentView extends FrameLayout {

    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private static final List<SampleUnsplashPhoto> photos = new ArrayList<SampleUnsplashPhoto>(){
        {
            add(new SampleUnsplashPhoto("Jason Ortego", R.mipmap.photo1));
            add(new SampleUnsplashPhoto("Tom Pavlakos", R.mipmap.photo2));
            add(new SampleUnsplashPhoto("Tom Grimbert", R.mipmap.photo6));
            add(new SampleUnsplashPhoto("neida zarate", R.mipmap.photo7));
            add(new SampleUnsplashPhoto("Jason Ortego", R.mipmap.photo1));
            add(new SampleUnsplashPhoto("Tom Pavlakos", R.mipmap.photo2));
            add(new SampleUnsplashPhoto("Tom Grimbert", R.mipmap.photo6));
            add(new SampleUnsplashPhoto("neida zarate", R.mipmap.photo7));
            add(new SampleUnsplashPhoto("Jason Ortego", R.mipmap.photo1));
            add(new SampleUnsplashPhoto("Tom Pavlakos", R.mipmap.photo2));
            add(new SampleUnsplashPhoto("Tom Grimbert", R.mipmap.photo6));
            add(new SampleUnsplashPhoto("neida zarate", R.mipmap.photo7));
        }
    };

    public SampleContentView(Context context) {
        this(context, null);
    }

    public SampleContentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SampleContentView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.view_sample_content, this);
        recyclerAdapter = new RecyclerAdapter();
        recyclerAdapter.register(SampleUnsplashPhotoViewHolder.class);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (recyclerAdapter.getPayloads().isEmpty()) {
            recyclerAdapter.setPayloads(photos);
        }
    }
}
