package com.sunshine.sunxin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sunshine.sun.lib.socket.bean.UserAccount;
import com.sunshine.sun.lib.socket.toolbox.SSSocket;
import com.sunshine.sunxin.plugin.PluginConstant;
import com.sunshine.sunxin.plugin.RootPluginActivity;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getTitleView().hide();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void startPlugin1(View view){
        Intent intent = new Intent(this,RootPluginActivity.class) ;
        intent.putExtra(PluginConstant.INTENT_PLUGIN_ID_KEY, "101") ;
        startActivity(intent);
    }
    public void startPlugin2(View view){
        Intent intent = new Intent(this,RootPluginActivity.class) ;
        intent.putExtra(PluginConstant.INTENT_PLUGIN_ID_KEY, "102") ;
        startActivity(intent);
    }
    public void startPlugin3(View view){
        Intent intent = new Intent(this,RootPluginActivity.class) ;
        intent.putExtra(PluginConstant.INTENT_PLUGIN_ID_KEY, "103") ;
        startActivity(intent);
    }
    public void connectSocket(View view){
        UserAccount account = new UserAccount() ;
        account.setAddress("10.25.0.149");
        account.setPort(5013);
        SSSocket.instance().initSocket(account);
    }

}
