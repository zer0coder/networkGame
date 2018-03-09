package game2017;

import game2017.Netcode.Server.CentralServer;

public class Main_Server  {
	public static void main(String[] args) {
		CentralServer centralServer = new CentralServer(50000);
		centralServer.start();
	}
}

