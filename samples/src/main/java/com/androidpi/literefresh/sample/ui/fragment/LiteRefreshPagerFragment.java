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
package com.androidpi.literefresh.sample.ui.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.androidpi.literefresh.sample.R;
import com.androidpi.literefresh.sample.base.ui.BaseFragment;
import com.androidpi.literefresh.sample.base.ui.BindLayout;
import com.androidpi.literefresh.sample.databinding.FragmentLiteRefreshPagerBinding;
import com.androidpi.literefresh.sample.model.LiteRefreshSamples;


@BindLayout(R.layout.fragment_lite_refresh_pager)
public class LiteRefreshPagerFragment extends BaseFragment<FragmentLiteRefreshPagerBinding> {

    public static LiteRefreshPagerFragment newInstance() {

        Bundle args = new Bundle();

        LiteRefreshPagerFragment fragment = new LiteRefreshPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.viewPager.setAdapter(new LiteRefreshSamplePagerAdapter(getChildFragmentManager()));
    }

    private class LiteRefreshSamplePagerAdapter extends FragmentStatePagerAdapter {

        public LiteRefreshSamplePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return Fragment.instantiate(getContext(), LiteRefreshSamples.INSTANCE.getSamples().get(position).getFragmentClass().getName());
        }

        @Override
        public int getCount() {
            return LiteRefreshSamples.INSTANCE.getSamples().size();
        }
    }
}
