package com.androidpi.literefresh.dependency;

import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

/**
 * The content can make header and footer to follow with it and vice versa.
 * In this situation, the dependency between them is not fixed, they will change depends on
 * which view is interacting with the user touch.
 */
public enum DependencyManager {

    INSTANCE;

    private static View interactingChild;
    private static CoordinatorLayout.Behavior interactingBehavior;

    /**
     * Record which view and it's associated behavior is interacting with user touch.
     * @param child
     * @param behavior
     */
    public void interact(View child, CoordinatorLayout.Behavior behavior) {
        interactingChild = child;
        interactingBehavior = behavior;
    }

    /**
     * User has just finish touching {@link DependencyManager#interactingChild}.
     */
    public void deinteract() {
        interactingChild = null;
        interactingBehavior = null;
    }
}
