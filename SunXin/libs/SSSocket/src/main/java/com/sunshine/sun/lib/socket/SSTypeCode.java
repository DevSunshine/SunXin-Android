package com.sunshine.sun.lib.socket;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

/**
 * Created by 钟光燕 on 2016/7/20.
 * e-mail guangyanzhong@163.com
 */
public interface SSTypeCode {

    short MESSAGE_COUNT = 0X01 ;

    short UNIQUE_KEY = 0x80 ;
    short QUERY_NAME = 0X81 ;

    short STRING_BODY = 0xFE ;
    short BINARY_BODY = 0xFF;

    /**
     * 结束码
     */
    short END_CODE = 0X00 ;


}
