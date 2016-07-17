package com.sunshine.sun.lib.socket.toolbox;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.


import com.sunshine.sun.lib.socket.SSBody;
import com.sunshine.sun.lib.socket.SSHeader;
import com.sunshine.sun.lib.socket.SSMessage;
import com.sunshine.sun.lib.socket.SSMessageCode;
import com.sunshine.sun.lib.socket.SSResponse;

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
                if (mStreamBuffer.getLength() < 4) {
                    break;
                }
                int startCode1 = mStreamBuffer.read();
                int startCode2 = mStreamBuffer.read();
                int startCode3 = mStreamBuffer.read();
                if (startCode1 != SSMessageCode.START_CODE_1 ||
                        startCode2 != SSMessageCode.START_CODE_2 ||
                        startCode3 != SSMessageCode.START_CODE_3) {
                    return false;
                }
                int messageCode = mStreamBuffer.read();
                mMessage = new SSResponse((short) messageCode);
                continue;
            }

            if (mHeader == null && mBody == null) {

                int type = mStreamBuffer.read();
                if (type == SSMessageCode.END_CODE){
                    messages.add(mMessage);
                    mMessage = null ;
                    mStreamBuffer.reset();
                }
                mHeader = new SSHeader();
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
        }

        return true;
    }
}
