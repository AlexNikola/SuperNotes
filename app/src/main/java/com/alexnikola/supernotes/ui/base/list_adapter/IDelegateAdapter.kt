package com.alexnikola.supernotes.ui.base.list_adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

interface IDelegateAdapter<in VH : RecyclerView.ViewHolder> {

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    fun onBindViewHolder(holder: VH, items: List<Any>, position: Int)

    fun onRecycled(holder: VH)

    fun isForViewType(items: List<Any>, position: Int): Boolean
}