package com.sunshine.sunxin.loader;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import com.squareup.picasso.Picasso;
import com.sunshine.sunxin.App;

/**
 * Created by 钟光燕 on 2016/8/11.
 * e-mail guangyanzhong@163.com
 */
public class PicassoLoader implements ImgLoader {

    private Picasso mPicasso ;

    public PicassoLoader() {
        mPicasso = Picasso.with(App.instance) ;
    }
}
