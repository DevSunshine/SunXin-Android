package com.sunxin.plugin.fileselector;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunshine.sunxin.BaseFragment;
import com.sunxin.plugin.fileselecter.R;
import com.sunxin.plugin.fileselector.adapter.TestAdapter;

/**
 * Created by 钟光燕 on 2016/8/16.
 * e-mail guangyanzhong@163.com
 */
public class FileBrowserFragment extends BaseFragment {
    private RecyclerView mFileListView ;
    private TestAdapter mTestAdapter ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.plugin_file_selecter_layout,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getTitleView().setTitle("文件浏览") ;
        mFileListView = (RecyclerView) view.findViewById(R.id.id_file_list);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext()) ;
        mFileListView.setLayoutManager(manager);
        mTestAdapter = new TestAdapter(getContext()) ;
        mFileListView.setAdapter(mTestAdapter);
        //测试时间
    }
}
