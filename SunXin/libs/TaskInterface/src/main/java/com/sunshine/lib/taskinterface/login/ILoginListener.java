package com.sunshine.lib.taskinterface.login;

/**
 * Created by gyzhong on 16/8/6.
 */
public interface ILoginListener {

    void loginSuccess() ;

    void loginFailed(String tip) ;
}
