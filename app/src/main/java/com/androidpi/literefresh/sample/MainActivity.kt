package com.androidpi.literefresh.sample

import android.os.Bundle
import com.androidpi.literefresh.sample.base.ui.BaseActivity
import com.androidpi.literefresh.sample.base.ui.BindLayout
import com.androidpi.literefresh.sample.databinding.ActivityMainBinding
import com.androidpi.literefresh.sample.demos.weather.ui.WeatherFragment

@BindLayout(R.layout.activity_main)
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            var ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.main_content, WeatherFragment.newInstance())
            ft.commit()
        }
    }
}
