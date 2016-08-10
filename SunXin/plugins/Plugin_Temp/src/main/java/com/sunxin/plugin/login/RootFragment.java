package com.sunxin.plugin.login;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunshine.sunxin.BaseFragment;

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
        getTitleView().hide();
        view.findViewById(R.id.id_jump).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity = getActivity() ;
                test(new Runnable() {
                    @Override
                    public void run() {
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    Log.v("zgy","=====getActivity========="+getActivity()) ;
                                    Thread.sleep(10000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        getActivity().finish();

                    }
                });
//                Intent intent = new Intent(getActivity(),RootPluginActivity.class) ;
//                intent.putExtra(PluginConstant.INTENT_PLUGIN_ID_KEY, "101") ;
//                startActivity(intent);
            }
        });
    }

    private void test(Runnable runnable){
        runnable.run();
    }
}
