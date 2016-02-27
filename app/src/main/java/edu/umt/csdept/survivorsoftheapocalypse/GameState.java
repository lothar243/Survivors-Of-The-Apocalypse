package edu.umt.csdept.survivorsoftheapocalypse;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sinless on 2/11/16.
 */
class Location{
    int xlocation;
    int ylocation;
}

public class GameState {

    String[][] tileNames;
    int[][] tileResources;
    ArrayList<Location> player1Positions;
    ArrayList<Location> player2Positions;
    ArrayList<Location> wallLocations;
    Deck tileDeck;
    Deck playerDeck;

    public GameState(int boardX, int boardY) {
        HashMap<String, Tile>  tileMap = new HashMap<>();


        tileNames = new String[boardX][boardY];

        tileResources = new int[boardX][boardY];

        player1Positions = new ArrayList<>();
        player2Positions = new ArrayList<>();

        wallLocations = new ArrayList<>();

        tileDeck = new Deck();
        playerDeck = new Deck();

        createTileMap();
        createTileDeck();
        createPlayerDeck();

    }
    public void createTileMap(){

    }

    public void createTileDeck(){

    }

    public void createPlayerDeck(){

    }

    public Tile drawTile(){
        Card tile =  tileDeck.draw();
        tile.onAquire(this);
        return (Tile)tile;
    }

    public PlayCard drawPlayCard(){
        Card drawnCard = playerDeck.draw();
        drawnCard.onAquire(this);
        return (PlayCard) drawnCard;
    }

}
