package com.sunxin.plugin.reader.main;

// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import com.sunshine.sunxin.base.BaseMVP;
import com.sunshine.sunxin.beans.Function;

import java.util.List;

/**
 * Created by 钟光燕 on 2016/10/12.
 * e-mail guangyanzhong@163.com
 */

public interface ReaderMenuMVP {

    interface View extends BaseMVP.BaseView{
        void showFunctions(List<Function> functions) ;
    }

    interface Presenter<T> extends BaseMVP.BasePresenter<T>{
        void getFunctions() ;
    }
}
