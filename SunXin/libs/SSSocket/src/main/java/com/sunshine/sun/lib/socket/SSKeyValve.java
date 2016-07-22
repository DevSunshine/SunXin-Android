package com.sunshine.sun.lib.socket;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;

/**
 * Created by 钟光燕 on 2016/7/14.
 * e-mail guangyanzhong@163.com
 */
public abstract class SSKeyValve {

    public byte[] value ;

    public short type ;

    public SSKeyValve(){

    }

    public SSKeyValve(short type){
        this.type = type ;
    }

    public SSKeyValve(short type , long value){
        this.type = type ;
        setValue(value);
    }

    public SSKeyValve(short type , String value){
        this.type = type ;
        setValue(value);
    }

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

    public void setValue(String stringValve){
        if (!TextUtils.isEmpty(stringValve)){
            try {
                value = stringValve.getBytes("UTF-8") ;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public long getLongValue(){
        long n = 0L ;
        n |= ((long)value[0]&0xFF) ;
        n |= ((long)value[1]&0xFF) << 8 ;
        n |= ((long)value[2]&0xFF) << 16 ;
        n |= ((long)value[3]&0xFF) << 24 ;
        n |= ((long)value[4]&0xFF) << 32 ;
        n |= ((long)value[5]&0xFF) << 40 ;
        n |= ((long)value[6]&0xFF) << 48 ;
        n |= ((long)value[7]&0xFF) << 56 ;
        return n ;
    }

    public void setValue(long longValve){
        if (longValve == 0){
            value = new byte[]{0} ;
        }
        value = new byte[8] ;
        value[0] = (byte) ((longValve)&0xFF);
        value[1] = (byte) ((longValve >> 8)&0xFF);
        value[2] = (byte) ((longValve >> 16)&0xFF);
        value[3] = (byte) ((longValve >> 24)&0xFF);
        value[4] = (byte) ((longValve >> 32)&0xFF);
        value[5] = (byte) ((longValve >> 40)&0xFF);
        value[6] = (byte) ((longValve >> 48)&0xFF);
        value[7] = (byte) ((longValve >> 56)&0xFF);
    }

    public void setTypeValue(short type ,String value){
        setType(type);
        setValue(value);
    }
}
