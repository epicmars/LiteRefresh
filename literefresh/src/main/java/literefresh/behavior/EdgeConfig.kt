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

import android.util.Log
import android.util.SparseArray
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.util.containsKey
import java.lang.Math.abs

class EdgeConfig {
    val TAG = EdgeConfig::class.simpleName
    var mHead: Checkpoint? = null
    var mCheckpoints = mutableSetOf<Checkpoint>()
    var mDeactived = mutableSetOf<Checkpoint>()
    var mMap: SparseArray<Checkpoint> = SparseArray<Checkpoint>()
    var mMinOffset: Int? = null
    var mMaxOffset: Int? = null
    var shouldRebuildCheckpoint = false

    fun onMeasure(
        parent: CoordinatorLayout, child: View
    ) {
        rebuildCheckpoints(parent.measuredHeight, 0)
        updateOffsetRange()
    }

    fun onLayout(parent: CoordinatorLayout, child: View, layoutDirection: Int) {
        rebuildCheckpoints(parent.height, layoutDirection)
        updateOffsetRange()
    }

    fun getMinOffset(): Int {
        return mMinOffset ?: 0
    }

    fun getMaxOffset(): Int {
        return mMaxOffset ?: 0
    }

    /**
     * Find closest checkpoint near given offset.
     * @param offset Given offset.
     */
    fun findClosestCheckpoint(offset: Int): Checkpoint? {
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

    fun deactiveCheckpoint(config: OffsetConfig, vararg types: Checkpoint.Type) {
        mDeactived.add(Checkpoint(config, *types))
    }

    fun addCheckpoint(config: OffsetConfig, vararg types: Checkpoint.Type) {
        mCheckpoints.add(Checkpoint(config, *types))
        shouldRebuildCheckpoint = true
    }

    // fixme stop point is special for now
    fun removeCheckpoint(config: OffsetConfig) {
        val ite = mCheckpoints.iterator()
        while (ite.hasNext()) {
            val next = ite.next()
            if (next.offset() == config.offset) {
                ite.remove()
            }
        }
        shouldRebuildCheckpoint = true;
    }

    private fun rebuildCheckpoints(parentSize: Int, layoutDirection: Int) {
        if (parentSize == 0) {
            shouldRebuildCheckpoint = true
            return
        }
        if (!shouldRebuildCheckpoint) {
            return
        }
        Log.d(TAG, "rebuildCheckpoints")
        mMap.clear()
        mDeactived.forEach {
            it.offsetConfig.updateOffset(parentSize)
        }
        mCheckpoints.forEach {
            it.offsetConfig.updateOffset(parentSize)
            mDeactived.forEach { deactive ->
                if (it.offset() == deactive.offset()) {
                    it.deactive(deactive.types.keys.toTypedArray())
                }
            }
            insertCheckpoint(it)
        }
        shouldRebuildCheckpoint = false
    }

    private fun updateOffsetRange() {
        var current = mHead
        var minStopPoint: Checkpoint? = null
        var maxStopPoint: Checkpoint? = null
        while (current != null) {
            if (minStopPoint == null && current.isActive(Checkpoint.Type.STOP_POINT)) {
                minStopPoint = current
                current = current.mNext
                continue
            }
            if (current.isActive(Checkpoint.Type.STOP_POINT)) {
                if (maxStopPoint == null || current.offset() >= maxStopPoint.offset()) {
                    maxStopPoint = current
                }
            }
            current = current.mNext
        }

        mMinOffset = minStopPoint?.offset()
        if (maxStopPoint == null) {
            mMaxOffset = mMinOffset
        } else {
            mMaxOffset = maxStopPoint.offset()
        }
    }


    private fun insertCheckpoint(checkpoint: Checkpoint) {
        val offset = checkpoint.offsetConfig.offset
        val types = checkpoint.types
        if (mMap.containsKey(offset)) {
            mMap.get(offset).types.putAll(types)
            return
        }
        if (mHead == null) {
            mHead = checkpoint
            return
        }
        var current = mHead
        while (current != null) {
            // (config.offset == cp.offsetConfig.offset) is excluded already
            if (offset < current.offsetConfig.offset) {
                insertBefore(current, checkpoint)
            } else if ((current.mNext == null || offset < current.mNext!!.offsetConfig.offset)) {
                // if current has no next or offset is within range of current and next
                insertAfter(current, checkpoint)
                break
            }
            current = current.mNext
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

    fun deleteCheckpoint(config: OffsetConfig) {
        if (!mMap.containsKey(config.offset)) {
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
}