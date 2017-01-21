package com.craig.couchbasesyncexample;

/**
 * Created by Clifton Craig on 1/21/17.
 * Copyright GE 1/21/17
 */
public class DependencyInjectorFactory {

    public static DependencyInjector instance() {
        return new SimpleDependencyInjector();
    }
}
