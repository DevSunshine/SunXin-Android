package com.sunshine.lib.taskinterface;

import com.sunshine.sun.lib.socket.SSResponse;
import com.sunshine.sun.lib.socket.request.SSITaskListener;
import com.sunshine.sun.lib.socket.request.SSTask;
import com.sunshine.sun.lib.socket.request.SSTaskManager;

/**
 * Created by gyzhong on 16/8/6.
 */
public abstract class BaseInterface implements SSITaskListener {

    @Override
    public void onProgress(SSTask task, int progress, int total) {

    }

    @Override
    public void onComplete(SSTask task, SSResponse response) {

    }

    @Override
    public void onError(SSTask task, int errorCode) {

    }

    public void commitTask(SSTask task){
        SSTaskManager.instance().commitTask(task);
    }
}
