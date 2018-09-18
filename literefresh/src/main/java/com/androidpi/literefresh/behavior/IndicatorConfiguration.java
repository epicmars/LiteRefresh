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
package com.androidpi.literefresh.behavior;

import android.content.Context;
import android.support.annotation.DimenRes;
import android.support.annotation.FractionRes;

public class IndicatorConfiguration extends Configuration {

    int visibleHeight;
    float visibleHeightRatio;
    float visibleHeightRatioOfParent;
    int triggerOffset;
    boolean useDefinedTriggerOffset;
    int invisibleHeight;
    Boolean showUpWhenRefresh;
    int initialVisibleHeight;

    public IndicatorConfiguration(Builder builder) {
        super(builder);
        this.visibleHeight = builder.visibleHeight == null ? 0 : builder.visibleHeight;
        this.visibleHeightRatio = builder.visibleHeightRatio == null ? 0f : builder.visibleHeightRatio;
        this.visibleHeightRatioOfParent = builder.visibleHeightRatioOfParent == null ? 0f : builder.visibleHeightRatioOfParent;
        this.triggerOffset = builder.triggerOffset == null
                ? (builder.defaultTriggerOffset == null ? this.defaultTriggerOffset : builder.defaultTriggerOffset)
                : builder.triggerOffset;
        this.useDefinedTriggerOffset = builder.useDefinedTriggerOffset == null ? false : builder.useDefinedTriggerOffset;
        this.invisibleHeight = builder.invisibleHeight == null ? 0 : builder.invisibleHeight;
        this.showUpWhenRefresh = builder.showUpWhenRefresh;
        this.initialVisibleHeight = builder.initialVisibleHeight == null ? 0 : builder.initialVisibleHeight;
    }

    public int getVisibleHeight() {
        return visibleHeight;
    }

    public float getVisibleHeightRatio() {
        return visibleHeightRatio;
    }

    public float getVisibleHeightRatioOfParent() {
        return visibleHeightRatioOfParent;
    }

    public int getTriggerOffset() {
        return triggerOffset;
    }

    public int getInvisibleHeight() {
        return invisibleHeight;
    }

    public int getInitialVisibleHeight() {
        return initialVisibleHeight;
    }

    public int getDefaultTriggerOffset() {
        return defaultTriggerOffset;
    }

    public boolean isUseDefinedTriggerOffset() {
        return useDefinedTriggerOffset;
    }

    public Boolean getShowUpWhenRefresh() {
        return showUpWhenRefresh;
    }

    void setVisibleHeight(int visibleHeight) {
        this.visibleHeight = visibleHeight;
    }

    void setVisibleHeightRatio(float visibleHeightRatio) {
        this.visibleHeightRatio = visibleHeightRatio;
    }

    void setVisibleHeightRatioOfParent(float visibleHeightRatioOfParent) {
        this.visibleHeightRatioOfParent = visibleHeightRatioOfParent;
    }

    void setTriggerOffset(int triggerOffset) {
        this.triggerOffset = triggerOffset;
    }

    void setUseDefinedTriggerOffset(boolean useDefinedTriggerOffset) {
        this.useDefinedTriggerOffset = useDefinedTriggerOffset;
    }

    void setInvisibleHeight(int invisibleHeight) {
        this.invisibleHeight = invisibleHeight;
    }

    void setShowUpWhenRefresh(Boolean showUpWhenRefresh) {
        this.showUpWhenRefresh = showUpWhenRefresh;
    }

    void setInitialVisibleHeight(int initialVisibleHeight) {
        this.initialVisibleHeight = initialVisibleHeight;
    }

    public static class Builder extends Configuration.Builder {

        Float visibleHeightRatio;
        Float visibleHeightRatioOfParent;
        Integer initialVisibleHeight;
        Integer triggerOffset;
        Boolean useDefinedTriggerOffset;
        Integer visibleHeight;
        Boolean showUpWhenRefresh;
        Integer invisibleHeight;

        Builder() {
        }

        Builder(IndicatorConfiguration configuration) {
            super(configuration);
            init(configuration);
        }

        public Builder(Context context, AnimationOffsetBehavior behavior, IndicatorConfiguration configuration) {
            super(context, behavior, configuration);
            init(configuration);
        }

        private void init(IndicatorConfiguration configuration) {
            if (configuration == null)
                return;
            this.visibleHeight = configuration.visibleHeight;
            this.visibleHeightRatio = configuration.visibleHeightRatio;
            this.visibleHeightRatioOfParent = configuration.visibleHeightRatioOfParent;
            this.triggerOffset = configuration.triggerOffset;
            this.useDefinedTriggerOffset = configuration.useDefinedTriggerOffset;
            this.invisibleHeight = configuration.invisibleHeight;
            this.showUpWhenRefresh = configuration.showUpWhenRefresh;
            this.initialVisibleHeight = configuration.initialVisibleHeight;
        }

        public Builder visibleHeight(int visibleHeight) {
            this.visibleHeight = visibleHeight;
            return this;
        }

        public Builder visibleHeightRes(@DimenRes int visibleHeightRes) {
            this.visibleHeight = context.getResources().getDimensionPixelOffset(visibleHeightRes);
            return this;
        }

        public Builder visibleHeightRatio(float visibleHeightRatio) {
            this.visibleHeightRatio = visibleHeightRatio;
            return this;
        }

        public Builder visibleHeightRatioOfParent(float visibleHeightRatioOfParent) {
            this.visibleHeightRatioOfParent = visibleHeightRatioOfParent;
            return this;
        }

        public Builder visibleHeightRatioRes(@FractionRes int visibleHeightRatioRes) {
            this.visibleHeightRatio = context.getResources().getFraction(visibleHeightRatioRes, 1, 1);
            this.visibleHeightRatioOfParent = context.getResources().getFraction(visibleHeightRatioRes, 1, 2);
            return this;
        }

        public Builder triggerOffset(int triggerOffset) {
            this.triggerOffset = triggerOffset;
            this.useDefinedTriggerOffset = true;
            return this;
        }

        public Builder triggerOffsetRes(@DimenRes int triggerOffsetRes) {
            this.triggerOffset = context.getResources().getDimensionPixelOffset(triggerOffsetRes);
            this.useDefinedTriggerOffset = true;
            return this;
        }

        public Builder showUpWhenRefresh(boolean showUpWhenRefresh) {
            this.showUpWhenRefresh = showUpWhenRefresh;
            return this;
        }

        Builder setUseDefinedTriggerOffset(boolean useDefinedTriggerOffset) {
            this.useDefinedTriggerOffset = useDefinedTriggerOffset;
            return this;
        }

        Builder setInvisibleHeight(int invisibleHeight) {
            this.invisibleHeight = invisibleHeight;
            return this;
        }

        @Override
        public Builder maxOffset(int maxOffset) {
            super.maxOffset(maxOffset);
            return this;
        }

        @Override
        public Builder maxOffsetRes(int maxOffsetRes) {
            super.maxOffsetRes(maxOffsetRes);
            return this;
        }

        @Override
        public Builder maxOffsetRatio(float maxOffsetRatio) {
            super.maxOffsetRatio(maxOffsetRatio);
            return this;
        }

        @Override
        public Builder maxOffsetRatioOfParent(float maxOffsetRatioOfParent) {
            super.maxOffsetRatioOfParent(maxOffsetRatioOfParent);
            return this;
        }

        @Override
        public Builder maxOffsetRatioRes(int maxOffsetRatioRes) {
            super.maxOffsetRatioRes(maxOffsetRatioRes);
            return this;
        }

        Builder setInitialVisibleHeight(Integer initialVisibleHeight) {
            this.initialVisibleHeight = initialVisibleHeight;
            return this;
        }

        @Override
        Builder setDefaultTriggerOffset(int defaultTriggerOffset) {
            super.setDefaultTriggerOffset(defaultTriggerOffset);
            return this;
        }

        @Override
        Builder setUseDefaultMaxOffset(Boolean isUseDefaultMaxOffset) {
            super.setUseDefaultMaxOffset(isUseDefaultMaxOffset);
            return this;
        }

        @Override
        Builder setHeight(Integer height) {
            super.setHeight(height);
            return this;
        }

        @Override
        Builder setParentHeight(Integer parentHeight) {
            super.setParentHeight(parentHeight);
            return this;
        }

        @Override
        Builder setTopMargin(Integer topMargin) {
            super.setTopMargin(topMargin);
            return this;
        }

        @Override
        Builder setBottomMargin(Integer bottomMargin) {
            super.setBottomMargin(bottomMargin);
            return this;
        }

        @Override
        Builder setSettled(Boolean settled) {
            super.setSettled(settled);
            return this;
        }

        @Override
        public IndicatorConfiguration build() {
            return new IndicatorConfiguration(this);
        }

        @Override
        public IndicatorConfiguration config() {
            IndicatorConfiguration config = setSettled(false).build();
            behavior.setConfiguration(config);
            return config;
        }
    }
}