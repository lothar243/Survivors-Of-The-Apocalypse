package edu.umt.csdept.survivorsoftheapocalypse;

import java.util.ArrayList;

/**
 * Created by sinless on 3/7/16.
 */
public class Player {
    ArrayList<Location> locations;
    int woodCount;
    int foodCount;
    private String playerName;

    public Player(String playerName) {
        this.playerName = playerName;
    }
}
