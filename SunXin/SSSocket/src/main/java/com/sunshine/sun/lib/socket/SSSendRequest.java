package com.sunshine.sun.lib.socket;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.


import com.sunshine.sun.lib.socket.request.SSTask;

/**
 * Created by 钟光燕 on 2016/7/15.
 * e-mail guangyanzhong@163.com
 */
public class SSSendRequest implements SSISendWork {
    @Override
    public void doSendSocket(SSTask task) {
        SSClient client = task.getClient() ;
        SSRequest request = new SSRequest(task.getMethod()) ;
        for (int i = 0 ; i < task.getHeaderList().size(); i ++){
            request.addHeader(task.getHeaderList().get(i));
        }
        SSPipeLine pipeLine = new SSPipeLine() ;
        pipeLine.setRequest(request);
        client.sendRequest(pipeLine);
    }
}
