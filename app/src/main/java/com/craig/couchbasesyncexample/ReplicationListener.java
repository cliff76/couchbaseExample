package com.craig.couchbasesyncexample;

/**
 * Created by Clifton Craig on 1/23/17.
 * Copyright GE 1/23/17
 */
public interface ReplicationListener {
    void updatedDocumentCount(int update);

    void status(String status);
}
