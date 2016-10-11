package com.sunshine.sunxin.ui.business.msg;

// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import com.sunshine.sunxin.base.BaseMVP;
import com.sunshine.sunxin.beans.msg.SunXinSession;

import java.util.List;

/**
 * Created by 钟光燕 on 2016/10/11.
 * e-mail guangyanzhong@163.com
 */

public interface MsgMVP {

    interface View extends BaseMVP.BaseView{

        void showSession(List<SunXinSession> sessions) ;

    }

    interface Presenter<T> extends BaseMVP.BasePresenter<T>{

        void getSessions() ;

    }
}
