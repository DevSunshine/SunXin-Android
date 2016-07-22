package com.sunshine.sun.lib.socket;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.os.Process;


import com.sunshine.sun.lib.socket.request.SSTask;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by 钟光燕 on 2016/7/14.
 * e-mail guangyanzhong@163.com
 */
public class SSClientThread extends Thread {

    private volatile boolean mQuit;
    private PriorityBlockingQueue<SSTask> mQueue ;
    private SSISendWork mSendWork ;

    public SSClientThread(PriorityBlockingQueue<SSTask> mQueue,SSISendWork mSendWork) {
        this.mQueue = mQueue;
        this.mSendWork = mSendWork ;
    }

    public void quite(){
        mQuit = true ;
        interrupt();
    }
    @Override
    public void run() {
        Process.setThreadPriority(Thread.MAX_PRIORITY);
        while (true){

            SSTask task ;
            while (true){
                try {
                    task = mQueue.take() ;
                    break;
                } catch (InterruptedException e) {
                    if (mQuit){
                        return;
                    }
                }
            }
            if (!task.canceled()){
                mSendWork.doSendSocket(task);
            }
            task.finish();
        }
    }
}
