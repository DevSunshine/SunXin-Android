package com.sunshine.sun.lib.socket.request;

import com.sunshine.sun.lib.socket.SSClientManager;
import com.sunshine.sun.lib.socket.SSResponse;
import com.sunshine.sun.lib.socket.toolbox.SSSocket;

/**
 * Created by gyzhong on 16/8/2.
 */
public class SSLoginTask extends SSTask {

    @Override
    public void onCompleteReceive(SSResponse response) {
        super.onCompleteReceive(response);
        SSClientManager.instance().closeClient();
        // TODO: 16/8/2 解析成功之后应该是UserAccount 
        SSSocket.instance().initSocket(null);
    }

    @Override
    public void onError(int errorCode) {
        super.onError(errorCode);
        SSClientManager.instance().closeClient();
    }
}
