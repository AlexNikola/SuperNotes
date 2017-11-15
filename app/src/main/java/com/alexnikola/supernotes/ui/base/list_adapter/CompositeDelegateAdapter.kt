package com.alexnikola.supernotes.ui.base.list_adapter

import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.ViewGroup

import java.util.ArrayList

class CompositeDelegateAdapter
protected constructor(protected val typeToAdapterMap: SparseArray<IDelegateAdapter<*>>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var data: List<Any> = ArrayList()

    override fun getItemViewType(position: Int): Int {
        for (i in FIRST_VIEW_TYPE until typeToAdapterMap.size()) {
            val delegate = typeToAdapterMap.valueAt(i)
            if (delegate.isForViewType(data, position)) {
                return typeToAdapterMap.keyAt(i)
            }
        }
        throw NullPointerException(
                "Can not get viewType for position " + position)
    }

    override fun onCreateViewHolder(
            parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return typeToAdapterMap.get(viewType)
                .onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(
            holder: RecyclerView.ViewHolder, position: Int) {
        val delegateAdapter = typeToAdapterMap.get(getItemViewType(position))
        if (delegateAdapter != null) {
           // delegateAdapter.onBindViewHolder(holder, data, position)
        } else {
            throw NullPointerException(
                    "can not find adapter for position " + position)
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder?) {
       // typeToAdapterMap.get(holder!!.itemViewType).onRecycled(holder)
    }

    fun swapData(data: List<Any>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {

        return data.size
    }

    class Builder {

        private var count: Int = 0
        private val typeToAdapterMap: SparseArray<IDelegateAdapter<*>> = SparseArray()

        fun add(delegateAdapter: IDelegateAdapter<*>): Builder {
            typeToAdapterMap.put(count++, delegateAdapter)
            return this
        }

        fun build(): CompositeDelegateAdapter {
            if (count == 0) {
                throw IllegalArgumentException("Register at least one adapter")
            }
            return CompositeDelegateAdapter(typeToAdapterMap)
        }
    }

    companion object {
        private val FIRST_VIEW_TYPE = 0
    }
}