package com.sunxin.plugin.login;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunshine.sunxin.BaseFragment;
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
        return inflater.inflate(R.layout.plugin_temp_layout,container,false) ;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getTitleView().setVisibility(View.VISIBLE);
        view.findViewById(R.id.id_jump).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().finish();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(10000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    }
                });
                Intent intent = new Intent(getActivity(),RootPluginActivity.class) ;
                intent.putExtra(PluginConstant.INTENT_PLUGIN_ID_KEY, "101") ;
                startActivity(intent);
            }
        });
    }

    private void test(Runnable runnable){
        runnable.run();
    }
}
