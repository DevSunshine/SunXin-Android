package com.sunxin.plugin.reader.main;

// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by 钟光燕 on 2016/10/24.
 * e-mail guangyanzhong@163.com
 */

public class BookImageView extends ImageView {
    public BookImageView(Context context) {
        super(context);
    }

    public BookImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BookImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BookImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = (int) (MeasureSpec.getSize(heightMeasureSpec)* 4 / 5.0f);
        setMeasuredDimension(width,MeasureSpec.getSize(heightMeasureSpec));
    }
}
