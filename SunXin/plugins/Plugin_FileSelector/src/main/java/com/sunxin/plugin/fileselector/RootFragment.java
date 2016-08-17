package com.sunxin.plugin.fileselector;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sunshine.sunxin.plugin.BasePluginFragment;

/**
 * Created by gyzhong on 16/8/14.
 * 文件选择器
 */
public class RootFragment extends BasePluginFragment {
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();

        if (bundle == null) {
            bundle = new Bundle();
        }
        if (bundle.containsKey("test")) {
//            startAndFinishSel
            startAndFinishSelf(FileBrowserFragment.class,bundle);
        }else {
            startAndFinishSelf(FileBrowserFragment.class,bundle);
        }
    }
}
