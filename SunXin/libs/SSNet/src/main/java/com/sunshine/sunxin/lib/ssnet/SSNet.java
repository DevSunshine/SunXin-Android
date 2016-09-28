package com.sunshine.sunxin.lib.ssnet;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 钟光燕 on 2016/9/8.
 * e-mail guangyanzhong@163.com
 */
public class SSNet {

    public void initNet(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder() ;
        OkHttpClient client = builder.build() ;
        Request request = new Request.Builder().url("").build() ;
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        }); ;
    }
}
