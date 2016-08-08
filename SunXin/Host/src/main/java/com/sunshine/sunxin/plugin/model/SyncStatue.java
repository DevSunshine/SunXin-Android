package com.sunshine.sunxin.plugin.model;

/**
 * Created by gyzhong on 15/11/22.
 */
public enum SyncStatue {

    SYNCED(0),
    ERROR(1),
    WAITING(2);


    SyncStatue(int in){
       nativeInt = in ;

    }

    final int nativeInt ;
}
