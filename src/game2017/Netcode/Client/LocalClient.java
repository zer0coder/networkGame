package game2017.Netcode.Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Author:  Francisco
 * Project:    NetworkGame
 * Date:    05-03-2018
 * Time:    11:22
 */
public class LocalClient extends Thread {

    private int portNumber;
    private String serverName;

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
            System.out.println("I'm a client running with IP address " + localhostAddress);
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
    private Socket connectToServer(String serverName) {
        Socket res = null;
        try {
            res = new Socket(serverName,portNumber);
        } catch (IOException e) {
            // We return null on IOExceptions
        }
        return res;
    }

    public void run() {

        printLocalHostAddress();

        Socket socket = connectToServer(serverName);

        if (socket != null) {
            System.out.println("Connected to " + socket);

            try {

                PrintWriter writer = new PrintWriter(socket.getOutputStream());
                writer.println("Hello Server");
                writer.flush();

                socket.close();

            } catch (IOException e) {
                // We ignore IOExceptions
            }
        }

        System.out.println("LocalClient ended...");
    }
}
