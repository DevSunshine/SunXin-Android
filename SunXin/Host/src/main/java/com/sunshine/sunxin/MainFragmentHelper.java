package com.sunshine.sunxin;

// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.ArrayMap;

import com.sunshine.sunxin.base.BaseFragmentHelper;
import com.sunshine.sunxin.ui.TabContactsFragment;
import com.sunshine.sunxin.ui.TabDiscoverFragment;
import com.sunshine.sunxin.ui.TabMineFragment;
import com.sunshine.sunxin.ui.TabMsgFragment;
import com.sunshine.sunxin.view.MainTab;

import java.util.Map;

/**
 * Created by 钟光燕 on 2016/9/29.
 * e-mail guangyanzhong@163.com
 */

public class MainFragmentHelper extends BaseFragmentHelper implements MainTab.OnTabItemListener {
    private Map<Integer, Fragment> mFragments = new ArrayMap<>();

    private int[] resId = {R.drawable.main_tab_msg, R.drawable.main_tab_contacts,R.drawable.main_tab_discover, R.drawable.main_tab_mine};
    static final int TAB_MSG_FRAGMENT = 0;
    static final int TAB_CONTACTS_FRAGMENT = 1 ;
    static final int TAB_DISCOVER_FRAGMENT = 2 ;
    static final int TAB_MINE_FRAGMENT = 3 ;

    public MainFragmentHelper(FragmentManager mFragmentManager, int mContainerId) {
        super(mFragmentManager, mContainerId);
    }

    @Override
    public <T extends Fragment> T getFragment(int position) {
        Fragment fragment = mFragments.get(position) ;
        if (fragment == null){
            switch (position){
                case TAB_MSG_FRAGMENT:
                    fragment = TabMsgFragment.newInstance() ;
                    break;
                case TAB_CONTACTS_FRAGMENT:
                    fragment = TabContactsFragment.newInstance() ;
                    break;
                case TAB_DISCOVER_FRAGMENT:
                    fragment = TabDiscoverFragment.newInstance() ;
                    break;
                case TAB_MINE_FRAGMENT:
                    fragment = TabMineFragment.newInstance() ;
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
        return context.getResources().getStringArray(R.array.main_tab_title)[position];
    }
}
