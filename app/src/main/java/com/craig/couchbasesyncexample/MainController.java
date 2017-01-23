package com.craig.couchbasesyncexample;

import android.support.annotation.NonNull;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.auth.Authenticator;
import com.couchbase.lite.auth.AuthenticatorFactory;
import com.couchbase.lite.replicator.Replication;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Clifton Craig on 1/21/17.
 * Copyright GE 1/21/17
 */
public class MainController {
    private static final String TAG = MainController.class.getName();
    public static final String USERNAME = "cliffuser1";
    public static final String PASSWORD = "pass";
    private Manager cbManager;
    private Database database;
    int counter = 0;
    private URL url;
    private ReplicationListener replicationListener;

    public MainController() {
        enableLogging();
    }

    public void onGo() {
        Log.d(TAG, "Begin Create button click handler.");
        final Map<String, Object> document = new HashMap<String, Object>() {{
            put("name", "Cliff");
            put("age", "40");
            put("count", counter++);
        }};
        final Document couchbaseDoc = getDatabase().createDocument();
        try { couchbaseDoc.putProperties(document); }
        catch (CouchbaseLiteException e) { e.printStackTrace(); }
        Log.d(TAG, "Created doc with ID " + couchbaseDoc.getId());
        Log.d(TAG, "Create button click was handled.");
    }


    private void enableLogging() {
        Manager.enableLogging(TAG, Log.VERBOSE);
        Manager.enableLogging(com.couchbase.lite.util.Log.TAG, Log.VERBOSE);
        Manager.enableLogging(com.couchbase.lite.util.Log.TAG_SYNC_ASYNC_TASK, Log.VERBOSE);
        Manager.enableLogging(com.couchbase.lite.util.Log.TAG_SYNC, Log.VERBOSE);
        Manager.enableLogging(com.couchbase.lite.util.Log.TAG_QUERY, Log.VERBOSE);
        Manager.enableLogging(com.couchbase.lite.util.Log.TAG_VIEW, Log.VERBOSE);
        Manager.enableLogging(com.couchbase.lite.util.Log.TAG_DATABASE, Log.VERBOSE);
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

    public void onSync() {
        startReplication();
    }

    private void startReplication() {
        Log.d(TAG, "Begin Sync button click handler.");
        Log.d(TAG, "Replicating to " + getDBUrl());
        final Replication pullReplication = getDatabase().createPullReplication(getDBUrl());
        final Replication pushReplication = getDatabase().createPushReplication(getDBUrl());
        pullReplication.setContinuous(true);
        pushReplication.setContinuous(true);
        final Authenticator basicAuthenticator = AuthenticatorFactory.createBasicAuthenticator(USERNAME, PASSWORD);
        pullReplication.setAuthenticator(basicAuthenticator);
        pushReplication.setAuthenticator(basicAuthenticator);
        pullReplication.start();
        pushReplication.start();
        pullReplication.addChangeListener(new Replication.ChangeListener() {
            @Override
            public void changed(Replication.ChangeEvent event) {
                getReplicationListener().updatedDocumentCount(database.getDocumentCount());
                String status = "Completed changes: " + event.getCompletedChangeCount() + "\n"
                        + "Changes: " + event.getChangeCount();
                if(event.getError()!=null) {
                    status += "\nError: " + event.getError().getLocalizedMessage();
                }
                getReplicationListener().status(status);
            }
        });
        Log.d(TAG, "End Sync button click handler.");
    }

    @NonNull
    private URL getDBUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public ReplicationListener getReplicationListener() {
        return replicationListener;
    }

    public void setReplicationListener(ReplicationListener replicationListener) {
        this.replicationListener = replicationListener;
    }
}
