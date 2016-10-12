package com.sunxin.plugin.reader.main;

import com.sunshine.sunxin.base.RxBasePersenter;
import com.sunshine.sunxin.beans.Function;

import java.util.ArrayList;
import java.util.List;


// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

/**
 * Created by 钟光燕 on 2016/10/12.
 * e-mail guangyanzhong@163.com
 */

public class ReaderMenuPresenter extends RxBasePersenter<ReaderMenuMVP.View> implements ReaderMenuMVP.Presenter<ReaderMenuMVP.View> {
    @Override
    public void getFunctions() {
        List<Function> functions = new ArrayList<>() ;
        functions.add(new Function()) ;
        functions.add(new Function()) ;
        functions.add(new Function()) ;
        functions.add(new Function()) ;
        functions.add(new Function()) ;
        view.showFunctions(functions);
    }
}
