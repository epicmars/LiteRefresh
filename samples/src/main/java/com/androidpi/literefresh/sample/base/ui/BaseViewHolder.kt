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
package com.androidpi.literefresh.sample.base.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.RecyclerView
import layoutbinder.annotations.BindLayout
import timber.log.Timber
import java.lang.IllegalArgumentException
import java.lang.reflect.InvocationTargetException
import java.util.*
import kotlin.reflect.KClass

/**
 * Convention prior to configuration:
 *
 * One view holder is bound with one layout, but the rendered data types has no limitation.
 */
abstract class BaseViewHolder<VDB : ViewDataBinding>(itemView: View?) : RecyclerView.ViewHolder(itemView!!), LifecycleOwner {
    private val lifecycleRegistry = LifecycleRegistry(this)
    protected var binding: VDB? = null
    protected var dataType: List<KClass<*>> = ArrayList()
    protected var fragmentManager: FragmentManager? = null
    fun onBindViewHolder(data: Any?, position: Int) {
        if (null == data) {
            Timber.w("Data is null!")
            return
        }
        if (!dataType.contains(data::class)) {
            Timber.e("Data type doesn't match the contract!")
            return
        }
        onBind(data, position)
    }

    override fun getLifecycle(): LifecycleRegistry {
        return lifecycleRegistry
    }

    abstract fun <T> onBind(data: T, position: Int)
    fun onAttachedToWindow() {
        // empty
    }

    fun onDetachedToWindow() {
        // empty
    }

    open fun onViewRecycled() {
        // empty
    }

    companion object {
        /**
         * Instantiate a subclass of BaseViewHolder.
         */
        fun instance(clazz: Class<out BaseViewHolder<*>>?, parent: ViewGroup): BaseViewHolder<*> {
            if (null == clazz) {
                throw NullPointerException("The view holder class to be instantiated is null, it may not be " +
                        "registered in RecyclerAdapter or the data types of the objects are not bound with the view holder.")
            }
            val bindLayout = clazz.getAnnotation(ViewBinder::class.java)
                    ?: throw NullPointerException("A view holder must be annotated with BindLayout!")
            val layoutId: Int = bindLayout.value
            val itemView = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
            try {
                val constructor = clazz.getDeclaredConstructor(View::class.java)
                val viewHolder = constructor.newInstance(itemView)
                viewHolder.binding = DataBindingUtil.bind(itemView)
                viewHolder.dataType = mutableListOf(*bindLayout.dataTypes)
                return viewHolder
            } catch (e: NoSuchMethodException) {
                logError(clazz, e)
            } catch (e: IllegalAccessException) {
                Timber.e(e, "%s instantiation error.", clazz)
            } catch (e: InvocationTargetException) {
                Timber.e(e, "%s instantiation error.", clazz)
            } catch (e: InstantiationException) {
                Timber.e(e, "%s instantiation error.", clazz)
            }
            throw IllegalArgumentException("View Holder initialization error!")
        }

        fun instance(clazz: Class<out BaseViewHolder<*>>?, parent: ViewGroup, fm: FragmentManager?): BaseViewHolder<*> {
            val holder = instance(clazz, parent)
            if (holder != null) holder.fragmentManager = fm
            return holder
        }

        private fun logError(clazz: Class<out BaseViewHolder<*>>, e: NoSuchMethodException) {
            Timber.e(e, "%s instantiation error.", clazz)
        }
    }
}