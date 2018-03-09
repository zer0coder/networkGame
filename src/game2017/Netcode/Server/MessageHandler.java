package game2017.Netcode.Server;

import game2017.StorageData.Queues.RelayMessageQueue;

import java.util.HashSet;
import java.util.concurrent.BlockingQueue;

/**
 * Author:  fgluv
 * Project: EAAA_DIS_DIP
 * Package: Netcode
 * Date:    19-01-2018
 */
public class MessageHandler extends Thread {

    private BlockingQueue<String> relayMessages;
    private HashSet<BlockingQueue<String>> relayQueues;

    public MessageHandler() {
        relayMessages = RelayMessageQueue.getRelayMessages();
        relayQueues = RelayMessageQueue.getRelayQueue();
    }

    public void run() {
        String message;

        try {

            while((message = relayMessages.take()) != null) {
                for(BlockingQueue<String> queue : relayQueues)
                    queue.add(message);
            }

        } catch (InterruptedException e) {
            System.out.println("RelayDirector error: " + e.getMessage());
        }

    }
}
