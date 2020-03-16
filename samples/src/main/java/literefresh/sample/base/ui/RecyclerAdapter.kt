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
package literefresh.sample.base.ui

import android.util.SparseArray
import android.util.SparseIntArray
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class RecyclerAdapter : RecyclerView.Adapter<BaseViewHolder<*>?>() {
    private val mDataViewMap = SparseIntArray()
    private val mViewHolderMap = SparseArray<Class<out BaseViewHolder<*>>>()
    private val mPayloads: MutableList<Any> = ArrayList()
    private val payloadSet: HashSet<Any> = LinkedHashSet()
    private var mFragmentManager: FragmentManager? = null
    private val adapterDataObserver: RecyclerView.AdapterDataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            super.onChanged()
            payloadSet.addAll(mPayloads)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            super.onItemRangeChanged(positionStart, itemCount)
            onChanged()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            super.onItemRangeChanged(positionStart, itemCount, payload)
            onChanged()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            onChanged()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount)
            onChanged()
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount)
            onChanged()
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        unregisterAdapterDataObserver(adapterDataObserver)
    }

    fun attachToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = this
    }

    /**
     * Register one or more view holder to display some data.
     * @param clazzArray class types of the registered view holer
     * @return
     */
    fun register(vararg clazzArray: Class<out BaseViewHolder<*>>): RecyclerAdapter {
        for (clazz in clazzArray) {
            val bindLayout = clazz.getAnnotation(ViewBinder::class.java) as ViewBinder
            // map data types to layout resource
            bindLayout.dataTypes.forEach {
                mDataViewMap.append(it.javaObjectType.hashCode(), bindLayout.value)
            }
            // map layout resource to view holder
            mViewHolderMap.put(bindLayout.value, clazz)
        }
        return this
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val viewHolderClass = mViewHolderMap[viewType]
        // If viewHolderClass is null, the ViewHolder may not be registered
        return BaseViewHolder.instance(viewHolderClass, parent, mFragmentManager)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val item = mPayloads[position]
        holder!!.onBindViewHolder(item, position)
    }

    override fun onViewAttachedToWindow(holder: BaseViewHolder<*>) {
        super.onViewAttachedToWindow(holder!!)
        holder.onAttachedToWindow()
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder<*>) {
        super.onViewDetachedFromWindow(holder!!)
        holder.onDetachedToWindow()
    }

    override fun onViewRecycled(holder: BaseViewHolder<*>) {
        super.onViewRecycled(holder!!)
        holder.onViewRecycled()
    }

    override fun getItemCount(): Int {
        return mPayloads.size
    }

    override fun getItemViewType(position: Int): Int {
        val item = payloads[position]
        if (item is Array<*>) {
            return mDataViewMap[((item as Array<Object>)[0]).`class`.hashCode()]
        }
        return mDataViewMap[item::class.java.hashCode()]
    }

    val payloads: List<Any>
        get() = mPayloads

    fun setFragmentManager(mFragmentManager: FragmentManager?) {
        this.mFragmentManager = mFragmentManager
    }

    /**
     * Set adapter payloads.
     *
     * @param payloads
     */
    fun setPayload(vararg payloads: Any) {
        val c = mutableListOf(*payloads)
        payloadSet.clear()
        mPayloads.clear()
        mPayloads.addAll(c)
        notifyDataSetChanged()
    }

    /**
     * Set adapter payloads.
     *
     * @param payloads
     */
    fun setPayloads(payloads: Collection<Any>?) {
        if (null == payloads) {
            return
        }
        payloadSet.clear()
        mPayloads.clear()
        mPayloads.addAll(payloads)
        notifyDataSetChanged()
    }

    /**
     * Add payload to current payloads.
     *
     * @param payloads
     */
    fun addPayloads(payloads: Collection<Any>?) {
        if (null == payloads || payloads.isEmpty()) {
            return
        }
        var added = 0
        val positionStart = mPayloads.size
        for (obj in payloads) {
            if (!contains(obj)) {
                mPayloads.add(obj)
                added++
            }
        }
        notifyItemRangeInserted(positionStart, added)
    }

    fun addPayload(vararg payloads: Any) {
        if (null == payloads || payloads.isEmpty()) {
            return
        }
        var added = 0
        val positionStart = mPayloads.size
        for (obj in payloads) {
            if (!contains(obj)) {
                mPayloads.add(obj)
                added++
            }
        }
        notifyItemRangeInserted(positionStart, added)
    }

    operator fun contains(`object`: Any): Boolean {
        return payloadSet.contains(`object`)
    }

    /**
     * Append payloads with source, the source doesn't contain the payloads to be appended.
     *
     * @param source   source payloads
     * @param payloads payloads to be appended
     */
    fun appendPayloads(source: Collection<Any>?, payloads: Collection<Any>?) {
        // Adapter payload is empty, set to source.
        if (null == payloads || payloads.isEmpty()) {
            setPayloads(source)
            return
        }
        if (!mPayloads.isEmpty() || source == null || source.isEmpty()) {
            // The source is empty, add to current payloads.
            addPayloads(payloads as Collection<Any>?)
            return
        }
        val positionStart = source.size
        val itemCount = payloads.size
        mPayloads.addAll(source)
        mPayloads.addAll(payloads)
        notifyItemRangeInserted(positionStart, itemCount)
    }

    init {
        registerAdapterDataObserver(adapterDataObserver)
    }
}