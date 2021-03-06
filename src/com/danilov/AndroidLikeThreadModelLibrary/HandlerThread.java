package com.danilov.AndroidLikeThreadModelLibrary;

public class HandlerThread extends Thread {

	private Looper mLooper;
	
	@Override
    public void run() {
        Looper.prepare();
        synchronized (this) {
            mLooper = Looper.myLooper();
            notifyAll();
        }
        Looper.loop();
    }
	
	public Looper getLooper() {
		if (!isAlive()) {
			return null;
		}
		
		synchronized (this) {
            while (isAlive() && mLooper == null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
        }
        return mLooper;
	}
	
}
