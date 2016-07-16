package com.sunshine.sun.lib.socket.toolbox;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 钟光燕 on 2016/7/14.
 * e-mail guangyanzhong@163.com
 */
public class BytePool {
    private LinkedList<byte[]> mUsedBytes = new LinkedList<>() ;
    private List<byte[]> mAllowBytes = new ArrayList<>(64) ;
    private long mCurrentSize ;
    private long mLimitSize ;

    public BytePool(int limitSize){
        mLimitSize = limitSize ;
    }
    public BytePool(){
        this(1024*1024*2) ;
    }
    public synchronized byte[] getBuffer(int len){

        for (int i = 0 ; i < mAllowBytes.size(); i ++){
            byte[] buf = mAllowBytes.get(i) ;
            if (buf.length >= len){
                mUsedBytes.remove(buf) ;
                mAllowBytes.remove(i) ;
                mCurrentSize -= buf.length ;
                return buf ;
            }
        }
        return new byte[len] ;
    }

    public synchronized void cacheBuffer(byte[] buf){

        if (buf != null && buf.length <= mLimitSize){
            int pos = Collections.binarySearch(mAllowBytes,buf, BUF_COMPARATOR) ;
            if (pos < 0){
                pos = -pos - 1 ;
            }
            mAllowBytes.add(pos,buf);
            mUsedBytes.add(buf);
            mCurrentSize += buf.length ;
            trim();
        }
    }

    private void trim(){

        while (mCurrentSize > mLimitSize){
            byte[] buf = mUsedBytes.remove(0) ;
            mAllowBytes.remove(buf) ;
            mCurrentSize -= buf.length ;
        }
    }


    protected static final Comparator<byte[]> BUF_COMPARATOR = new Comparator<byte[]>() {
        public int compare(byte[] lhs, byte[] rhs) {
            return lhs.length - rhs.length;
        }
    };
}
