package game2017.Netcode.Server;

import game2017.Model.Player;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Author:  Francisco
 * Project:    NetworkGame
 * Date:    05-03-2018
 * Time:    11:00
 */
public class ConnectedClient extends Thread {

    private Socket socket;
    private Player player;
    private List<Player> players = new ArrayList<Player>();
    private  String[] board = {    // 20x20
            "wwwwwwwwwwwwwwwwwwww",
            "w        ww        w",
            "w w  w  www w  w  ww",
            "w w  w   ww w  w  ww",
            "w  w               w",
            "w w w w w w w  w  ww",
            "w w     www w  w  ww",
            "w w     w w w  w  ww",
            "w   w w  w  w  w   w",
            "w     w  w  w  w   w",
            "w ww ww        w  ww",
            "w  w w    w    w  ww",
            "w        ww w  w  ww",
            "w         w w  w  ww",
            "w        w     w  ww",
            "w  w              ww",
            "w  w www  w w  ww ww",
            "w w      ww w     ww",
            "w   w   ww  w      w",
            "wwwwwwwwwwwwwwwwwwww"
    };

    public ConnectedClient(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("Client: " + reader.readLine());

        } catch (IOException e) {
            e.printStackTrace();
        }
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
