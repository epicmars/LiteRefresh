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
package com.androidpi.literefresh.sample

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import com.androidpi.literefresh.sample.base.ui.BaseActivity
import com.androidpi.literefresh.sample.base.ui.BindLayout
import com.androidpi.literefresh.sample.databinding.ActivityMainBinding
import com.androidpi.literefresh.sample.ui.fragment.LiteRefreshPagerFragment
import com.androidpi.literefresh.sample.ui.fragment.LiteRefreshSamplesFragment
import com.androidpi.literefresh.sample.ui.fragment.TempFragment

@BindLayout(R.layout.activity_main)
class MainActivity : BaseActivity<ActivityMainBinding>() {

    companion object {

        val NAV_IDS = mapOf(R.id.nav_literefresh_pager to 0,
                R.id.nav_literefresh_list to 1)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.pager.offscreenPageLimit = 2
        binding.pager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {

            override fun getItem(position: Int): Fragment {
                return when (position) {
                    0 -> LiteRefreshPagerFragment.newInstance()
                    1 -> LiteRefreshSamplesFragment.newInstance()
                    else -> TempFragment.newInstance("An error occurred.")
                }
            }

            override fun getCount(): Int {
                return NAV_IDS.size
            }
        }

        binding.pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                binding.bottomNavigation.menu.getItem(position).isChecked = true
            }
        })

        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            val itemId = item.itemId
            val current : Int? =  NAV_IDS[itemId]
            if (current == null) {
                false
            } else {
                binding.pager.currentItem = current
                true
            }
        }
    }
}
