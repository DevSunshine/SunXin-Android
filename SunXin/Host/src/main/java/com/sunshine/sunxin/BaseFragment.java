package com.sunshine.sunxin;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.sunshine.sunxin.view.TitleView;

/**
 * Created by 钟光燕 on 2016/8/4.
 * e-mail guangyanzhong@163.com
 */
public class BaseFragment extends Fragment {

    public TitleView getTitleView(){
        BaseActivity baseActivity = (BaseActivity) getActivity();
        return baseActivity.getTitleView() ;
    }

    public Activity getHostActivity(){
        BaseActivity baseActivity = (BaseActivity) getActivity();
        return baseActivity ;
    }
}
