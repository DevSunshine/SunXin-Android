package com.sunxin.plugin.login;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by 钟光燕 on 2016/8/30.
 * e-mail guangyanzhong@163.com
 */
public class GridAdapter extends BaseAdapter<GridAdapter.ViewHolder,String> {

    private int mHidePosition = -1 ;
    private List<String> data ;
    public GridAdapter(List<String> list) {
        super(list);
        data = list ;
    }

    @Override
    public ViewHolder createViewHolder(LayoutInflater inflater, int position) {
        return new ViewHolder(inflater.inflate(R.layout.top_item,null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mView.setVisibility(View.VISIBLE);
        holder.mText.setText("---"+getDataList().get(position));
        if (mHidePosition == position){
            holder.mView.setVisibility(View.INVISIBLE);
        }
        holder.mIcon.setImageResource(R.drawable.header_icon);
    }

    static class ViewHolder extends BaseAdapter.BaseViewHolder{
        private TextView mText ;
        private ImageView mIcon ;
        protected ViewHolder(View mView) {
            super(mView);
            mText = (TextView) mView.findViewById(R.id.id_text);
            mIcon = (ImageView) mView.findViewById(R.id.id_icon_test);
        }
    }


    public void swapItem(int oldPosition, int newPosition) {
        String temp = data.get(oldPosition);
        if(oldPosition < newPosition){
            for(int i=oldPosition; i<newPosition; i++){
                Collections.swap(data, i, i + 1);
            }
        }else if(oldPosition > newPosition){
            for(int i=oldPosition; i>newPosition; i--){
                Collections.swap(data, i, i-1);
            }
        }
        data.set(newPosition, temp);
        notifyDataSetChanged();
    }

    public void hidePosition(int position){
        mHidePosition = position ;
        notifyDataSetChanged();
    }
}
