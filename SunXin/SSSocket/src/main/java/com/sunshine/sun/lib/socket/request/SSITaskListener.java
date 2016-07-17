package com.sunshine.sun.lib.socket.request;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.


import com.sunshine.sun.lib.socket.SSRequest;
import com.sunshine.sun.lib.socket.SSResponse;

/**
 * Created by 钟光燕 on 2016/7/15.
 * e-mail guangyanzhong@163.com
 */
public interface SSITaskListener {

    void onProgress(SSTask task, int progress, int total) ;

    void onComplete(SSTask task, SSResponse response) ;

    void onError(SSTask task, int errorCode) ;


}
