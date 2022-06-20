package literefresh.behavior;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

public interface NestedScrollingListener {

    void onStartScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, Configuration config, int type);

    void onPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, Configuration config, int currentOffset, int type);

    void onPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, Configuration config, int currentOffset, float velocityX, float velocityY);

    void onFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, Configuration config, int currentOffset, float velocityX, float velocityY);

    void onScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, Configuration config, int currentOffset, int delta, int type);

    void onStopScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, Configuration config, int currentOffset, int type);
}