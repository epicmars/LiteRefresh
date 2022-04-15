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
package literefresh.behavior

import android.content.Context
import androidx.annotation.IntDef
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

class IndicatorConfiguration(builder: Builder) : Configuration(builder) {
    @IntDef(MODE_FOLLOW, MODE_STILL, MODE_FOLLOW_DOWN, MODE_FOLLOW_UP)
    @Retention(RetentionPolicy.SOURCE)
    internal annotation class FollowMode

    var showUpWhenRefresh: Boolean?

    @get:FollowMode
    @FollowMode
    var followMode: Int

    class Builder : Configuration.Builder {
        var showUpWhenRefresh: Boolean? = null

        @FollowMode
        var followMode = 0

        internal constructor() {}
        internal constructor(configuration: IndicatorConfiguration?) : super(configuration) {
            init(configuration)
        }

        constructor(
            context: Context?,
            behavior: AnimationOffsetBehavior<*>?,
            configuration: IndicatorConfiguration?
        ) : super(context, behavior, configuration) {
            init(configuration)
        }

        private fun init(config: IndicatorConfiguration?) {
            if (config == null) return
            showUpWhenRefresh = config.showUpWhenRefresh
            followMode = config.followMode
        }

        fun setFollowMode(@FollowMode followMode: Int): Builder {
            this.followMode = followMode
            return this
        }

        override fun setParentHeight(parentHeight: Int?): Builder? {
            super.setParentHeight(parentHeight)
            return this
        }

        override fun setTopMargin(topMargin: Int?): Builder? {
            super.setTopMargin(topMargin)
            return this
        }

        override fun setBottomMargin(bottomMargin: Int?): Builder? {
            super.setBottomMargin(bottomMargin)
            return this
        }

        override fun setSettled(settled: Boolean?): Builder? {
            super.setSettled(settled)
            return this
        }

        override fun build(): IndicatorConfiguration? {
            return IndicatorConfiguration(this)
        }

        override fun <B : AnimationOffsetBehavior<*>?> config(): B {
            val config = setSettled(false)!!.build()
            behavior!!.config = config
            return behavior as B
        }
    }

    companion object {
        /**
         * Follow content view.
         */
        const val MODE_FOLLOW = 0

        /**
         * Still, does not follow content view.
         */
        const val MODE_STILL = 1

        /**
         * Follow when scroll down.
         */
        const val MODE_FOLLOW_DOWN = 2

        /**
         * Follow when scroll up.
         */
        const val MODE_FOLLOW_UP = 3
    }

    init {
        showUpWhenRefresh = builder.showUpWhenRefresh
        followMode = builder.followMode
    }
}