package com.danilov.AndroidLikeThreadModelLibrary;

import java.util.Random;


public class Test {

	public static void main(String[] args) {
		MyHandlerThread thread = new MyHandlerThread();
		thread.start();
		Random random = new Random();
		MyHandlerThread.MyHandler handler = thread.new MyHandler(thread.getLooper());
		AnotherThread t2 = new AnotherThread(handler);
		t2.start();
		while (true) {
			handler.sendMessage(handler.obtainMessage(random.nextInt(2)));
		}
	}
	
	public static class AnotherThread extends Thread{
		
		MyHandlerThread.MyHandler mHandler;
		public AnotherThread(MyHandlerThread.MyHandler handler) {
			mHandler = handler;
		}
		
		@Override
		public void run() {
			while (true) {
				mHandler.sendMessage(mHandler.obtainMessage(2));
			}
		}
		
	}
	
	public static class MyHandlerThread extends HandlerThread {
		
		MyHandler mHandler;
		
		
		public class MyHandler extends Handler {
			
			public MyHandler(Looper looper) {
				super(looper);
			}
			
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 0) {
					Utils.log("works");
				}
				switch (msg.what) {
				case 0:
					Utils.log("works");
					break;
				case 1:
					Utils.log("even more works");
					break;
				case 2:
					Utils.log("another thread more works");
					break;
				}
					
			}
		}
	}

}
