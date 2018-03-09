package game2017;

import game2017.Netcode.Server.CentralServer;

/**
 * Author:  fgluv
 * Project: networkGame
 * Package: game2017
 * Date:    09-03-2018
 */
public class DevTest {

    public static void main(String args[]) {
        CentralServer centralServer = new CentralServer(50000);
        centralServer.start();
        Main_Client.main(args);
    }
}
