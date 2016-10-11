package com.sunshine.sunxin.ui.business.mine;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunshine.sunxin.R;
import com.sunshine.sunxin.beans.Function;
import com.sunshine.sunxin.widget.recyclerview.adapter.BaseViewHolder;
import com.sunshine.sunxin.widget.recyclerview.adapter.RecyclerArrayAdapter;


// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

/**
 * Created by 钟光燕 on 2016/10/11.
 * e-mail guangyanzhong@163.com
 */

public class MineAdapter extends RecyclerArrayAdapter<Function> {
    public MineAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder<Function>(parent, R.layout.adapter_contacts) {
            @Override
            public void setData(Function item) {

                View line = holder.getView(R.id.id_contacts_line) ;
                updateMineLine(line,holder.getAdapterPosition() - 1);
                TextView index = holder.getView(R.id.id_contacts_index) ;
                updateMineIndex(index,holder.getAdapterPosition() - 1) ;
                holder.setText(R.id.id_contacts_name,item.name) ;
            }
            private void updateMineLine(View line, int position) {
                if (compareNext(position)) {
                    line.setVisibility(View.VISIBLE);
                } else {
                    line.setVisibility(View.GONE);
                }
            }

            private void updateMineIndex(TextView index, int position) {
                if (comparePre(position)) {
                    index.setVisibility(View.GONE);
                } else {
                    index.setVisibility(View.VISIBLE);

                }
            }

            /**
             * 与前一个item对比，字母是否相等
             *
             * @return true 表示相等
             */
            private boolean comparePre(int currentPosition) {
                int prePosition = currentPosition - 1;
                if (prePosition >= 0) {
                    if (TextUtils.equals(getAllData().get(currentPosition).orderIndex.toUpperCase()
                            , getAllData().get(prePosition).orderIndex.toUpperCase())) {
                        return true;
                    }
                }
                return false;
            }

            /**
             * 与后一个item对比，字母是否相等
             *
             * @return true 表示相等
             */
            private boolean compareNext(int currentPosition) {
                int nextPosition = currentPosition + 1;
                if (getAllData().size() - 1 >= nextPosition) {
                    if (TextUtils.equals(getAllData().get(currentPosition).orderIndex.toUpperCase()
                            , getAllData().get(nextPosition).orderIndex.toUpperCase())) {
                        return true;
                    }
                }
                return false;
            }
        };
    }


}
