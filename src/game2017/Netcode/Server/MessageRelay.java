package game2017.Netcode.Server;

import game2017.Model.MType;
import game2017.Model.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
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
    private BlockingQueue<Message> relayMessages;

    public MessageRelay(Socket socket, BlockingQueue<Message> queue) {
        this.socket = socket;
        this.relayMessages = queue;
    }

    @Override
    public void run() {
        ObjectOutputStream outputstream;
        Message message;

        try {
            outputstream = new ObjectOutputStream(socket.getOutputStream());

            while (!(message = relayMessages.take()).getType().equals(MType.DISCONNECT)) {
                System.out.println("MessageRelay: \n" + message.toString());
                outputstream.writeObject(message);
                outputstream.flush();
                outputstream.reset();
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
