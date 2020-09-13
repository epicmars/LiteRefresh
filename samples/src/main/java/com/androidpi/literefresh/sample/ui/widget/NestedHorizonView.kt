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
package com.androidpi.literefresh.sample.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.FrameLayout

class NestedHorizonView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyle: Int = 0) : FrameLayout(context, attrs, defStyle) {
    private var mActivePointerId = INVALID_POINTER
    private val mTouchSlop: Int
    private var mLastMotionX = 0f
    private val mLastMotionY = 0f
    private val mInitialMotionX = 0f
    private val mInitialMotionY = 0f

    //    @Override
    //    public boolean dispatchTouchEvent(MotionEvent ev) {
    //        final int action = ev.getAction();
    //        final int actionMasked = ev.getActionMasked();
    //        switch (actionMasked) {
    //            case MotionEvent.ACTION_DOWN:
    //                mLastMotionX = mInitialMotionX = ev.getX();
    //                mLastMotionY = mInitialMotionY = ev.getY();
    //                mActivePointerId  = ev.getPointerId(0);
    //                break;
    //            case MotionEvent.ACTION_MOVE:
    //                final int activePointerId = mActivePointerId;
    //                if (activePointerId == INVALID_POINTER) {
    //                    // If we don't have a valid id, the touch down wasn't on content.
    //                    break;
    //                }
    //
    //                final int pointerIndex = ev.findPointerIndex(activePointerId);
    //                if (pointerIndex == -1) {
    //                    Log.e(TAG, "Invalid pointerId=" + activePointerId
    //                            + " in dispatchTouchEvent");
    //                    break;
    //                }
    //
    //                final float x = ev.getX(pointerIndex);
    //                final float dx = x - mLastMotionX;
    //                final float xDiff = Math.abs(dx);
    //                final float y = ev.getY(pointerIndex);
    //                final float yDiff = Math.abs(y - mLastMotionY);
    //                if (yDiff > mTouchSlop) {
    //                    // We are scrolling vertically.
    //                    requestDisallowInterceptTouchEvent(false);
    //                } else {
    //                    // Otherwise, disallow interception.
    //                    requestDisallowInterceptTouchEvent(true);
    //                }
    //                mLastMotionY = y;
    //                mLastMotionX = x;
    //                break;
    //
    //            case MotionEvent.ACTION_CANCEL:
    //            case MotionEvent.ACTION_UP:
    //                /* Release the drag */
    //                mActivePointerId = INVALID_POINTER;
    //                break;
    //
    //            case MotionEvent.ACTION_POINTER_DOWN: {
    //                final int index = ev.getActionIndex();
    //                mLastMotionX = (int) ev.getX(index);
    //                mActivePointerId = ev.getPointerId(index);
    //                break;
    //            }
    //            case MotionEvent.ACTION_POINTER_UP:
    //                onSecondaryPointerUp(ev);
    //                mLastMotionX = (int) ev.getX(ev.findPointerIndex(mActivePointerId));
    //                break;
    //        }
    //        return super.dispatchTouchEvent(ev);
    //    }
    private fun onSecondaryPointerUp(ev: MotionEvent) {
        val pointerIndex = ev.action and MotionEvent.ACTION_POINTER_INDEX_MASK shr
                MotionEvent.ACTION_POINTER_INDEX_SHIFT
        val pointerId = ev.getPointerId(pointerIndex)
        if (pointerId == mActivePointerId) {
            val newPointerIndex = if (pointerIndex == 0) 1 else 0
            mLastMotionX = ev.getX(newPointerIndex)
            mActivePointerId = ev.getPointerId(newPointerIndex)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return super.onTouchEvent(event)
    }

    companion object {
        private const val TAG = "NestedHorizonView"
        private const val INVALID_POINTER = -1
    }

    init {
        val configuration = ViewConfiguration.get(context)
        mTouchSlop = configuration.scaledTouchSlop
    }
}