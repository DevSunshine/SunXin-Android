package com.sunshine.sunxin.ui.business.msg;

// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

/**
 * Created by 钟光燕 on 2016/9/30.
 * e-mail guangyanzhong@163.com
 */

import android.content.Context;
import android.view.ViewGroup;

import com.sunshine.sunxin.R;
import com.sunshine.sunxin.beans.msg.SunXinSession;
import com.sunshine.sunxin.widget.recyclerview.adapter.BaseViewHolder;
import com.sunshine.sunxin.widget.recyclerview.adapter.RecyclerArrayAdapter;

/**
 * 会话session
 */
public class SessionAdapter extends RecyclerArrayAdapter<SunXinSession> {
    public SessionAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder<SunXinSession>(parent, R.layout.adapter_session) {
            @Override
            public void setData(SunXinSession item) {

            }
        };
    }
}
