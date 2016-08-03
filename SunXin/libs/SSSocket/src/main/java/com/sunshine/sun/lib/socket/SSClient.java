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
    private HandlerThread mHandlerThread;
    private BytePool mBytePool;
    private Map<String, SSPipeLine> mPipeLines;
    private SSSocketStatue mStatue;

    private BinaryParser mParser;
    private SSIReceiveWork mReceiveWork;
    private SSIClientConnectListener mConnectListener;

    private String mHost;
    private int mPort;
    private byte[] mLock = new byte[0];
    private int mConnectTryCount ;

    public SSClient(BytePool mBytePool, SSIClientConnectListener mConnectListener) {
        this.mBytePool = mBytePool;
        mPipeLines = new HashMap<>();
        mStatue = SSSocketStatue.disconnect;
        mParser = new BinaryParser(mBytePool);
        mReceiveWork = new SSReceiveWork();

        this.mConnectListener = mConnectListener;
    }

    public void connect(){
        connect(mHost,mPort);
    }
    public void connect(String host, int port) {
        mConnectTryCount ++ ;
        mStatue = SSSocketStatue.connecting;
        mHost = host;
        mPort = port;
        ReceiverThread mReceiverThread = new ReceiverThread();
        mReceiverThread.host = host;
        mReceiverThread.port = port;
        Thread thread = new Thread(mReceiverThread, "receive thread");
        thread.start();
    }

    public int getTryCount(){
        return mConnectTryCount ;
    }

    public SSClientMode getClientMode() {
        return mClientMode;
    }

    public void setClientMode(SSClientMode mPriority) {
        this.mClientMode = mPriority;
    }

    public void sendRequest(SSPipeLine pipeLine) {
        if (mStatue == SSSocketStatue.connecting) {
            try {
                mLock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (mStatue != SSSocketStatue.connected) {
            // TODO: 16/8/2 errorCode 尚未定义 
            pipeLine.onError(0);
            return;
        }
        mPipeLines.put(pipeLine.getRequest().getUniqueKey(), pipeLine);
        sendMessage(pipeLine.getRequest());
    }

    public void sendResponse(SSPipeLine pipeLine) {
        if (mStatue == SSSocketStatue.connecting) {
            try {
                mLock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (mStatue != SSSocketStatue.connected) {
            // TODO: 16/8/2 errorCode 尚未定义 
            pipeLine.onError(0);
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
                    SSPipeLine pipeLine = mPipeLines.get(message.getUniqueKey());
                    if (pipeLine != null) {
                        pipeLine.requestEnd();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void connectClient() {

        if (mStatue == SSSocketStatue.connecting) {
            mHandlerThread = new HandlerThread("send thread");
            mHandlerThread.start();
            mSendHandler = new Handler(mHandlerThread.getLooper());
            mStatue = SSSocketStatue.connected;
            if (mConnectListener != null) {
                mConnectListener.connected(this);
            }
            mConnectTryCount = 0 ;
            mLock.notify();

            // TODO: 16/8/2 如果已经登录了，请执行认证 
            // 认证，
        }
    }

    private void onDataReceived(byte[] buf, int offset, int count) {
        List<SSMessage> messages = new ArrayList<>();
        boolean ret = mParser.parser(buf, offset, count, messages);
        if (!ret) {
            errorClose();
            return;
        }
        for (SSMessage message : messages) {
            if (message instanceof SSRequest) {//通知类消息,服务器发送的请求；
                if (mReceiveWork != null) {
                    mReceiveWork.doReceiveSocket((SSRequest) message);
                }
            } else { //客服端发送的请求，
                SSPipeLine pipeLine = mPipeLines.get(message.getUniqueKey());
                if (pipeLine == null) {
                    return;
                }
                if (((int) message.getMessageCode()) < 0xB0) {
                    pipeLine.onResponseMiddle((SSResponse) message);
                } else {
                    mPipeLines.remove(message.getUniqueKey());
                    pipeLine.onResponseComplete((SSResponse) message);
                }
            }
        }


    }

    public void userClose() {
        if (mStatue != SSSocketStatue.disconnect) {
            close();
        }
    }

    public void errorClose() {
        if (mStatue != SSSocketStatue.disconnect) {
            if (mConnectListener != null) {
                mConnectListener.connectFailed(this);
            }
            close();
        }
    }

    public void close() {

        if (mSocket != null) {
            try {
                mSocket.close();
                mSocket = null;
                if (mSocketOutputStream != null) {
                    mSocketOutputStream.close();
                }
                if (mSocketInputStream != null) {
                    mSocketInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (mHandlerThread != null) {
            mHandlerThread.quit();
            mSendHandler = null;
            mHandlerThread = null;
        }
        mStatue = SSSocketStatue.disconnect;
    }

    private class ReceiverThread implements Runnable {
        String host;
        int port;

        @Override
        public void run() {

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
                errorClose();
            }
            if (mSocket != null && mSocket.isConnected()) {

                try {
                    mSocketOutputStream = mSocket.getOutputStream();
                    mSocketInputStream = mSocket.getInputStream();

                } catch (IOException e) {
                    e.printStackTrace();
                    errorClose();
                }
                connectClient();
                BufferQueue mBufferQueue = new BufferQueue();
                while (true) {
                    try {
                        try {
                            byte buf[] = mBufferQueue.getBuf();
                            int position = mSocketInputStream.read(buf);
                            if (position == -1) {
                                errorClose();
                                break;
                            }
                            onDataReceived(buf, 0, position);
                            mBufferQueue.releaseBuf(buf);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            errorClose();
                            break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        errorClose();
                        break;
                    }
                }
            } else {
                errorClose();
            }

        }
    }
}
