package com.sunshine.sun.lib.socket;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.


import com.sunshine.sun.lib.socket.bean.UserAccount;
import com.sunshine.sun.lib.socket.toolbox.BytePool;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by 钟光燕 on 2016/7/14.
 * e-mail guangyanzhong@163.com
 */
public class SSClientManager implements SSIClientConnectListener{

    private BytePool mBytePool ;
    private final Set<SSClient> mClients = new HashSet<>() ;
    private int mPrimaryTryCount ;
    private int mSecondlyTryCount ;
    private UserAccount mUserAccount ;
    public static SSClientManager instance() {
        return InnerInstance.instance;
    }


    public static class InnerInstance {
        private static SSClientManager instance = new SSClientManager();
    }

    private SSClientManager() {
        mBytePool = new BytePool() ;
        new SSClientQueue(new SSSendRequest()).start();
    }

    public void setUserAccount(UserAccount account){
        mUserAccount = account ;
    }

    public SSClient connect(String host,int port,SSClientMode priority){
        SSClient client = new SSClient(mBytePool,this) ;
        synchronized (mClients){
            mClients.add(client) ;
            client.connect(host,port);
            client.setClientMode(priority);
        }
        return client ;
    }

    /**
     * 获取主链路
     * @return 主链路
     */
    public SSClient getPrimaryClient(){
        synchronized (mClients){
            for (SSClient client : mClients){
                if (client.getClientMode() == SSClientMode.primary){
                    return client ;
                }
            }
        }
        return null ;
    }

    /**
     * 获取二级链路
     * @return 二级链路
     */
    public SSClient getSecondlyClient(){
        synchronized (mClients){
            for (SSClient client : mClients){
                if (client.getClientMode() == SSClientMode.secondly){
                    return client ;
                }
            }
        }
        return null ;
    }

    public void closeClient(){
        synchronized (mClients){
            for (SSClient client : mClients){
                client.close();
            }
            mClients.clear();
        }
        mUserAccount = null ;
    }

    public void close(SSClient client){
        synchronized (mClients){
            mClients.remove(client) ;
        }
        int RETRY_COUNT = 3;
        if (mPrimaryTryCount < RETRY_COUNT && client.getClientMode() == SSClientMode.primary){
            mPrimaryTryCount ++ ;
            connect(mUserAccount.getAddress(),mUserAccount.getPort(),SSClientMode.primary) ;
        }
        if (mSecondlyTryCount < RETRY_COUNT && client.getClientMode() == SSClientMode.primary){
            mSecondlyTryCount ++ ;
            connect(mUserAccount.getAddress(),mUserAccount.getPort(),SSClientMode.secondly) ;
        }
    }

    @Override
    public void connected(SSClient client) {
        if (client.getClientMode() == SSClientMode.primary){
            mPrimaryTryCount = 0 ;
        }else if (client.getClientMode() == SSClientMode.secondly){
            mSecondlyTryCount = 0 ;
        }
    }

}
