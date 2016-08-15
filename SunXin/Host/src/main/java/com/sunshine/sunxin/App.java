package com.sunshine.sunxin;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Process;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.sunshine.sunxin.logcat.SunXinCrashHandler;
import com.sunshine.sunxin.plugin.PluginApk;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
    }

    @Override
    public void onCreate() {
        super.onCreate();
        watcher = LeakCanary.install(this) ;
        Thread.setDefaultUncaughtExceptionHandler(new SunXinCrashHandler());
        instance = this ;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public RefWatcher getRefWatcher(){
        return watcher ;
    }

    private ThreadPoolExecutor mThreadPool = new ThreadPoolExecutor(0, 2, 2, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(), THREAD_FACTORY);
    public static final ThreadFactory THREAD_FACTORY = new ThreadFactory() {
        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setPriority(Process.THREAD_PRIORITY_DEFAULT+Process.THREAD_PRIORITY_LESS_FAVORABLE);
            return thread;
        }
    };

    public static void runBackground(Runnable runnable) {
        instance.mThreadPool.execute(runnable);
    }

    public static boolean removeTask(Runnable runnable) {
        return instance.mThreadPool.remove(runnable);
    }

    public static Resources getAppResources()
    {
        return instance.getResources();
    }
}
