package com.sunxin.plugin.reader.main;

// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.ArrayMap;

import com.sunshine.sunxin.base.BaseFragmentHelper;
import com.sunshine.sunxin.view.MainTab;

import java.util.Map;

/**
 * Created by 钟光燕 on 2016/9/29.
 * e-mail guangyanzhong@163.com
 */

public class ReaderFragmentHelper extends BaseFragmentHelper implements MainTab.OnTabItemListener {
    private Map<Integer, Fragment> mFragments = new ArrayMap<>();

    private int[] resId = {R.drawable.main_tab_msg, R.drawable.main_tab_contacts, R.drawable.main_tab_discover, R.drawable.main_tab_mine};
    static final int TAB_BOOK_FRAGMENT = 0;
    static final int TAB_GOOD_FRAGMENT = 1 ;
    static final int TAB_SORT_FRAGMENT = 2 ;
    static final int TAB_RANK_FRAGMENT = 3 ;

    public ReaderFragmentHelper(FragmentManager mFragmentManager, int mContainerId) {
        super(mFragmentManager, mContainerId);
    }

    @Override
    public <T extends Fragment> T getFragment(int position) {
        Fragment fragment = mFragments.get(position) ;
        if (fragment == null){
            switch (position){
                case TAB_BOOK_FRAGMENT:
                    fragment = TabBookFragment.newInstance() ;
                    break;
                case TAB_GOOD_FRAGMENT:
                    fragment = TabGoodFragment.newInstance() ;
                    break;
                case TAB_SORT_FRAGMENT:
                    fragment = TabSortFragment.newInstance() ;
                    break;
                case TAB_RANK_FRAGMENT:
                    fragment = TabRankFragment.newInstance() ;
                    break;
            }
            mFragments.put(position,fragment) ;
        }

        return (T) fragment;
    }

    @Override
    public int getCount() {
        return resId.length;
    }

    @Override
    public int getTabRes(int position) {
        return resId[position];
    }

    @Override
    public CharSequence getTabText(Context context, int position) {
        return context.getResources().getStringArray(R.array.reader_tab_title)[position];
    }
}
