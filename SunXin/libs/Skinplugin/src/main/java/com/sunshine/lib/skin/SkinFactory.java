package com.sunshine.lib.skin;

// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by 钟光燕 on 2016/10/13.
 * e-mail guangyanzhong@163.com
 */

public class SkinFactory implements LayoutInflater.Factory{


    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        Log.v("zgy","=============name========"+name) ;
        View view = createView(name,context,attrs) ;
        return view;
    }

    private View createView(String name, Context context, AttributeSet attrs) {
        View view = null;
        try {
            if (-1 == name.indexOf('.')){
                if ("View".equals(name)) {
                    view = LayoutInflater.from(context).createView(name, "android.view.", attrs);
                }
                if (view == null) {
                    view = LayoutInflater.from(context).createView(name, "android.widget.", attrs);
                }
                if (view == null) {
                    view = LayoutInflater.from(context).createView(name, "android.webkit.", attrs);
                }
            }else {
                view = LayoutInflater.from(context).createView(name, null, attrs);
            }
        } catch (Exception e) {
            view = null;
        }
        return view;
    }

    public void clearPage(){
//        SkinManager.getInstance()
    }
}
