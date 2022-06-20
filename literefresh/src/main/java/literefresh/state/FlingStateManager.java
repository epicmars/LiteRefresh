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
package literefresh.state;

public class FlingStateManager {

    public static final int FLING_STATE_IDLE = 0;
    public static final int FLING_STATE_START = 1;
    public static final int FLING_STATE_START_REACH_TOP = 2;
    public static final int FLING_STATE_START_REACH_BOTTOM = 3;
    public static final int FLING_STATE_START_REACH_TOP_BOTTOM = 4;
    public static final int FLING_STATE_STOP = 5;

    private int mState = FLING_STATE_IDLE;

    public void moveToState(int state) {
        switch (state) {
            case FLING_STATE_START:
                if (mState == FLING_STATE_IDLE) {
                    mState = state;
                }
                break;

            case FLING_STATE_START_REACH_TOP:
                if (mState == FLING_STATE_START) {
                    mState = state;
                } else if (mState == FLING_STATE_START_REACH_BOTTOM) {
                    mState = FLING_STATE_START_REACH_TOP_BOTTOM;
                }
                break;
            case FLING_STATE_START_REACH_BOTTOM:
                if (mState == FLING_STATE_START) {
                    mState = state;
                } else if (mState == FLING_STATE_START_REACH_TOP) {
                    mState = FLING_STATE_START_REACH_TOP_BOTTOM;
                }
                break;

            case FLING_STATE_STOP:
                if (mState == FLING_STATE_START || mState == FLING_STATE_START_REACH_TOP
                        || mState == FLING_STATE_START_REACH_BOTTOM
                        || mState == FLING_STATE_START_REACH_TOP_BOTTOM) {
                    mState = state;
                }
                break;
        }
    }

}
