package com.alexnikola.supernotes.di.component

import com.alexnikola.supernotes.di.ScopeFragment
import com.alexnikola.supernotes.di.module.FragmentModule
import com.alexnikola.supernotes.ui.notes_list.NoteListFragment

import dagger.Component

@ScopeFragment
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(FragmentModule::class))
interface FragmentComponent {
    fun inject(fragment: NoteListFragment)
}
