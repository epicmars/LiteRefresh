package com.androidpi.literefresh.sample.base.ui;

import android.support.annotation.LayoutRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface BindLayout {
    /**
     * Bound layout resource.
     */
    @LayoutRes int value() default 0;

    /**
     * Data types bound with the layout.
     */
    Class<?>[] dataTypes() default {};
}
