package game2017.Queues;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Author:  fgluv
 * Project: EAAA_DIS_DIP
 * Package: Services
 * Date:    18-01-2018
 */
public class OutgoingMessageQueue {

    private static volatile OutgoingMessageQueue outgoingMessageQueue;
    private static BlockingQueue<String> outgoingMessages = new LinkedBlockingQueue<>();

    private OutgoingMessageQueue() {
        if (outgoingMessageQueue != null) {
            throw new RuntimeException("Use getStorage() to get instance of this class.");
        }
    }

    public static OutgoingMessageQueue getStorage() {
        if (outgoingMessageQueue == null) {
            synchronized (OutgoingMessageQueue.class) {
                if (outgoingMessageQueue == null) {
                    outgoingMessageQueue = new OutgoingMessageQueue();
                }
            }
        }
        return outgoingMessageQueue;
    }

    public static BlockingQueue<String> getOutgoingMessages() {
        return outgoingMessages;
    }
}
