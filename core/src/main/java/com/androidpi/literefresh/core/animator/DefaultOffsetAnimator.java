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
package com.androidpi.literefresh.core.animator;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.animation.DecelerateInterpolator;

/**
 * The default offset animator used to animate view's top and bottom offset.
 */
public class DefaultOffsetAnimator extends OffsetAnimator {

    private ValueAnimator mOffsetAnimator;

    public void animateOffsetWithDuration(int currentOffset, int destOffset, long duration,
                                          AnimationUpdateListener listener) {
        if (mOffsetAnimator == null) {
            mOffsetAnimator = new ValueAnimator();
            mOffsetAnimator.setInterpolator(new DecelerateInterpolator());
            mOffsetAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (listener == null)
                        return;
                    listener.onAnimationUpdate((int) animation.getAnimatedValue());
                }
            });
        } else {
            mOffsetAnimator.cancel();
        }
        mOffsetAnimator.setDuration(duration);
        mOffsetAnimator.setIntValues(currentOffset, destOffset);
        mOffsetAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                setRunning(true);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setRunning(false);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                setRunning(false);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                setRunning(true);
            }
        });
        mOffsetAnimator.start();
    }


    @Override
    public void cancel() {
        if (mOffsetAnimator != null) {
            mOffsetAnimator.cancel();
        }
    }
}
