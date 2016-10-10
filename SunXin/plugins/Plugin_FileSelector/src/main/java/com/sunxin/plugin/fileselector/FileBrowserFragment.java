package com.sunxin.plugin.fileselector;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunshine.sunxin.BaseFragment;
import com.sunxin.plugin.fileselecter.R;
import com.sunxin.plugin.fileselector.adapter.FileBrowserAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by 钟光燕 on 2016/8/16.
 * e-mail guangyanzhong@163.com
 */
public class FileBrowserFragment extends BaseFragment {
    private RecyclerView mFileListView;
    private FileBrowserAdapter mAdapter;
    private Stack<ViewStatue> statueStack = new Stack<>();
    private String mRootPath;
    private String mCurrentPath;

    private TextView mTitle;
    private TextView mBackTitle;
    private TextView mFileNum;
    private TextView mFileSend;
    private List<FileInfo> mFileInfos = new ArrayList<>();
    private LinearLayoutManager mManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.root_file_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitle = getTitleView().setTitle("文件浏览");
        mFileNum = (TextView) view.findViewById(R.id.id_file_select_num);
        mFileSend = (TextView) view.findViewById(R.id.id_file_send_btn);
        mFileListView = (RecyclerView) view.findViewById(R.id.id_root_file_list);
        mManager = new LinearLayoutManager(getContext());
        mFileListView.setLayoutManager(mManager);
        mAdapter = new FileBrowserAdapter(this);
        mFileListView.setAdapter(mAdapter);
                mBackTitle = getTitleView().addLeftBtn(R.drawable.btn_back, "返回", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        back();
            }
        });
        resetBottomView();
        mRootPath = "/";
        openRootDir();
    }


    public void openDir(FileInfo fileInfo) {
        saveStatue();
        openDir(fileInfo.path, false);
    }

    private void openDir(final String path, final boolean back) {
        //show loading

        /**
         * 异步操作
         */
        final List<FileInfo> fileInfos = new ArrayList<>();
        mCurrentPath = path;
        if (mCurrentPath == null || TextUtils.equals(mCurrentPath, mRootPath)) {
            mBackTitle.setText("返回");
            mTitle.setText("手机内存");
        } else {
            mBackTitle.setText("上一级");
            String name = new File(path).getName();
            mTitle.setText(name);
        }

        subscription = Observable.just(path)
                .map(new Func1<String, List<FileInfo>>() {
                    @Override
                    public List<FileInfo> call(String s) {
                        FileUtil.getFiles(fileInfos, s);
                        for (FileInfo fileInfo : mFileInfos) {
                            for (FileInfo file : fileInfos) {
                                if (file.isDir) {
                                    continue;
                                }
                                if (fileInfo.path.equals(file.path)) {
                                    file.selected = fileInfo.selected;
                                    break;
                                }
                            }
                        }
                        return fileInfos;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<FileInfo>>() {
                    @Override
                    public void call(List<FileInfo> fileInfos) {
                        mAdapter.setFile(fileInfos);
                        if (statueStack.size() > 0 && back) {
                            ViewStatue viewStatue = statueStack.pop();
                            mManager.scrollToPositionWithOffset(viewStatue.position, viewStatue.top);
                        }
                    }
                });

    }

    public void handleFile(FileInfo fileInfo) {

        if (fileInfo.selected) {
            mFileInfos.add(fileInfo);
        } else {
            for (FileInfo file : mFileInfos) {
                if (file.path.equals(fileInfo.path)) {
                    mFileInfos.remove(file);
                    break;
                }
            }
        }

        resetBottomView();
    }

    private void openRootDir() {
        openDir(mRootPath, false);
    }


    private void back() {
        if (mCurrentPath == null || TextUtils.equals(mCurrentPath, mRootPath)) {
            getActivity().finish();
        } else {
            String parentPath = new File(mCurrentPath).getParent();
            openDir(parentPath, true);

        }
    }

    private void resetBottomView() {
        mFileNum.setText(String.format("已选%d个", mFileInfos.size()));
        mFileNum.setEnabled(mFileInfos.size() != 0);
        mFileSend.setEnabled(mFileInfos.size() != 0);
    }


    private void saveStatue() {
        int position = mManager.findFirstVisibleItemPosition();
        int top = 0;
        View item = mManager.findViewByPosition(position);
        if (item != null) {
            top = item.getTop();
        }
        ViewStatue viewStatue = new ViewStatue(position, top);
        statueStack.push(viewStatue);
    }

}
