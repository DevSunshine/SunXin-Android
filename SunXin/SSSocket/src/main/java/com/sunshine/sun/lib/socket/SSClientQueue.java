package com.sunshine.sun.lib.socket;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.text.TextUtils;


import com.sunshine.sun.lib.socket.request.SSTask;

import java.util.HashSet;
import java.util.Iterator;
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
    }

    public void start() {
        stop();
        mPrimaryClientThread = new SSClientThread(mPrimaryQueue, mSendWork);
        mSecondlyClientThread = new SSClientThread(mPrimaryQueue, mSendWork);
        mPrimaryClientThread.start();
        mSecondlyClientThread.start();
    }

    public SSTask add(SSTask task) {
        synchronized (mCurrentTask) {
            mCurrentTask.add(task);
        }
        task.setSequence(mSequenceGenerator.decrementAndGet());
        SSClient client = task.getClient() ;
        if (client.getClientMode() == SSClientMode.primary){
            mPrimaryQueue.add(task);
        }else {
            mSecondlyQueue.add(task) ;
        }
        return task;
    }

    public void cancelAll(SSITaskFilter filter) {
        synchronized (mCurrentTask) {
            Iterator<SSTask> iterator = mCurrentTask.iterator();
            while (iterator.hasNext()) {
                SSTask task = iterator.next();
                if (filter.apply(task)) {
                    task.cancel();
                }
            }
        }
    }

    public void cancel(SSTask newTask) {
        final String name = newTask.getHeader(SSResquestCode.QUERY).getStringValue();
        if (!TextUtils.isEmpty(name)) {
            cancelAll(new SSITaskFilter() {
                @Override
                public boolean apply(SSTask task) {
                    if (task.getHeader(name) != null) {
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    public void cancelAll(final String queryName) {
        if (!TextUtils.isEmpty(queryName)) {
            cancelAll(new SSITaskFilter() {
                @Override
                public boolean apply(SSTask task) {
                    if (task.getHeader(queryName) != null) {
                        return true;
                    }
                    return false;
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
