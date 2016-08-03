package com.sunshine.sunxin.plugin.model;

import android.widget.ImageView;

/**
 * Created by gyzhong on 15/11/22.
 */
public enum SyncStatue {

    SYNCED(0),

    DISCARD(1),

    ERROR(2),
    WAITTING(3);


    SyncStatue(int in){
       nativeInt = in ;

    }

    final int nativeInt ;
}
