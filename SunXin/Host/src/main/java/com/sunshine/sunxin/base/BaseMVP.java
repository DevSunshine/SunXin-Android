package com.sunshine.sunxin.base;

// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

/**
 * Created by 钟光燕 on 2016/10/9.
 * e-mail guangyanzhong@163.com
 *
 * MVP base view 和  base presenter
 */

public interface BaseMVP {

    interface BaseView{

        void showError() ;

        void complete() ;

    }

    interface BasePresenter<T>{

        void attachView(T view) ;

        void detachView() ;
    }
}
