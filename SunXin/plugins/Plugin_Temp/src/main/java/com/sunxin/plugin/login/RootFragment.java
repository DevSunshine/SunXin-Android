package com.sunxin.plugin.login;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunshine.sunxin.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 钟光燕 on 2016/8/4.
 * e-mail guangyanzhong@163.com
 */
public class RootFragment extends BaseFragment {
    private SwapListView mListView ;
    private DragMutilLayout mDragMutilLayout ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.drag_layout,container,false) ;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getTitleView().setTitle("调试啥拓展");
        getTitleView().addRightBtn(R.drawable.ic_cut_white, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }) ;
        getTitleView().addRightBtn(R.drawable.ic_copy_white, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }) ;

        getTitleView().addLeftBtn(R.drawable.btn_back, "返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        }) ;

        mDragMutilLayout = (DragMutilLayout) view.findViewById(R.id.id_drag);
//        mDragMutilLayout.setData(initList());
        MaxGridView[] gridViews = mDragMutilLayout.getGridViews() ;
        GridAdapter[] gridAdapters = mDragMutilLayout.getGridAdapters() ;
        for (int j = 0; j < gridViews.length; j++) {
            List<String> data = new ArrayList<>();
            gridAdapters[j] = new GridAdapter(data);
            gridViews[j].setAdapter(gridAdapters[j]);
        }
        List<String> data = initList() ;
        if (data != null) {
            if (data.size() > 3) {
                gridAdapters[0].getDataList().clear();
                gridAdapters[0].getDataList().addAll(data.subList(0, 3));
                gridAdapters[0].notifyDataSetChanged();
                gridAdapters[1].getDataList().clear();
                gridAdapters[1].getDataList().addAll(data.subList(3, data.size()));
                gridAdapters[1].notifyDataSetChanged();
            } else {
                gridAdapters[0].getDataList().clear();
                gridAdapters[0].getDataList().addAll(data.subList(0, data.size()));
                gridAdapters[0].notifyDataSetChanged();
            }
        }
//        mListView = (SwapListView) view.findViewById(R.id.id_swap_list_view) ;
//        mListView.setAdapter(new StringAdapter(initList()));

    }
    private List<String> initList() {
        List<String> list = new ArrayList<String>();
        for (char i = 'a'; i < 's'; i++) {
            list.add(String.valueOf(i) + "--->>");
        }
        for (char i = 'A'; i < 'Z'; i++) {
            list.add(String.valueOf(i)+ "--->>");
        }
        Log.v("zgy", "===========list=======" + list);
        return list;
    }

}
