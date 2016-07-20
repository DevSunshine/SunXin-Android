package com.sunshine.sun.lib.socket.toolbox;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.


import com.sunshine.sun.lib.socket.SSBody;
import com.sunshine.sun.lib.socket.SSHeader;
import com.sunshine.sun.lib.socket.SSMessage;
import com.sunshine.sun.lib.socket.SSRequest;
import com.sunshine.sun.lib.socket.SSResponse;
import com.sunshine.sun.lib.socket.SSStartCode;
import com.sunshine.sun.lib.socket.SSTypeCode;

import java.util.List;

/**
 * Created by 钟光燕 on 2016/7/14.
 * e-mail guangyanzhong@163.com
 */
public class BinaryParser {

    private SSBody mBody;
    private SSHeader mHeader;
    private SSMessage mMessage;
    private StreamBuffer mStreamBuffer;

    public BinaryParser(BytePool pool) {
        mStreamBuffer = new StreamBuffer(pool);
    }

    public boolean parser(byte[] buffer, int offset, int count, List<SSMessage> messages) {
        mStreamBuffer.write(buffer, offset, count);
        while (mStreamBuffer.available() > 0) {
            if (mMessage == null) {
                if (mStreamBuffer.available() < 4) {
                    break;
                }
                int startCode1 = mStreamBuffer.read();
                int startCode2 = mStreamBuffer.read();
                int startCode3 = mStreamBuffer.read();
                if (startCode1 != SSStartCode.START_CODE_1 ||
                        startCode2 != SSStartCode.START_CODE_2 ||
                        startCode3 != SSStartCode.START_CODE_3) {
                    return false;
                }
                int messageCode = mStreamBuffer.read();
                if ((messageCode | 0x7F) == 0x7F){
                    mMessage = new SSRequest((short) messageCode);
                }else {
                    mMessage = new SSResponse((short) messageCode);
                }

                continue;
            }

            if (mHeader == null && mBody == null) {

                short type = (short) mStreamBuffer.read();
                if (type == SSTypeCode.END_CODE){
                    messages.add(mMessage);
                    mMessage = null ;
                    break;
                }
                if (type == SSTypeCode.STRING_BODY ||type == SSTypeCode.BINARY_BODY){
                    mBody = new SSBody(type) ;
                }else {
                    mHeader = new SSHeader(type);
                }
                continue;
            }

            if (mHeader != null) {
                if (mHeader.getValue() == null) {
                    int headerLength = mStreamBuffer.read();
                    mHeader.setValue(new byte[headerLength]);
                } else {
                    if (mHeader.getValue().length > mStreamBuffer.available()) {
                        break;
                    }
                    mStreamBuffer.read(mHeader.getValue(), 0, mHeader.getValue().length);
                    mMessage.addHeader(mHeader);
                    mHeader = null;
                }
            }
            if (mBody != null){
                if (mBody.getValue() == null){
                    if (mStreamBuffer.available() < 2){
                        break;
                    }
                    int temp1 = mStreamBuffer.read() ;
                    int temp2 = mStreamBuffer.read() ;
                    int length = (temp1&0xFF) | ((temp2&0xFF) << 8);
                    mBody.setValue(new byte[length]);
                }else {
                    if (mBody.getValue().length > mStreamBuffer.available()){
                        break;
                    }
                    mStreamBuffer.read(mBody.getValue(),0,mBody.getValue().length) ;
                    mMessage.addBody(mBody);
                    mBody = null ;
                }
            }
        }

        return true;
    }
}
