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
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.androidpi.literefresh.sample.R;


public class CircularLoadingView extends AppCompatImageView {

    private CircularProgressDrawable drawable;
    private boolean isStyleReset = true;

    public CircularLoadingView(Context context) {
        this(context, null);
    }

    public CircularLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularLoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircularLoadingView, 0, 0);
        int color = a.getColor(R.styleable.CircularLoadingView_lr_circle_color, Color.WHITE);
        a.recycle();
        drawable = new CircularProgressDrawable(getContext());
        drawable.setStyle(CircularProgressDrawable.DEFAULT);
        drawable.setColorSchemeColors(color);
        setImageDrawable(drawable);
    }

    public void setProgress(float progress) {
        drawable.setStartEndTrim(progress, 0f);
    }

    public void resetStyle() {
        drawable.setStyle(CircularProgressDrawable.DEFAULT);
        isStyleReset = true;
    }

    public void fillCircle() {
        setProgress(1f);
        drawable.setStrokeWidth(drawable.getStrokeWidth() + drawable.getCenterRadius());
        drawable.setCenterRadius(0.1f);
        isStyleReset = false;
    }

    public void startLoading() {
        if (!isStyleReset) {
            resetStyle();
        }
        drawable.start();
    }

    public void stopLoading() {
        drawable.stop();
        if (!isStyleReset) {
            resetStyle();
        }
        setProgress(1f);
    }

    public void setColor(int color) {
        drawable.setColorSchemeColors(color);
    }

}
