package com.sunxin.plugin.reader.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunshine.sunxin.base.BaseFragment;
import com.sunshine.sunxin.view.MainTab;


// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

/**
 * Created by 钟光燕 on 2016/10/12.
 * e-mail guangyanzhong@163.com
 */
//Skin_Test-debug.apk

public class ReaderMainFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.reader_context_layout,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainTab mainTab = (MainTab) view.findViewById(R.id.id_reader_tab);
        ReaderFragmentHelper mHelper = new ReaderFragmentHelper(getFragmentManager(),R.id.id_reader_content_fragment) ;
        mainTab.setFragmentHelper(mHelper);
        mHelper.setCurrentItem(ReaderFragmentHelper.TAB_BOOK_FRAGMENT);
    }
}
