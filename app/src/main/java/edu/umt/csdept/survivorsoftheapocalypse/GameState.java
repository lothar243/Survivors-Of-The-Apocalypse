package edu.umt.csdept.survivorsoftheapocalypse;

import java.util.ArrayList;

/**
 * Created by sinless on 2/11/16.
 */
class Location{
    int xlocation;
    int ylocation;
}

public class GameState {



    public GameState(int boardX, int boardY) {
        String[][] tileNames = new String[boardX][boardY];
        int[][] tileResources = new int[boardX][boardY];
        ArrayList<Location> player1Positions = new ArrayList<Location>();
        ArrayList<Location> player2Positions = new ArrayList<Location>();
        ArrayList<Location> wallLocations = new ArrayList<Location>();
        Deck tileDeck = new Deck();
        Deck playerDeck = new Deck();


    }


}
