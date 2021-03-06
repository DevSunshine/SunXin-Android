package com.sunshine.sunxin.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunshine.sunxin.base.BaseFragment;
import com.sunshine.sunxin.R;
import com.sunshine.sunxin.beans.Function;
import com.sunshine.sunxin.ui.business.mine.MineAdapter;
import com.sunshine.sunxin.ui.business.mine.MineMVP;
import com.sunshine.sunxin.ui.business.mine.MinePresenter;
import com.sunshine.sunxin.view.TitleView;
import com.sunshine.sunxin.widget.recyclerview.adapter.RecyclerArrayAdapter;

import java.util.List;


// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

/**
 * Created by 钟光燕 on 2016/9/29.
 * e-mail guangyanzhong@163.com
 * 首页中，我的页面
 */

public class TabMineFragment extends BaseFragment implements MineMVP.View{
    public static TabMineFragment newInstance(){
        TabMineFragment fragment = new TabMineFragment() ;
        return fragment ;
    }

    private TitleView mTitle ;
    private MinePresenter mPresenter ;
    private MineAdapter mMineAdapter ;
    private RecyclerView mMineList ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_mine_fragment,container,false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitle = (TitleView) view.findViewById(R.id.id_tab_mine_title);
        initTitle() ;
        mMineList = (RecyclerView) getView().findViewById(R.id.id_mine_list);
        mMineList.setLayoutManager(new LinearLayoutManager(getContext()));
        mMineAdapter = new MineAdapter(getContext()) ;
        mMineList.setAdapter(mMineAdapter);
        mMineAdapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                View headerView  = LayoutInflater.from(getContext()).inflate(R.layout.head_mine_info,parent,false);
                return headerView;
            }

            @Override
            public void onBindView(View headerView) {

            }
        });
        mPresenter = new MinePresenter() ;
        mPresenter.attachView(this);
        mPresenter.getFunctions();
    }

    private void initTitle() {
        mTitle.setTitle("我的") ;
    }

    @Override
    public void showFunctions(List<Function> functions) {
        mMineAdapter.addAll(functions);
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }
}
