package com.sunshine.plugin.model;


import java.io.Serializable;

/**
 * Created by gyzhong on 15/11/22.
 */
public class PluginSyncInfo implements Serializable{

    public PluginInfo pluginInfo ;

    public String pluginId ;

    public SyncStatue syncStatue ;

    public PluginSyncInfo(PluginInfo pluginInfo, String pluginId, SyncStatue syncStatue) {
        this.pluginInfo = pluginInfo;
        this.pluginId = pluginId;
        this.syncStatue = syncStatue;
    }
}
