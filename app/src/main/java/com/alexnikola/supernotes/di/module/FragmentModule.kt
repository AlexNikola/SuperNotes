package com.alexnikola.supernotes.di.module

import android.support.v4.app.Fragment

import com.alexnikola.supernotes.di.ScopeFragment
import com.alexnikola.supernotes.ui.base.ViewState
import com.alexnikola.supernotes.ui.base.ViewStateManager
import com.alexnikola.supernotes.ui.base.ViewStateManagerImpl
import com.alexnikola.supernotes.ui.notes_list.NotesListPresenter
import com.alexnikola.supernotes.ui.notes_list.NotesListPresenterImpl
import com.alexnikola.supernotes.ui.notes_list.NotesMvpView
import dagger.Module

import java.util.HashMap

import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class FragmentModule(private val fragment: Fragment) {

    @Provides
    @ScopeFragment
    internal fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()

    @Provides
    @ScopeFragment
    internal fun provideViewStatesManager(viewStateManager: ViewStateManagerImpl): ViewStateManager = viewStateManager

    @Provides
    @ScopeFragment
    internal fun provideViewStates(): HashMap<Int, ViewState> = HashMap()

    @Provides
    @ScopeFragment
    internal fun provideNoteListPresenter(presenter: NotesListPresenterImpl<NotesMvpView>): NotesListPresenter<NotesMvpView> = presenter
}
