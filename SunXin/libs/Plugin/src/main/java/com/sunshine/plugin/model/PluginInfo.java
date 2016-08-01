package com.sunshine.plugin.model;

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

    public String MD5;

    public String title;

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
        MD5 = pluginInfo.MD5 ;
        title = pluginInfo.title ;
    }
}
