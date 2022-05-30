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

import literefresh.controller.ScrollableBehaviorController;

public class ScrollableState {

    private @ScrollableBehaviorController.EdgeFlag int edgeFlag;
    private @ScrollableStateManager.ScrollableStateFlag int state;
    private int offset;

    public ScrollableState(int edgeFlag, int state, int offset) {
        this.edgeFlag = edgeFlag;
        this.state = state;
        this.offset = offset;
    }

    public int getEdgeFlag() {
        return edgeFlag;
    }

    public void setEdgeFlag(int edgeFlag) {
        this.edgeFlag = edgeFlag;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isTopLeftEdge() {
        return edgeFlag == ScrollableBehaviorController.FLAG_EDGE_TOP_LEFT;
    }

    public boolean isBottomRightEdge() {
        return edgeFlag == ScrollableBehaviorController.FLAG_EDGE_BOTTOM_RIGHT;
    }
}
