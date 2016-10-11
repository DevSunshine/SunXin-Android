package com.sunshine.sunxin.beans;

// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

/**
 * Created by 钟光燕 on 2016/10/9.
 * e-mail guangyanzhong@163.com
 */

public class Function {

    public String name ;

    public String drawableId ;

    /**
     * 跳转地址
     */
    public String callUrl ;

    /**
     * 分类
     */
    public String orderIndex ;

    public Function(String name, String drawableId, String callUrl, String orderIndex) {
        this.name = name;
        this.drawableId = drawableId;
        this.callUrl = callUrl;
        this.orderIndex = orderIndex;
    }

    public Function() {
    }
}
