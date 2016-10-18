package com.sunshine.sunxin.skin.attr;

// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.content.res.ColorStateList;
import android.view.View;
import android.widget.TextView;

import com.sunshine.sunxin.skin.bean.SkinInfo;
import com.sunshine.sunxin.view.MainTab;

/**
 * Created by 钟光燕 on 2016/10/13.
 * e-mail guangyanzhong@163.com
 */

public class AttrTextColor extends SkinAttr {
    @Override
    public void apply(View view, SkinInfo info) {

        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            int resId = info.resources.getIdentifier(attrValueName, attrType, info.pkgName);
            ColorStateList colorStateList = info.resources.getColorStateList(resId);
            if (colorStateList != null) {
                textView.setTextColor(colorStateList);
            } else {
                int color = info.resources.getColor(resId);
                textView.setTextColor(color);
            }
        }else if (view instanceof MainTab){
            MainTab tab = (MainTab) view;
            int resId = info.resources.getIdentifier(attrValueName, attrType, info.pkgName);
            ColorStateList colorStateList = info.resources.getColorStateList(resId);
            tab.setColorStateList(colorStateList);
        }

    }
}
