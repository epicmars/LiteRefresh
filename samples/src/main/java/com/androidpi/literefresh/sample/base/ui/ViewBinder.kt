package com.androidpi.literefresh.sample.base.ui

import java.lang.annotation.Inherited
import kotlin.reflect.KClass

/**
 * A layout resource you want to bind to an activity, a fragment or a view.
 * @return
 */
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS, AnnotationTarget.FIELD)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Inherited
annotation class ViewBinder(val value: Int = 0, val dataTypes: Array<KClass<*>>)