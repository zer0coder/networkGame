package game2017.Netcode.Client;

import game2017.StorageData.Queues.IncomingMessageQueue;
import game2017.StorageData.Queues.OutgoingMessageQueue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
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
    private BlockingQueue<String> outgoingMessages;
    private BlockingQueue<String> incomingMessages;

    public LocalClient(String IP, int portNumber) {
        this.serverName = IP;
        this.portNumber = portNumber;
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
        incomingMessages = IncomingMessageQueue.getIncomingMessages();
        PrintWriter outputStream;
        String message;

        if (socket != null) {
            System.out.println("Connected to " + socket);

            Reciever reciever = new Reciever(socket);
            reciever.start();

            try {

                outputStream = new PrintWriter(socket.getOutputStream());

                while((message = outgoingMessages.take()) != null) {
                    outputStream.println(message);
                    outputStream.flush();
                }

                socket.close();

            } catch (IOException e) {
                System.out.println("Local client error: " + e.getMessage());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Local client dead...");
    }

    private class Reciever extends Thread {

        Socket socket;
        BufferedReader inputStream;

        Reciever(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message;

                message = inputStream.readLine();
                System.out.println(message);
                while(message != null) {
                    message = inputStream.readLine();
                    incomingMessages.add(message);
                    System.out.println(message);
                }

            } catch (IOException e) {
                System.out.println("Reciever error: " + e.getMessage());
            }
        }
    }
}
