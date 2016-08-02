package com.sunshine.sunxin.plugin.model;

import android.content.res.AssetManager;
import android.content.res.Resources;

/**
 * Created by gyzhong on 15/11/22.
 */
public class PluginRuntimeEnv {
    public AssetManager assetManager ;
    public ClassLoader classLoader ;
    public Resources resources ;
    public Resources.Theme theme ;
    public PluginInfo pluginInfo ;

    public PluginRuntimeEnv(AssetManager assetManager, ClassLoader classLoader, Resources resources, Resources.Theme theme, PluginInfo pluginInfo) {
        this.assetManager = assetManager;
        this.classLoader = classLoader;
        this.resources = resources;
        this.theme = theme;
        this.pluginInfo = pluginInfo;
    }
}
