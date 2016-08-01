package com.sunshine.plugin.model;

import android.widget.ImageView;

/**
 * Created by gyzhong on 15/11/22.
 */
public enum SyncStatue {

    SYNCED(0),

    DISCARD(1),

    ERROR(2),

    NETDISCONNECT(3),

    NETERROR(4),

    WAITTIMEOUT(5),

    WATTING(6) ;

    SyncStatue(int in){

       nativeInt = in ;

    }

    final int nativeInt ;
}
