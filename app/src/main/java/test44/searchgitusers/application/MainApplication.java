package test44.searchgitusers.application;

import android.app.Application;

import test44.searchgitusers.interfaces.RestApis;
import test44.searchgitusers.retrofit.RetroClient;

public class MainApplication extends Application {
    private static MainApplication instance;

    public static MainApplication getAppContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static RestApis getDefaultRestClient() {
        return RetroClient.getInstance().getApiServices();
    }
}


