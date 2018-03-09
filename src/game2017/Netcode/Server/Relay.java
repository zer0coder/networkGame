package game2017.Netcode.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

/**
 * Author:  fgluv
 * Project: EAAA_DIS_DIP
 * Package: Netcode
 * Date:    20-01-2018
 */
public class Relay extends Thread {

    private BlockingQueue<String> localRelayQueue;
    private Socket socket;

    public Relay(Socket socket, BlockingQueue<String> queue) {
        this.socket = socket;
        this.localRelayQueue = queue;
    }

    public void run() {
        String message;
        ObjectOutputStream outputStream;

        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());

            while((message = localRelayQueue.take()) != null) {
                    outputStream.writeObject(message);
            }

        } catch (IOException e) {
            System.out.println("Relay error: " + e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
