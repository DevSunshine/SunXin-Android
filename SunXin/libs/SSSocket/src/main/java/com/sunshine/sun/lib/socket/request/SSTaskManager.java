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
    private SSINotifyListener mNotifyListener ;

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

    public void setNotifyListener(SSINotifyListener listener){
        mNotifyListener = listener ;
    }

    public void notifyData(SSRequest request){
        if (mNotifyListener!= null){
            mNotifyListener.onNotify(request);
        }
    }

    public SSClient getSecondlyClient() {
        SSClient client = SSSocket.instance().getSecondlyClient();
        if (client == null) {
            SSSocket.instance().requestClient(SSClientMode.secondly);
        }
        return client;
    }

    public SSClient getPrimaryClient() {
        SSClient client = SSSocket.instance().getPrimaryClient();
        if (client == null) {
            SSSocket.instance().requestClient(SSClientMode.primary);
        }
        return client;
    }
    public SSTask createResponseTask() {
        SSTask task = new SSResponseTask();
        task.setClient(getPrimaryClient());
        task.setPriority(Priority.HIGH) ;
        return task;
    }
    public SSTask createQueryTask() {
        SSTask task = new SSQueryTask();
        task.setClient(getPrimaryClient());
        return task;
    }

    public SSTask createDownloadTask() {
        SSTask task = new SSDownloadTask();
        task.setClient(getSecondlyClient());
        return task;
    }

    public SSTask createUploadTask() {
        SSTask task = new SSUploadTask();
        task.setClient(getSecondlyClient());
        return task;
    }

    public void cancelTask(String queryName){
        mClientQueue.cancelAll(queryName);
    }
    public void commitTask(SSTask task) {
        mClientQueue.cancel(task);
        mClientQueue.add(task);
        task.lockWait();
    }
}
