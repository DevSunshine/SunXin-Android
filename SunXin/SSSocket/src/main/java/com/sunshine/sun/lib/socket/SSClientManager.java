package com.sunshine.sun.lib.socket;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.


import com.sunshine.sun.lib.socket.toolbox.BytePool;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by 钟光燕 on 2016/7/14.
 * e-mail guangyanzhong@163.com
 */
public class SSClientManager {

    private SSClientQueue mClientQueue ;
    private SSISendWork mSendWork;
    private BytePool mBytePool ;
    private final Set<SSClient> mClients = new HashSet<>() ;
    public static SSClientManager instance() {
        return InnerInstance.instance;
    }

    public static class InnerInstance {
        private static SSClientManager instance = new SSClientManager();
    }

    private SSClientManager() {
        mBytePool = new BytePool() ;
        mSendWork = new SSSendRequest() ;
        mClientQueue = new SSClientQueue(mSendWork) ;
        mClientQueue.start();
    }

    public SSClient connect(String host,int port,SSClientMode priority){
        SSClient client = new SSClient(mBytePool) ;
        synchronized (mClients){
            client.connect(host,port);
            client.setClientMode(priority);
            mClients.add(client) ;
        }
        return client ;
    }

    /**
     * 获取主链路
     * @return 主链路
     */
    public SSClient getPrimaryClient(){
        synchronized (mClients){
            Iterator<SSClient> iterator = mClients.iterator() ;
            while (iterator.hasNext()){
                SSClient client = iterator.next() ;
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
            Iterator<SSClient> iterator = mClients.iterator() ;
            while (iterator.hasNext()){
                SSClient client = iterator.next() ;
                if (client.getClientMode() == SSClientMode.secondly){
                    return client ;
                }
            }
        }
        return null ;
    }

    public void closeClient(){
        synchronized (mClients){
            mClients.clear();
        }
    }
}
