package game2017.Netcode.Server;

import game2017.Model.MType;
import game2017.Model.Message;
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

    private BlockingQueue<Message> relayMessages;
    private HashSet<BlockingQueue<Message>> relayQueues;

    public MessageHandler() {
        relayMessages = RelayMessageQueue.getRelayMessages();
        relayQueues = RelayMessageQueue.getRelayQueue();
    }

    public void run() {
        Message message;

        try {

            while(!(message = relayMessages.take()).getType().equals(MType.DISCONNECT)) {
                for(BlockingQueue<Message> queue : relayQueues) {
                    queue.add(message);
                }
            }

        } catch (InterruptedException e) {
            System.out.println("RelayDirector error: " + e.getMessage());
        }

    }
}
