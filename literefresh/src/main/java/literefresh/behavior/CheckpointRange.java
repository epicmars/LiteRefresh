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
package literefresh.behavior;

public class CheckpointRange implements Comparable<CheckpointRange> {
    private Checkpoint front;
    private Checkpoint back;

    public CheckpointRange() {
    }

    public CheckpointRange(Checkpoint first, Checkpoint second) {
        update(first, second);
    }

    public void update(Checkpoint first, Checkpoint second) {
        front = first;
        back = second;
    }

    public boolean isNull() {
        return front == null || back == null;
    }

    @Override
    public int compareTo(CheckpointRange o) {
        if (isNull() || o.isNull()) {
            throw new IllegalArgumentException("Checkpoint of a range can not be null.");
        }
        if (front.offset() == o.front.offset() && back.offset() == o.back.offset()) {
            return 0;
        } else if (back.offset() <= o.front.offset()) {
            return -1;
        } else {
            return 1;
        }
    }
}
