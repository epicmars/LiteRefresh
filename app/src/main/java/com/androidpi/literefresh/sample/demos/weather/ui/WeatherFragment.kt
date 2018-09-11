package com.androidpi.literefresh.sample.demos.weather.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.view.View
import com.androidpi.literefresh.core.OnRefreshListener


import com.androidpi.literefresh.core.behavior.RefreshHeaderBehavior
import com.androidpi.literefresh.sample.R
import com.androidpi.literefresh.sample.base.ui.BaseFragment
import com.androidpi.literefresh.sample.base.ui.BindLayout
import com.androidpi.literefresh.sample.databinding.FragmentWeatherBinding
import com.androidpi.literefresh.sample.demos.weather.vm.WeatherViewModel

@BindLayout(value = R.layout.fragment_weather)
class WeatherFragment : BaseFragment<FragmentWeatherBinding>() {

    lateinit var viewModel: WeatherViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java)

        val behavior = (binding.refreshHeader.layoutParams as CoordinatorLayout.LayoutParams).behavior as RefreshHeaderBehavior
        behavior.addOnRefreshListener(object : OnRefreshListener {
            override fun onRefreshEnd(throwable: Throwable?) {
            }

            override fun onReleaseToRefresh() {
            }

            override fun onRefreshStart() {
            }

            override fun onRefresh() {
                viewModel.getCurrentWeather(30.67f, 104.08f)
            }
        })

        viewModel.weatherResult.observe(this, Observer { resCurrentWeatherResource ->
            if (resCurrentWeatherResource == null) return@Observer
            if (resCurrentWeatherResource.isSuccess) {
                behavior.refreshComplete()
                val weatherBean = resCurrentWeatherResource.data!!.weather[0]
                val mainBean = resCurrentWeatherResource.data!!.main
                binding.ivIcon.setImageResource(resources.getIdentifier("ic_" + weatherBean.icon, "drawable", context?.packageName))
                binding.tvDescription.text = weatherBean.description.capitalize()
                binding.tvTemperature.text = getString(R.string.format_temperature, mainBean.temp - 273.15)
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
