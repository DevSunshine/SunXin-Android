package com.sunshine.plugin.test;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 钟光燕 on 2016/7/22.
 * e-mail guangyanzhong@163.com
 */
public class Test extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String string = getActivity().getResources().getString(R.string.app_name) ;
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
