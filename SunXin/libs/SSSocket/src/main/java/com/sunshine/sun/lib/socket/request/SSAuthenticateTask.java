package com.sunshine.sun.lib.socket.request;

import com.sunshine.sun.lib.socket.SSClientManager;
import com.sunshine.sun.lib.socket.SSMessageCode;
import com.sunshine.sun.lib.socket.SSResponse;
import com.sunshine.sun.lib.socket.toolbox.SSSocket;

/**
 * Created by gyzhong on 16/8/2.
 */
public class SSAuthenticateTask extends SSTask {
    public SSAuthenticateTask(){
        super(SSMessageCode.AUTHENTICATE);
    }

}
