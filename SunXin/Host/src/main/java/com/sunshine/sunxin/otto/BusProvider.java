package com.sunshine.sunxin.otto;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by 钟光燕 on 2016/8/8.
 * e-mail guangyanzhong@163.com
 */
public class BusProvider {

    private BusProvider(){

    }
    public static Bus provide(){
        return InnerClass.instance ;
    }

    private static class InnerClass{
        private static Bus instance = new Bus(ThreadEnforcer.ANY);
    }
}
