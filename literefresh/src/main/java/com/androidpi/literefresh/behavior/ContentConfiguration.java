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

public class ContentConfiguration extends Configuration {

    int minOffset;
    float minOffsetRatio;
    float minOffsetRatioOfParent;
    int cachedMinOffset;
    boolean useDefaultMinOffset;

    public ContentConfiguration(Builder builder) {
        super(builder);
        this.minOffset = builder.minOffset == null ? 0 : builder.minOffset;
        this.minOffsetRatio = builder.minOffsetRatio == null ? 0 : builder.minOffsetRatio;
        this.minOffsetRatioOfParent = builder.minOffsetRatioOfParent == null ? 0 : builder.minOffsetRatioOfParent;
        this.cachedMinOffset = builder.cachedMinOffset == null ? 0 : builder.cachedMinOffset;
        this.useDefaultMinOffset = builder.useDefaultMinOffset == null ? false : builder.useDefaultMinOffset;
    }

    public int getMinOffset() {
        return minOffset;
    }

    public float getMinOffsetRatio() {
        return minOffsetRatio;
    }

    public float getMinOffsetRatioOfParent() {
        return minOffsetRatioOfParent;
    }

    public int getCachedMinOffset() {
        return cachedMinOffset;
    }

    public boolean isUseDefaultMinOffset() {
        return useDefaultMinOffset;
    }

    void setMinOffset(int minOffset) {
        this.minOffset = minOffset;
    }

    void setMinOffsetRatio(float minOffsetRatio) {
        this.minOffsetRatio = minOffsetRatio;
    }

    void setMinOffsetRatioOfParent(float minOffsetRatioOfParent) {
        this.minOffsetRatioOfParent = minOffsetRatioOfParent;
    }

    void setCachedMinOffset(int cachedMinOffset) {
        this.cachedMinOffset = cachedMinOffset;
    }

    void setUseDefaultMinOffset(boolean useDefaultMinOffset) {
        this.useDefaultMinOffset = useDefaultMinOffset;
    }

    public static class Builder extends Configuration.Builder {

        Integer minOffset;
        Float minOffsetRatio;
        Float minOffsetRatioOfParent;
        Integer cachedMinOffset;
        Boolean useDefaultMinOffset;

        Builder() {

        }

        Builder(ContentConfiguration configuration) {
            super(configuration);
            init(configuration);
        }

        public Builder(Context context, AnimationOffsetBehavior behavior, ContentConfiguration configuration) {
            super(context, behavior, configuration);
            init(configuration);
        }

        private void init(ContentConfiguration configuration) {
            if (configuration == null)
                return;
            this.minOffset = configuration.getMinOffset();
            this.minOffsetRatio = configuration.getMinOffsetRatio();
            this.minOffsetRatioOfParent = configuration.getMinOffsetRatioOfParent();
            this.cachedMinOffset = configuration.getCachedMinOffset();
            this.useDefaultMinOffset = configuration.isUseDefaultMinOffset();
        }

        public Builder minOffset(int minOffset) {
            this.minOffset = minOffset;
            this.useDefaultMinOffset = false;
            return this;
        }

        public Builder minOffsetRes(@DimenRes int minOffsetRes) {
            this.minOffset = context.getResources().getDimensionPixelOffset(minOffsetRes);
            this.useDefaultMinOffset = false;
            return this;
        }

        public Builder minOffsetRatio(Float minOffsetRatio) {
            this.minOffsetRatio = minOffsetRatio;
            this.useDefaultMinOffset = false;
            return this;
        }

        public Builder minOffsetRatioOfParent(Float minOffsetRatioOfParent) {
            this.minOffsetRatioOfParent = minOffsetRatioOfParent;
            this.useDefaultMinOffset = false;
            return this;
        }

        public Builder minOffsetRatioRes(@FractionRes int minOffsetRatioRes) {
            this.minOffsetRatio = context.getResources().getFraction(minOffsetRatioRes, 1, 1);
            this.minOffsetRatioOfParent = context.getResources().getFraction(minOffsetRatioRes, 1, 2);
            this.useDefaultMinOffset = false;
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

        Builder setCachedMinOffset(Integer cachedMinOffset) {
            this.cachedMinOffset = cachedMinOffset;
            return this;
        }

        Builder setUseDefaultMinOffset(Boolean useDefaultMinOffset) {
            this.useDefaultMinOffset = useDefaultMinOffset;
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
        public ContentConfiguration build() {
            return new ContentConfiguration(this);
        }

        @Override
        public ContentConfiguration config() {
            ContentConfiguration config = setSettled(false).build();
            behavior.setConfiguration(config);
            return config;
        }
    }
}