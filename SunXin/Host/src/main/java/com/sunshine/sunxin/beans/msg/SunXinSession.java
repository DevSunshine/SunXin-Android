package com.sunshine.sunxin.beans.msg;

// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

/**
 * Created by 钟光燕 on 2016/10/11.
 * e-mail guangyanzhong@163.com
 */

/**
 * 会话Session，一级session
 */

public class SunXinSession {
    /**
     * id
     */
    public String sessionId ;
    /**
     *
     */
    public String sessionName ;
    /**
     *
     */
    public String sessionType ;
    /**
     *
     */
    public String sessionSummary ;
    /**
     *
     */
    public String parentSessionId ;
    /**
     *
     */
    public long lastMsgTime ;
    /**
     *
     */
    public boolean unReadFlag ;
    /**
     *
     */
    public int unReadCount ;
    /**
     *
     */
    public String lastMsgType ;
    /**
     *
     */
    public String lastMsgSummary ;
    /**
     *
     */
    public boolean isSticky ;
    /**
     *
     */
    public int status ;
}
