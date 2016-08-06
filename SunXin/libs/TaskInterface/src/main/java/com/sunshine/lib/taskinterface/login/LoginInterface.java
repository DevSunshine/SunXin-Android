package com.sunshine.lib.taskinterface.login;

import com.sunshine.lib.taskinterface.BaseInterface;
import com.sunshine.sun.lib.socket.SSResponse;
import com.sunshine.sun.lib.socket.request.SSTask;
import com.sunshine.sun.lib.socket.request.SSTaskManager;
import com.sunxin.lib.protobuf.TestProto;

/**
 * Created by gyzhong on 16/8/6.
 */
public class LoginInterface extends BaseInterface{

    private ILoginListener mLoginListener ;

    public void login(String userName,String password, ILoginListener lister){
        SSTask task = SSTaskManager.instance().createLoginTask() ;
        mLoginListener = lister ;
        task.setTaskListener(this) ;
        TestProto.UserLoginArg.Builder builder = TestProto.UserLoginArg.newBuilder() ;
        builder.setUserName(userName) ;
        builder.setPassword(password) ;
        TestProto.UserLoginArg arg = builder.build() ;
        task.addBody(arg.toByteArray()) ;
        commitTask(task);
    }



    @Override
    public void onComplete(SSTask task, SSResponse response) {
        //save db;

        if (mLoginListener != null){
            mLoginListener.loginSuccess();
        }
    }

    @Override
    public void onError(SSTask task, int errorCode) {
        // TODO: 16/8/6
        if (mLoginListener != null){
            mLoginListener.loginFailed("未知错误");
        }
    }
}
