package com.sunshine.lib.skin.attr;

// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.sunshine.lib.skin.bean.SkinInfo;

/**
 * Created by 钟光燕 on 2016/10/13.
 * e-mail guangyanzhong@163.com
 */

public class AttrImgSrc extends SkinAttr{
    @Override
    public void apply(View view, SkinInfo info) {

        if (view instanceof ImageView){
            ImageView img = (ImageView) view;
            int resId = info.resources.getIdentifier(attrValueName, attrType, info.pkgName) ;
            Drawable drawable = info.resources.getDrawable(resId,info.theme) ;
            img.setImageDrawable(drawable);
        }
    }
}
