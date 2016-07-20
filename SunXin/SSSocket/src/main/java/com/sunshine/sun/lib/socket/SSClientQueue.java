package com.sunshine.sun.lib.socket;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.text.TextUtils;


import com.sunshine.sun.lib.socket.request.SSTask;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 钟光燕 on 2016/7/15.
 * e-mail guangyanzhong@163.com
 */
public class SSClientQueue {
    private SSClientThread mPrimaryClientThread;
    private SSClientThread mSecondlyClientThread;
    private AtomicInteger mSequenceGenerator;
    private PriorityBlockingQueue<SSTask> mPrimaryQueue;
    private PriorityBlockingQueue<SSTask> mSecondlyQueue;
    private final Set<SSTask> mCurrentTask;
    private SSISendWork mSendWork;

    public SSClientQueue(SSISendWork sendWork) {
        mPrimaryQueue = new PriorityBlockingQueue<>();
        mSecondlyQueue = new PriorityBlockingQueue<>();
        mSequenceGenerator = new AtomicInteger();
        mCurrentTask = new HashSet<>();
        mSendWork = sendWork;
    }

    public void stop() {
        if (mPrimaryClientThread != null) {
            mPrimaryClientThread.quite();
        }
        if (mSecondlyClientThread != null) {
            mSecondlyClientThread.quite();
        }
        mCurrentTask.clear();
    }

    public void start() {
        stop();
        mPrimaryClientThread = new SSClientThread(mPrimaryQueue, mSendWork);
        mSecondlyClientThread = new SSClientThread(mSecondlyQueue, mSendWork);
        mPrimaryClientThread.start();
        mSecondlyClientThread.start();
    }

    public SSTask add(SSTask task) {
        synchronized (mCurrentTask) {
            mCurrentTask.add(task);
        }
        task.setSequence(mSequenceGenerator.decrementAndGet());
        SSClient client = task.getClient();
        if (client.getClientMode() == SSClientMode.primary) {
            mPrimaryQueue.add(task);
        } else {
            mSecondlyQueue.add(task);
        }
        return task;
    }

    public void cancelAll(SSITaskFilter filter) {
        synchronized (mCurrentTask) {
            for (SSTask task : mCurrentTask){
                if (filter.apply(task)) {
                    task.cancel();
                }
            }
        }
    }

    public void cancel(SSTask newTask) {
        final String name = newTask.getHeader(SSTypeCode.QUERY_NAME).getStringValue();
        if (!TextUtils.isEmpty(name)) {
            cancelAll(new SSITaskFilter() {
                @Override
                public boolean apply(SSTask task) {
                    return task.getHeader(name) != null ;
                }
            });
        }
    }

    public void cancelAll(final String queryName) {
        if (!TextUtils.isEmpty(queryName)) {
            cancelAll(new SSITaskFilter() {
                @Override
                public boolean apply(SSTask task) {
                   return task.getHeader(queryName) != null ;
                }
            });
        }
    }

    public void finish(SSTask task) {
        synchronized (mCurrentTask) {
            mCurrentTask.remove(task);
        }
    }
}
