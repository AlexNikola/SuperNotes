package com.alexnikola.supernotes.ui.base.list_adapter

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseDelegateAdapter<VH : KViewHolder, in T> : IDelegateAdapter<VH> {

    protected abstract fun onBindViewHolder(view: View, item: T, viewHolder: VH)

    protected abstract fun createViewHolder(parent: View): VH

    @LayoutRes
    protected abstract fun getLayoutId(): Int



    override fun onRecycled(holder: VH) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflatedView = LayoutInflater
                .from(parent.context)
                .inflate(getLayoutId(), parent, false)

        val holder = createViewHolder(inflatedView)
        holder.setListener(object : KViewHolder.ItemInflateListener {
            override fun inflated(viewType: Any, view: View) {
                onBindViewHolder(view, viewType as T, holder)
            }
        })
        return holder
    }

    override fun onBindViewHolder(holder: VH, items: List<Any>, position: Int) {
        holder.bind(items[position])
    }
}