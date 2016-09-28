package com.sunshine.sunxin.plugin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
    private boolean mShowTitle;
    private String mTitle;
    private String mBackTitle;
    private int mTintColor;
    private boolean mTintFull;

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
            mShowTitle = getIntent().getBooleanExtra(PluginConstant.INTENT_SHOW_TITLE_KEY, true);
            mTitle = getIntent().getStringExtra(PluginConstant.INTENT_TITLE_KEY);
            mBackTitle = getIntent().getStringExtra(PluginConstant.INTENT_BACK_TITLE_KEY);
            mTintColor = getIntent().getIntExtra(PluginConstant.INTENT_TINT_COLOR_KEY, -1);
            mTintFull = getIntent().getBooleanExtra(PluginConstant.INTENT_TINT_FULL_KEY, false);
        } else {
            mPluginId = savedInstanceState.getString(PluginConstant.INTENT_PLUGIN_ID_KEY);
            mShowTitle = savedInstanceState.getBoolean(PluginConstant.INTENT_SHOW_TITLE_KEY, true);
            mTitle = savedInstanceState.getString(PluginConstant.INTENT_TITLE_KEY);
            mBackTitle = savedInstanceState.getString(PluginConstant.INTENT_BACK_TITLE_KEY);
            mTintColor = savedInstanceState.getInt(PluginConstant.INTENT_TINT_COLOR_KEY, -1);
            mTintFull = savedInstanceState.getBoolean(PluginConstant.INTENT_TINT_FULL_KEY, false);
        }
        if (!mShowTitle) {
            getTitleView().hide();
        }
        if (!TextUtils.isEmpty(mTitle)) {
            getTitleView().setTitle(mTitle);
        }

        if (!TextUtils.isEmpty(mBackTitle)) {
            getTitleView().addLeftBtn(mBackTitle, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }

        if (mTintColor != -1 && systemBarTintManager.isStatusBarTintEnabled()) {
            systemBarTintManager.setStatusBarTintColor(mTintColor);
        }
        if (mTintFull && systemBarTintManager.isStatusBarTintEnabled()) {
//            setFullTintContext();
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
        paramBundle.putBoolean(PluginConstant.INTENT_SHOW_TITLE_KEY, mShowTitle);
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
            mIsInstallPlug = false;
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
            showLoadingView();
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
            if (pluginSyncInfo.pluginInfo.debug) {
                // TODO: 2016/8/8  等待进度条
                showLoadingView();
                Log.v("zgy", "=======checkInstall==========");
                PluginApk.checkInstall(this, mPluginId);
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
            if (mIsInstallPlug) {
                // TODO: 2016/8/8 安装成功
                hideLoadingView();
            } else {
                // TODO: 2016/8/8 安装失败
            }
        } else {
            // TODO: 2016/8/8 安装失败
        }
    }

    @Subscribe
    public void onPluginInfo(PluginInfoEvent event) {
        if (!mPluginId.equals(event.pluginInfo.id)) {
            return;
        }
        pluginInfo.deepCopy(event.pluginInfo);
        Log.v("zgy", "==========onPluginInfo======" + pluginInfo.crc);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                install();
            }
        });
    }

    @Subscribe
    public void onPluginInstalled(PluginInstalledEvent event) {
        if (pluginInfo == null) {
            PluginInfo info = PluginCache.getInstance().getPluginInfo(mPluginId);
            if (info != null) {
                BusProvider.provide().post(new PluginInfoEvent(info));
            } else {
                Log.v("zgy", "==========没有找到插件======");
                Toast.makeText(getApplicationContext(), "请确保你已经安装了插件!!!", Toast.LENGTH_LONG).show();
            }
        }


    }
}
