package com.sunshine.sunxin;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.sunshine.sunxin.view.TitleView;
import com.sunshine.sunxin.view.loadingview.LoadingIndicatorView;

/**
 * Created by 钟光燕 on 2016/8/4.
 * e-mail guangyanzhong@163.com
 */
public class BaseActivity extends FragmentActivity {
    private TitleView mTitleView ;
    private LoadingIndicatorView mLoadingView ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
       setContentView(getLayoutInflater().inflate(layoutResID,null));
    }

    @Override
    public void setContentView(View view) {
        View parent = getLayoutInflater().inflate(R.layout.base_view,null) ;
        FrameLayout contentView = (FrameLayout) parent.findViewById(R.id.id_contentView);
        mTitleView = (TitleView) parent.findViewById(R.id.id_titleBar);
        mLoadingView = (LoadingIndicatorView) parent.findViewById(R.id.id_loadingView);
        contentView.addView(view);
        super.setContentView(parent);

    }

    public void showLoadingView(){
        mLoadingView.setVisibility(View.VISIBLE);
    }
    public void hideLoadingView(){
        mLoadingView.smoothToHide();
    }
    public TitleView getTitleView(){
        return mTitleView ;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.instance.getRefWatcher() ;
    }
}
