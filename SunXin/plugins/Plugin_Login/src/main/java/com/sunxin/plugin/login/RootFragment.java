package com.sunxin.plugin.login;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunshine.sun.lib.socket.SSResponse;
import com.sunshine.sun.lib.socket.request.SSITaskListener;
import com.sunshine.sun.lib.socket.request.SSTask;
import com.sunshine.sunxin.BaseFragment;
import com.sunshine.sunxin.TestJava;
import com.sunshine.sunxin.plugin.PluginConstant;
import com.sunshine.sunxin.plugin.RootPluginActivity;

/**
 * Created by 钟光燕 on 2016/8/4.
 * e-mail guangyanzhong@163.com
 */
public class RootFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.plugin_login_layout,container,false) ;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getTitleView().hide();
        view.findViewById(R.id.id_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestJava.login("jxxfzgy", "jxxfzgy", new SSITaskListener() {
                    @Override
                    public void onProgress(SSTask ssTask, int i, int i1) {

                    }

                    @Override
                    public void onComplete(SSTask ssTask, SSResponse ssResponse) {

                    }

                    @Override
                    public void onError(SSTask ssTask, int i) {

                    }
                });
            }
        });
    }
}