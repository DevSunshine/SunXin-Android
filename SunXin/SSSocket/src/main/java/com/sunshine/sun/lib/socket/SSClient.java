package com.sunshine.sun.lib.socket;
// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.os.Handler;
import android.os.HandlerThread;


import com.sunshine.sun.lib.socket.toolbox.BinaryParser;
import com.sunshine.sun.lib.socket.toolbox.BufferQueue;
import com.sunshine.sun.lib.socket.toolbox.BytePool;
import com.sunshine.sun.lib.socket.toolbox.StreamBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 钟光燕 on 2016/7/14.
 * e-mail guangyanzhong@163.com
 */
public class SSClient {

    private Socket mSocket;
    private OutputStream mSocketOutputStream;
    private InputStream mSocketInputStream;
    private SSClientMode mClientMode;
    private Handler mSendHandler;
    private BytePool mBytePool;
    private Map<String, SSPipeLine> mPipeLines;
    private SSSocketStatue mStatue ;

    private ReceiverThread mReceiverThread;
    private HandlerThread mSendThread ;
    private BinaryParser mParser ;
    private SSIReceiveWork mReceiveWork ;
    private BufferQueue mBufferQueue ;

    public SSClient(BytePool mBytePool) {
        this.mBytePool = mBytePool;
        mPipeLines = new HashMap<>();
        mStatue = SSSocketStatue.disconnect ;
        mParser = new BinaryParser(mBytePool) ;
        mReceiveWork = new SSReceiveWork() ;


    }

    public void connect(String host, int port) {
        mReceiverThread = new ReceiverThread();
        mReceiverThread.host = host;
        mReceiverThread.port = port;
        Thread thread = new Thread(mReceiverThread, "receive thread");
        thread.start();
    }

    public SSClientMode getClientMode() {
        return mClientMode;
    }

    public void setClientMode(SSClientMode mPriority) {
        this.mClientMode = mPriority;
    }

    public void sendRequest(SSPipeLine pipeLine) {
        if (mStatue != SSSocketStatue.connected){
            return;
        }
        mPipeLines.put(pipeLine.getRequest().getUniqueKey(),pipeLine) ;
        sendMessage(pipeLine.getRequest());
    }

    public void sendResponse(SSPipeLine pipeLine) {
        if (mStatue != SSSocketStatue.connected){
            return;
        }
        sendMessage(pipeLine.getResponse());
    }

    private void sendMessage(final SSMessage message) {

        final OutputStream outputStream = mSocketOutputStream;
        StreamBuffer streamBuffer = message.toStreamBuffer(mBytePool);
        final byte[] buf = streamBuffer.readToByte();
        streamBuffer.close();
        mSendHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    outputStream.write(buf);
                    SSPipeLine pipeLine = mPipeLines.get(message.getUniqueKey()) ;
                    if (pipeLine != null){
                        pipeLine.requestEnd();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void connectClient(){

        if (mStatue == SSSocketStatue.connecting){
            mSendThread = new HandlerThread("send thread") ;
            mSendThread.start();
            mSendHandler = new Handler(mSendThread.getLooper()) ;
            mStatue = SSSocketStatue.connected ;
        }
    }

    private void onDataReceived(byte[] buf, int offset, int count){
        List<SSMessage> messages = new ArrayList<>() ;
        boolean ret = mParser.parser(buf,offset,count,messages) ;
        if (ret){
            for (SSMessage message : messages){
                if (message instanceof SSRequest){//通知类消息,服务器发送的请求；
                    if (mReceiveWork != null){
                        mReceiveWork.doReceiveSocket((SSRequest) message);
                    }
                }else { //客服端发送的请求，
                    SSPipeLine pipeLine = mPipeLines.remove(message.getUniqueKey());
                    pipeLine.complete();
                }
            }

        }

    }

    public void close(){
        mStatue = SSSocketStatue.disconnecting ;
        if (mSocket != null){
            try {
                mSocket.close();
                mSocket = null ;
                if (mSocketOutputStream != null){
                    mSocketOutputStream.close();
                }
                if (mSocketInputStream != null){
                    mSocketInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mStatue = SSSocketStatue.disconnect ;
    }
    private class ReceiverThread implements Runnable {
        String host;
        int port;

        @Override
        public void run() {
            mStatue = SSSocketStatue.connecting ;
            mSocket = new Socket();
            try {
                mSocket.setKeepAlive(false);
                mSocket.setReceiveBufferSize(8192);
                mSocket.setSendBufferSize(8192);
                mSocket.setTcpNoDelay(true);
                mSocket.setSoTimeout(0);
                SocketAddress address = new InetSocketAddress(host, port);
                mSocket.connect(address, 10 * 10000);
            } catch (IOException e) {
                e.printStackTrace();
                close();
            }
            if (mSocket !=null && mSocket.isConnected()){

                try {
                    mSocketOutputStream = mSocket.getOutputStream() ;
                    mSocketInputStream = mSocket.getInputStream() ;

                } catch (IOException e) {
                    e.printStackTrace();
                    close();
                }
                connectClient() ;
                mBufferQueue = new BufferQueue() ;
                while (true){
                    try {
                        try {
                            byte buf[] = mBufferQueue.getBuf();
                            int position = mSocketInputStream.read(buf) ;
                            onDataReceived(buf,0,position);
                            mBufferQueue.releaseBuf(buf);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        close();
                        break;
                    }
                }
            }else {
                close();
            }

        }
    }
}
