package com.danilov.AndroidLikeThreadModelLibrary;

public class MessageQueue {
	
	private boolean mQuiting;
	private boolean mQuitAllowed;
	private Message mMessages;
	
	MessageQueue(boolean quitAllowed) {
        mQuitAllowed = quitAllowed;
    }
	
	boolean enqueueMessage(Message msg, long when) {
//        if (msg.isInUse()) {
//            throw new AndroidLikeThreadException(msg + " This message is already in use.");
//        }
//        if (msg.target == null) {
//            throw new AndroidLikeThreadException("Message must have a target.");
//        }

        synchronized (this) {
            if (mQuiting) {
                RuntimeException e = new RuntimeException(
                        msg.target + " sending message to a Handler on a dead thread");
                Utils.log("MessageQueue", e.getMessage(), e);
                return false;
            }

            msg.when = when;
            Message p = mMessages;
            if (p == null || when == 0 || when < p.when) {
                // New head, wake up the event queue if blocked.
                msg.next = p;
                mMessages = msg;
            } else {
                Message prev;
                for (;;) {
                    prev = p;
                    p = p.next;
                    if (p == null || when < p.when) {
                        break;
                    }
                }
                msg.next = p; // invariant: p == prev.next
                prev.next = msg;
            }
        }
        return true;
    }
	
	void quit(boolean safe) {
        if (!mQuitAllowed) {
            throw new RuntimeException("Main thread not allowed to quit.");
        }

        synchronized (this) {
            if (mQuiting) {
                return;
            }
            mQuiting = true;

//            if (safe) {
//                removeAllFutureMessagesLocked();
//            } else {
//                removeAllMessagesLocked();
//            }
        }
    }
	
}
