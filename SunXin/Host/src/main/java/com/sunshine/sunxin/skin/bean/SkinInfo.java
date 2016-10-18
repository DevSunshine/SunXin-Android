package com.sunshine.sunxin.skin.bean;

// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.content.res.Resources;

/**
 * Created by 钟光燕 on 2016/10/13.
 * e-mail guangyanzhong@163.com
 */

public class SkinInfo {

    public Resources resources ;

    public String pkgName ;

    public Resources.Theme theme ;

    public SkinInfo(Resources resources, Resources.Theme theme,String pkgName) {
        this.resources = resources;
        this.pkgName = pkgName;
        this.theme = theme ;
    }
}
