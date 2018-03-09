package game2017.Netcode.Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Author:  Francisco
 * Project:    NetworkGame
 * Date:    09-03-2018
 * Time:    11:02
 */
public class MessageRelay extends Thread {

    private Socket socket;
    private BlockingQueue<String> relayMessages;

    public MessageRelay(Socket socket, LinkedBlockingQueue<String> queue) {
        this.socket = socket;
        this.relayMessages = queue;
    }

    @Override
    public void run() {
        PrintWriter outputstream;
        String message;

        try {
            outputstream = new PrintWriter(socket.getOutputStream());

            // Read what the client is sending
            while ((message = relayMessages.take()) != null) {
                outputstream.println(message);
            }
            socket.close();
        } catch (IOException e) {
            // We report but otherwise ignore IOExceptions
            System.out.println("ConnectedClient error: " + e);
            System.exit(1);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Connection closed by client.");
    }
}
