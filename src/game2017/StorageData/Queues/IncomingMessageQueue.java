package game2017.StorageData.Queues;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Author:  fgluv
 * Project: EAAA_DIS_DIP
 * Package: Services
 * Date:    18-01-2018
 */
public class IncomingMessageQueue {

    private static volatile IncomingMessageQueue incomingMessageQueue;
    private static BlockingQueue<String> incomingMessages = new LinkedBlockingQueue<>();

    private IncomingMessageQueue() {
        if (incomingMessageQueue != null) {
            throw new RuntimeException("Use getStorage() to get instance of this class.");
        }
    }

    public static IncomingMessageQueue getStorage() {
        if (incomingMessageQueue == null) {
            synchronized (IncomingMessageQueue.class) {
                if (incomingMessageQueue == null) {
                    incomingMessageQueue = new IncomingMessageQueue();
                }
            }
        }
        return incomingMessageQueue;
    }

    public static BlockingQueue<String> getIncomingMessages() {
        return incomingMessages;
    }
}
