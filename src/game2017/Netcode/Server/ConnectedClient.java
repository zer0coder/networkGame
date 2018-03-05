package game2017.Netcode.Server;

import java.net.Socket;

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

    }
}
