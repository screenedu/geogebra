package org.geogebra.common.main;

public class AppProviderImpl extends AppProvider {

    private App app;

    public void setProvider(AppProvider provider) {
        AppProvider.provider = provider;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public App getApp() {
        return app;
    }
}
