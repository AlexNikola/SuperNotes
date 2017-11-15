package com.alexnikola.supernotes.ui.notes_list

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.alexnikola.supernotes.R
import com.alexnikola.supernotes.ui.base.BaseFragment
import com.alexnikola.supernotes.ui.notes_list.dummy.DummyContent
import com.alexnikola.supernotes.ui.notes_list.dummy.DummyContent.Note
import kotlinx.android.synthetic.main.fragment_note_list.view.*
import javax.inject.Inject

class NoteListFragment : BaseFragment(), NotesMvpView {

    private val columnCount = 1

    private var callback: Callback? = null

    @Inject
    lateinit var presenter: NotesListPresenter<NotesMvpView>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as? Callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentComponent.inject(this)
        partialSyncPresenterLifeCycle(presenter)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_note_list, container, false)

        view.recyclerView.layoutManager = GridLayoutManager(view.context, columnCount)
        view.recyclerView.adapter = NoteRecyclerAdapter(DummyContent.NOTES, callback)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onAttachView(this)
    }




    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    interface Callback {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: Note?)
    }

    companion object {
        val TAG = "NoteListFragment"
    }
}
