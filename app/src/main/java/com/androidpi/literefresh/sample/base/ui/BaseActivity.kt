package com.androidpi.literefresh.sample.base.ui

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import com.androidpi.literefresh.sample.R

abstract class BaseActivity<VDB : ViewDataBinding> : AppCompatActivity(){

    lateinit var binding: VDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bindLayout = javaClass.getAnnotation(BindLayout::class.java)
        if (bindLayout != null) {
            binding = DataBindingUtil.setContentView(this, bindLayout.value)
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

        // x
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (R.style.AppTheme_NoActionBar_Light == getThemeRes()) {
                view.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
            }
        }
    }

    protected fun getContext(): Activity {
        return this
    }

    protected fun getThemeRes(): Int {
        try {
            return packageManager.getActivityInfo(componentName, 0).themeResource
        } catch (e: PackageManager.NameNotFoundException) {
            return applicationInfo.theme
        }

    }

    protected fun <T : ViewModel> getViewModel(viewModelClass: Class<T>): T {
        return ViewModelProviders.of(this).get(viewModelClass)
    }
}
