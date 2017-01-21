package com.craig.couchbasesyncexample;

import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Clifton Craig on 1/21/17.
 * Copyright GE 1/21/17
 */
public class MainController {
    private static final String TAG = MainController.class.getName();
    private Manager cbManager;
    private Database database;
    int counter = 0;

    public void onGo() {
        Log.d(TAG, "Begin Go button click handler.");
        final Map<String, Object> document = new HashMap<String, Object>() {{
            put("name", "Cliff");
            put("age", "40");
            put("count", counter++);
        }};
        final Document couchbaseDoc = getDatabase().createDocument();
        try { couchbaseDoc.putProperties(document); }
        catch (CouchbaseLiteException e) { e.printStackTrace(); }
        Log.d(TAG, "Created doc with ID " + couchbaseDoc.getId());
        Log.d(TAG, "Go button click was handled.");
    }

    public void setCBManager(Manager cbmanager) {
        this.cbManager = cbmanager;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public Database getDatabase() {
        return database;
    }
}
