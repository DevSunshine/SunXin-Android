package com.sunshine.lib.skin;

// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.sunshine.lib.skin.bean.SkinInfo;
import com.sunshine.lib.skin.skip.SkinPage;
import com.sunshine.lib.skin.skip.SkinView;
import com.sunshine.lib.skin.util.SkinSP;

import java.lang.reflect.Method;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by 钟光燕 on 2016/10/13.
 * e-mail guangyanzhong@163.com
 */

public class SkinManager {

    private Context mContext ;
    private Subscription mSubscription ;
    private String mSkinPath ;
    private SkinSP mSkinSP ;
    private Map<String,SkinPage> mSkinPages = new ArrayMap<>() ;
    private SkinInfo mSkinInfo ;

    private SkinManager(){
    }

    public static SkinManager getInstance(){
        return InnerInstance.instance ;
    }

    static class InnerInstance {
        private static SkinManager instance = new SkinManager();
    }

    public void init(Context context){
        mContext = context ;
        mSkinSP = new SkinSP(context) ;
        mSkinPath = mSkinSP.getCurrentSkinPath() ;
    }

    /**
     * 更换皮肤
     */
    public void changeSkin(final String skinPath, final SkinChangeListener listener){

        if (mSubscription != null&& mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
        mSubscription = Observable.just(skinPath)
                .map(new Func1<String, SkinInfo>() {
                    @Override
                    public SkinInfo call(String s) {
                        if (TextUtils.isEmpty(s)){
                            return null ;
                        }
                        try {
                            PackageManager mPm = mContext.getPackageManager();
                            PackageInfo mInfo = mPm.getPackageArchiveInfo(s, PackageManager.GET_ACTIVITIES);
                            AssetManager assetManager = AssetManager.class.newInstance();
                            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
                            addAssetPath.invoke(assetManager, s);
                            Resources superRes = mContext.getResources();
                            Resources resources = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
                            Resources.Theme theme = resources.newTheme();
                            theme.applyStyle(android.R.style.Theme, true);
                            return new SkinInfo(resources,theme,mInfo.packageName) ;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SkinInfo>() {
                    @Override
                    public void onCompleted() {
                        if (mSubscription.isUnsubscribed()){
                            mSubscription.unsubscribe();
                            mSubscription = null ;
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        if (listener != null){
                            listener.onError();
                        }

                    }

                    @Override
                    public void onNext(SkinInfo skinInfo) {

                        if (skinInfo != null){
                            mSkinSP.setCurrentSkinPath(skinPath);
                            convertSkinInfo(skinInfo) ;
                            if (listener != null){
                                listener.onComplete();
                            }
                        }else {
                            if (listener != null){
                                listener.onError();
                            }
                        }
                    }
                });
    }

    private void convertSkinInfo(SkinInfo info){
        for (SkinPage page : mSkinPages.values()){
            page.info = info ;
            applyItem(page);
        }
    }



    public void addSkinPage(String name,SkinPage page){
        if (mSkinPages.containsKey(name)){
            mSkinPages.remove(name) ;
        }
        mSkinPages.put(name,page) ;
    }

    public void removeSkinPage(String name){
        if (mSkinPages.containsKey(name)){
            mSkinPages.remove(name) ;
        }
    }


    public void applyItem(SkinPage page){
        page.apply();
    }

    public void applyView(SkinView view){
        if (mSkinInfo != null){
            view.info = mSkinInfo ;
            view.apply();
        }
    }


}
