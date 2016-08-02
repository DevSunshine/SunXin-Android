package com.sunshine.sun.lib.socket;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

/**
 * Created by 钟光燕 on 2016/7/15.
 * e-mail guangyanzhong@163.com
 */
public class SSPipeLine {

    private long mCount ;
    private long mCurrentCount ;
    private SSRequest mRequest;

    private SSResponse mResponse;

    private SSITranslation mTranslation;

    public SSRequest getRequest() {
        return mRequest;
    }

    public void setRequest(SSRequest mRequest) {
        this.mRequest = mRequest;
    }

    public SSResponse getResponse() {
        return mResponse;
    }

    public void setResponse(SSResponse mResponse) {
        this.mResponse = mResponse;
    }

    /**
     * 发送请求结束
     */
    public void requestEnd() {
        if (mTranslation != null) {
            mTranslation.onRequestEnd();
        }
    }

    public void onResponseMiddle(SSResponse response){

        if (response.getMessageCode() == SSMessageCode.SERIES){
            mCount = response.getHeader(SSTypeCode.MESSAGE_COUNT).getLongValue() ;
            if (mTranslation != null){
                mTranslation.onProgressReceive(0, (int) mCount);
            }
        }else if (response.getMessageCode() == SSMessageCode.CONTINUOUS){
            mCurrentCount ++ ;
            if (mTranslation != null){
                mTranslation.onProgressReceive((int) mCurrentCount, (int) mCount);
            }
        }


    }

    public void onResponseComplete(SSResponse response){
        if (response.getMessageCode() == SSMessageCode.OK){
            if (mTranslation!= null){
                mTranslation.onCompleteReceive(response);
            }
        }else {
            if (mTranslation!= null){
                mTranslation.onError(response.getMessageCode());
            }
        }

    }

    public void onError(int code){
        if (mTranslation != null){
            mTranslation.onError(code);
        }
    }

    public void setOnTranslation(SSITranslation translation){
        this.mTranslation = translation ;
    }

}
