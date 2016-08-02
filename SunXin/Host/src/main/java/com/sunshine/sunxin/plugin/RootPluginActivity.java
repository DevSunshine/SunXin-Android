package com.sunshine.sunxin.plugin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import com.sunshine.sunxin.R;
import com.sunshine.sunxin.plugin.model.PluginInfo;
import com.sunshine.sunxin.plugin.model.PluginSyncInfo;
import com.sunshine.sunxin.plugin.model.SyncStatue;

/**
 * Created by gyzhong on 15/11/22.
 */
public class RootPluginActivity extends BasePluginActivity {

    private boolean mIsInstallPlug = false;
    private PluginSyncManager mPluginSyncManager;
    private String mPluginId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPluginSyncManager = PluginSyncManager.getInstance(getApplicationContext());

        if (savedInstanceState == null){
            mPluginId = getIntent().getStringExtra(PluginConstant.INTENT_PLUGIN_ID_KEY) ;
            if(TextUtils.isEmpty(mPluginId)){
                //error
            }
        }else {
            mPluginId = savedInstanceState.getString(PluginConstant.INTENT_PLUGIN_ID_KEY) ;
        }

        syncPluginById(mPluginId);
    }

    protected void onSaveInstanceState(Bundle paramBundle) {
        super.onSaveInstanceState(paramBundle);
        if (this.pluginInfo != null)
            paramBundle.putString(PluginConstant.INTENT_PLUGIN_ID_KEY, this.mPluginId);
    }

    private void installPlugin() {
        Log.v("zgy","=======installPlugin=========="+mIsInstallPlug) ;
        try {
            if (!isFinishing() && !mIsInstallPlug) {

                String host = getHostFragment();
                if (TextUtils.isEmpty(host))
                    host = pluginInfo.rootFragment;
                Fragment fragment = (Fragment) getClassLoader().loadClass(host).newInstance();
                Bundle bundle = getIntent().getExtras();
                bundle.putSerializable(PluginConstant.INTENT_PLUGIN_INFO_KEY, pluginInfo);
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.id_plugin_root_view, fragment)
                        .commitAllowingStateLoss();
                mIsInstallPlug = true;
                Log.v("zgy","=======mIsInstallPlug=========="+mIsInstallPlug) ;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getHostFragment() {
        if (getIntent() != null && getIntent().getData() != null
                && getIntent().getData().getHost() != null) {
            return getIntent().getData().getHost();
        }
        return null;
    }

    private void syncPluginById(String pluginId) {
        PluginSyncInfo pluginSyncInfo = mPluginSyncManager.getPluginSyncInfo(pluginId);
        if (pluginSyncInfo != null && pluginSyncInfo.syncStatue == SyncStatue.WAITTING) {

        } else {
            syncPlugin(pluginSyncInfo);
        }
    }

    private void syncPlugin(PluginSyncInfo syncInfo) {
        if (mIsInstallPlug) {
            return;
        }
        if (syncInfo == null) {
            //
        }
        if (pluginInfo == null) {
            pluginInfo = new PluginInfo();
        }
        if (syncInfo.syncStatue == SyncStatue.SYNCED) {
            pluginInfo.deepCopy(syncInfo.pluginInfo);
            if (installRuntimeEnv(pluginInfo)) {
                installPlugin();
            }
        }
    }
}
