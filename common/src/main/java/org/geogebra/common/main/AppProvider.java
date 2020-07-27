package org.geogebra.common.main;

public abstract class AppProvider {

    protected static AppProvider provider;

    public static AppProvider get() {
        return provider;
    }

    public abstract App getApp();
}
