package com.alexnikola.supernotes.ui.base;

import java.util.HashMap;

import javax.inject.Inject;

public class ViewStateManagerImpl implements ViewStateManager {

    private final HashMap<Integer, ViewState> viewStates;

    @Inject
    public ViewStateManagerImpl(HashMap<Integer, ViewState> viewStates) {
        this.viewStates = viewStates;
    }

    @Override
    public void applyViewStates() {

    }

    @Override
    public void addAndApplyViewState() {

    }

    @Override
    public void clear() {

    }
}
