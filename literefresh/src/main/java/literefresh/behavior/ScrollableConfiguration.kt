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

class ScrollableConfiguration(builder: Builder) : Configuration(builder), IScrollableConfig {

    companion object {
        val TAG = ScrollableConfiguration.javaClass.name
    }

    override fun onLayout(parent: CoordinatorLayout, child: View, layoutDirection: Int) {
        super.onLayout(parent, child, layoutDirection)
        Log.d(TAG, "onLayout")
        val parentHeight = parent.height
        val childHeight = child.height
        val parentWidth = parent.width
        val childWidth = child.width
        // Compute max offset, it will not exceed parent height.
        topEdgeConfig.addCheckpoint(
            OffsetConfig.Builder()
                .setParentSize(parentHeight)
                .setChildSize(childHeight)
                .setOffset(0).build(),
                Checkpoint.Type.STOP_POINT,
                Checkpoint.Type.ANCHOR_POINT
        )
        topEdgeConfig.addCheckpoint(
            OffsetConfig.Builder()
                .setParentSize(parentHeight)
                .setChildSize(childHeight)
                .setOffsetRatioOfParent(1.0f).build(),
            Checkpoint.Type.STOP_POINT
        )
        // Compute content view's minimum offset.
        bottomEdgeConfig.addCheckpoint(
            OffsetConfig.Builder()
                .setParentSize(parentHeight)
                .setChildSize(childHeight)
                .setOffset(0)
                .build(),
            Checkpoint.Type.STOP_POINT
        )
        bottomEdgeConfig.addCheckpoint(
            OffsetConfig.Builder()
                .setParentSize(parentHeight)
                .setChildSize(childHeight)
                .setOffsetRatioOfParent(1.0f).build(),
            Checkpoint.Type.STOP_POINT,
            Checkpoint.Type.ANCHOR_POINT
        )
    }


    class Builder : Configuration.Builder {

        internal constructor() {}
        internal constructor(configuration: Configuration?) : super(configuration) {
            init(configuration)
        }

        constructor(
            context: Context?,
            behavior: AnimationOffsetBehavior<*>?,
            configuration: Configuration?
        ) : super(context, behavior, configuration) {
            init(configuration)
        }

        private fun init(config: Configuration?) {
            if (config == null) return
        }

        public override fun setHeight(height: Int?): Builder {
            super.setHeight(height)
            return this
        }

        public override fun setParentHeight(parentHeight: Int?): Builder {
            super.setParentHeight(parentHeight)
            return this
        }

        public override fun setTopMargin(topMargin: Int?): Builder {
            super.setTopMargin(topMargin)
            return this
        }

        public override fun setBottomMargin(bottomMargin: Int?): Builder {
            super.setBottomMargin(bottomMargin)
            return this
        }

        public override fun setSettled(settled: Boolean?): Builder {
            super.setSettled(settled)
            return this
        }

        override fun build(): ScrollableConfiguration {
            return ScrollableConfiguration(this)
        }

        override fun <B : AnimationOffsetBehavior<*>?> config(): B {
            val config = setSettled(false).build()
            behavior?.config = config
            return behavior as B
        }
    }


}