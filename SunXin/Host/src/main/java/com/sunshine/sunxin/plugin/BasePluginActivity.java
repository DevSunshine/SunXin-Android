package com.sunshine.sunxin.plugin;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.sunshine.sunxin.R;
import com.sunshine.sunxin.base.BaseActivity;
import com.sunshine.sunxin.plugin.model.PluginInfo;
import com.sunshine.sunxin.plugin.model.PluginRuntimeEnv;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * Created by gyzhong on 15/11/22.
 */
public abstract class BasePluginActivity extends BaseActivity {

    protected PluginInfo pluginInfo;
    protected FrameLayout rootView;
    protected PluginCache pluginCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pluginCache = PluginCache.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        if (getIntent().getExtras().containsKey(PluginConstant.INTENT_PLUGIN_INFO_KEY)) {
            pluginInfo = (PluginInfo) getIntent().getExtras().getSerializable(PluginConstant.INTENT_PLUGIN_INFO_KEY);
            installRuntimeEnv(pluginInfo);
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle paramBundle) {
        super.onRestoreInstanceState(paramBundle);
        if (this.pluginInfo == null) {
            this.pluginInfo = ((PluginInfo) paramBundle.getSerializable(PluginConstant.INTENT_PLUGIN_INFO_KEY));
            installRuntimeEnv(this.pluginInfo);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle paramBundle) {
        super.onSaveInstanceState(paramBundle);
        if (this.pluginInfo != null)
            paramBundle.putSerializable(PluginConstant.INTENT_PLUGIN_INFO_KEY, this.pluginInfo);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    protected View getContentView() {
        this.rootView = new FrameLayout(this);
        if ((Build.VERSION.SDK_INT >= 14) && (needFitsSystemWindows()))
            this.rootView.setFitsSystemWindows(false);
        this.rootView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        this.rootView.setId(R.id.id_plugin_root_view);
        return this.rootView;
    }

    protected boolean installRuntimeEnv(PluginInfo pluginInfo) {
        boolean installRuntimeEnv;
        try {
            if (pluginCache.getPluginRuntimeEnv(pluginInfo) != null && !pluginInfo.debug) {
                installRuntimeEnv = true;
            } else {
                File localDex = getDir(PluginConstant.DIR_PLUGIN_OUT_DEX, Context.MODE_PRIVATE);
                localDex.mkdirs();
                String localDexPath = localDex.getAbsolutePath();
                DexClassLoader dexClassLoader = new DexClassLoader(pluginInfo.localPath, localDexPath, null, getClassLoader());
                AssetManager assetManager = AssetManager.class.newInstance();
                Class assetClass = assetManager.getClass();
                Method method = assetClass.getMethod("addAssetPath", String.class);
                method.invoke(assetManager, getPackageResourcePath());
                method.invoke(assetManager, pluginInfo.localPath);
                Resources resources = new Resources(assetManager, getResources().getDisplayMetrics(),
                        getResources().getConfiguration());
                Resources.Theme theme = resources.newTheme();
//                theme.setTo(super.getTheme());
                theme.applyStyle(R.style.AppTheme, true);
//                PluginUtil.setField(getApplicationContext(), "mResources", resources);
//                PluginUtil.setField(BasePluginActivity.this, "mResources", null);
//                PluginUtil.setField(this, "mResources", resources);
                PluginRuntimeEnv pluginRuntimeEnv = new PluginRuntimeEnv(assetManager, dexClassLoader,
                        resources, theme, pluginInfo);
                pluginCache.addPluginRuntimeEnv(pluginRuntimeEnv);
                installRuntimeEnv = true;
                Log.v("zgy", "==========installRuntimeEnv======" + pluginInfo.localPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
            installRuntimeEnv = false;
        }
        Log.v("zgy", "==========installRuntimeEnv==2====" + installRuntimeEnv);
        return installRuntimeEnv;
    }

    private PluginRuntimeEnv getPluginRuntimeEnv() {
        if (pluginInfo == null) {
            return null;
        } else {
            return pluginCache.getPluginRuntimeEnv(pluginInfo);
        }
    }

    @Override
    public Resources.Theme getTheme() {
        return getPluginRuntimeEnv() == null ? super.getTheme()
                : getPluginRuntimeEnv().theme;
//        return TestAppCom.theme ;
    }

    @Override
    public Resources getResources() {
        return getPluginRuntimeEnv() == null ? super.getResources()
                : getPluginRuntimeEnv().resources;
//        return TestAppCom.resources ;
    }

    @Override
    public AssetManager getAssets() {
        return getPluginRuntimeEnv() == null ? super.getAssets()
                : getPluginRuntimeEnv().assetManager;
    }

    public ClassLoader getClassLoader() {
        return getPluginRuntimeEnv() == null ? super.getClassLoader()
                : getPluginRuntimeEnv().classLoader;
    }

    public boolean needFitsSystemWindows() {
        return true;
    }

    protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
        super.onActivityResult(paramInt1, paramInt2, paramIntent);
        Fragment localFragment = getSupportFragmentManager().findFragmentById(R.id.id_plugin_root_view);
        if (localFragment != null)
            localFragment.onActivityResult(paramInt1, paramInt2, paramIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (pluginInfo.debug){
//            File dir = getDir(PluginConstant.DIR_PLUGIN_OUT_DEX, Context.MODE_PRIVATE);
//            String name = pluginInfo.localPath.substring(pluginInfo.localPath.lastIndexOf("/"),
//                    pluginInfo.localPath.length()) ;
//            name = name.replace(".zip",".dex") ;
//            File dex = new File(dir,name) ;
//            Log.v("plugin", "======dex=====" + dex.getAbsolutePath());
//            boolean delete = dex.delete() ;
//            Log.v("plugin","======delete====="+delete);
//        }
    }
}
