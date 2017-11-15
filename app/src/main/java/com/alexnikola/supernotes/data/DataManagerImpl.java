package com.alexnikola.supernotes.data;

import com.alexnikola.supernotes.data.DataManager;
import com.alexnikola.supernotes.data.db.DbManager;
import com.alexnikola.supernotes.data.network.ApiManager;
import com.alexnikola.supernotes.data.prefs.PrefsManager;

import javax.inject.Inject;

public class DataManagerImpl implements DataManager {

    private final DbManager dbManager;
    private final PrefsManager prefsManager;
    private final ApiManager apiManager;

    @Inject
    public DataManagerImpl(DbManager dbManager, PrefsManager prefsManager, ApiManager apiManager) {
        this.dbManager = dbManager;
        this.prefsManager = prefsManager;
        this.apiManager = apiManager;
    }
}
