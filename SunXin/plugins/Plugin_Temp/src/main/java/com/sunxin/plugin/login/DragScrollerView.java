package com.sunxin.plugin.login;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.OverScroller;
import android.widget.ScrollView;

import java.lang.reflect.Field;

/**
 * Created by 钟光燕 on 2016/8/30.
 * e-mail guangyanzhong@163.com
 */
public class DragScrollerView extends ScrollView {

    private OverScroller mOverScroller ;
    public DragScrollerView(Context context) {
        super(context);
        setMyScroller(context);
    }

    public DragScrollerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setMyScroller(context);
    }

    public DragScrollerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setMyScroller(context);
    }

    public DragScrollerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setMyScroller(context);
    }

    private void setMyScroller(Context context){
        mOverScroller = new OverScroller(context) ;

        try {
            Field field = getClass().getDeclaredField("mScroller") ;
            field.setAccessible(true);
            try {
                field.set(this,mOverScroller);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
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
        void onScrollFinish() ;
    }


    private void checkScrollStop() {
        if(mOverScroller.isFinished() ) {
            // TODO do something
            Log.v("zgy","===============isFinished========");
            mScrollerListener.onScrollFinish();
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        checkScrollStop();
    }
}
