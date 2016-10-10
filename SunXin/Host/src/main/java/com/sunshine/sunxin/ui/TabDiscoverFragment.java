package com.sunshine.sunxin.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunshine.sunxin.BaseFragment;
import com.sunshine.sunxin.R;
import com.sunshine.sunxin.beans.discover.Function;
import com.sunshine.sunxin.ui.business.discover.DiscoverAdapter;
import com.sunshine.sunxin.ui.business.discover.DiscoverMVP;
import com.sunshine.sunxin.ui.business.discover.DiscoverPresenter;
import com.sunshine.sunxin.view.TitleView;

import java.util.List;


// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

/**
 * Created by 钟光燕 on 2016/9/29.
 * e-mail guangyanzhong@163.com
 * 首页中，探索页面
 */

public class TabDiscoverFragment extends BaseFragment implements DiscoverMVP.View{
    public static TabDiscoverFragment newInstance(){
        TabDiscoverFragment fragment = new TabDiscoverFragment() ;
        return fragment ;
    }

    private TitleView mTitle ;
    private RecyclerView mDiscoverList ;
    private DiscoverAdapter mDiscoverAdapter ;
    private DiscoverPresenter mPresenter ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_discover_fragment,container,false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitle = (TitleView) view.findViewById(R.id.id_tab_discover_title);
        initTitle() ;
        mDiscoverList = (RecyclerView) view.findViewById(R.id.id_discover_list);
        mDiscoverAdapter = new DiscoverAdapter() ;
        mDiscoverList.setLayoutManager(new LinearLayoutManager(getContext()));
        mDiscoverList.setAdapter(mDiscoverAdapter);
        mPresenter = new DiscoverPresenter() ;
        mPresenter.attachView(this);
        mPresenter.getFunctions();
    }

    private void initTitle() {
        mTitle.setTitle("探索") ;
    }

    @Override
    public void fragmentSelect() {
    }

    @Override
    public void showFunctions(List<Function> functions) {
        mDiscoverAdapter.setFunctionsList(functions);
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
    }
}
