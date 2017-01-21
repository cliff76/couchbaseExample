package com.craig.couchbasesyncexample;

import android.content.Context;
import android.support.annotation.NonNull;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;

import java.io.IOException;

/**
 * Created by Clifton Craig on 1/21/17.
 * Copyright GE 1/21/17
 */
public class SimpleDependencyInjector implements DependencyInjector {

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
        mainActivity.getMainController().setCBManager(manager);
        mainActivity.getMainController().setDatabase(getDatabaseForManager(manager));
    }

    private Database getDatabaseForManager(Manager manager) {
        final Database database;
        try { database = manager.getDatabase(getDatabaseName()); }
        catch (CouchbaseLiteException e) { throw new RuntimeException("Could not create database.", e); }
        return database;
    }

    @NonNull
    private String getDatabaseName() {
        return "todo";
    }

    @NonNull
    private Manager getManagerForContext(Context context) {
        final Manager manager;
        try { manager = new Manager(new AndroidContext(context), Manager.DEFAULT_OPTIONS); }
        catch (IOException e) { throw new RuntimeException("Cannot setup Couchbase manager.", e); }
        return manager;
    }
}
