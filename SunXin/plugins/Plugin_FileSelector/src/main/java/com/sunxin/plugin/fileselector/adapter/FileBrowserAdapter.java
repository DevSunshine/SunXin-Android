package com.sunxin.plugin.fileselector.adapter;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.sunxin.plugin.fileselector.FileBrowserFragment;
import com.sunxin.plugin.fileselector.FileInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 钟光燕 on 2016/8/16.
 * e-mail guangyanzhong@163.com
 */
public class FileBrowserAdapter extends RecyclerView.Adapter<FileBrowserAdapter.BrowserViewHolder> {

    private List<FileInfo> mFileInfos ;

    private FileBrowserFragment fragment ;

    public FileBrowserAdapter(FileBrowserFragment fragment) {
        this.fragment = fragment;
        mFileInfos = new ArrayList<>() ;
    }

    @Override
    public BrowserViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(BrowserViewHolder browserViewHolder, int i) {
        final FileInfo fileInfo  = mFileInfos.get(i) ;
        browserViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fileInfo.isDir){
                    fragment.openDir(fileInfo);
                }else {
                    fragment.handleFile(fileInfo);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFileInfos.size();
    }

    public void setFile(List<FileInfo> files){

        notifyDataSetChanged();
    }

    class BrowserViewHolder extends RecyclerView.ViewHolder{
        public BrowserViewHolder(View itemView) {
            super(itemView);
        }
    }
}
