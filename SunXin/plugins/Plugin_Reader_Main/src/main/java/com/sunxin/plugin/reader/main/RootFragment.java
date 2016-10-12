package com.sunxin.plugin.reader.main;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunshine.sunxin.base.BaseFragment;
import com.sunshine.sunxin.util.Util;

/**
 * Created by gyzhong on 16/8/14.
 *
 * 阅读首页
 */
public class RootFragment extends BaseFragment implements SlidingPaneLayout.PanelSlideListener{

    private SlidingPaneLayout mSlidingPaneLayout ;
    private View mMenuView ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.plugin_reader_main_layout,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSlidingPaneLayout = (SlidingPaneLayout) view.findViewById(R.id.id_reader_sliding) ;
        mSlidingPaneLayout.setSliderFadeColor(Color.TRANSPARENT);
        mSlidingPaneLayout.setPanelSlideListener(this);
        mMenuView = view.findViewById(R.id.id_reader_menu) ;
    }

    @Override
    public void onPanelSlide(View view, float v) {
        ViewCompat.setTranslationX(mMenuView, Util.dp2px(-140)*(1-v));
    }

    @Override
    public void onPanelOpened(View view) {

    }

    @Override
    public void onPanelClosed(View view) {

    }
}
