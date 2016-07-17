package com.sunshine.sun.lib.socket;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.


import com.sunshine.sun.lib.socket.toolbox.BytePool;
import com.sunshine.sun.lib.socket.toolbox.StreamBuffer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 钟光燕 on 2016/7/14.
 * e-mail guangyanzhong@163.com
 */
public class SSMessage {

    private List<SSBody> mBodies = new ArrayList<>() ;
    private List<SSHeader> mHeaders = new ArrayList<>() ;
    private short mMessageCode ;
    private String mUniqueKey ;

    public SSMessage(short mMessageCode) {
        this.mMessageCode = mMessageCode;
    }

    public void addHeader(SSHeader header){
        if (header != null){
            mHeaders.add(header);
        }
    }

    public StreamBuffer toStreamBuffer(BytePool pool){
        int size = 5 ;
        for (SSHeader header : mHeaders){
            size += header.valueLength() + 2 ;
        }
        for (SSBody body : mBodies){
            size += body.valueLength() + 3 ;
        }
        StreamBuffer streamBuffer = new StreamBuffer(pool,size) ;
        streamBuffer.write(SSMessageCode.START_CODE_1);
        streamBuffer.write(SSMessageCode.START_CODE_2);
        streamBuffer.write(SSMessageCode.START_CODE_3);
        streamBuffer.write(mMessageCode);
        //write header and body

        streamBuffer.write(SSMessageCode.END_CODE);
        return streamBuffer ;
    }

    public String getUniqueKey() {
        return mUniqueKey;
    }

    public void setUniqueKey(String uniqueKey){
        mUniqueKey = uniqueKey ;
    }
}
