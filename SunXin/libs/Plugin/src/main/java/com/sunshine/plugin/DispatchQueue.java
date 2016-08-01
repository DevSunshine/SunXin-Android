package com.sunshine.plugin;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class DispatchQueue extends Thread {
    public volatile Handler handler = null;
    private final Object handlerSyncObject = new Object();

    public DispatchQueue(String name) {
        setName(name);
        start();
    }

    private void sendMessage(Message message, int delay) {
        if (this.handler == null) {
            return;
        }
        synchronized (this.handlerSyncObject) {
            try {
                this.handlerSyncObject.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (handler != null) {
                if (delay <= 0)
                    this.handler.sendMessage(message);
                else {
                    this.handler.sendMessageDelayed(message, delay);
                }
            }

        }


    }

    public void cancelRunnable(Runnable runnable) {
        if (this.handler == null) {
            return;
        }
        synchronized (this.handlerSyncObject) {
            if (handler == null) return;
            try {
                this.handlerSyncObject.wait();
                if (this.handler != null)
                    this.handler.removeCallbacks(runnable);
                return;
            } catch (Throwable localThrowable) {
                localThrowable.printStackTrace();
            }
        }
    }

    public void cleanupQueue() {
        if (this.handler != null)
            this.handler.removeCallbacksAndMessages(null);
    }

    public void postRunnable(Runnable runnable) {
        postRunnable(runnable, 0L);
    }

    public void postRunnable(Runnable runnable, long delay) {
        if (this.handler == null)
            return;
        synchronized (this.handlerSyncObject) {
            try {
                this.handlerSyncObject.wait();
                if (this.handler == null)
                    return;
                if (delay <= 0L) {
                    this.handler.post(runnable);
                } else {
                    this.handler.postDelayed(runnable, delay);
                }
            } catch (Throwable localThrowable) {
                localThrowable.printStackTrace();
            }

        }
    }

    public void run() {
        Looper.prepare();
        synchronized (this.handlerSyncObject) {
            this.handler = new Handler();
            this.handlerSyncObject.notify();
            Looper.loop();
        }
    }
}