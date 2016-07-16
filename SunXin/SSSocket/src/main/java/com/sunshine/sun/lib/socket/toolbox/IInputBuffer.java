package com.sunshine.sun.lib.socket.toolbox;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

/**
 * Created by 钟光燕 on 2016/7/14.
 * e-mail guangyanzhong@163.com
 */
public interface IInputBuffer {

    int read() ;

    int read(byte[] buffer) ;

    int read(byte[] buffer, int offset, int count) ;

    int available() ;

    byte[] readToByte() ;
}
