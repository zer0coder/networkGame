package game2017.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author:  fgluv
 * Project: networkGame
 * Package: game2017.Model
 * Date:    09-03-2018
 */
public class Message implements Serializable {

    private List<Player> players = new ArrayList<Player>();
    private String scoreList;
    private String username;
    private MType type;
    private int xpos;
    private int ypos;
    private int point;

    public Message(String username) {
        this.username = username;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
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

    @Override
    public String toString() {
        return username + ": " + type;
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
}
