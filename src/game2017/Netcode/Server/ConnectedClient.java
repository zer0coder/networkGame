package game2017.Netcode.Server;

import game2017.Model.Player;
import game2017.StorageData.Queues.IncomingMessageQueue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Author:  Francisco
 * Project:    NetworkGame
 * Date:    05-03-2018
 * Time:    11:00
 */
public class ConnectedClient extends Thread {

    private Socket socket;
    private BufferedReader inputStream;
    private String message;
    private BlockingQueue<String> incomingMessages;
    private Player player;
    private List<Player> players = new ArrayList<Player>();
    private String[] board;

    public ConnectedClient(Socket socket, String[] map) {
        this.socket = socket;
        this.board = map;
    }

    @Override
    public void run() {
        try {
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            incomingMessages = IncomingMessageQueue.getIncomingMessages();

            // TODO: Read User info
            message = inputStream.readLine();

            // Read what the client is sending
            while (message != null) {
                message = inputStream.readLine();
                System.out.println(message);
                if(message.equals("NEW_PLAYER")) {
                    incomingMessages.add(message);
                } else {
                    incomingMessages.add(message);
                }
            }
            socket.close();
        } catch (IOException e) {
            // We report but otherwise ignore IOExceptions
            System.out.println("ConnectedClient error: " + e);
            System.exit(1);

        }
        System.out.println("Connection closed by client.");
    }

    public void playerMoved(int delta_x, int delta_y, String direction) {
        player.setDirection(direction);
        int x = player.getXpos(),y = player.getYpos();

        if (board[y+delta_y].charAt(x+delta_x)=='w') {
            player.addPoints(-1);
        }
        else {
            Player p = getPlayerAt(x+delta_x,y+delta_y);
            if (p!=null) {
                player.addPoints(10);
                p.addPoints(-10);
            } else {
                player.addPoints(1);

                x+=delta_x;
                y+=delta_y;


                player.setXpos(x);
                player.setYpos(y);
            }
        }
    }

    public Player getPlayerAt(int x, int y) {
        for (Player p : players) {
            if (p.getXpos()==x && p.getYpos()==y) {
                return p;
            }
        }
        return null;
    }
}
