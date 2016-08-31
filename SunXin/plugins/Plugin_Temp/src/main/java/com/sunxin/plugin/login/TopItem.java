/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.sunxin.plugin.login;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;


/**
 * Created by 钟光燕 on 2016/3/18.
 * ===================================================
 * <p>
 * code is m h l
 * <p>
 * ===================================================
 */
public class TopItem extends FrameLayout {
    private View mImageView;
    public TopItem(Context context) {
        super(context);
        initView();
    }

    public TopItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TopItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    public void initView() {
//        mImageView = LayoutInflater.from(getContext()).inflate(R.layout.top_item,null);
//        addView(mImageView);
    }


}
