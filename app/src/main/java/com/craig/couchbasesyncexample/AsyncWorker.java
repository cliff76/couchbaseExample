package com.craig.couchbasesyncexample;

/**
 * Created by Clifton Craig on 1/21/17.
 * Copyright GE 1/21/17
 */
public interface AsyncWorker {
    void doAsync(Runnable work);
}
