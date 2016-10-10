package com.sunshine.sunxin.base;

// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by 钟光燕 on 2016/10/9.
 * e-mail guangyanzhong@163.com
 */

public class RxBasePersenter<T> implements BaseMVP.BasePresenter<T> {

    public T view;
    protected CompositeSubscription mCompositeSubscription;

    protected void unSubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    protected void addSubscribe(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void attachView(T view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
        if (mCompositeSubscription != null){
            mCompositeSubscription.unsubscribe();
        }
    }
}
