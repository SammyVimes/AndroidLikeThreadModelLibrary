package com.danilov.AndroidLikeThreadModelLibrary;

import java.util.Calendar;

public class Message {

	
	public Object object;
	public int what;
	public int arg1;
	public int arg2;
	
	static final int FLAG_IN_USE = 1 << 0;
	static final int FLAG_ASYNCHRONOUS  = 1 << 1;
	int flags;
	long when;
	Handler target;
	Runnable callback;
	Message next;
	
	private static final Object sPoolSync = new Object();
	private static Message sPool;
    private static int sPoolSize = 0;
	
    private static final int MAX_POOL_SIZE = 50;
    
    public static Message obtain() {
        synchronized (sPoolSync) {
            if (sPool != null) {
                Message m = sPool;
                sPool = m.next;
                m.next = null;
                sPoolSize--;
                return m;
            }
        }
        return new Message();
    }
    
    public static Message obtain(Message original) {
        Message m = obtain();
        m.what = original.what;
        m.arg1 = original.arg1;
        m.arg2 = original.arg2;
        m.object = original.object;
        m.target = original.target;
        m.callback = original.callback;
        
        return m;
    }
    
    public static Message obtain(Handler h, Runnable callback) {
        Message m = obtain();
        m.target = h;
        m.callback = callback;

        return m;
    }
    
    public static Message obtain(Handler h, int what) {
        Message m = obtain();
        m.target = h;
        m.what = what;

        return m;
    }
    
    public static Message obtain(Handler h, int what, Object object) {
        Message m = obtain();
        m.target = h;
        m.what = what;
        m.object = object;

        return m;
    }
    
    public static Message obtain(Handler h, int what, int arg1, int arg2) {
        Message m = obtain();
        m.target = h;
        m.what = what;
        m.arg1 = arg1;
        m.arg2 = arg2;

        return m;
    }
    
    public static Message obtain(Handler h, int what, 
            int arg1, int arg2, Object object) {
        Message m = obtain();
        m.target = h;
        m.what = what;
        m.arg1 = arg1;
        m.arg2 = arg2;
        m.object = object;

        return m;
    }
    
   
    public long getWhen() {
        return when;
    }
    
    public void setTarget(Handler target) {
        this.target = target;
    }
    
    public Handler getTarget() {
        return target;
    }
    
    public Runnable getCallback() {
        return callback;
    }
    
    public void sendToTarget() {
        target.sendMessage(this);
    }
    
    public boolean isAsynchronous() {
        return (flags & FLAG_ASYNCHRONOUS) != 0;
    }
    
    public void setAsynchronous(boolean async) {
        if (async) {
            flags |= FLAG_ASYNCHRONOUS;
        } else {
            flags &= ~FLAG_ASYNCHRONOUS;
        }
    }
    
    boolean isInUse() {
        return ((flags & FLAG_IN_USE) == FLAG_IN_USE);
    }
    
    public void recycle() {
        clearForRecycle();

        synchronized (sPoolSync) {
            if (sPoolSize < MAX_POOL_SIZE) {
                next = sPool;
                sPool = this;
                sPoolSize++;
            }
        }
    }
    
    
    void clearForRecycle() {
        flags = 0;
        what = 0;
        arg1 = 0;
        arg2 = 0;
        object = null;
        when = 0;
        target = null;
        callback = null;
    }
    
    public Message() {
    }
    
    public String toString() {
        return Long.toString(System.nanoTime());
    }
    
    String toString(long now) {
        StringBuilder   b = new StringBuilder();
        
        b.append("{ what=");
        b.append(what);

        b.append(" when=");

        if (arg1 != 0) {
            b.append(" arg1=");
            b.append(arg1);
        }

        if (arg2 != 0) {
            b.append(" arg2=");
            b.append(arg2);
        }

        if (object != null) {
            b.append(" obj=");
            b.append(object);
        }

        b.append(" }");
        
        return b.toString();
    } 
	
}
