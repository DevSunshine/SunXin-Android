package com.sunshine.sunxin.plugin;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.sunshine.sunxin.BaseFragment;
import com.sunshine.sunxin.plugin.model.PluginInfo;

/**
 * Created by 钟光燕 on 2016/8/16.
 * e-mail guangyanzhong@163.com
 */
public class BasePluginFragment extends BaseFragment {
    PluginInfo mPluginInfo;

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        if ((getArguments() != null) && (getArguments().containsKey(PluginConstant.INTENT_PLUGIN_INFO_KEY))) {
            PluginInfo localPluginInfo = (PluginInfo) getArguments().getSerializable(PluginConstant.INTENT_PLUGIN_INFO_KEY);
            if (localPluginInfo != null) {
                this.mPluginInfo = localPluginInfo;
            }
        }
    }

    protected void startAndFinishSelf(Class<?> paramClass, Bundle paramBundle) {
        startFragment(paramClass, paramBundle);
//        getActivity().overridePendingTransition(0, 0);
        getActivity().finish();
    }

    protected void startFragment(Class<?> paramClass, Bundle paramBundle) {
        Intent intent = new Intent(getActivity(),RootPluginActivity.class);
        if (!paramBundle.containsKey(PluginConstant.INTENT_PLUGIN_INFO_KEY)) {
            paramBundle.putSerializable(PluginConstant.INTENT_PLUGIN_INFO_KEY, this.mPluginInfo);
        }
        intent.putExtras(paramBundle);
        intent.setData(Uri.parse("SunXin://" + paramClass.getName()));
        getActivity().startActivity(intent);
    }

}
