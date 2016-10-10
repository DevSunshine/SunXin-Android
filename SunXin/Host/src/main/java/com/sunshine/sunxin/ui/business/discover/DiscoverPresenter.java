package com.sunshine.sunxin.ui.business.discover;

// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import com.sunshine.sunxin.base.RxBasePersenter;
import com.sunshine.sunxin.beans.discover.Function;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by 钟光燕 on 2016/10/10.
 * e-mail guangyanzhong@163.com
 */

public class DiscoverPresenter extends RxBasePersenter<DiscoverMVP.View> implements DiscoverMVP.Presenter<DiscoverMVP.View> {
    @Override
    public void getFunctions() {

        Subscription subscription = Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                subscriber.onNext("fuck");
            }
        }).observeOn(Schedulers.io())
          .map(new Func1<Object, List<Function>>() {
                    @Override
                    public List<Function> call(Object o) {
                        List<Function> functions = new ArrayList<>();
                        functions.add(new Function("朋友圈", "menu_add_friends", "", "a"));
                        functions.add(new Function("扫一扫", "menu_add_friends", "", "b"));
                        functions.add(new Function("摇一摇", "menu_add_friends", "", "b"));
                        functions.add(new Function("附近的人", "menu_add_friends", "", "c"));
                        functions.add(new Function("漂流瓶", "menu_add_friends", "", "c"));
                        functions.add(new Function("阅读", "menu_add_friends", "", "d"));
                        functions.add(new Function("游戏", "menu_add_friends", "", "d"));
                        return functions;
                    }
          }).observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Action1<List<Function>>() {
                    @Override
                    public void call(List<Function> functions) {
                        view.showFunctions(functions);
                    }
          });
        addSubscribe(subscription);

    }

}
