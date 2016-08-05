package com.sunshine.sunxin.view;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.sunshine.sunxin.R;


/**
 * Created by 钟光燕 on 2016/8/4.
 * e-mail guangyanzhong@163.com
 */
public class TitleView extends LinearLayout {
    private LinearLayout mContent ;
    public TitleView(Context context) {
        super(context);
        initView();
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TitleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView(){
        mContent = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.title_view,this,false);
        addView(mContent);
    }

    public void hide(){
        setVisibility(GONE);
    }
}
