package com.sunxin.plugin.reader.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunshine.sunxin.base.BaseFragment;
import com.sunshine.sunxin.beans.Function;

import java.util.List;


// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

/**
 * Created by 钟光燕 on 2016/10/12.
 * e-mail guangyanzhong@163.com
 */

public class ReaderMenuFragment extends BaseFragment implements ReaderMenuMVP.View{
    private RecyclerView mMenuList ;
    private ReaderMenuPresenter mPresenter ;
    private ReaderMenuAdapter mMenuAdapter ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.reader_menu_layout,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMenuList = (RecyclerView) view.findViewById(R.id.id_reader_menu_list);
        mMenuAdapter = new ReaderMenuAdapter(getContext()) ;
        mMenuList.setLayoutManager(new LinearLayoutManager(getContext()));
        mMenuList.setAdapter(mMenuAdapter);
        mPresenter = new ReaderMenuPresenter() ;
        mPresenter.attachView(this);
        mPresenter.getFunctions();
    }

    @Override
    public void showFunctions(List<Function> functions) {
        mMenuAdapter.addAll(functions);
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }
}
