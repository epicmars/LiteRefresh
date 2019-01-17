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

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.androidpi.literefresh.sample.R;
import com.androidpi.literefresh.sample.ui.TemplateActivity;

public class SampleToolbar extends AppBarLayout {

    AppBarLayout appBar;
    Toolbar toolbar;

    public SampleToolbar(Context context) {
        this(context, null);
    }

    public SampleToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.sample_toolbar, this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SampleToolbar, 0, 0);
        int bgColor = a.getColor(R.styleable.SampleToolbar_toolbar_background, getResources().getColor(R.color.transparent));
        String title = a.getString(R.styleable.SampleToolbar_toolbar_title);
        a.recycle();

        appBar = findViewById(R.id.app_bar);
        toolbar = findViewById(R.id.toolbar);
        setBackgroundColor(bgColor);
        toolbar.setTitle(title);

        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity(context);
                if (activity instanceof TemplateActivity) {
                    activity.finish();
                } else {
                    Toast.makeText(activity, "Navigation button clicked.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Activity getActivity(Context context) {
        if (context == null)
            return null;
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return ((Activity) context);
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }
}
