package com.sunshine.sunxin;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import com.sunshine.sun.lib.socket.request.SSITaskListener;
import com.sunshine.sun.lib.socket.request.SSTask;
import com.sunshine.sun.lib.socket.request.SSTaskManager;
import com.sunxin.lib.protobuf.TestProto;

/**
 * Created by 钟光燕 on 2016/7/29.
 * e-mail guangyanzhong@163.com
 */
public class TestJava {

    public static void login(String userName,String password,SSITaskListener listener){
        SSTask task = SSTaskManager.instance().createLoginTask() ;
        task.setTaskListener(listener) ;
        SSTaskManager.instance().commitTask(task);
    }
}
