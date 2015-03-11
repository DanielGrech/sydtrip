package com.dgsd.android.sydtrip;

import android.app.Application;

import com.dgsd.android.data.DataSource;
import com.dgsd.android.data.db.RealmDatabaseBackend;
import com.dgsd.android.data.importer.ImportManager;
import com.dgsd.sydtrip.updater.UpdateManager;

import timber.log.Timber;

public class STApp extends Application {

    private UpdateManager updateChecker;
    private ImportManager importManager;
    private DataSource dataSource;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public DataSource getDataSource() {
        if (dataSource == null) {
            dataSource = new DataSource(RealmDatabaseBackend.getInstance(this));
        }

        return dataSource;
    }

    public UpdateManager getUpdateManager() {
        if (updateChecker == null) {
            updateChecker = new UpdateManager(this, BuildConfig.UPDATE_URL_ENDPOINT);
        }

        return updateChecker;
    }

    public ImportManager getImportManager() {
        if (importManager == null) {
            importManager = new ImportManager(getDataSource());
        }
        return importManager;
    }


}
