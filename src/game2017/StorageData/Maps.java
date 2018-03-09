package game2017.StorageData;

import java.util.ArrayList;

/**
 * Author:  Francisco
 * Project:    NetworkGame
 * Date:    09-03-2018
 * Time:    09:48
 */
public class Maps {

    private static ArrayList<String[]> maps = new ArrayList<>();

    public static String[] getMap(int number) {
        return maps.get(number);
    }

    public static void addMap(String[] map) {
        maps.add(map);
    }

    public Maps() {
        String[] map1 = {    // 20x20
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

        addMap(map1);
    }

    public static int getNumberOfMaps() {
        return maps.size();
    }
}
