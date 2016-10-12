package com.sunxin.plugin.reader.main;

// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.content.Context;
import android.view.ViewGroup;

import com.sunshine.sunxin.beans.Function;
import com.sunshine.sunxin.widget.recyclerview.adapter.BaseViewHolder;
import com.sunshine.sunxin.widget.recyclerview.adapter.RecyclerArrayAdapter;

/**
 * Created by 钟光燕 on 2016/10/12.
 * e-mail guangyanzhong@163.com
 */

public class ReaderMenuAdapter extends RecyclerArrayAdapter<Function> {
    public ReaderMenuAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup viewGroup, int i) {
        return new BaseViewHolder<Function>(viewGroup,R.layout.item_menu) {
            @Override
            public void setData(Function item) {
                super.setData(item);
            }
        };
    }
}
