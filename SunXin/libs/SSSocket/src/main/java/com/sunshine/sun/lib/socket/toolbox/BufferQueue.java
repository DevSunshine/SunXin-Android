package com.sunshine.sun.lib.socket.toolbox;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by gyzhong on 16/7/18.
 */
public class BufferQueue {
    private LinkedBlockingQueue<byte[]> mQueue ;

    public BufferQueue() {
        mQueue = new LinkedBlockingQueue<>() ;
        int MAX_BUF = 10;
        for (int i = 0; i < MAX_BUF; i ++){
            mQueue.offer(new byte[4096]) ;
        }
    }

    public byte[] getBuf()throws InterruptedException{
        return mQueue.take();
    }

    public void releaseBuf(byte[] buf){
        mQueue.offer(buf) ;
    }
}
