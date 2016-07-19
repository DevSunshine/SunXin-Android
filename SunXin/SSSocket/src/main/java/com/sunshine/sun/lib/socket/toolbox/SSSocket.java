package com.sunshine.sun.lib.socket.toolbox;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import com.sunshine.sun.lib.socket.SSClient;
import com.sunshine.sun.lib.socket.SSClientManager;
import com.sunshine.sun.lib.socket.SSClientMode;
import com.sunshine.sun.lib.socket.bean.UserAccount;

/**
 * Created by 钟光燕 on 2016/7/14.
 * e-mail guangyanzhong@163.com
 */
public class SSSocket {
    private UserAccount mUserAccount ;
    public static SSSocket instance() {
        return InnerInstance.instance;
    }

    public static class InnerInstance {
        private static SSSocket instance = new SSSocket();
    }



    public synchronized void initSocket(UserAccount account) {
        mUserAccount = account ;
        requestClient(SSClientMode.primary) ;
    }
    public synchronized void logoutSocket(){
        mUserAccount = null ;
        SSClientManager.instance().closeClient();
    }

    public void requestClient(SSClientMode mode) {
        if (mUserAccount == null){

        }else {
            SSClientManager.instance().closeClient();
            SSClientManager.instance().setUserAccount(mUserAccount);
            String address = mUserAccount.getAddress() ;
            int port = mUserAccount.getPort() ;
            SSClientManager.instance().connect(address,port,mode) ;
        }
    }

    public SSClient getPrimaryClient(){
        return SSClientManager.instance().getPrimaryClient() ;
    }

    public SSClient getSecondlyClient(){
        return SSClientManager.instance().getSecondlyClient() ;
    }
}
