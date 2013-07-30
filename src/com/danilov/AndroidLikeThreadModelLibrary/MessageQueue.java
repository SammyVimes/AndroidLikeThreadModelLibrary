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
	
	public Message next() {
		int pendingIdleHandlerCount = -1; // -1 only during first iteration
        int nextPollTimeoutMillis = 0;

        for (;;) {
            synchronized (this) {
                // Try to retrieve the next message.  Return if found.
                final long now = System.currentTimeMillis();
                Message prevMsg = null;
                Message msg = mMessages;
                if (msg != null && msg.target == null) {
                    // Stalled by a barrier.  Find the next asynchronous message in the queue.
                    do {
                        prevMsg = msg;
                        msg = msg.next;
                    } while (msg != null && !msg.isAsynchronous());
                }
                if (msg != null) {
                    if (now < msg.when) {
                        // Next message is not ready.  Set a timeout to wake up when it is ready.
                        nextPollTimeoutMillis = (int) Math.min(msg.when - now, Integer.MAX_VALUE);
                    } else {
                        // Got a message.
                        if (prevMsg != null) {
                            prevMsg.next = msg.next;
                        } else {
                            mMessages = msg.next;
                        }
                        msg.next = null;
                        if (false) {
                        	Utils.log("MessageQueue", "Returning message: " + msg);
                        }
                        msg.markInUse();
                        return msg;
                    }
                } else {
                    // No more messages.
                    nextPollTimeoutMillis = -1;
                }

                // Process the quit message now that all pending messages have been handled.
                if (mQuiting) {
                    return null;
                }
            }

        }
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
