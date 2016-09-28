package com.sunshine.sunxin.lib.ssnet;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

/**
 * Created by 钟光燕 on 2016/9/8.
 * e-mail guangyanzhong@163.com
 */
public interface NetCallBack<T> {

    void onResponse(T data) ;

    void onFailure(int code) ;
}
