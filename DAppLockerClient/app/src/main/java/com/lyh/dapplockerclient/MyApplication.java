package com.lyh.dapplockerclient;

import android.app.Application;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        GreenDaoManager.getInstance().init(getApplicationContext());
    }
}
