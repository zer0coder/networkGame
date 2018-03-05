package game2017.Netcode.Server;

import game2017.Model.Player;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

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
    public static List<Player> players = new ArrayList<Player>();
    private TextArea scoreList;
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

                fields[x][y].setGraphic(new ImageView(image_floor));
                x+=delta_x;
                y+=delta_y;

                if (direction.equals("right")) {
                    fields[x][y].setGraphic(new ImageView(hero_right));
                };
                if (direction.equals("left")) {
                    fields[x][y].setGraphic(new ImageView(hero_left));
                };
                if (direction.equals("up")) {
                    fields[x][y].setGraphic(new ImageView(hero_up));
                };
                if (direction.equals("down")) {
                    fields[x][y].setGraphic(new ImageView(hero_down));
                };

                player.setXpos(x);
                player.setYpos(y);
            }
        }
        scoreList.setText(getScoreList());
    }

    public String getScoreList() {
        StringBuffer b = new StringBuffer(100);
        for (Player p : players) {
            b.append(p+"\r\n");
        }
        return b.toString();
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
