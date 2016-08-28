package com.sunxin.plugin.personal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunshine.sunxin.BaseFragment;

/**
 * Created by gyzhong on 16/8/24.
 */
public class PersonalFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.test_layouut,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getTitleView().hideTitleLine();
        getTitleView().addLeftBtn(R.drawable.btn_back, "返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        }) ;
        getTitleView().addLeftBtn("改变", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTitleView().showTitleLine();
            }
        }) ;
        getTitleView().setTitleBottomHide();
        getTitleView().setTitle("钟光燕") ;
        getTitleView().addRightBtn("向上", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTitleView().setTitlePosition(-5);
            }
        });
        getTitleView().addRightBtn("向下", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTitleView().setTitlePosition(5);
            }
        });
    }
}
