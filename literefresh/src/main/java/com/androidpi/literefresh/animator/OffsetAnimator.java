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


/**
 * Abstract class of offset animator.
 */
public abstract class OffsetAnimator {

    public interface AnimationUpdateListener {
        void onAnimationUpdate(int value);
        void onAnimationEnd();
    }

    private boolean isRunning = false;

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    /**
     * Cancel running animation.
     */
    public abstract void cancel();

    /**
     * Animate current offset to destination offset with a duration.
     * @param currentOffset  current offset
     * @param destOffset   destination offset
     * @param duration animation duration
     * @param listener animaiton listener
     */
    public abstract void animateOffsetWithDuration(int currentOffset, int destOffset, long duration,
                                                   AnimationUpdateListener listener);
}
