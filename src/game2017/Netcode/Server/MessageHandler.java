package game2017.Netcode.Server;

import game2017.Model.MType;
import game2017.Model.Message;
import game2017.Model.Player;
import game2017.StorageData.Maps;
import game2017.StorageData.Queues.RelayMessageQueue;

import java.util.*;
import java.util.concurrent.BlockingQueue;

/**
 * Author:  fgluv
 * Project: EAAA_DIS_DIP
 * Package: Netcode
 * Date:    19-01-2018
 */
public class MessageHandler extends Thread {

    private BlockingQueue<Message> relayMessages;
    private HashSet<BlockingQueue<Message>> relayQueues;
    private String[] board;
    private HashMap<String, Player> players = new HashMap<>();
    private String scoreList;
    private Message message;
    private int playerCounter = 0;

    public MessageHandler(int num) {
        this.board = Maps.getMap(num);
        relayMessages = RelayMessageQueue.getRelayMessages();
        relayQueues = RelayMessageQueue.getRelayQueue();
    }

    public void run() {
        try {
            Message relayMessage;
            while(!(message = relayMessages.take()).getType().equals(MType.DISCONNECT)) {
                relayMessage = new Message("SERVER");

                MType type = message.getType();

                if(type.equals(MType.DATA)) {
                    Player player = CreatePlayer(message.getUsername());
                    relayMessage.setBoard(board);
                    relayMessage.setPlayers(players);
                    relayMessage.setScoreList(getScoreList());
                    relayMessage.setPlayer(player);
                } else if (type.equals(MType.MOVE)) {
                    Player player = players.get(message.getUsername());

                    int x = message.getXpos();
                    int y = message.getYpos();
                    String direction = message.getDirection();
                    playerMoved(player, x, y, direction);

                    players.put(message.getUsername(), player);

                    relayMessage.setXpos(message.getXpos());
                    relayMessage.setYpos(message.getYpos());
                    relayMessage.setPlayers(players);
                }

                relayMessage.setScoreList(scoreList);
                relayMessage.setType(message.getType());

                for(BlockingQueue<Message> queue : relayQueues) {
                    queue.add(relayMessage);
                }
            }

        } catch (InterruptedException e) {
            System.out.println("RelayDirector error: " + e.getMessage());
        }

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Player CreatePlayer(String name) {

        int startX;
        int startY;

        if (playerCounter == 0){
            startX = 2;
            startY = 2;
        }
        else if (playerCounter == 1){
            startX = 19;
            startY = 19;
        }
        else if (playerCounter == 3){
            startX = 19;
            startY = 2;
        }
        else{
            startX = 2;
            startY = 19;
        }
        playerCounter++;

		Player player = new Player(name,startX,startY,"up");
		player.setPrev_xpos(startX);
		player.setPrev_ypos(startY);
		players.put(name, player);

		return player;
	}

    public void playerMoved(Player player, int delta_x, int delta_y, String direction) {
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

                player.setPrev_xpos(x);
                player.setPrev_ypos(y);

                x+=delta_x;
                y+=delta_y;

                player.setXpos(x);
                player.setYpos(y);
            }
        }
        scoreList = getScoreList();
    }

    public String getScoreList() {
        StringBuffer b = new StringBuffer(100);
        for (Map.Entry<String, Player> p : players.entrySet()) {
            b.append(p.getValue()+"\r\n");
        }
        return b.toString();
    }

    public Player getPlayerAt(int x, int y) {
        for (Map.Entry<String, Player> pa : players.entrySet()) {
            Player p = pa.getValue();
            if (p.getXpos()==x && p.getYpos()==y) {
                return p;
            }
        }
        return null;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
