package com.craig.couchbasesyncexample;

import android.content.Context;
import android.support.annotation.NonNull;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Clifton Craig on 1/21/17.
 * Copyright GE 1/21/17
 */
public class SimpleDependencyInjector implements DependencyInjector {

    public static final String DBHOST = "10.15.21.6";
    public static final String DBPORT = "4984";
    public static final String DBNAME = "pm";
    private final SingleThreadWorker asyncWorker;

    public SimpleDependencyInjector() {
        asyncWorker = new SingleThreadWorker();
    }

    @Override
    public void inject(Object rootObject) {
        injectMainActivity((MainActivity)rootObject);
    }

    private void injectMainActivity(final MainActivity mainActivity) {
        asyncWorker.doAsync(new Runnable() {
            public void run() {
                doInjection(mainActivity);
            }
        });
    }

    private void doInjection(MainActivity mainActivity) {
        mainActivity.setAsyncWorker(asyncWorker);
        final Manager manager = getManagerForContext(mainActivity);
        injectMainController(manager, mainActivity, mainActivity.getMainController());
    }

    private void injectMainController(Manager manager, MainActivity mainActivity, MainController mainController) {
        mainController.setCBManager(manager);
        mainController.setDatabase(getDatabaseForManager(manager));
        mainController.setUrl(getDBUrl());
        mainController.setReplicationListener(mainActivity);
    }

    @NonNull
    private URL getDBUrl() {
        final String dbURLString = "http://" + DBHOST + ":" + DBPORT + "/" + DBNAME;
        try { return new URL(dbURLString); }
        catch (MalformedURLException e) { throw new RuntimeException("Could not create DB URL string " + dbURLString, e); }
    }

    private Database getDatabaseForManager(Manager manager) {
        final Database database;
        try {
            database = manager.getDatabase(DBNAME); }
        catch (CouchbaseLiteException e) { throw new RuntimeException("Could not create database.", e); }
        return database;
    }

    @NonNull
    private Manager getManagerForContext(Context context) {
        final Manager manager;
        try { manager = new Manager(new AndroidContext(context), Manager.DEFAULT_OPTIONS); }
        catch (IOException e) { throw new RuntimeException("Cannot setup Couchbase manager.", e); }
        return manager;
    }
}
