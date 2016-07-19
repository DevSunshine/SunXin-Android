package com.sunshine.sun.lib.socket.toolbox;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

/**
 * Created by 钟光燕 on 2016/7/14.
 * e-mail guangyanzhong@163.com
 */
public class SSSocket {
    public static SSSocket instance() {
        return InnerInstance.instance;
    }

    public static class InnerInstance {
        private static SSSocket instance = new SSSocket();
    }

    private SSSocket(){
        init();
    }

    private void init() {
        requestPrimaryClient();
    }

    public void requestPrimaryClient(){

    }
}
