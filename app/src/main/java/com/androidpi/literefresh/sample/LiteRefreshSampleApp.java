package com.androidpi.literefresh.sample;

import android.app.Application;

import com.androidpi.literefresh.sample.common.log.LogHelper;

public class LiteRefreshSampleApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        LogHelper.INSTANCE.init();
    }
}
