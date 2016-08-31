package com.sunxin.plugin.login;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;

/**
 * Created by moon.zhong on 2015/3/11.
 */
public abstract class BaseAdapter<T extends BaseAdapter.BaseViewHolder, D> extends android.widget.BaseAdapter {

    private List<D> mDataList;
    HashMap<D, Integer> mIdMap = new HashMap<D, Integer>();

    public BaseAdapter(List<D> list) {
        mDataList = list;
        for (int i = 0; i < list.size(); ++i) {
            mIdMap.put(list.get(i), i);
        }
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public D getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (position < 0 || position >= mIdMap.size()) {
            return -1;
        }
        D item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        T viewHolder;
        if (convertView == null) {
            viewHolder = createViewHolder(LayoutInflater.from(parent.getContext()), position);
            convertView = viewHolder.mView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (T) convertView.getTag();
        }
        onBindViewHolder(viewHolder, position);
        return convertView;
    }

    public static abstract class BaseViewHolder {
        public final View mView;

        protected BaseViewHolder(View mView) {
            this.mView = mView;
        }

        public <K extends View> K findView(int id) {
            return (K) mView.findViewById(id);
        }
    }

    public List<D> getDataList() {
        return mDataList;
    }

    public abstract T createViewHolder(LayoutInflater inflater, int position);

    public abstract void onBindViewHolder(T holder, int position);
}
