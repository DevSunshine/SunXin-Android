package com.sunxin.plugin.login;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by 钟光燕 on 2016/8/30.
 * e-mail guangyanzhong@163.com
 */
public class DragScrollerView extends ScrollView {

    public DragScrollerView(Context context) {
        super(context);
    }

    public DragScrollerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragScrollerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DragScrollerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public int getScollerHeight(){
        return computeVerticalScrollRange() ;
    }


    OnScrollerListener mScrollerListener ;
    public void setOnScrollerListener(OnScrollerListener scrollerListener){
        mScrollerListener = scrollerListener ;
    }
    public interface OnScrollerListener{
        void onScrollY(int y) ;
    }


}
