package com.sunxin.plugin.login;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private SwapListView mListView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (SwapListView) findViewById(R.id.id_swap_list_view) ;
        mListView.setAdapter(new StringAdapter(initList()));
    }

    private List<String> initList() {
        List<String> list = new ArrayList<String>();
        for (char i = 'a'; i < 'z'; i++) {
            list.add(String.valueOf(i) + "--->>");
        }
        for (char i = 'A'; i < 'Z'; i++) {
            list.add(String.valueOf(i)+ "--->>");
        }
        Log.v("zgy", "===========list=======" + list);
        return list;
    }
}
