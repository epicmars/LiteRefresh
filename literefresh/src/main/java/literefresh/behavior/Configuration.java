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
package literefresh.behavior;

import android.content.Context;

import androidx.annotation.DimenRes;
import androidx.annotation.FractionRes;

/** Common configurations of content and indicator behavior. */
abstract class Configuration {
    int defaultTriggerOffset;
    int triggerOffset;
    Boolean useDefinedTriggerOffset;

    int maxOffset;
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
        this.maxOffsetRatioOfParent =
                builder.maxOffsetRatioOfParent == null ? 0f : builder.maxOffsetRatioOfParent;
        this.isUseDefaultMaxOffset =
                builder.isUseDefaultMaxOffset == null ? false : builder.isUseDefaultMaxOffset;
        this.height = builder.height == null ? 0 : builder.height;
        this.parentHeight = builder.parentHeight == null ? 0 : builder.parentHeight;
        this.topMargin = builder.topMargin == null ? 0 : builder.topMargin;
        this.bottomMargin = builder.bottomMargin == null ? 0 : builder.bottomMargin;
        this.isSettled = builder.isSettled == null ? true : builder.isSettled;
        this.triggerOffset = builder.triggerOffset == null
                ? (builder.defaultTriggerOffset == null ? this.defaultTriggerOffset : builder.defaultTriggerOffset)
                : builder.triggerOffset;
        this.useDefinedTriggerOffset = builder.useDefinedTriggerOffset == null ? false : builder.useDefinedTriggerOffset;
    }

    public int getMaxOffset() {
        return maxOffset;
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

    public int getTriggerOffset() {
        return triggerOffset;
    }

    public boolean isUseDefinedTriggerOffset() {
        return useDefinedTriggerOffset;
    }

    void setDefaultTriggerOffset(int defaultTriggerOffset) {
        this.defaultTriggerOffset = defaultTriggerOffset;
    }

    void setMaxOffset(int maxOffset) {
        this.maxOffset = maxOffset;
    }

    void setMaxOffsetRatioOfParent(float maxOffsetRatioOfParent) {
        this.maxOffsetRatioOfParent = maxOffsetRatioOfParent;
    }

    void setUseDefaultMaxOffset(boolean useDefaultMaxOffset) {
        isUseDefaultMaxOffset = useDefaultMaxOffset;
    }

    void setTriggerOffset(int triggerOffset) {
        this.triggerOffset = triggerOffset;
    }

    void setUseDefinedTriggerOffset(boolean useDefinedTriggerOffset) {
        this.useDefinedTriggerOffset = useDefinedTriggerOffset;
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
        Integer triggerOffset;
        Boolean useDefinedTriggerOffset;

        Builder() {}

        Builder(Context context, AnimationOffsetBehavior behavior, Configuration configuration) {
            this(configuration);
            this.context = context;
            this.behavior = behavior;
        }

        public Builder(Configuration config) {
            if (config == null) return;
            this.defaultTriggerOffset = config.defaultTriggerOffset;
            this.maxOffset = config.getMaxOffset();
            this.maxOffsetRatioOfParent = config.getMaxOffsetRatioOfParent();
            this.isUseDefaultMaxOffset = config.isUseDefaultMaxOffset();
            this.height = config.getHeight();
            this.parentHeight = config.getParentHeight();
            this.topMargin = config.getTopMargin();
            this.bottomMargin = config.getBottomMargin();
            this.isSettled = config.isSettled();
            this.triggerOffset = config.triggerOffset;
            this.useDefinedTriggerOffset = config.useDefinedTriggerOffset;
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


        public Configuration.Builder triggerOffset(int triggerOffset) {
            this.triggerOffset = triggerOffset;
            this.useDefinedTriggerOffset = true;
            return this;
        }

        public Configuration.Builder triggerOffsetRes(@DimenRes int triggerOffsetRes) {
            this.triggerOffset = context.getResources().getDimensionPixelOffset(triggerOffsetRes);
            this.useDefinedTriggerOffset = true;
            return this;
        }

        Configuration.Builder setUseDefinedTriggerOffset(boolean useDefinedTriggerOffset) {
            this.useDefinedTriggerOffset = useDefinedTriggerOffset;
            return this;
        }

        Configuration.Builder setDefaultTriggerOffset(int defaultTriggerOffset) {
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
