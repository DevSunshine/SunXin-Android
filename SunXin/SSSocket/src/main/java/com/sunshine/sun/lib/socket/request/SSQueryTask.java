package com.sunshine.sun.lib.socket.request;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import com.sunshine.sun.lib.socket.SSResquestCode;

/**
 * Created by 钟光燕 on 2016/7/15.
 * e-mail guangyanzhong@163.com
 */
public class SSQueryTask extends SSTask {
    public SSQueryTask(short mMethod) {
        super(mMethod);
    }

    public SSQueryTask() {
        super(SSResquestCode.QUERY);
    }
}
