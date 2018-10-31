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

/** Common configurations of content and indicator behavior. */
abstract class Configuration {
    int defaultTriggerOffset;
    int maxOffset;
    float maxOffsetRatio;
    float maxOffsetRatioOfParent;
    boolean isUseDefaultMaxOffset;
    int height;
    int parentHeight;
    int topMargin;
    int bottomMargin;
    boolean isSettled;

    public Configuration(Builder builder) {
        this.defaultTriggerOffset =
                builder.defaultTriggerOffset == null ? 196 : builder.defaultTriggerOffset;
        this.maxOffset = builder.maxOffset == null ? 0 : builder.maxOffset;
        this.maxOffsetRatio = builder.maxOffsetRatio == null ? 0f : builder.maxOffsetRatio;
        this.maxOffsetRatioOfParent =
                builder.maxOffsetRatioOfParent == null ? 0f : builder.maxOffsetRatioOfParent;
        this.isUseDefaultMaxOffset =
                builder.isUseDefaultMaxOffset == null ? false : builder.isUseDefaultMaxOffset;
        this.height = builder.height == null ? 0 : builder.height;
        this.parentHeight = builder.parentHeight == null ? 0 : builder.parentHeight;
        this.topMargin = builder.topMargin == null ? 0 : builder.topMargin;
        this.bottomMargin = builder.bottomMargin == null ? 0 : builder.bottomMargin;
        this.isSettled = builder.isSettled == null ? true : builder.isSettled;
    }

    public int getMaxOffset() {
        return maxOffset;
    }

    public float getMaxOffsetRatio() {
        return maxOffsetRatio;
    }

    public float getMaxOffsetRatioOfParent() {
        return maxOffsetRatioOfParent;
    }

    public boolean isUseDefaultMaxOffset() {
        return isUseDefaultMaxOffset;
    }

    public int getHeight() {
        return height;
    }

    public int getParentHeight() {
        return parentHeight;
    }

    public int getTopMargin() {
        return topMargin;
    }

    public int getBottomMargin() {
        return bottomMargin;
    }

    public boolean isSettled() {
        return isSettled;
    }

    public int getDefaultTriggerOffset() {
        return defaultTriggerOffset;
    }

    void setDefaultTriggerOffset(int defaultTriggerOffset) {
        this.defaultTriggerOffset = defaultTriggerOffset;
    }

    void setMaxOffset(int maxOffset) {
        this.maxOffset = maxOffset;
    }

    void setMaxOffsetRatio(float maxOffsetRatio) {
        this.maxOffsetRatio = maxOffsetRatio;
    }

    void setMaxOffsetRatioOfParent(float maxOffsetRatioOfParent) {
        this.maxOffsetRatioOfParent = maxOffsetRatioOfParent;
    }

    void setUseDefaultMaxOffset(boolean useDefaultMaxOffset) {
        isUseDefaultMaxOffset = useDefaultMaxOffset;
    }

    void setHeight(int height) {
        this.height = height;
    }

    void setParentHeight(int parentHeight) {
        this.parentHeight = parentHeight;
    }

    void setTopMargin(int topMargin) {
        this.topMargin = topMargin;
    }

    void setBottomMargin(int bottomMargin) {
        this.bottomMargin = bottomMargin;
    }

    void setSettled(boolean settled) {
        isSettled = settled;
    }

    abstract static class Builder {
        Context context;
        AnimationOffsetBehavior behavior;
        Integer defaultTriggerOffset;
        Integer maxOffset;
        Float maxOffsetRatio;
        Float maxOffsetRatioOfParent;
        Boolean isUseDefaultMaxOffset;
        Integer height;
        Integer parentHeight;
        Integer topMargin;
        Integer bottomMargin;
        Boolean isSettled;

        Builder() {}

        Builder(Context context, AnimationOffsetBehavior behavior, Configuration configuration) {
            this(configuration);
            this.context = context;
            this.behavior = behavior;
        }

        public Builder(Configuration configuration) {
            if (configuration == null) return;
            this.defaultTriggerOffset = configuration.defaultTriggerOffset;
            this.maxOffset = configuration.getMaxOffset();
            this.maxOffsetRatio = configuration.getMaxOffsetRatio();
            this.maxOffsetRatioOfParent = configuration.getMaxOffsetRatioOfParent();
            this.isUseDefaultMaxOffset = configuration.isUseDefaultMaxOffset();
            this.height = configuration.getHeight();
            this.parentHeight = configuration.getParentHeight();
            this.topMargin = configuration.getTopMargin();
            this.bottomMargin = configuration.getBottomMargin();
            this.isSettled = configuration.isSettled();
        }

        public Builder maxOffset(int maxOffset) {
            this.maxOffset = maxOffset;
            this.isUseDefaultMaxOffset = false;
            return this;
        }

        public Builder maxOffsetRes(@DimenRes int maxOffsetRes) {
            this.maxOffset = context.getResources().getDimensionPixelOffset(maxOffsetRes);
            this.isUseDefaultMaxOffset = false;
            return this;
        }

        public Builder maxOffsetRatio(float maxOffsetRatio) {
            this.maxOffsetRatio = maxOffsetRatio;
            this.isUseDefaultMaxOffset = false;
            return this;
        }

        public Builder maxOffsetRatioOfParent(float maxOffsetRatioOfParent) {
            this.maxOffsetRatioOfParent = maxOffsetRatioOfParent;
            this.isUseDefaultMaxOffset = false;
            return this;
        }

        public Builder maxOffsetRatioRes(@FractionRes int maxOffsetRatioRes) {
            this.maxOffsetRatio = context.getResources().getFraction(maxOffsetRatioRes, 1, 1);
            this.maxOffsetRatioOfParent =
                    context.getResources().getFraction(maxOffsetRatioRes, 1, 2);
            this.isUseDefaultMaxOffset = false;
            return this;
        }

        Builder setDefaultTriggerOffset(int defaultTriggerOffset) {
            this.defaultTriggerOffset = defaultTriggerOffset;
            return this;
        }

        Builder setUseDefaultMaxOffset(Boolean isUseDefaultMaxOffset) {
            this.isUseDefaultMaxOffset = isUseDefaultMaxOffset;
            return this;
        }

        Builder setHeight(Integer height) {
            this.height = height;
            return this;
        }

        Builder setParentHeight(Integer parentHeight) {
            this.parentHeight = parentHeight;
            return this;
        }

        Builder setTopMargin(Integer topMargin) {
            this.topMargin = topMargin;
            return this;
        }

        Builder setBottomMargin(Integer bottomMargin) {
            this.bottomMargin = bottomMargin;
            return this;
        }

        Builder setSettled(Boolean settled) {
            isSettled = settled;
            return this;
        }

        public abstract Configuration build();

        public abstract <B extends AnimationOffsetBehavior> B config();
    }
}
