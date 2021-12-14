package com.labs.stimaupdate;

import android.app.Application;

import com.backendless.Backendless;

public class ApplicationDataControl extends Application {

    public static final String APPLICATION_ID = "EFC54068-E661-7FD2-FF0B-04B760F24400";
    public static final String API_KEY = "0960F547-C649-474D-8AFB-A42EB1268642";
    public static final String SERVER_URL = "https://eu-api.backendless.com";

    @Override
    public void onCreate() {
        super.onCreate();
        Backendless.initApp(getApplicationContext(), APPLICATION_ID, API_KEY);
        Backendless.setUrl(SERVER_URL);
    }
}
