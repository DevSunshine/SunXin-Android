package com.sunshine.sunxin.base;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.support.v4.app.Fragment;

import com.sunshine.sunxin.util.SystemBarTintManager;
import com.sunshine.sunxin.view.TitleView;

import rx.Subscription;

/**
 * Created by 钟光燕 on 2016/8/4.
 * e-mail guangyanzhong@163.com
 */
public class BaseFragment extends Fragment {

    public Subscription subscription ;

    public TitleView getTitleView(){
        BaseActivity baseActivity = (BaseActivity) getActivity();
        return baseActivity.getTitleView() ;
    }

    public BaseActivity getHostActivity(){
        BaseActivity baseActivity = (BaseActivity) getActivity();
        return baseActivity ;
    }

    public SystemBarTintManager getSystemBarTintManager(){
        return getHostActivity().systemBarTintManager ;
    }

    public void fullTintContext(){
        getHostActivity().setFullTintContext();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unSubscription();
    }

    private void unSubscription(){
        if (subscription != null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
    }

    public void fragmentSelect(){

    }
}
