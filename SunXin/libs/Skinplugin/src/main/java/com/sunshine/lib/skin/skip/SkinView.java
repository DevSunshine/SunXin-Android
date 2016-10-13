package com.sunshine.lib.skin.skip;

// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.view.View;

import com.sunshine.lib.skin.attr.SkinAttr;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 钟光燕 on 2016/10/13.
 * e-mail guangyanzhong@163.com
 */

public class SkinView extends Skin implements SkinApply {

    public View view ;

    List<SkinAttr> attrs = new ArrayList<>() ;
    @Override
    public void apply() {

        for (SkinAttr attr : attrs){
            attr.apply(view, info);
        }

    }
}
