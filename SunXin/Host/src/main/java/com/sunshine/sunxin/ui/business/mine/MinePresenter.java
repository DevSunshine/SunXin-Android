package com.sunshine.sunxin.ui.business.mine;

import android.util.Log;

import com.sunshine.sunxin.base.RxBasePersenter;
import com.sunshine.sunxin.beans.Function;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

/**
 * Created by 钟光燕 on 2016/10/11.
 * e-mail guangyanzhong@163.com
 */

public class MinePresenter extends RxBasePersenter<MineMVP.View> implements MineMVP.Presenter<MineMVP.View> {
    @Override
    public void getFunctions() {
        Subscription subscription = Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                subscriber.onNext(1);
            }
        }).subscribeOn(Schedulers.io())
                .map(new Func1<Object, List<Function>>() {
                    @Override
                    public List<Function> call(Object o) {
                        List<Function> functions = new ArrayList<>();
                        functions.add(new Function("相册", "menu_add_friends", "", "a"));
                        functions.add(new Function("收藏", "menu_add_friends", "", "b"));
                        functions.add(new Function("标签", "menu_add_friends", "", "b"));
                        functions.add(new Function("表情", "menu_add_friends", "", "c"));
                        functions.add(new Function("设置", "menu_add_friends", "", "d"));

                        return functions;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Function>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.v("onError","==========e============"+e) ;
                    }

                    @Override
                    public void onNext(List<Function> functions) {
                        view.showFunctions(functions);
                    }
                });
//                .subscribe(new Action1<List<Function>>() {
//                    @Override
//                    public void call(List<Function> functions) {
//
//                    }
//                });
        addSubscribe(subscription);
    }
}
