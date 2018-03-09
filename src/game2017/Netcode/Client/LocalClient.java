package game2017.Netcode.Client;

import game2017.Main_Client;
import game2017.Model.MType;
import game2017.Model.Message;
import game2017.Model.Player;
import game2017.StorageData.Queues.IncomingMessageQueue;
import game2017.StorageData.Queues.OutgoingMessageQueue;
import javafx.application.Platform;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Author:  Francisco
 * Project:    NetworkGame
 * Date:    05-03-2018
 * Time:    11:22
 */
public class LocalClient extends Thread {

    private int portNumber;
    private String serverName;
    private BlockingQueue<Message> outgoingMessages;
    private Main_Client client;

    public LocalClient(String IP, int portNumber, Main_Client client) {
        this.serverName = IP;
        this.portNumber = portNumber;
        this.client = client;
    }

    /**
     *
     * Will print out the IP address of the local host on which this client runs.
     */
    private void printLocalHostAddress() {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            String localhostAddress = localhost.getHostAddress();
            System.out.println("I'm a client running with IP address " + localhostAddress + ":" + portNumber);
        } catch (UnknownHostException e) {
            System.err.println("Cannot resolve the Internet address of the local host.");
            System.err.println(e);
            System.exit(-1);
        }
    }

    /**
     *
     * Connects to the server on IP address serverName and port number portNumber.
     */
    private Socket connectToServer() {
        Socket res = null;
        try {
            res = new Socket(serverName, portNumber);
        } catch (IOException e) {
            System.err.println(e);
        }
        return res;
    }


    public void run() {
        printLocalHostAddress();
        Socket socket = connectToServer();

        outgoingMessages = OutgoingMessageQueue.getOutgoingMessages();
        ObjectOutputStream outputStream;
        Message message;

        if (socket != null) {
            System.out.println("Connected to " + socket);

            Reciever reciever = new Reciever(socket, client);
            reciever.start();

            try {

                outputStream = new ObjectOutputStream(socket.getOutputStream());

                while(!(message = outgoingMessages.take()).getType().equals(MType.DISCONNECT)) {
                    outputStream.writeObject(message);
                    outputStream.flush();
                }

                socket.close();

            } catch (IOException e) {
//                System.out.println("Local client error: \n" + e.getMessage());
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Local client dead...");
    }

    private class Reciever extends Thread {

        private Socket socket;
        private ObjectInputStream inputStream;
        private Main_Client client;
        private Message message;

        Reciever(Socket socket, Main_Client client) {
            this.socket = socket;
            this.client = client;
        }

        public void run() {
            System.out.println("Local client - reciever running...");
            try {
                inputStream = new ObjectInputStream(socket.getInputStream());

                while(!(message = (Message) inputStream.readObject()).getType().equals(MType.DISCONNECT)) {
                    System.out.println("LocalClient: \n" + message.toString());
                    if(message.getType().equals(MType.MOVE)) {
                        Platform.runLater(() -> {
                            for(Map.Entry<String, Player> p : message.getPlayers().entrySet()) {
                                Player pa = p.getValue();
                                client.playerMoved(pa, pa.getDirection());
                            }
                            client.setScoreList(message.getScoreList());
                        });
                    } else if (message.getType().equals(MType.DATA)) {
                        Platform.runLater(() -> {
                            client.CreatePlayer(message.getXpos(), message.getYpos());
                        });
                    }
                }

            } catch (IOException e) {
                System.out.println("Reciever error: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println("Local client - reciever dead...");
        }
    }
}
