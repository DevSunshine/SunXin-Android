package com.sunshine.sun.lib.socket.request;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.text.TextUtils;


import com.sunshine.sun.lib.socket.SSClient;
import com.sunshine.sun.lib.socket.SSClientQueue;
import com.sunshine.sun.lib.socket.SSHeader;
import com.sunshine.sun.lib.socket.SSITranslation;
import com.sunshine.sun.lib.socket.SSResponse;
import com.sunshine.sun.lib.socket.toolbox.ExecuteType;
import com.sunshine.sun.lib.socket.toolbox.Priority;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 钟光燕 on 2016/7/14.
 * e-mail guangyanzhong@163.com
 */
public abstract class SSTask implements SSITranslation{

    private Priority mPriority ;
    private String mSequence;
    private SSClientQueue mClientQueue ;
    private SSITaskListener mTaskListener ;
    private boolean mCanceled ;
    private List<SSHeader> mHeaders = new ArrayList<>();
    private SSClient mClient ;
    private short mMethod ;
    private ExecuteType mExecuteType ;
    private byte[] mSyncLock = new byte[0] ;
    private SSResponse mResponse ;

    public SSTask(short mMethod) {
        this.mMethod = mMethod;
        mExecuteType = ExecuteType.async ;
    }

    public SSTask setPriority(Priority priority){
        mPriority = priority ;
        return this ;
    }

    public Priority getPriority(){
        return mPriority ;
    }

    public final SSTask setSequence(int sequence){
        mSequence = String.valueOf(sequence);
        return this ;
    }

    public String getSequence(){
        return mSequence ;
    }

    public SSTask setRequestQueue(SSClientQueue queue) {
        mClientQueue = queue;
        return this;
    }

    public SSTask setTaskListener(SSITaskListener listener){
        if (mExecuteType == ExecuteType.async){
            mTaskListener = listener ;
        }
        return this ;
    }

    public void cancel(){
        this.mCanceled = true;
    }

    public boolean canceled(){
        return mCanceled ;
    }

    public SSTask addHeader(short key, String value){

        return this ;
    }

    public SSTask addHeader(short key, long value){

        return this ;
    }

    public SSHeader getHeader(short type){
        for (int i = 0 ; i < mHeaders.size() ; i ++){
            if (type == mHeaders.get(i).getType()){
                return mHeaders.get(i) ;
            }
        }
        return null ;
    }

    public SSHeader getHeader(String value){
        for (int i = 0 ; i < mHeaders.size() ; i ++){
            if (TextUtils.equals(value,mHeaders.get(i).getStringValue())){
                return mHeaders.get(i) ;
            }
        }
        return null ;
    }

    public List<SSHeader> getHeaderList(){
        return mHeaders ;
    }

    public SSClient getClient() {
        return mClient;
    }

    public void setClient(SSClient mClient) {
        this.mClient = mClient;
    }

    public short getMethod() {
        return mMethod;
    }

    public SSTask setMethod(short mMethod) {
        this.mMethod = mMethod;
        return this ;
    }

    public SSTask setExecuteType(ExecuteType type){
        this.mExecuteType = type ;
        return this ;
    }

    public ExecuteType getExecuteType(){
        return mExecuteType ;
    }

    public void execute(){

    }

    /**
     * 同步线程锁
     */
    public void lockWait(){
        if (mExecuteType == ExecuteType.sync){
            try {
                mSyncLock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void finish(){
        if (mClientQueue != null){
            mClientQueue.finish(this);
        }
    }

    public SSResponse getResponse(){
        return mResponse ;
    }

    @Override
    public void onRequestEnd() {

    }

    @Override
    public void onProgressReceive() {

    }

    @Override
    public void onCompleteReceive() {
        if (mExecuteType == ExecuteType.sync){
            mSyncLock.notify();
            mResponse = null ;
        }
        if (mTaskListener != null){
            mTaskListener.onComplete(this,null);
        }
    }
}
