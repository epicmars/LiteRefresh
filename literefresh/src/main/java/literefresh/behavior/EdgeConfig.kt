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

import android.util.SparseArray
import android.view.View
import androidx.annotation.NonNull
import androidx.coordinatorlayout.widget.CoordinatorLayout
import java.lang.Math.abs

class EdgeConfig {
    var mHead: Checkpoint? = null
    var mMap: SparseArray<Checkpoint> = SparseArray()
    var mMinOffset = Int.MAX_VALUE
    var mMaxOffset = Int.MIN_VALUE

    fun getMinOffset() : Int? {
        return mHead?.offsetConfig?.offset
    }

    fun getMaxOffset() : Int? {
        var current = mHead
        while (current != null) {
            if (current.mNext == null) {
                break
            }
            current = current.mNext
        }
        return current?.offsetConfig?.offset
    }

    /**
     * Find closest checkpoint near given offset.
     * @param offset Given offset.
     */
    fun findClosestCheckpoint(offset: Int) : Checkpoint? {
        var current = mHead
        while (current != null && offset > current.offsetConfig.offset) {
            if (current.mNext == null) {
                break
            }
            current = current.mNext
        }

        if (current?.mPrevious != null) {
            current = current.mPrevious
        }

        if (current?.mNext != null) {
            return if (abs(offset - current.offsetConfig.offset) < abs(current.mNext!!.offsetConfig.offset - offset)) current else current.mNext
        }

        return current
    }

    fun addCheckpoint(config: OffsetConfig, vararg types: Checkpoint.Type) {
        if (mMap.contains(config.offset)) {
            mMap.get(config.offset).types.addAll(types)
            return
        }
        if (mHead == null) {
            mHead = createCheckpoint(config, *types)
            return
        }
        var current = mHead
        while (current != null) {
            // (config.offset == cp.offsetConfig.offset) is excluded already
            if (config.offset < current.offsetConfig.offset) {
                // cp is head
                val isHead = current.mPrevious == null
                if (types.contains(Checkpoint.Type.STOP_POINT)) {
                    if (isHead) {
                        val checkpoint = createCheckpoint(config, *types)
                        insertBefore(current, checkpoint)
                        current.types.remove(Checkpoint.Type.STOP_POINT)
                        break
                    } else {
                        throw IllegalArgumentException("Stop point must have minimum or maximum offset of all the checkpoints.")
                    }
                } else {
                    if (isHead) {
                        throw IllegalArgumentException("Only minimum stop point can insert before head.")
                    } else {
                        val checkpoint = createCheckpoint(config, *types)
                        insertBefore(current, checkpoint)
                        break
                    }
                }
            } else if ((current.mNext == null || config.offset < current.mNext!!.offsetConfig.offset)) {
                val checkpoint = createCheckpoint(config, *types)
                insertAfter(current, checkpoint)
                break
            }
            current = current.mNext
        }
    }

    fun onLayout(parent: CoordinatorLayout, child: View, layoutDirection: Int) {
        var current = mHead
        while (current != null) {
            current.offsetConfig.updateOffset(parent.height, child.height)
        }
    }

    private fun insertBefore(current: Checkpoint, checkpoint: Checkpoint) {
        checkpoint.mNext = current
        checkpoint.mPrevious = current.mPrevious
        current.mPrevious?.mNext = checkpoint
        current.mPrevious = checkpoint
        if (checkpoint.mPrevious == null) {
            mHead = checkpoint
        }
        mMap.put(checkpoint.offsetConfig.offset, checkpoint)
    }

    private fun insertAfter(
        current: Checkpoint,
        checkpoint: Checkpoint
    ) {
        checkpoint.mPrevious = current
        checkpoint.mNext = current.mNext
        current.mNext?.mPrevious = checkpoint
        current.mNext = checkpoint
        mMap.put(checkpoint.offsetConfig.offset, checkpoint)
    }

    fun removeCheckpoint(config: OffsetConfig) {
        if (!mMap.contains(config.offset)) {
            return
        }
        var cp = mHead
        while (cp != null) {
            if (cp.offsetConfig.offset == config.offset) {
                cp.mPrevious?.mNext = cp.mNext
                cp.mNext?.mPrevious = cp.mPrevious
                cp.mNext = null
                cp.mPrevious = null
                mMap.remove(config.offset)
                break
            }
            cp = cp.mNext
        }
    }

    @NonNull
    private fun createStopPoint(config: OffsetConfig): Checkpoint {
        return createCheckpoint(config, Checkpoint.Type.STOP_POINT)
    }

    @NonNull
    private fun createCheckpoint(config: OffsetConfig, vararg types: Checkpoint.Type): Checkpoint {
        var checkpoint = Checkpoint()
        checkpoint.types.addAll(types)
        checkpoint.offsetConfig = config
        return checkpoint
    }


}