package com.sunshine.sunxin;

import android.os.Bundle;

import com.sunshine.sunxin.view.MainTab;


public class MainActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getTitleView().hide();
        MainTab mainTab = (MainTab) findViewById(R.id.id_main_tab);
        MainFragmentHelper mHelper = new MainFragmentHelper(getSupportFragmentManager(),R.id.id_content_fragment) ;
        mainTab.setFragmentHelper(mHelper);
        mHelper.setCurrentItem(MainFragmentHelper.TAB_MSG_FRAGMENT);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }
}
