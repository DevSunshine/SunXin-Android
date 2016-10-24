package com.sunxin.plugin.reader.main;

// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.content.Context;
import android.view.ViewGroup;

import com.sunshine.sunxin.beans.book.RecommendBook;
import com.sunshine.sunxin.widget.recyclerview.adapter.BaseViewHolder;
import com.sunshine.sunxin.widget.recyclerview.adapter.RecyclerArrayAdapter;

/**
 * Created by 钟光燕 on 2016/10/24.
 * e-mail guangyanzhong@163.com
 */

public class BookAdapter extends RecyclerArrayAdapter<RecommendBook> {
    public BookAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup viewGroup, int i) {
        return new BaseViewHolder<RecommendBook>(viewGroup, R.layout.adapter_book_recommend) {
            @Override
            public void setData(RecommendBook item) {

            }
        };
    }
}
