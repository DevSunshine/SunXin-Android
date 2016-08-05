package com.sunshine.sun.lib.socket.request;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.


import com.sunshine.sun.lib.socket.SSClient;
import com.sunshine.sun.lib.socket.SSClientMode;
import com.sunshine.sun.lib.socket.SSClientQueue;
import com.sunshine.sun.lib.socket.SSRequest;
import com.sunshine.sun.lib.socket.SSSendRequest;
import com.sunshine.sun.lib.socket.toolbox.Priority;
import com.sunshine.sun.lib.socket.toolbox.SSSocket;

/**
 * Created by 钟光燕 on 2016/7/15.
 * e-mail guangyanzhong@163.com
 */
public class SSTaskManager {
    private SSClientQueue mClientQueue;
    private SSINotifyListener mNotifyListener;

    public static SSTaskManager instance() {
        return InnerInstance.instance;
    }

    private SSTaskManager() {
        mClientQueue = new SSClientQueue(new SSSendRequest());
        mClientQueue.start();
    }

    public static class InnerInstance {
        private static SSTaskManager instance = new SSTaskManager();
    }

    public void setNotifyListener(SSINotifyListener listener) {
        mNotifyListener = listener;
    }

    public void notifyData(SSRequest request) {
        if (mNotifyListener != null) {
            mNotifyListener.onNotify(request);
        }
    }

    public SSClient getLoginClient() {
        SSClient client = SSSocket.instance().getLoginClient() ;
        if (client == null){
            client = SSSocket.instance().requestClient(SSClientMode.loginMode);
        }
        return client;
    }

    public SSClient getSecondlyClient() {
        SSClient client = SSSocket.instance().getSecondlyClient();
        if (client == null) {
            client = SSSocket.instance().requestClient(SSClientMode.secondly);
        }
        return client;
    }

    public SSClient getPrimaryClient() {
        SSClient client = SSSocket.instance().getPrimaryClient();
        if (client == null) {
            client = SSSocket.instance().requestClient(SSClientMode.primary);
        }
        return client;
    }

    public SSTask createResponseTask() {
        SSClient client = getPrimaryClient() ;
        SSTask task = new SSResponseTask();
        task.setPriority(Priority.IMMEDIATE) ;
        task.setClient(client);
        return task;
    }

    public SSTask createQueryTask() {
        SSClient client = getPrimaryClient() ;
        SSTask task = new SSQueryTask();
        task.setPriority(Priority.HIGH) ;
        task.setClient(client);
        return task;
    }

    public SSTask createDownloadTask() {
        SSClient client = getSecondlyClient() ;
        SSTask task = new SSDownloadTask();
        task.setClient(client);
        return task;
    }

    public SSTask createUploadTask() {

        SSClient client = getSecondlyClient() ;
        SSTask task = new SSUploadTask();
        task.setClient(client);
        return task;
    }

    public SSTask createLoginTask() {
        SSTask task = new SSLoginTask();
        task.setClient(getLoginClient());
        return task;
    }
    public SSTask createAuthenticateTask() {
        SSTask task = new SSAuthenticateTask();
        return task;
    }

    public void cancelTask(String queryName) {
        mClientQueue.cancelAll(queryName);
    }

    public void commitTask(SSTask task) {
        mClientQueue.cancel(task);
        mClientQueue.add(task);
        task.lockWait();
    }
}
