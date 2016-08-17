package com.sunxin.plugin.fileselector.adapter;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunxin.plugin.fileselecter.R;
import com.sunxin.plugin.fileselector.FileBrowserFragment;
import com.sunxin.plugin.fileselector.FileInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        return new BrowserViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_file_list,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(BrowserViewHolder browserViewHolder, int i) {
        final FileInfo fileInfo  = mFileInfos.get(i) ;
        browserViewHolder.mFileSelector.setVisibility(View.GONE);
        browserViewHolder.mFileName.setText(fileInfo.name);
        browserViewHolder.mFileCount.setVisibility(View.GONE);
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(new Date(fileInfo.lastModified)) ;
        if (!fileInfo.isDir){
            browserViewHolder.mFileSelector.setVisibility(View.VISIBLE);
            browserViewHolder.mFileIcon.setImageResource(R.drawable.unknown_file);
        }else {
            browserViewHolder.mFileCount.setVisibility(View.VISIBLE);
            browserViewHolder.mFileCount.setText(fileInfo.fileCount+"项");
            browserViewHolder.mFileIcon.setImageResource(R.drawable.folder);
        }
        browserViewHolder.mLastModified.setText(time);
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
        mFileInfos.clear();
        mFileInfos.addAll(files) ;
        notifyDataSetChanged();
    }

    class BrowserViewHolder extends RecyclerView.ViewHolder{
        private TextView mFileName ;
        private ImageView mFileSelector ;
        private ImageView mFileIcon ;
        private TextView mFileCount ;
        private TextView mLastModified ;
        public BrowserViewHolder(View itemView) {
            super(itemView);
            mFileName = (TextView) itemView.findViewById(R.id.id_file_name);
            mFileSelector = (ImageView) itemView.findViewById(R.id.id_file_selector);
            mFileIcon = (ImageView) itemView.findViewById(R.id.id_file_icon);
            mFileCount = (TextView) itemView.findViewById(R.id.id_file_count);
            mLastModified = (TextView) itemView.findViewById(R.id.id_file_modified);
        }
    }
}
