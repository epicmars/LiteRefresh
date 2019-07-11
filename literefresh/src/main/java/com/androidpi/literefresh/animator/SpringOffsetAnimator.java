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
package com.androidpi.literefresh.animator;


import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FloatValueHolder;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

/**
 * A animator can make offset changes like spring.
 */
public class SpringOffsetAnimator extends OffsetAnimator {

    private SpringAnimation springAnimation;

    @Override
    public void cancel() {
        if (null != springAnimation && springAnimation.isRunning()) {
            springAnimation.cancel();
        }
    }

    @Override
    public void animateOffsetWithDuration(int currentOffset, int destOffset, long duration,
                                          final AnimationUpdateListener listener) {
        if (null == springAnimation) {
            springAnimation = new SpringAnimation(new FloatValueHolder());
        } else {
            springAnimation.cancel();
        }
        springAnimation.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {
            @Override
            public void onAnimationUpdate(DynamicAnimation animation, float value, float velocity) {
                if (listener == null)
                    return;
                listener.onAnimationUpdate((int) value);
            }
        });

        springAnimation.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value,
                                       float velocity) {
                setRunning(false);
            }
        });

        SpringForce springForce = new SpringForce()
                .setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY)
                .setFinalPosition(destOffset);

        springAnimation
                .setSpring(springForce)
                .setStartValue(currentOffset);

        springAnimation.start();
        setRunning(true);
    }
}
