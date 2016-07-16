package com.sunshine.sun.lib.socket;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

/**
 * Created by 钟光燕 on 2016/7/14.
 * e-mail guangyanzhong@163.com
 */
public interface SSMessageCode {

    /**
     * 起始码
     */
    short START_CODE_1 = 0X05;
    short START_CODE_2 = 0X25;
    short START_CODE_3 = 0X55;

    /**
     * 结束码
     */
    short END_CODE = 0X00 ;
}
