package com.sunshine.sunxin.base;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.sunshine.sunxin.App;
import com.sunshine.sunxin.R;
import com.sunshine.sunxin.plugin.PluginConstant;
import com.sunshine.sunxin.util.SystemBarTintManager;
import com.sunshine.sunxin.view.TitleView;
import com.sunshine.sunxin.view.loadingview.LoadingIndicatorView;

/**
 * Created by 钟光燕 on 2016/8/4.
 * e-mail guangyanzhong@163.com
 */
public class BaseActivity extends FragmentActivity {
    private TitleView mTitleView;
    private LoadingIndicatorView mLoadingView;
    public SystemBarTintManager systemBarTintManager;
    private View mParentView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        setContentView(getLayoutInflater().inflate(layoutResID, null));

    }

    @Override
    public void setContentView(View view) {
        boolean fullTint = getIntent().getBooleanExtra(PluginConstant.INTENT_TINT_FULL_KEY, false);
        mParentView = getLayoutInflater().inflate(fullTint ? R.layout.base_view_full : R.layout.base_view, null);
        FrameLayout contentView = (FrameLayout) mParentView.findViewById(R.id.id_contentView);
        mTitleView = (TitleView) mParentView.findViewById(R.id.id_titleBar);
        mLoadingView = (LoadingIndicatorView) mParentView.findViewById(R.id.id_loadingView);
        contentView.addView(view);
        super.setContentView(mParentView);
        systemBarTintManager = new SystemBarTintManager(this);
        systemBarTintManager.setStatusBarTintEnabled(true);
        ViewGroup window = (ViewGroup) getWindow().getDecorView();
        View test = new View(this) ;
        test.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        test.setBackgroundColor(0x80000000);
        window.addView(test);
//        systemBarTintManager.setNavigationBarTintEnabled(false);
    }

    public void showLoadingView() {
        mLoadingView.setVisibility(View.VISIBLE);
    }

    public void hideLoadingView() {
        mLoadingView.smoothToHide();
    }

    public TitleView getTitleView() {
        return mTitleView;
    }

    public void setFullTintContext() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mParentView.setFitsSystemWindows(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.instance.getRefWatcher();
    }
}
