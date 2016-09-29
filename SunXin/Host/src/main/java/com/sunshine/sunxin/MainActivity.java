package com.sunshine.sunxin;

import android.os.Bundle;
import android.view.View;

import com.sunshine.sunxin.view.MainTab;


public class MainActivity extends BaseActivity {

    private MainTab mainTab ;
    private MainFragmentHelper mHelper ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getTitleView().setTitle("深信");
        getTitleView().addRightBtn(R.drawable.ic_copy_white, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }) ;
        mainTab = (MainTab) findViewById(R.id.id_main_tab);
        mHelper = new MainFragmentHelper(getSupportFragmentManager(),R.id.id_content_fragment) ;
        mainTab.setFragmentHelper(mHelper);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }

//    public void startPlugin1(View view){
//        Intent intent = new Intent(this,RootPluginActivity.class) ;
//        intent.putExtra(PluginConstant.INTENT_PLUGIN_ID_KEY, "101") ;
//        intent.putExtra(PluginConstant.INTENT_SHOW_TITLE_KEY, false) ;
//        intent.putExtra(PluginConstant.INTENT_TINT_COLOR_KEY, 0xffe2e3e7) ;
//        startActivity(intent);
//    }
//    public void startPlugin2(View view){
//        Intent intent = new Intent(this,RootPluginActivity.class) ;
//        intent.putExtra(PluginConstant.INTENT_PLUGIN_ID_KEY, "102") ;
////        intent.putExtra(PluginConstant.INTENT_SHOW_TITLE_KEY, false) ;
//        intent.putExtra(PluginConstant.INTENT_TINT_FULL_KEY, true) ;
//        intent.putExtra(PluginConstant.INTENT_TINT_COLOR_KEY, 0x44000000) ;
//        startActivity(intent);
//    }
//    public void startPlugin3(View view){
//        Intent intent = new Intent(this,RootPluginActivity.class) ;
//        intent.putExtra(PluginConstant.INTENT_PLUGIN_ID_KEY, "103") ;
//        startActivity(intent);
//    }
//    public void startPlugin4(View view){
//        Intent intent = new Intent(this,RootPluginActivity.class) ;
//        intent.putExtra(PluginConstant.INTENT_PLUGIN_ID_KEY, "106") ;
//        startActivity(intent);
//    }
//    public void connectSocket(View view){
//        UserAccount account = new UserAccount() ;
//        account.setAddress("10.25.0.149");
//        account.setPort(5013);
//        SSSocket.instance().initSocket(account);
//    }

}
