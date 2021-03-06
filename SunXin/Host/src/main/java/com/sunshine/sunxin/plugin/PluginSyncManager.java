package com.sunshine.sunxin.plugin;

import android.content.Context;
import android.text.TextUtils;

import com.sunshine.sunxin.plugin.model.PluginInfo;
import com.sunshine.sunxin.plugin.model.PluginSyncInfo;
import com.sunshine.sunxin.plugin.model.SyncStatue;


/**
 * Created by gyzhong on 15/11/22.
 */
public class PluginSyncManager {

    private Context mContext ;

    private static PluginSyncManager instance;

    public static PluginSyncManager getInstance(Context context) {
        if (instance == null){
            instance = new PluginSyncManager(context.getApplicationContext());
        }
        return instance;
    }

    private PluginSyncManager(Context context){
        this.mContext = context ;
    }

    public PluginSyncInfo getPluginSyncInfo(String pluginId){
        PluginSyncInfo pluginSyncInfo ;
        if (TextUtils.isEmpty(pluginId)){
            pluginSyncInfo = new PluginSyncInfo(null,pluginId, SyncStatue.ERROR) ;
            return pluginSyncInfo ;
        }
        PluginInfo pluginInfo = PluginCache.getInstance().getPluginInfo(pluginId) ;
        if (pluginInfo != null ){
            pluginSyncInfo = new PluginSyncInfo(pluginInfo,pluginId,SyncStatue.SYNCED) ;
        }else {
            if (PluginApk.installed){
                PluginApk.install(mContext);
            }
            pluginSyncInfo = new PluginSyncInfo(null,pluginId,SyncStatue.WAITING) ;
        }
        return pluginSyncInfo ;
    }

}
