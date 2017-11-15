package com.alexnikola.supernotes.ui.base;

import android.content.Intent;
import android.support.annotation.Nullable;

import com.alexnikola.supernotes.data.DataManager;
import com.alexnikola.supernotes.schedulers.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class BasePresenterImpl<V extends BaseMvpView> implements BasePresenter<V> {

    @Nullable
    private V mvpView;

    private final DataManager dataManager;

    private final CompositeDisposable compositeDisposable;

    private final SchedulerProvider schedulerProvider;

    private final ViewStateManager viewStateManager;

    @Inject
    public BasePresenterImpl(DataManager dataManager,
                             CompositeDisposable compositeDisposable,
                             SchedulerProvider schedulerProvider,
                             ViewStateManager viewStateManager) {
        this.dataManager = dataManager;
        this.compositeDisposable = compositeDisposable;
        this.schedulerProvider = schedulerProvider;
        this.viewStateManager = viewStateManager;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onAttachView(V mvpView) {
        this.mvpView = mvpView;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDetachView() {
        mvpView = null;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }




    @Nullable
    protected V getMvpView() {
        return mvpView;
    }

    protected boolean isViewAttached() {
        return mvpView != null;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }

    public SchedulerProvider getSchedulerProvider() {
        return schedulerProvider;
    }

    public ViewStateManager getViewStateManager() {
        return viewStateManager;
    }
}
