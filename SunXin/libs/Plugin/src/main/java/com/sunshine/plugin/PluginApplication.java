package com.sunshine.plugin;

import android.app.Application;

import com.sunshine.plugin.PluginSyncUtil;

/**
 * Created by gyzhong on 15/11/22.
 */
public class PluginApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        onInit();
    }

    private void onInit(){
        PluginSyncUtil.getInstance(this).syncInit();
    }
}
