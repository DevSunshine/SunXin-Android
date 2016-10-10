package com.sunshine.sunxin.ui.business.discover;

// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunshine.sunxin.R;
import com.sunshine.sunxin.beans.discover.Function;
import com.sunshine.sunxin.plugin.PluginConstant;
import com.sunshine.sunxin.plugin.RootPluginActivity;

import java.util.List;

/**
 * Created by 钟光燕 on 2016/9/30.
 * e-mail guangyanzhong@163.com
 */

/**
 * 通讯录
 */
public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.ContactsViewHolder> {
    /**
     * 联系人数据集
     */
    private List<Function> mFunctions;

    public DiscoverAdapter() {
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_contacts, parent, false));
    }

    @Override
    public void onBindViewHolder(ContactsViewHolder holder, int position) {
        holder.mName.setText(mFunctions.get(position).name);
        holder.mIcon.setImageResource(R.drawable.icon_function_read);
        if (position == getItemCount() - 1){
            holder.mIcon.setImageResource(R.drawable.icon_function_game);
        }
        updateFunctionsIndex(holder.mIndexText, position);
        updateFunctionsLine(holder.mContactsLine, position);
        enterClickListener(holder.itemView, mFunctions.get(position));
    }

    public void setFunctionsList(List<Function> functionses) {
        mFunctions = functionses;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mFunctions == null ? 0 : mFunctions.size();
    }

    private void updateFunctionsLine(View line, int position) {
        if (compareNext(position)) {
            line.setVisibility(View.VISIBLE);
        } else {
            line.setVisibility(View.GONE);
        }
    }

    private void updateFunctionsIndex(TextView index, int position) {
        if (comparePre(position)) {
            index.setVisibility(View.GONE);
        } else {
//            index.setText(mFunctions.get(position).orderIndex);
            index.setVisibility(View.VISIBLE);

        }
    }

    private void enterClickListener(View view, final Function function) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RootPluginActivity.class);
                intent.putExtra(PluginConstant.INTENT_PLUGIN_ID_KEY, "106");
                v.getContext().startActivity(intent);
            }
        });
    }

    /**
     * 与前一个item对比，字母是否相等
     *
     * @return true 表示相等
     */
    private boolean comparePre(int currentPosition) {
        int prePosition = currentPosition - 1;
        if (prePosition >= 0) {
            if (TextUtils.equals(mFunctions.get(currentPosition).orderIndex.toUpperCase()
                    , mFunctions.get(prePosition).orderIndex.toUpperCase())) {
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
        if (mFunctions.size() - 1 >= nextPosition) {
            if (TextUtils.equals(mFunctions.get(currentPosition).orderIndex.toUpperCase()
                    , mFunctions.get(nextPosition).orderIndex.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder {

        private TextView mIndexText;
        private ImageView mIcon;
        private TextView mName;
        private View mContactsLine;

        public ContactsViewHolder(View itemView) {
            super(itemView);
            mIndexText = (TextView) itemView.findViewById(R.id.id_contacts_index);
            mIcon = (ImageView) itemView.findViewById(R.id.id_contacts_icon);
            mName = (TextView) itemView.findViewById(R.id.id_contacts_name);
            mContactsLine = itemView.findViewById(R.id.id_contacts_line);
        }
    }
}
