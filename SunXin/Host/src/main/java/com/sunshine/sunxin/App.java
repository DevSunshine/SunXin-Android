package com.sunshine.sunxin;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.sunshine.sunxin.plugin.PluginApk;

/**
 * Created by 钟光燕 on 2016/8/4.
 * e-mail guangyanzhong@163.com
 */
public class App extends Application {

    RefWatcher watcher ;
    public static App instance ;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        PluginApk.install(this);
        instance = this ;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        watcher = LeakCanary.install(this) ;
    }

    public RefWatcher getRefWatcher(){
        return watcher ;
    }
}
