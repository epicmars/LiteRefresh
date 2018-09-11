package com.androidpi.literefresh.sample.base.ui

import android.support.v4.app.Fragment

object FragmentFactoryMap {

    val factoryMap = HashMap<String, FragmentFactory<Fragment>>()

    fun put(key: String, value: FragmentFactory<Fragment>) {
        factoryMap.remove(key)
        factoryMap[key] = value
    }
}


abstract class FragmentFactory<out Fragment> {
    abstract fun create(): Fragment
}