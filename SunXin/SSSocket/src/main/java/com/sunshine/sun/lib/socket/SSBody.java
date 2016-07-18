package com.sunshine.sun.lib.socket;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

/**
 * Created by 钟光燕 on 2016/7/14.
 * e-mail guangyanzhong@163.com
 */
public class SSBody extends SSKeyValve{
    public SSBody() {
    }

    public SSBody(short type) {
        super(type);
    }

    public SSBody(short type, long value) {
        super(type, value);
    }

    public SSBody(short type, String value) {
        super(type, value);
    }

    @Override
    public void setValue(byte[] value) {
        super.setValue(value);
        type = SSKeyType.BINARY_KEY ;
    }
}
