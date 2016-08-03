package com.sunshine.sunxin.plugin;

import android.content.Context;
import android.text.TextUtils;


import com.sunshine.sunxin.plugin.model.PluginInfo;
import com.sunshine.sunxin.plugin.model.PluginRuntimeEnv;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by gyzhong on 15/11/22.
 */
public class PluginCache {


    private HashMap<String, PluginRuntimeEnv> mPluginRuntimeEnvs = new HashMap();
    private ConcurrentHashMap<String, PluginInfo> sMemCache = new ConcurrentHashMap();
    private static PluginCache sInstance = null;
    private File mCachePath;

    private PluginCache(Context context) {
        this.mCachePath = context.getDir(PluginConstant.DIR_CACHE_INFO, 0);
        this.mCachePath.mkdirs();
    }

    public static PluginCache getInstance(Context context){
        if (sInstance == null){
            sInstance = new PluginCache(context);
        }
        return sInstance ;
    }

    public void addPluginRuntimeEnv(PluginRuntimeEnv pluginRuntimeEnv) {
        if (pluginRuntimeEnv != null && pluginRuntimeEnv.pluginInfo != null) {
            String key = pluginRuntimeEnv.pluginInfo.localPath;
            if (mPluginRuntimeEnvs.containsKey(key))
                mPluginRuntimeEnvs.remove(key);
            mPluginRuntimeEnvs.put(key, pluginRuntimeEnv);

        }
    }

    public PluginRuntimeEnv getPluginRuntimeEnv(PluginInfo pluginInfo) {
        if (pluginInfo == null || TextUtils.isEmpty(pluginInfo.localPath)) {
            return null;
        }
        return mPluginRuntimeEnvs.get(pluginInfo.localPath);
    }

    public void updatePluginInfo(String pluginId,PluginInfo pluginInfo){
        sMemCache.put(pluginId,pluginInfo) ;
    }

    public PluginInfo getPluginInfo(String pluginId){
        PluginInfo pluginInfo = null ;
        pluginInfo = sMemCache.get(pluginId) ;
        return pluginInfo ;
    }

}
