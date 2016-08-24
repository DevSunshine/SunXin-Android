package com.sunxin.plugin.login;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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

    static Activity activity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.plugin_temp_layout,container,false) ;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.id_jump).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity = getActivity();
                test(new Runnable() {
                    @Override
                    public void run() {
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    Log.v("zgy", "=====getActivity=========" + getActivity());
                                    Thread.sleep(10000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        getActivity().finish();

                    }
                });
                Intent intent = new Intent(getActivity(), RootPluginActivity.class);
                intent.putExtra(PluginConstant.INTENT_PLUGIN_ID_KEY, "101");
                startActivity(intent);
            }
        });

        getTitleView().setTitle("内存泄漏");
        getTitleView().addRightBtn(R.drawable.ic_cut_white, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }) ;
        getTitleView().addRightBtn(R.drawable.ic_copy_white, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }) ;

        getTitleView().addLeftBtn(R.drawable.ic_back_white, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        }) ;

    }

    private void test(Runnable runnable){
        runnable.run();
    }
}
