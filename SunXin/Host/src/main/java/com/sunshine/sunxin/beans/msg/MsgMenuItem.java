package com.sunshine.sunxin.beans.msg;

// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

/**
 * Created by 钟光燕 on 2016/9/30.
 * e-mail guangyanzhong@163.com
 */

/**
 * 消息页面 下拉菜单
 */
public class MsgMenuItem {

    public int resId ;
    public String title ;

    public MsgMenuItem(int id,  String title){
        this.resId = id ;
        this.title = title ;
    }

    public MsgMenuItem(){

    }
}
