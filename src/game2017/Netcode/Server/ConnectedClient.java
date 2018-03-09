package game2017.Netcode.Server;

import game2017.Model.MType;
import game2017.Model.Message;
import game2017.Model.Player;
import game2017.StorageData.Queues.IncomingMessageQueue;
import game2017.StorageData.Queues.RelayMessageQueue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Author:  Francisco
 * Project:    NetworkGame
 * Date:    05-03-2018
 * Time:    11:00
 */
public class ConnectedClient extends Thread {

    private Socket socket;

    public ConnectedClient(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            BlockingQueue<Message> relayMessages = RelayMessageQueue.getRelayMessages();

            Message message;
            while (!(message = (Message) inputStream.readObject()).getType().equals(MType.DISCONNECT)) {
                relayMessages.add(message);
            }
            socket.close();

        } catch (IOException e) {
            // We report but otherwise ignore IOExceptions
            System.out.println("ConnectedClient error: " + e);
            System.exit(1);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Connection closed by client.");
    }
}
