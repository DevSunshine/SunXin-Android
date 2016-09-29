package com.sunshine.sunxin.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunshine.sunxin.BaseFragment;
import com.sunshine.sunxin.R;
import com.sunshine.sunxin.view.TitleView;


// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

/**
 * Created by 钟光燕 on 2016/9/29.
 * e-mail guangyanzhong@163.com
 * 首页中，消息页面
 */

public class TabMsgFragment extends BaseFragment {

    private TitleView mTitle ;
    public static TabMsgFragment newInstance(){
        TabMsgFragment fragment = new TabMsgFragment() ;
        return fragment ;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_msg_fragment,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitle = (TitleView) view.findViewById(R.id.id_tab_msg_title);
        initTitle() ;
    }

    private void initTitle() {
        mTitle.setTitle("消息") ;
        mTitle.addRightBtn(R.drawable.btn_more, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }) ;
    }

    @Override
    public void fragmentSelect() {

    }
}
