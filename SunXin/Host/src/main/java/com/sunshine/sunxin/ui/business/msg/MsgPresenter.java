package com.sunshine.sunxin.ui.business.msg;

import com.sunshine.sunxin.base.RxBasePersenter;
import com.sunshine.sunxin.beans.msg.SunXinSession;

import java.util.ArrayList;
import java.util.List;


// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

/**
 * Created by 钟光燕 on 2016/10/11.
 * e-mail guangyanzhong@163.com
 */

public class MsgPresenter extends RxBasePersenter<MsgMVP.View> implements MsgMVP.Presenter<MsgMVP.View> {

    @Override
    public void getSessions() {
        List<SunXinSession> sunXinSessions = new ArrayList<>() ;
        for (int i = 0 ; i < 10; i ++){
            sunXinSessions.add(new SunXinSession()) ;
        }
        view.showSession(sunXinSessions);
    }
}
