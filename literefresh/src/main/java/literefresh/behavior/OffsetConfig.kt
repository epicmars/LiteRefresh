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
    var offsetRatioOfSelf: Float? = null
    var offsetRatioOfParent: Float? = null
    var isUseDefaultOffset = false
    var parentSizePx = 0
    var selfSizePx = 0

    constructor() {}
    constructor(
        initialOffset: Int, defaultOffset: Int, cachedOffset: Int,
        offsetRatioOfParent: Float, isUseDefaultOffset: Boolean,
        parentSizePx: Int, selfSizePx: Int
    ) {
        this.offset = initialOffset
        this.initialOffset = initialOffset
        this.defaultOffset = defaultOffset
        this.cachedOffset = cachedOffset
        this.offsetRatioOfParent = offsetRatioOfParent
        this.isUseDefaultOffset = isUseDefaultOffset
        this.parentSizePx = parentSizePx
        this.selfSizePx = selfSizePx
    }

    /**
     * Must call this method when the view layout.
     * @param parentSizePx parent height if vertical scroll or parent width if horizon scroll
     * @param selfSizePx self height in pixel if vertical scroll or self width if horizon scroll
     */
    fun updateOffset(parentSizePx: Int, selfSizePx: Int) : Int {
        this.parentSizePx = parentSizePx;
        this.selfSizePx = selfSizePx

        if (isUseDefaultOffset) {
            offset = defaultOffset;
        }
        if (offsetRatioOfParent != null) {
            offset = (offsetRatioOfParent!! * parentSizePx).toInt()
        }

        if (offsetRatioOfSelf != null) {
            offset = (offsetRatioOfSelf!! * selfSizePx).toInt()
        }
        return offset;
    }

    class Builder {
        var offset = 0
        var initialOffset = 0
        var defaultOffset = 0
        var cachedOffset = 0
        var offsetRatioOfSelf : Float? = null
        var offsetRatioOfParent : Float? = null
        var isUseDefaultOffset = false
        var parentSizePx = 0
        var selfSizePx = 0

        constructor() {}
        constructor(config: OffsetConfig?) {
            init(config)
        }

        private fun init(config: OffsetConfig?) {
            if (config == null) return
            offset = config.offset
            initialOffset = config.initialOffset
            offsetRatioOfSelf = config.offsetRatioOfSelf
            offsetRatioOfParent = config.offsetRatioOfParent
            defaultOffset = config.defaultOffset
            cachedOffset = config.cachedOffset
            isUseDefaultOffset = config.isUseDefaultOffset
            parentSizePx = config.parentSizePx
            selfSizePx = config.selfSizePx
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
            this.parentSizePx = parentSizePx;
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
            config.parentSizePx = parentSizePx
            config.selfSizePx = selfSizePx
            config.updateOffset(parentSizePx, selfSizePx)
            return config
        }
    }
}