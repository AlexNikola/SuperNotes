package com.alexnikola.supernotes.ui.notes_list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.alexnikola.supernotes.R

import com.alexnikola.supernotes.ui.notes_list.NoteListFragment.Callback
import com.alexnikola.supernotes.ui.notes_list.dummy.DummyContent.Note

/**
 * [RecyclerView.Adapter] that can display a [Note] and makes a call to the
 * specified [Callback].
 * TODO: Replace the implementation with code for your data type.
 */
class NoteRecyclerAdapter(private val mValues: List<Note>, private val mListener: Callback?)
    : RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_note, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = mValues[position]
        holder.mIdView.text = mValues[position].id

        holder.mView.setOnClickListener {
            mListener?.onListFragmentInteraction(holder.mItem)
        }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView
        val mContentView: TextView
        var mItem: Note? = null

        init {
            mIdView = mView.findViewById(R.id.id)
            mContentView = mView.findViewById(R.id.content)
        }

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
