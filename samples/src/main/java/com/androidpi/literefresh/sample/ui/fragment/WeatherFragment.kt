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
package com.androidpi.literefresh.sample.ui.fragment


import android.os.Bundle
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.androidpi.literefresh.OnRefreshListener
import com.androidpi.literefresh.behavior.RefreshHeaderBehavior
import com.androidpi.literefresh.sample.R
import com.androidpi.literefresh.sample.base.ui.BaseFragment
import com.androidpi.literefresh.sample.databinding.FragmentWeatherBinding
import com.androidpi.literefresh.sample.vm.WeatherViewModel
import layoutbinder.annotations.BindLayout

class WeatherFragment : BaseFragment() {

    @BindLayout(R.layout.fragment_weather)
    lateinit var binding : FragmentWeatherBinding
    val viewModel: WeatherViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val behavior = (binding.refreshHeader.layoutParams as CoordinatorLayout.LayoutParams).behavior as RefreshHeaderBehavior
        behavior.addOnRefreshListener(object : OnRefreshListener {
            override fun onRefreshEnd(throwable: Throwable?) {
            }

            override fun onReleaseToRefresh() {
            }

            override fun onRefreshStart() {
            }

            override fun onRefresh() {
                viewModel.getCurrentWeather(21.34f, -158.12f)
            }
        })

        viewModel.weatherResult.observe(viewLifecycleOwner, Observer { resCurrentWeatherResource ->
            if (resCurrentWeatherResource == null) return@Observer
            if (resCurrentWeatherResource.isSuccess) {
                behavior.refreshComplete()
                val weatherBean = resCurrentWeatherResource.data?.weather?.get(0)
                val mainBean = resCurrentWeatherResource.data?.main
                binding.ivIcon.setImageResource(resources.getIdentifier("ic_" + weatherBean?.icon, "drawable", context?.packageName))
                binding.tvDescription.text = weatherBean?.description?.capitalize()
                binding.tvTemperature.text = getString(R.string.format_temperature, mainBean?.temp?.minus(273.15))
            } else if (resCurrentWeatherResource.isError) {
                behavior.refreshError(resCurrentWeatherResource.throwable)
            }
        })

        behavior.refresh()
    }

    companion object {

        fun newInstance(): WeatherFragment {

            val args = Bundle()

            val fragment = WeatherFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
