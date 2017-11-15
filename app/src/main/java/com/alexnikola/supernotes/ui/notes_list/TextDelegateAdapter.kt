package com.alexnikola.supernotes.ui.notes_list

import android.view.View
import com.alexnikola.supernotes.ui.base.list_adapter.BaseDelegateAdapter
import com.alexnikola.supernotes.ui.base.list_adapter.KViewHolder
import com.alexnikola.supernotes.ui.notes_list.dummy.DummyContent

class TextDelegateAdapter : BaseDelegateAdapter<KViewHolder, DummyContent.TextNote>() {

    override fun onBindViewHolder(view: View, item: DummyContent.TextNote, viewHolder: KViewHolder) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createViewHolder(parent: View): KViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLayoutId(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}