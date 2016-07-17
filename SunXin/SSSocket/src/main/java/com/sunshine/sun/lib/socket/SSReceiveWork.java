package com.sunshine.sun.lib.socket;

import com.sunshine.sun.lib.socket.request.SSTask;
import com.sunshine.sun.lib.socket.request.SSTaskManager;

/**
 * Created by gyzhong on 16/7/17.
 */
public class SSReceiveWork implements SSIReceiveWork {
    @Override
    public void doReceiveSocket(SSRequest request) {

        SSTaskManager.instance().notifyData(request);
        //回应服务器
        SSTask task = SSTaskManager.instance().createResponseTask() ;
        SSTaskManager.instance().commitTask(task);
        task.execute();
        SSClient client = task.getClient() ;
        SSPipeLine pipeLine = new SSPipeLine() ;
        SSResponse response = new SSResponse(SSResponseCode.OK) ;
        pipeLine.setResponse(response);
        client.sendResponse(pipeLine);
    }
}
