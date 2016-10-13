package com.sunshine.lib.skin.skip;

// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 钟光燕 on 2016/10/13.
 * e-mail guangyanzhong@163.com
 */

public class SkinPage extends Skin implements SkinApply {

    public List<SkinView> skins = new ArrayList<>();

    @Override
    public void apply() {
        for (SkinView view : skins) {
            view.apply();
        }
    }
}
