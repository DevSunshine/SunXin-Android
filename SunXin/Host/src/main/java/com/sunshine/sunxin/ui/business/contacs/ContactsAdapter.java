package com.sunshine.sunxin.ui.business.contacs;

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
import com.sunshine.sunxin.beans.contacts.Contacts;
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
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {
    /**
     * 联系人数据集
     */
    private List<Contacts> mContactses;

    public ContactsAdapter() {
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_contacts, parent, false));
    }

    @Override
    public void onBindViewHolder(ContactsViewHolder holder, int position) {
        holder.mName.setText(mContactses.get(position).name);
        updateContactsIndex(holder.mIndexText, position);
        updateContactsLine(holder.mContactsLine, position);
        enterClickListener(holder.itemView, mContactses.get(position));
    }

    public void setContactsList(List<Contacts> contactses) {
        mContactses = contactses;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mContactses == null ? 0 : mContactses.size();
    }

    private void updateContactsLine(View line, int position) {
        if (compareNext(position)) {
            line.setVisibility(View.VISIBLE);
        } else {
            line.setVisibility(View.GONE);
        }
    }

    private void updateContactsIndex(TextView index, int position) {
        if (comparePre(position)) {
            index.setVisibility(View.GONE);
        } else {
            index.setText(mContactses.get(position).nameOrder);
            index.setVisibility(View.VISIBLE);

        }
    }

    private void enterClickListener(View view, final Contacts contacts) {
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
        if (prePosition > 0) {
            if (TextUtils.equals(mContactses.get(currentPosition).nameOrder.toUpperCase()
                    , mContactses.get(prePosition).nameOrder.toUpperCase())) {
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
        if (mContactses.size() - 1 >= nextPosition) {
            if (TextUtils.equals(mContactses.get(currentPosition).nameOrder.toUpperCase()
                    , mContactses.get(nextPosition).nameOrder.toUpperCase())) {
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
