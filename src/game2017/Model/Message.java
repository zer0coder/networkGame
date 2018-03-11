package game2017.Model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Author:  fgluv
 * Project: networkGame
 * Package: game2017.Model
 * Date:    09-03-2018
 */
public class Message implements Serializable {

    private String[] board;
    private HashMap<String, Player> players;
    private Player player;
    private String scoreList;
    private String username;
    private MType type;
    private int xpos;
    private int ypos;
    private int point;
    private String direction;

    public String[] getBoard() {
        return board;
    }

    public void setBoard(String[] board) {
        this.board = board;
    }

    public Message(String username) {
        this.username = username;
    }

    public HashMap<String, Player> getPlayers() {
        return players;
    }

    public void setPlayers(HashMap<String, Player> players) {
        this.players = players;
    }

    public String getScoreList() {
        return scoreList;
    }

    public void setScoreList(String scoreList) {
        this.scoreList = scoreList;
    }

    public MType getType() {
        return type;
    }

    public void setType(MType type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public int getXpos() {
        return xpos;
    }

    public void setXpos(int xpos) {
        this.xpos = xpos;
    }

    public int getYpos() {
        return ypos;
    }

    public void setYpos(int ypos) {
        this.ypos = ypos;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return username + ": " + type + "\n" + players.toString() + "\n";
    }
}
