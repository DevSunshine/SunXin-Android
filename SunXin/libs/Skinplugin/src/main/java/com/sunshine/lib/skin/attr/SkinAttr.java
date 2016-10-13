package com.sunshine.lib.skin.attr;

// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.view.View;

import com.sunshine.lib.skin.bean.SkinInfo;

/**
 * Created by 钟光燕 on 2016/10/13.
 * e-mail guangyanzhong@163.com
 */

public abstract class SkinAttr {

    public static String DRAWABLE = "drawable" ;
    public static String COLOR = "color" ;

    public static String SKIN_VALUE_FLAG = "skin" ;
    public static String SKIN_ATTR_NAME = "tag" ;

    public String attrName ;

    public String attrType ;

    public String attrValueName ;


    public abstract void apply(View view, SkinInfo info) ;
}
