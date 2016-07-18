package com.sunshine.sun.lib.socket;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

/**
 * Created by 钟光燕 on 2016/7/15.
 * e-mail guangyanzhong@163.com
 */
public interface SSKeyType {

    /**
     * 0x00 ~0xEF 为头 key type
     */

    /**
     * 0xF0~0xFF 为 body key type
     */

    short BINARY_KEY = 0xF0 ;
}
