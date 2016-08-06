package com.sunshine.sun.lib.socket.request;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.text.TextUtils;


import com.sunshine.sun.lib.socket.SSBody;
import com.sunshine.sun.lib.socket.SSClient;
import com.sunshine.sun.lib.socket.SSClientQueue;
import com.sunshine.sun.lib.socket.SSHeader;
import com.sunshine.sun.lib.socket.SSITranslation;
import com.sunshine.sun.lib.socket.SSResponse;
import com.sunshine.sun.lib.socket.toolbox.ExecuteType;
import com.sunshine.sun.lib.socket.toolbox.Priority;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by 钟光燕 on 2016/7/14.
 * e-mail guangyanzhong@163.com
 */
public abstract class SSTask implements SSITranslation,Comparable<SSTask>{

    private static int MAX_LENGTH_BODY = 65535 ;
    private Priority  mPriority = Priority.NORMAL ;
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
    private byte[] mBody ;

    public SSTask(short mMethod) {
        this.mMethod = mMethod;
        mExecuteType = ExecuteType.async;

    }

    public SSTask(){

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
        SSHeader header = new SSHeader() ;
        header.setTypeValue(key, value);
        mHeaders.add(header);
        return this ;
    }

    public SSTask addHeader(short key, long value){
        SSHeader header = new SSHeader(key) ;
        header.setValue(value);
        mHeaders.add(header);
        return this ;
    }

    public SSTask addBody(byte[] body){
        mBody = body ;
        return this ;
    }

    public List<SSBody> getBodies(){
        List<SSBody> bodies = new ArrayList<>() ;
        if (mBody == null){
            return bodies ;
        }
        if (mBody.length <= MAX_LENGTH_BODY){
            SSBody body = new SSBody() ;
            body.setValue(mBody);
            bodies.add(body);
        }else {
            int count = mBody.length/MAX_LENGTH_BODY ;
            for (int i = 0 ; i < count ; i ++){
                byte temp[] = new byte[MAX_LENGTH_BODY];
                System.arraycopy(mBody,i*MAX_LENGTH_BODY,temp,0,MAX_LENGTH_BODY);
                SSBody body = new SSBody() ;
                body.setValue(temp);
                bodies.add(body);
            }
            if (count*MAX_LENGTH_BODY < mBody.length){
                int end = mBody.length - count*MAX_LENGTH_BODY ;
                byte temp[] = new byte[end];
                System.arraycopy(mBody,count*MAX_LENGTH_BODY,temp,0,end);
                SSBody body = new SSBody() ;
                body.setValue(temp);
                bodies.add(body);
            }

        }
        return bodies ;
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
        //doSomething different
    }

    /**
     * 同步线程锁
     */
    public void lockWait(){
        if (mExecuteType == ExecuteType.sync){
            synchronized (mSyncLock){
                try {
                    mSyncLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
    public void onProgressReceive(int progress,int total) {
        if (mTaskListener != null){
            mTaskListener.onProgress(this,progress,total);
        }
    }

    @Override
    public void onCompleteReceive(SSResponse response) {
        if (mExecuteType == ExecuteType.sync){
            synchronized (mSyncLock){
                mSyncLock.notify();
            }

            mResponse = response ;
        }
        if (mTaskListener != null){
            mTaskListener.onComplete(this,response);
        }
        mClient = null ;
    }

    @Override
    public void onError(int errorCode) {
        if (mTaskListener != null){
            mTaskListener.onError(this,errorCode);
        }
        if (mExecuteType == ExecuteType.sync){
            synchronized (mSyncLock){
                mSyncLock.notify();
            }

            mResponse = null ;
        }
        mClient = null ;
    }

    @Override
    public int compareTo(SSTask ssTask) {
        Priority left = this.getPriority() ;
        Priority right = ssTask.getPriority() ;
        return right.ordinal() - left.ordinal();
    }
}
