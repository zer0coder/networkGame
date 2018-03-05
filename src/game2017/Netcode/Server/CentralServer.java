package game2017.Netcode.Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Author:  Francisco
 * Project:    NetworkGame
 * Date:    05-03-2018
 * Time:    10:43
 */
public class CentralServer {

    private int portNumber = 50000;
    private ServerSocket serverSocket;
    private ServerData serverData;


    public static void main(String[] args) {
        CentralServer server = new CentralServer();
        server.run();
    }

    /**
     *
     * Will print out the IP address of the local host and the port on which this
     * server is accepting connections.
     */
    private void printLocalHostAddress() {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            String localhostAddress = localhost.getHostAddress();
            System.out.println("Contact this server on the IP address " + localhostAddress);
        } catch (UnknownHostException e) {
            System.err.println("Cannot resolve the Internet address of the local host.");
            System.err.println(e);
            System.exit(-1);
        }
    }

    /**
     *
     * Will register this server on the port number portNumber. Will not start waiting
     * for connections. For this you should call waitForConnectionFromClient().
     */
    private void registerOnPort() {
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            serverSocket = null;
            System.err.println("Cannot open server socket on port number" + portNumber);
            System.err.println(e);
            System.exit(-1);
        }
    }

    private void deregisterOnPort() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
                serverSocket = null;
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    /**
     *
     * Waits for the next client to connect on port number portNumber or takes the
     * next one in line in case a client is already trying to connect. Returns the
     * socket of the connection, null if there were any failures.
     */
    private Socket waitForConnectionFromClient() {
        Socket res = null;
        try {
            res = serverSocket.accept();

        } catch (IOException e) {
            // We return null on IOExceptions
        }
        return res;
    }

    private void run() {
        printLocalHostAddress();
        registerOnPort();
        serverData = new ServerData();

        while (true) {
            Socket socket = waitForConnectionFromClient();

            if (socket != null) {
                ConnectedClient connectedClient = new ConnectedClient(socket);
                connectedClient.start();
                System.out.println("Connection from " + socket);


            } else {
                // We rather agressively terminate the server on the first connection exception
                break;
            }
        }

        deregisterOnPort();
    }
}
