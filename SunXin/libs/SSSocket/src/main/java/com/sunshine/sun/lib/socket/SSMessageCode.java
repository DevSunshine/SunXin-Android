package com.sunshine.sun.lib.socket;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

/**
 * Created by 钟光燕 on 2016/7/14.
 * e-mail guangyanzhong@163.com
 */
public interface SSMessageCode {




    short CONNECT = 0x01 ;
    short AUTHENTICATE = 0x02 ;
    short DISCONNECT = 0x03 ;
    short HEATBEAT = 0x04 ;
    short QUERY = 0x05 ;
    short NOTIFY = 0x06 ;

    short OK = 0x80 ;
    short WRONG = 0x81 ;
    short TRY = 0xB0 ;
    short SERIES = 0xB1 ;
    short CONTINUOUS = 0xB2 ;
}
