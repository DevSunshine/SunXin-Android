package com.sunxin.plugin.reader.main;

// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sunshine.sunxin.base.BaseFragment;
import com.sunshine.sunxin.beans.book.RecommendBook;
import com.sunshine.sunxin.view.TitleView;
import com.sunshine.sunxin.widget.recyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 钟光燕 on 2016/10/12.
 * e-mail guangyanzhong@163.com
 */

public class TabBookFragment extends BaseFragment {
    public static TabBookFragment newInstance() {
        TabBookFragment fragment = new TabBookFragment();
        return fragment;
    }

    private TitleView mTitleView ;
    private LinearLayout mTitleLayout ;
    private RecyclerView mListView ;
    private BookAdapter mAdapter ;
    private LinearLayoutManager mManager ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_book,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitleView = (TitleView) view.findViewById(R.id.id_book_title);
        mTitleView.setBackgroundColor(Color.TRANSPARENT);
        mTitleView.hideTitleLine();
        mTitleLayout = (LinearLayout) view.findViewById(R.id.id_title_layout);
        mTitleView.addRightBtn(R.drawable.ic_cut_white, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }) ;
        mListView = (RecyclerView) view.findViewById(R.id.id_book_list_view);
        mManager = new LinearLayoutManager(getContext()) ;
        mListView.setLayoutManager(mManager);
        mAdapter = new BookAdapter(getContext()) ;
        mListView.setAdapter(mAdapter);
        List<RecommendBook> books = new ArrayList<>() ;
        for (int i = 0 ; i < 20 ; i ++){
            books .add(new RecommendBook()) ;
        }
        mAdapter.addAll(books);
        mAdapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup viewGroup) {
                View headerView = LayoutInflater.from(getContext()).inflate(R.layout.head_book, viewGroup, false);
                return headerView;
            }

            @Override
            public void onBindView(View view) {

                Log.v("zgy","===========view============="+view.getMeasuredHeight()) ;
            }
        });

        mListView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                setTitleViewStatue() ;

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }
        });

    }

    private void setTitleViewStatue(){
        int position = mManager.findFirstVisibleItemPosition() ;
        if (position == 0){
            View view = mManager.findViewByPosition(mManager.findFirstVisibleItemPosition()) ;
            Log.v("zgy","=================top="+view.getTop()) ;
        }


    }
}
