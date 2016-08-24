package com.sunshine.sunxin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.sunshine.sun.lib.socket.bean.UserAccount;
import com.sunshine.sun.lib.socket.toolbox.SSSocket;
import com.sunshine.sunxin.plugin.PluginConstant;
import com.sunshine.sunxin.plugin.RootPluginActivity;
import com.sunshine.sunxin.widget.checkbox.CheckBox;


public class MainActivity extends BaseActivity {

    private CheckBox checkBox ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getTitleView().hide();
        getTitleView().setTitle("深信");
        getTitleView().addRightBtn(R.drawable.ic_copy_white, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }) ;
        checkBox = (CheckBox) findViewById(R.id.id_checkBox);
        checkBox.setMarkColor(ContextCompat.getColorStateList(this,R.color.check_color_selector));
        checkBox.setChecked(false);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }

    public void startPlugin1(View view){
        Intent intent = new Intent(this,RootPluginActivity.class) ;
        intent.putExtra(PluginConstant.INTENT_PLUGIN_ID_KEY, "101") ;
        intent.putExtra(PluginConstant.INTENT_SHOW_TITLE_KEY, false) ;
        startActivity(intent);
    }
    public void startPlugin2(View view){
        Intent intent = new Intent(this,RootPluginActivity.class) ;
        intent.putExtra(PluginConstant.INTENT_PLUGIN_ID_KEY, "102") ;
//        intent.putExtra(PluginConstant.INTENT_SHOW_TITLE_KEY, false) ;
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
