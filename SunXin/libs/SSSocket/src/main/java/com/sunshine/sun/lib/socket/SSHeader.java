package com.sunshine.sun.lib.socket;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

/**
 * Created by 钟光燕 on 2016/7/14.
 * e-mail guangyanzhong@163.com
 */
public class SSHeader extends SSKeyValve {
    public SSHeader() {
    }

    public SSHeader(short type) {
        super(type);
    }

    public SSHeader(short type, long value) {
        super(type, value);
    }

    public SSHeader(short type, String value) {
        super(type, value);
    }
}
