package com.sunshine.sun.lib.socket;

/**
 * Created by gyzhong on 16/7/17.
 */
public interface SSITranslation {
    void onRequestEnd() ;
    void onProgressReceive() ;
    void onCompleteReceive() ;
}
