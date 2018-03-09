package game2017.Netcode.Server;

import game2017.StorageData.Maps;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

/**
 * Author:  Francisco
 * Project:    NetworkGame
 * Date:    05-03-2018
 * Time:    10:43
 */
public class CentralServer extends Thread {

    private int portNumber;
    private ServerSocket serverSocket;

    private int mapNumber;

    public CentralServer(int portNumber) {
        this.portNumber = portNumber;
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

    public void run() {
        printLocalHostAddress();
        registerOnPort();
        loadMaps();



        while (true) {
            Socket socket = waitForConnectionFromClient();

            if (socket != null) {
                ConnectedClient connectedClient = new ConnectedClient(socket, Maps.getMap(mapNumber));
//                MessageRelay relay = new MessageRelay(socket, )
                connectedClient.start();
                System.out.println("Connection from " + socket);


            } else {
                // We rather agressively terminate the server on the first connection exception
                break;
            }
        }

        deregisterOnPort();
        System.out.println("CentralServer ended...");
    }

    private void loadMaps() {
        Maps maps = new Maps();
        Random random = new Random();
//        mapNumber = random.nextInt(maps.getNumberOfMaps());
        mapNumber = 0;
        System.out.println("Map: " + mapNumber);
    }

}
