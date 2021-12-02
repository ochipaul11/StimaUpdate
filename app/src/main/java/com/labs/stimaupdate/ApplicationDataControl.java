package com.labs.stimaupdate;

import android.app.Application;

import com.backendless.Backendless;

public class ApplicationDataControl extends Application {

    public static final String APPLICATION_ID = "437021B4-73DE-8326-FF9B-63E739DE4C00";
    public static final String API_KEY = "F511B368-B865-40EA-A85C-D6F8288EC945";
    public static final String SERVER_URL = "https://api.backendless.com";

    @Override
    public void onCreate() {
        super.onCreate();
        Backendless.initApp(getApplicationContext(), APPLICATION_ID, API_KEY);
        Backendless.setUrl(SERVER_URL);
    }
}
