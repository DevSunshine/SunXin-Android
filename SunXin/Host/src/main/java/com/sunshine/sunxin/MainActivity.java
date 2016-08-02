package com.sunshine.sunxin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.sunshine.sun.lib.socket.bean.UserAccount;
import com.sunshine.sun.lib.socket.toolbox.SSSocket;
import com.sunshine.sunxin.plugin.PluginConstant;
import com.sunshine.sunxin.plugin.RootPluginActivity;


public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void startPlugin(View view){
        Intent intent = new Intent(this,RootPluginActivity.class) ;
        intent.putExtra(PluginConstant.INTENT_PLUGIN_ID_KEY, "101") ;
        startActivity(intent);
    }
    public void connectSocket(View view){
        UserAccount account = new UserAccount() ;
        account.setAddress("10.25.0.149");
        account.setPort(5013);
        SSSocket.instance().initSocket(account);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            SSSocket.instance().logoutSocket();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
