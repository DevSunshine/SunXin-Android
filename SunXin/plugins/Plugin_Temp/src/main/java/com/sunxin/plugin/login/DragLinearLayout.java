package com.sunxin.plugin.login;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 钟光燕 on 2016/8/30.
 * e-mail guangyanzhong@163.com
 */
public class DragLinearLayout extends LinearLayout implements AdapterView.OnItemLongClickListener{

    private List<String> mData = new ArrayList<>();
    private List<String> mTopData ;
    private List<String> mContentData  ;
    public DragLinearLayout(Context context) {
        super(context);
    }

    public DragLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DragLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {


        return true;
    }

    public void setDataList(List<String> dataList){
        if (dataList != null){
            mData.clear();
            mData.addAll(dataList) ;
            if (mData.size() < 3){
                //// TODO: 2016/8/30 bao cuo
            }
            mTopData = mData.subList(0,3) ;
            mContentData = mData.subList(3,mData.size()) ;
            populate();
        }
    }


    /**
     * 填充数据
     */
    private void populate(){

    }

    /**
     * 顶部区域
     */
    private void populateTopView(){

    }

    /**
     *中间这块区域填数据
     */
    private void populateContentView(){

    }

    /**
     * 填充空白区域,主要是区分线
     */
    private void populateEmpty(){}

}
