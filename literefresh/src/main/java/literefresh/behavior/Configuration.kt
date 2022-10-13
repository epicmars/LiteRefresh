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
import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout

/** Common configurations of content and indicator behavior.  */
abstract class Configuration(builder: Builder) {
    var behavior: AnimationOffsetBehavior<*>? = null
    var height: Int
    var parentHeight: Int
    var topMargin: Int
    var bottomMargin: Int
    var isSettled: Boolean

    var topEdgeConfig: EdgeConfig = EdgeConfig()
    var bottomEdgeConfig: EdgeConfig = EdgeConfig()
    var leftEdgeConfig: EdgeConfig = EdgeConfig()
    var rightEdgeConfig: EdgeConfig = EdgeConfig()

    init {
        height = if (builder.height == null) 0 else builder.height!!
        parentHeight = if (builder.parentHeight == null) 0 else builder.parentHeight!!
        topMargin = if (builder.topMargin == null) 0 else builder.topMargin!!
        bottomMargin = if (builder.bottomMargin == null) 0 else builder.bottomMargin!!
        isSettled = if (builder.isSettled == null) true else builder.isSettled!!
    }

    open fun onLayout(parent: CoordinatorLayout, child: View, layoutDirection: Int) {
        height = child.height
        parentHeight = parent.height
        Log.d(javaClass.name, "parentHeight: " + parentHeight + " childHeight: " + height)

        topEdgeConfig.onLayout(parent, child, layoutDirection)
        bottomEdgeConfig.onLayout(parent, child, layoutDirection)
    }

    fun addTopCheckpoint(config: OffsetConfig, vararg types: Checkpoint.Type) {
        topEdgeConfig.addCheckpoint(config, *types)
    }

    fun addBottomCheckpoint(config: OffsetConfig, vararg types: Checkpoint.Type) {
        bottomEdgeConfig.addCheckpoint(config, *types)
    }

    abstract class Builder {
        @JvmField
        var behavior: AnimationOffsetBehavior<*>? = null
        var height: Int? = null
        var parentHeight: Int? = null
        var topMargin: Int? = null
        var bottomMargin: Int? = null
        var isSettled: Boolean? = null

        internal constructor() {}
        internal constructor(
            context: Context?,
            behavior: AnimationOffsetBehavior<*>?,
            configuration: Configuration?
        ) : this(configuration) {
            this.behavior = behavior
        }

        constructor(config: Configuration?) {
            if (config == null) return
            height = config.height
            parentHeight = config.parentHeight
            topMargin = config.topMargin
            bottomMargin = config.bottomMargin
            isSettled = config.isSettled
        }

        open fun setHeight(height: Int?): Builder? {
            this.height = height
            return this
        }

        open fun setParentHeight(parentHeight: Int?): Builder? {
            this.parentHeight = parentHeight
            return this
        }

        open fun setTopMargin(topMargin: Int?): Builder? {
            this.topMargin = topMargin
            return this
        }

        open fun setBottomMargin(bottomMargin: Int?): Builder? {
            this.bottomMargin = bottomMargin
            return this
        }

        open fun setSettled(settled: Boolean?): Builder? {
            isSettled = settled
            return this
        }

        abstract fun build(): Configuration?
        abstract fun <B : AnimationOffsetBehavior<*>?> config(): B
    }
}