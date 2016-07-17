package com.sunshine.sun.lib.socket;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

/**
 * Created by 钟光燕 on 2016/7/15.
 * e-mail guangyanzhong@163.com
 */
public class SSPipeLine {

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

    public void complete(){
        if (mTranslation!= null){
            mTranslation.onCompleteReceive();
        }
    }

    public void setOnTranslation(SSITranslation translation){
        this.mTranslation = translation ;
    }

}
