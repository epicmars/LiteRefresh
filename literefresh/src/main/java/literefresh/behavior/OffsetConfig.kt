/*
 * Copyright 2022 yinpinjiu@gmail.com
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
package literefresh.behavior

class OffsetConfig {
    var offset = 0
    var initialOffset = 0
    var defaultOffset = 0
    var cachedOffset = 0
    var offsetRatioOfSelf = 0f
    var offsetRatioOfParent = 0f
    var isUseDefaultOffset = false
    var parentSize = 0
    var childSize = 0

    constructor() {}
    constructor(
        initialOffset: Int, defaultOffset: Int, cachedOffset: Int, offsetRatioOfSelf: Float,
        offsetRatioOfParent: Float, isUseDefaultOffset: Boolean,
        parentSizePx: Int, childSizePx: Int
    ) {
        this.offset = initialOffset
        this.initialOffset = initialOffset
        this.defaultOffset = defaultOffset
        this.cachedOffset = cachedOffset
        this.offsetRatioOfSelf = offsetRatioOfSelf
        this.offsetRatioOfParent = offsetRatioOfParent
        this.isUseDefaultOffset = isUseDefaultOffset
        this.parentSize = parentSize;
        this.childSize = childSize;
    }

    /**
     * Must call this method when the view layout.
     */
    fun updateOffset(parentSizePx: Int, childSizePx: Int) : Int {
        this.parentSize = parentSizePx;
        this.childSize = childSizePx;
        if (isUseDefaultOffset) {
            offset = defaultOffset;
        }
        if (offsetRatioOfParent > 0) {
            offset = (offsetRatioOfParent * parentSize).toInt()
        }
        if (offsetRatioOfSelf > 0) {
            offset = (offsetRatioOfSelf * childSize).toInt()
        }
        return offset;
    }

    class Builder {
        var offset = 0
        var initialOffset = 0
        var defaultOffset = 0
        var cachedOffset = 0
        var offsetRatioOfSelf = 0f
        var offsetRatioOfParent = 0f
        var isUseDefaultOffset = false
        var parentSize = 0
        var childSize = 0

        constructor() {}
        constructor(config: OffsetConfig?) {
            init(config)
        }

        private fun init(config: OffsetConfig?) {
            if (config == null) return
            offset = config.offset
            offsetRatioOfSelf = config.offsetRatioOfSelf
            offsetRatioOfParent = config.offsetRatioOfParent
            defaultOffset = config.defaultOffset
            cachedOffset = config.cachedOffset
            isUseDefaultOffset = config.isUseDefaultOffset
        }

        fun setOffset(minOffset: Int): Builder {
            offset = minOffset
            isUseDefaultOffset = false
            return this
        }

        fun setDefaultOffset(defaultOffset: Int): Builder {
            this.defaultOffset = defaultOffset
            return this
        }

        fun setCachedOffset(cachedOffset: Int): Builder {
            this.cachedOffset = cachedOffset
            return this
        }

        fun setOffsetRatioOfSelf(offsetRatioOfSelf: Float): Builder {
            this.offsetRatioOfSelf = offsetRatioOfSelf
            return this
        }

        fun setOffsetRatioOfParent(offsetRatioOfParent: Float): Builder {
            this.offsetRatioOfParent = offsetRatioOfParent
            return this
        }

        fun setUseDefaultMinOffset(useDefaultMinOffset: Boolean): Builder {
            isUseDefaultOffset = useDefaultMinOffset
            return this
        }

        fun setParentSize(parentSizePx: Int): Builder {
            this.parentSize = parentSizePx;
            return this;
        }

        fun setChildSize(childSizePx: Int): Builder {
            this.childSize = childSizePx;
            return this;
        }

        fun build(): OffsetConfig {
            val config = OffsetConfig()
            config.offset = offset
            config.cachedOffset = cachedOffset
            config.defaultOffset = defaultOffset
            config.offsetRatioOfSelf = offsetRatioOfSelf
            config.offsetRatioOfParent = offsetRatioOfParent
            config.isUseDefaultOffset = isUseDefaultOffset
            config.parentSize = parentSize
            config.childSize = childSize;
            config.updateOffset(parentSize, childSize)
            return config
        }
    }
}