package com.sunshine.plugin.test;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunshine.sunxin.TestJava;

import butterknife.BindView;

/**
 * Created by 钟光燕 on 2016/7/22.
 * e-mail guangyanzhong@163.com
 */
public class Test extends Fragment{
    @BindView(0)
    TextView textView ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String string = getActivity().getResources().getString(R.string.app_name) ;
        TestJava testJava = new TestJava() ;
        ActionBar.Tab tab = new ActionBar.Tab() {
            @Override
            public int getPosition() {
                return 0;
            }

            @Override
            public Drawable getIcon() {
                return null;
            }

            @Override
            public CharSequence getText() {
                return null;
            }

            @Override
            public ActionBar.Tab setIcon(Drawable drawable) {
                return null;
            }

            @Override
            public ActionBar.Tab setIcon(@DrawableRes int i) {
                return null;
            }

            @Override
            public ActionBar.Tab setText(CharSequence charSequence) {
                return null;
            }

            @Override
            public ActionBar.Tab setText(int i) {
                return null;
            }

            @Override
            public ActionBar.Tab setCustomView(View view) {
                return null;
            }

            @Override
            public ActionBar.Tab setCustomView(int i) {
                return null;
            }

            @Override
            public View getCustomView() {
                return null;
            }

            @Override
            public ActionBar.Tab setTag(Object o) {
                return null;
            }

            @Override
            public Object getTag() {
                return null;
            }

            @Override
            public ActionBar.Tab setTabListener(ActionBar.TabListener tabListener) {
                return null;
            }

            @Override
            public void select() {

            }

            @Override
            public ActionBar.Tab setContentDescription(int i) {
                return null;
            }

            @Override
            public ActionBar.Tab setContentDescription(CharSequence charSequence) {
                return null;
            }

            @Override
            public CharSequence getContentDescription() {
                return null;
            }
        };
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
