package com.sunxin.plugin.reader.main;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sunshine.sunxin.base.BaseFragment;
import com.sunshine.sunxin.beans.Function;

import java.lang.reflect.Method;
import java.util.List;


// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

/**
 * Created by 钟光燕 on 2016/10/12.
 * e-mail guangyanzhong@163.com
 */

public class ReaderMenuFragment extends BaseFragment implements ReaderMenuMVP.View {
    private RecyclerView mMenuList;
    private ReaderMenuPresenter mPresenter;
    private ReaderMenuAdapter mMenuAdapter;
    private ImageView mBg ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.reader_menu_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBg = (ImageView) view.findViewById(R.id.id_reader_menu_bg);
        mMenuList = (RecyclerView) view.findViewById(R.id.id_reader_menu_list);
        mMenuAdapter = new ReaderMenuAdapter(getContext());
        mMenuList.setLayoutManager(new LinearLayoutManager(getContext()));
        mMenuList.setAdapter(mMenuAdapter);
        mPresenter = new ReaderMenuPresenter();
        mPresenter.attachView(this);
        mPresenter.getFunctions();
    }

    @Override
    public void showFunctions(List<Function> functions) {
        mMenuAdapter.addAll(functions);
        test() ;
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }

    private void test() {
        AssetManager assetManager = null;
        try {
            assetManager = AssetManager.class.newInstance();
            Method addAssetPath = null;
            addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, "sdcard/Skin_Test-debug.apk");
            Resources superRes = getContext().getResources();
            Resources mResources = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
            int id = mResources.getIdentifier("bg_main", "drawable", "com.sunshine.skin.test") ;
            Resources.Theme theme = mResources.newTheme();
            theme.applyStyle(android.R.style.Theme, true);
            Drawable drawable = mResources.getDrawable(id,theme);
            mBg.setImageDrawable(drawable);

            Log.v("zgy","========id======~~~====="+id) ;


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
