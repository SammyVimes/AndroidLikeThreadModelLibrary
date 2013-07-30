package com.danilov.AndroidLikeThreadModelLibrary;

public class Looper {
	
	static final ThreadLocal<Looper> sThreadLocal = new ThreadLocal<Looper>();
	
    final MessageQueue mQueue;
    final Thread mThread;
    volatile boolean mRun;
	
	public static void prepare() {
        prepare(true);
    }
	
	private Looper(boolean quitAllowed) {
        mQueue = new MessageQueue(quitAllowed);
        mRun = true;
        mThread = Thread.currentThread();
    }

    private static void prepare(boolean quitAllowed) {
        if (sThreadLocal.get() != null) {
            throw new RuntimeException("Only one Looper may be created per thread");
        }
        sThreadLocal.set(new Looper(quitAllowed));
    }
    
	
	public void quit() {
        mQueue.quit(false);
    }
	
	public void quitSafely() {
        mQueue.quit(true);
    }
	
	public Thread getThread() {
        return mThread;
    }

    public MessageQueue getQueue() {
        return mQueue;
    }
	
	public static void loop() {
		final Looper me = myLooper();
        if (me == null) {
            throw new RuntimeException("No Looper; Looper.prepare() wasn't called on this thread.");
        }
        final MessageQueue queue = me.mQueue;

        for (;;) {
            Message msg = queue.next(); // might block
            if (msg == null) {
                // No message indicates that the message queue is quitting.
                return;
            }

            msg.target.dispatchMessage(msg);
            msg.recycle();
        }
	}
	
	public static Looper myLooper() {
        return sThreadLocal.get();
    }
}
