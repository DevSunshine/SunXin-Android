package com.sunshine.sun.lib.socket;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

/**
 * Created by 钟光燕 on 2016/7/14.
 * e-mail guangyanzhong@163.com
 */
public abstract class SSKeyValve {

    public byte[] value ;

    public short type ;

    public int valueLength(){
        if (value != null){
            return value.length ;
        }else {
            return 0 ;
        }
    }

    public String getStringValue(){
        return "" ;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }
}
