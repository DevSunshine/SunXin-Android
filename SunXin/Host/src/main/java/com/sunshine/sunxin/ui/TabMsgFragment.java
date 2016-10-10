package com.sunshine.sunxin.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunshine.sunxin.BaseFragment;
import com.sunshine.sunxin.R;
import com.sunshine.sunxin.beans.msg.MsgMenuItem;
import com.sunshine.sunxin.plugin.PluginConstant;
import com.sunshine.sunxin.plugin.RootPluginActivity;
import com.sunshine.sunxin.view.TabMsgPopView;
import com.sunshine.sunxin.view.TitleView;

import java.util.ArrayList;
import java.util.List;


// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

/**
 * Created by 钟光燕 on 2016/9/29.
 * e-mail guangyanzhong@163.com
 * 首页中，消息页面
 */

public class TabMsgFragment extends BaseFragment implements TabMsgPopView.onItemClickListener {

    private TitleView mTitle;
    private TabMsgPopView mMsgPopView;

    public static TabMsgFragment newInstance() {
        TabMsgFragment fragment = new TabMsgFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_msg_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitle = (TitleView) view.findViewById(R.id.id_tab_msg_title);
        initPopView();
        initTitle();

    }

    private void initPopView() {
        List<MsgMenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MsgMenuItem(R.drawable.menu_call_chat, "发起聊天"));
        menuItems.add(new MsgMenuItem(R.drawable.menu_add_friends, "添加好友"));
        menuItems.add(new MsgMenuItem(R.drawable.menu_scan, "扫一扫"));
        menuItems.add(new MsgMenuItem(R.drawable.menu_file_trans, "文件直传"));
        mMsgPopView = new TabMsgPopView(getContext());
        mMsgPopView.setConfiguration(new TabMsgPopView.Configuration(0, 4))
                .setMenuItems(menuItems);

    }

    private void initTitle() {
        mTitle.setTitle("消息");
        mTitle.addRightBtn(R.drawable.btn_more, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMsgPopView.show(mTitle, TabMsgFragment.this);
            }
        });
    }

    @Override
    public void fragmentSelect() {

    }

    @Override
    public void clickItem(int position) {
        Log.v("position", "=================position==========" + position);
        mMsgPopView.dismiss();
        Intent intent = new Intent(getContext(), RootPluginActivity.class);
        switch (position) {
            case 0:
                intent.putExtra(PluginConstant.INTENT_PLUGIN_ID_KEY, "101");
                intent.putExtra(PluginConstant.INTENT_SHOW_TITLE_KEY, false);
                intent.putExtra(PluginConstant.INTENT_TINT_COLOR_KEY, 0xffe2e3e7);
                startActivity(intent);
                break;
            case 1:
                intent.putExtra(PluginConstant.INTENT_PLUGIN_ID_KEY, "102");
                intent.putExtra(PluginConstant.INTENT_SHOW_TITLE_KEY, false);
                intent.putExtra(PluginConstant.INTENT_TINT_FULL_KEY, true);
                intent.putExtra(PluginConstant.INTENT_TINT_COLOR_KEY, 0x44000000);
                startActivity(intent);
                break;
            case 2:
                intent.putExtra(PluginConstant.INTENT_PLUGIN_ID_KEY, "103");
                startActivity(intent);
                break;
            case 3:

                intent.putExtra(PluginConstant.INTENT_PLUGIN_ID_KEY, "106");
                startActivity(intent);
                break;
            case 4:
                intent.putExtra(PluginConstant.INTENT_PLUGIN_ID_KEY, "105");
                intent.putExtra(PluginConstant.INTENT_SHOW_TITLE_KEY, false);
                startActivity(intent);
                break;
        }
    }
}
