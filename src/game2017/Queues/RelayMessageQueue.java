package game2017.Queues;

import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Author:  fgluv
 * Project: EAAA_DIS_DIP
 * Package: Services
 * Date:    18-01-2018
 */
public class RelayMessageQueue {

    private static volatile RelayMessageQueue relayMessageQueue;
    private static BlockingQueue<String> relayMessages = new LinkedBlockingQueue<>();
    private static HashSet<BlockingQueue<String>> relayQueue = new HashSet<>();

    private RelayMessageQueue() {
        if (relayMessageQueue != null) {
            throw new RuntimeException("Use getStorage() to get instance of this class.");
        }
    }

    public static RelayMessageQueue getStorage() {
        if (relayMessageQueue == null) {
            synchronized (RelayMessageQueue.class) {
                if (relayMessageQueue == null) {
                    relayMessageQueue = new RelayMessageQueue();
                }
            }
        }
        return relayMessageQueue;
    }

    public static BlockingQueue<String> getRelayMessages() {
        return relayMessages;
    }

    public static HashSet<BlockingQueue<String>> getRelayQueue() {
        return relayQueue;
    }

    public static void AddRelayQueue(BlockingQueue<String> queue) {
        relayQueue.add(queue);
    }

    public static void RemoveRelayQueue(BlockingQueue<String> queue) {
        relayQueue.remove(queue);
    }
}
