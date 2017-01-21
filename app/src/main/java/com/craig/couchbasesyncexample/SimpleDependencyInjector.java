package com.craig.couchbasesyncexample;

/**
 * Created by Clifton Craig on 1/21/17.
 * Copyright GE 1/21/17
 */
public class SimpleDependencyInjector implements DependencyInjector {
    @Override
    public void inject(Object rootObject) {
        injectMainActivity((MainActivity)rootObject);
    }

    private void injectMainActivity(MainActivity mainActivity) {
        mainActivity.setAsyncWorker(new SingleThreadWorker());
    }
}
