package com.sunshine.sunxin.plugin.model;

import java.io.Serializable;

/**
 * Created by gyzhong on 15/11/22.
 */
public class PluginInfo implements Serializable {

    public String id;

    public String path;

    public String localPath;

    public String rootFragment;

    public String version;


    public String title;

    public long crc ;

    public boolean debug;

    public String debugKey ;

    public boolean checkEqual(PluginInfo pluginInfo) {
        if (pluginInfo == null)
            return false ;
        return id.equals(pluginInfo.id) ;
    }

    public boolean checkVersion(PluginInfo pluginInfo) {

        if (!checkEqual(pluginInfo)) {
            return false ;
        }
        return version.equals(pluginInfo.version) ;
    }

    public void deepCopy(PluginInfo pluginInfo){
        id = pluginInfo.id ;
        path = pluginInfo.path ;
        localPath = pluginInfo.localPath ;
        rootFragment = pluginInfo.rootFragment ;
        version = pluginInfo.version ;
        title = pluginInfo.title ;
        crc = pluginInfo.crc ;
        debug = pluginInfo.debug;
        debugKey = pluginInfo.debugKey;
    }

    @Override
    public String toString() {
        return "PluginInfo :{\n id:"+id+"\n path:"+path+"\n localPath:"+localPath+"\n}";
    }
}
