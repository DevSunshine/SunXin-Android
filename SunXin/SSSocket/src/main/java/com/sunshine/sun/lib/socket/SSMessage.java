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
            if (header.getType() == SSTypeCode.UNIQUE_KEY){
                mUniqueKey = header.getStringValue() ;
            }
        }
    }

    public void addBody(SSBody body){
        if (body != null){
            mBodies.add(body);
        }
    }

    public SSHeader getHeader(short type){
        for (SSHeader header : mHeaders){
            if (header.getType() == type){
                return header ;
            }
        }
        return null ;
    }

    public StreamBuffer toStreamBuffer(BytePool pool){
        int size = 5 ; //起始码（3） + 状态码（1） + 类型码作为结束码（1）
        for (SSHeader header : mHeaders){
            size += header.valueLength() + 2 ;//类型码 + 长度
        }
        for (SSBody body : mBodies){
            size += body.valueLength() + 3 ;
        }
        StreamBuffer streamBuffer = new StreamBuffer(pool,size) ;
        streamBuffer.write(SSStartCode.START_CODE_1);
        streamBuffer.write(SSStartCode.START_CODE_2);
        streamBuffer.write(SSStartCode.START_CODE_3);
        streamBuffer.write(mMessageCode);
        //write header and body

        for (SSHeader header : mHeaders){
            streamBuffer.write(header.getType());
            streamBuffer.write(header.getValue().length);
            streamBuffer.write(header.getValue());
        }

        for (SSBody body : mBodies){
            streamBuffer.write(body.getType());
            int length = body.getValue().length ;
            int temp1 = length&0xFF ;
            int temp2 = (length >> 8) & 0xFF ;
            streamBuffer.write(temp1);
            streamBuffer.write(temp2);
            streamBuffer.write(body.getValue());
        }

        streamBuffer.write(SSTypeCode.END_CODE);
        return streamBuffer ;
    }

    public String getUniqueKey() {
        return mUniqueKey;
    }

    public void setUniqueKey(String uniqueKey){
        addHeader(new SSHeader(SSTypeCode.UNIQUE_KEY,uniqueKey));
    }

    public short getMessageCode() {
        return mMessageCode;
    }
}
