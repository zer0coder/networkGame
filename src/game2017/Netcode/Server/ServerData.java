package game2017.Netcode.Server;

import game2017.Model.Player;

import java.util.ArrayList;

/**
 * Author:  Francisco
 * Project:    NetworkGame
 * Date:    05-03-2018
 * Time:    10:57
 */
public class ServerData {

    private static ArrayList<Player> listOfPlayers = new ArrayList<>();

    public static ArrayList<Player> getListOfPlayers() {
        return new ArrayList<>(listOfPlayers);
    }

    public static void addPlayer(Player player) {
        listOfPlayers.add(player);
    }

    public static void removePlayer(Player player) {
        listOfPlayers.remove(player);
    }
}
