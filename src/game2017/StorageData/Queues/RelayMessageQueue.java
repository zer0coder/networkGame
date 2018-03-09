package game2017.StorageData.Queues;

import game2017.Model.Message;

import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Author:  fgluv
 * Project: EAAA_DIS_DIP
 * Package: Services
 * Date:    09-03-2018
 */
public class RelayMessageQueue {

    private static volatile RelayMessageQueue relayMessageQueue;
    private static BlockingQueue<Message> relayMessages = new LinkedBlockingQueue<>();
    private static HashSet<BlockingQueue<Message>> relayQueue = new HashSet<>();

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

    public static BlockingQueue<Message> getRelayMessages() {
        return relayMessages;
    }

    public static HashSet<BlockingQueue<Message>> getRelayQueue() {
        return relayQueue;
    }

    public static void AddRelayQueue(BlockingQueue<Message> queue) {
        relayQueue.add(queue);
    }

    public static void RemoveRelayQueue(BlockingQueue<Message> queue) {
        relayQueue.remove(queue);
    }
}
