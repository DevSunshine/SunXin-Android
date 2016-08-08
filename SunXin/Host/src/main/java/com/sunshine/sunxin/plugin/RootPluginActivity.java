package com.sunshine.sunxin.plugin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import com.squareup.otto.Subscribe;
import com.sunshine.sunxin.R;
import com.sunshine.sunxin.otto.BusProvider;
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
        /**
         * 注册监听
         */
        BusProvider.provide().register(this);

        mPluginSyncManager = PluginSyncManager.getInstance(getApplicationContext());
        if (savedInstanceState == null) {
            mPluginId = getIntent().getStringExtra(PluginConstant.INTENT_PLUGIN_ID_KEY);
        } else {
            mPluginId = savedInstanceState.getString(PluginConstant.INTENT_PLUGIN_ID_KEY);
        }
        syncPluginById(mPluginId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**
         * 注销监听
         */
        BusProvider.provide().unregister(this);
    }

    protected void onSaveInstanceState(Bundle paramBundle) {
        super.onSaveInstanceState(paramBundle);
        if (this.pluginInfo != null)
            paramBundle.putString(PluginConstant.INTENT_PLUGIN_ID_KEY, this.mPluginId);
    }

    private void installPlugin() {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
            mIsInstallPlug = false ;
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
        if (pluginSyncInfo.syncStatue == SyncStatue.WAITING) {
            // TODO: 2016/8/8  等待进度条
        } else if (pluginSyncInfo.syncStatue == SyncStatue.ERROR) {
            Log.v("zgy", "=======mPluginId==========" + mPluginId);
            finish();
        } else {
            if (mIsInstallPlug) {
                return;
            }
            if (pluginInfo == null) {
                pluginInfo = new PluginInfo();
            }
            if (pluginSyncInfo.pluginInfo.sdcard) {
                // TODO: 2016/8/8  等待进度条
                PluginApk.checkChange(this, mPluginId);
            } else {
                pluginInfo.deepCopy(pluginSyncInfo.pluginInfo);
                install();
            }
        }
    }

    public void install() {
//        BusProvider.provide().unregister(this);
        if (installRuntimeEnv(pluginInfo)) {
            installPlugin();
            if (mIsInstallPlug){
                // TODO: 2016/8/8 安装成功
            }else {
                // TODO: 2016/8/8 安装失败
            }
        }else {
            // TODO: 2016/8/8 安装失败
        }
    }

    @Subscribe
    public void onPluginInfo(PluginInfoEvent event) {
        pluginInfo.deepCopy(event.pluginInfo);
        Log.v("zgy", "==========onPluginInfo======" + pluginInfo.crc);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                install();
            }
        });
    }
}
