package com.sunshine.sunxin.plugin;

import android.support.v4.util.LruCache;
import android.text.TextUtils;

import com.sunshine.sunxin.otto.BusProvider;
import com.sunshine.sunxin.plugin.model.PluginInfo;
import com.sunshine.sunxin.plugin.model.PluginRuntimeEnv;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by gyzhong on 15/11/22.
 */
public class PluginCache {


    private ConcurrentHashMap<String, PluginInfo> sMemCache = new ConcurrentHashMap();
    private Map<String,PluginRuntimeEnv> sPluginRuntimeEnvMap = new HashMap<>() ;
    private static PluginCache sInstance = null;
    private static final RunTimeEnvLruCache RUN_TIME_CACHE = new RunTimeEnvLruCache(12);

    private PluginCache() {

    }

    public static PluginCache getInstance(){
        if (sInstance == null){
            sInstance = new PluginCache();
        }
        return sInstance ;
    }

    public void addPluginRuntimeEnv(PluginRuntimeEnv pluginRuntimeEnv) {
        if (pluginRuntimeEnv != null && pluginRuntimeEnv.pluginInfo != null) {
            if (RUN_TIME_CACHE.get(pluginRuntimeEnv.pluginInfo) != null){
                RUN_TIME_CACHE.remove(pluginRuntimeEnv.pluginInfo.id) ;
            }
            RUN_TIME_CACHE.put(pluginRuntimeEnv.pluginInfo,pluginRuntimeEnv) ;
            if (sPluginRuntimeEnvMap.containsKey(pluginRuntimeEnv.pluginInfo.id)){
                sPluginRuntimeEnvMap.remove(pluginRuntimeEnv.pluginInfo.id) ;
            }
            sPluginRuntimeEnvMap.put(pluginRuntimeEnv.pluginInfo.id,pluginRuntimeEnv) ;
        }
    }

    public PluginRuntimeEnv getPluginRuntimeEnv(PluginInfo pluginInfo) {
        if (pluginInfo == null || TextUtils.isEmpty(pluginInfo.id)) {
            return null;
        }
        return sPluginRuntimeEnvMap.get(pluginInfo.id) ;
//        return RUN_TIME_CACHE.get(pluginInfo);
    }

    public void updatePluginInfo(String pluginId,PluginInfo pluginInfo){
        if (sMemCache.containsKey(pluginId)){
            sMemCache.remove(pluginId) ;
        }
        sMemCache.put(pluginId,pluginInfo) ;
        BusProvider.provide().post(new PluginInfoEvent(pluginInfo));
    }

    public PluginInfo getPluginInfo(String pluginId){
        return sMemCache.get(pluginId) ;
    }

    private static class RunTimeEnvLruCache extends LruCache<String,PluginRuntimeEnv>{
        /**
         * @param maxSize for caches that do not override {@link #sizeOf}, this is
         *                the maximum number of entries in the cache. For all other caches,
         *                this is the maximum sum of the sizes of the entries in this cache.
         */
        public RunTimeEnvLruCache(int maxSize) {
            super(maxSize);
        }

        public PluginRuntimeEnv get(PluginInfo info){
            return get(info.id) ;
        }

        public PluginRuntimeEnv put(PluginInfo info,PluginRuntimeEnv env){
            return put(info.id,env) ;
        }

    }

}
