package com.sunxin.plugin.fileselector;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import java.io.Serializable;

/**
 * Created by 钟光燕 on 2016/8/16.
 * e-mail guangyanzhong@163.com
 */
public class FileInfo implements Serializable {

    public String name ;

    public String path ;

    public int count ;

    public boolean selected ;

    public boolean isDir ;

    public long fileSize ;

    public long lastModified ;

    public boolean isHide ;


}
