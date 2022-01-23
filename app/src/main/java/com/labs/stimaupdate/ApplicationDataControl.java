package com.labs.stimaupdate;

import android.app.Application;

import com.backendless.Backendless;

public class ApplicationDataControl extends Application {

    public static final String APPLICATION_ID = "BAD55F6E-C68F-0201-FFCE-6DA647782F00";
    public static final String API_KEY = "EC377614-569A-42D3-A8F9-40E881EBC56B";
    public static final String SERVER_URL = "https://eu-api.backendless.com";

    @Override
    public void onCreate() {
        super.onCreate();
        Backendless.initApp(getApplicationContext(), APPLICATION_ID, API_KEY);
        Backendless.setUrl(SERVER_URL);
    }
}
