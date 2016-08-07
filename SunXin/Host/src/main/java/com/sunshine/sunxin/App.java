package com.sunshine.sunxin;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.app.Application;

import com.sunshine.sunxin.plugin.PluginApk;

/**
 * Created by 钟光燕 on 2016/8/4.
 * e-mail guangyanzhong@163.com
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PluginApk.install(this);
    }
}
