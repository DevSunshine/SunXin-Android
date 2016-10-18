package com.sunshine.sunxin.skin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;


// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

/**
 * Created by 钟光燕 on 2016/10/13.
 * e-mail guangyanzhong@163.com
 */

public class SkinActivity extends FragmentActivity {

    private SkinFactory mFactory ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFactory = new SkinFactory(getClass().getName()) ;
        getLayoutInflater().setFactory(mFactory);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFactory.clearPage();
    }
}
