package com.alexnikola.supernotes.ui.notes_list;

import android.graphics.BitmapFactory;

import com.alexnikola.supernotes.data.DataManager;
import com.alexnikola.supernotes.schedulers.SchedulerProvider;
import com.alexnikola.supernotes.ui.base.BasePresenterImpl;
import com.alexnikola.supernotes.ui.base.ViewStateManager;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class NotesListPresenterImpl<V extends NotesMvpView> extends BasePresenterImpl<V> implements NotesListPresenter<V> {

    @Inject
    public NotesListPresenterImpl(DataManager dataManager,
                                  CompositeDisposable compositeDisposable,
                                  SchedulerProvider schedulerProvider,
                                  ViewStateManager viewStateManager) {
        super(dataManager, compositeDisposable, schedulerProvider, viewStateManager);

    }
}
