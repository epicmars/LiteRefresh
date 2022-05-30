package literefresh.state;

import literefresh.behavior.Checkpoint;
import literefresh.behavior.CheckpointRange;

public class RefreshState {
    private CheckpointRange currentRange;
    private Checkpoint anchorPoint;
    private Checkpoint triggerPoint;
    private @RefreshStateManager.RefreshStateFlag
    int refreshState;

    public RefreshState(@RefreshStateManager.RefreshStateFlag int refreshState,
                        CheckpointRange currentRange, Checkpoint anchorPoint,
                        Checkpoint triggerPoint) {
        this.refreshState = refreshState;
        this.currentRange = currentRange;
        this.anchorPoint = anchorPoint;
        this.triggerPoint = triggerPoint;
    }

    public CheckpointRange getCurrentRange() {
        return currentRange;
    }

    public void setCurrentRange(CheckpointRange currentRange) {
        this.currentRange = currentRange;
    }

    public Checkpoint getAnchorPoint() {
        return anchorPoint;
    }

    public void setAnchorPoint(Checkpoint anchorPoint) {
        this.anchorPoint = anchorPoint;
    }

    public Checkpoint getTriggerPoint() {
        return triggerPoint;
    }

    public void setTriggerPoint(Checkpoint triggerPoint) {
        this.triggerPoint = triggerPoint;
    }

    public int getRefreshState() {
        return refreshState;
    }

    public void setRefreshState(int refreshState) {
        this.refreshState = refreshState;
    }
}