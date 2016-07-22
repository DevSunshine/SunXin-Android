package com.sunshine.sun.lib.socket;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.


import com.sunshine.sun.lib.socket.request.SSTask;

/**
 * Created by 钟光燕 on 2016/7/15.
 * e-mail guangyanzhong@163.com
 */
public interface SSITaskFilter {

    boolean apply(SSTask task) ;
}
