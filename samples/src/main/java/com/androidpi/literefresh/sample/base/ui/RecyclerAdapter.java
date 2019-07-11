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
package com.androidpi.literefresh.sample.base.ui;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private final SparseIntArray mDataViewMap = new SparseIntArray();
    private final SparseArray<Class<? extends BaseViewHolder>> mViewHolderMap = new SparseArray<>();
    private final List<Object> mPayloads = new ArrayList<>();
    private final HashSet<Object> payloadSet = new LinkedHashSet<>();
    private FragmentManager mFragmentManager;
    private RecyclerView.AdapterDataObserver adapterDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            payloadSet.addAll(mPayloads);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            onChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
            onChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            onChanged();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            onChanged();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            onChanged();
        }
    };

    {
        registerAdapterDataObserver(adapterDataObserver);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        unregisterAdapterDataObserver(adapterDataObserver);
    }

    /**
     * Register one or more view holder to display some data.
     * @param clazzArray class types of the registered view holer
     * @return
     */
    public RecyclerAdapter register(Class<? extends BaseViewHolder>... clazzArray) {

        for (Class clazz : clazzArray) {
            BindLayout bindLayout = (BindLayout) clazz.getAnnotation(BindLayout.class);
            // map data types to layout resource
            for (Class dataType : bindLayout.dataTypes()) {
                mDataViewMap.append(dataType.hashCode(), bindLayout.value());
            }
            // map layout resource to view holder
            mViewHolderMap.put(bindLayout.value(), clazz);
        }
        return this;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Class<? extends BaseViewHolder> viewHolderClass = mViewHolderMap.get(viewType);
        // If viewHolderClass is null, the ViewHolder may not be registered
        return BaseViewHolder.instance(viewHolderClass, parent, mFragmentManager);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        Object item = mPayloads.get(position);
        holder.onBindViewHolder(item, position);
    }

    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.onAttachedToWindow();
    }

    @Override
    public void onViewDetachedFromWindow(BaseViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.onDetachedToWindow();
    }

    @Override
    public void onViewRecycled(BaseViewHolder holder) {
        super.onViewRecycled(holder);
        holder.onViewRecycled();
    }

    @Override
    public int getItemCount() {
        return mPayloads.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = mPayloads.get(position);
        return mDataViewMap.get(item.getClass().hashCode());
    }

    public List<Object> getPayloads() {
        return mPayloads;
    }

    public void setFragmentManager(FragmentManager mFragmentManager) {
        this.mFragmentManager = mFragmentManager;
    }

    /**
     * Set adapter payloads.
     *
     * @param payloads
     */
    public void setPayloads(Object... payloads) {
        setPayloads(Arrays.asList(payloads));
    }

    /**
     * Set adapter payloads.
     *
     * @param payloads
     */
    public void setPayloads(Collection<?> payloads) {
        if (null == payloads) {
            return;
        }
        payloadSet.clear();
        this.mPayloads.clear();
        this.mPayloads.addAll(payloads);
        notifyDataSetChanged();
    }

    /**
     * Add payload to current payloads.
     *
     * @param payloads
     */
    public void addPayloads(Collection<?> payloads) {
        if (null == payloads || payloads.isEmpty()) {
            return;
        }
        int added = 0;
        int positionStart = mPayloads.size();
        for (Object obj : payloads) {
            if (!contains(obj)) {
                this.mPayloads.add(obj);
                added++;
            }
        }
        notifyItemRangeInserted(positionStart, added);
    }

    public void addSinglePayload(Object payload) {
        if (payload == null || contains(payload)) return;
        int positionStart = mPayloads.size();
        int itemCount = 1;
        mPayloads.add(payload);
        notifyItemRangeInserted(positionStart, itemCount);
    }

    public boolean contains(Object object) {
        return payloadSet.contains(object);
    }

    /**
     * Append payloads with source, the source doesn't contain the payloads to be appended.
     *
     * @param source   source payloads
     * @param payloads payloads to be appended
     */
    public void appendPayloads(Collection<?> source, Collection<?> payloads) {
        // Adapter payload is empty, set to source.
        if (null == payloads || payloads.isEmpty()) {
            setPayloads(source);
            return;
        }
        if (!mPayloads.isEmpty() || source == null || source.isEmpty()) {
            // The source is empty, add to current payloads.
            addPayloads(payloads);
            return;
        }
        int positionStart = source.size();
        int itemCount = payloads.size();
        mPayloads.addAll(source);
        mPayloads.addAll(payloads);
        notifyItemRangeInserted(positionStart, itemCount);
    }
}
