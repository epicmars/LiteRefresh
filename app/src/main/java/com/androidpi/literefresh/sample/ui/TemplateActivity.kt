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
package com.androidpi.literefresh.sample.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.view.View
import android.view.WindowManager
import com.androidpi.literefresh.sample.R
import com.androidpi.literefresh.sample.base.ui.BaseActivity
import com.androidpi.literefresh.sample.base.ui.BindLayout
import com.androidpi.literefresh.sample.base.ui.FragmentFactory
import com.androidpi.literefresh.sample.base.ui.FragmentFactoryMap
import com.androidpi.literefresh.sample.databinding.ActivityTemplateBinding
import timber.log.Timber

/**
 * Activity that doesn't have a special launch mode can use this template and
 * fill the content with a fragment.
 */
@BindLayout(R.layout.activity_template)
class TemplateActivity : BaseActivity<ActivityTemplateBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            supportPostponeEnterTransition()
        }
        val fragmentName = intent.getStringExtra(EXTRA_FRAGMENT_NAME)
        if (fragmentName == null) return
        val factory = FragmentFactoryMap.factoryMap[fragmentName]
        try {
            val ft = supportFragmentManager.beginTransaction()
            val fragment = factory?.create()
            ft.replace(R.id.content, fragment)
            ft.commitAllowingStateLoss()
        } catch (e: Exception) {
            Timber.e(e)
        }
        FragmentFactoryMap.factoryMap.remove(fragmentName)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            supportStartPostponedEnterTransition()
        }

        val view = window.decorView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }

        // Status bar in light theme, the text and icon should be dark.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (R.style.AppTheme_NoActionBar_Light == getThemeRes()) {
                view.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
            }
        }
    }

    companion object {
        val EXTRA_FRAGMENT_NAME = "TemplateActivity.EXTRA_FRAGMENT_NAME"

        fun startWith(optionsCompat: ActivityOptionsCompat, context: Context, flags: Int = 0,
                      fragmentName: String, factory: FragmentFactory<Fragment>) {
            FragmentFactoryMap.put(fragmentName, factory)
            val intent = Intent(context, TemplateActivity::class.java)
            intent.putExtra(EXTRA_FRAGMENT_NAME, fragmentName)
            intent.flags = flags
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                context.startActivity(intent, optionsCompat.toBundle())
            } else {
                context.startActivity(intent)
            }
        }

        fun startWith(context: Context, flags: Int = 0, fragmentName: String,
                      factory: FragmentFactory<Fragment>) {
            FragmentFactoryMap.put(fragmentName, factory)
            val intent = Intent(context, TemplateActivity::class.java)
            intent.putExtra(EXTRA_FRAGMENT_NAME, fragmentName)
            intent.flags = flags
            context.startActivity(intent)
        }
    }
}

