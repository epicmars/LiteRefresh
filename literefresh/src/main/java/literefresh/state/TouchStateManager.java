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

import androidx.core.view.ViewCompat;

public class TouchStateManager {

    public static final int TOUCH_STATE_IDLE = 0;
    public static final int TOUCH_STATE_IN_TOUCH = 1;

    private int mState = TOUCH_STATE_IDLE;

    public void onStart(int type) {
        if (type == ViewCompat.TYPE_TOUCH) {
            moveToState(TOUCH_STATE_IN_TOUCH);
        }
    }

    public void onStop(int type) {
        if (type == ViewCompat.TYPE_TOUCH) {
            moveToState(TOUCH_STATE_IDLE);
        }
    }

    public void moveToState(int state) {
        switch (state) {
            case TOUCH_STATE_IDLE:
                mState = state;
                break;

            case TOUCH_STATE_IN_TOUCH:
                mState = state;
                break;

            default:
                break;
        }
    }

    public boolean isInTouch() {
        return mState == TOUCH_STATE_IN_TOUCH;
    }
}
