package com.androidpi.literefresh.sample.common.log

import timber.log.Timber

object LogHelper {

    fun init() {
        Timber.plant(Timber.DebugTree())
    }
}
