package edu.umt.csdept.survivorsoftheapocalypse;

import android.util.Range;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sinless on 2/11/16.
 */
class Location{

    public Location(int xlocation, int ylocation) {
        this.xlocation = xlocation;
        this.ylocation = ylocation;
    }

    int xlocation;
    int ylocation;
}

public class GameState {
    HashMap<String, Tile>  tileMap;
    String[][] tileNames;
    int[][] tileResources;
    ArrayList<Player> players;
    ArrayList<Location> wallLocations;
    Deck tileDeck;
    Deck playerDeck;

//make player object

    public GameState(int boardX, int boardY, int playerCount) {
       tileMap = new HashMap<>();


        tileNames = new String[boardX][boardY];

        tileResources = new int[boardX][boardY];

        for(int i=0; i< playerCount; i++){
            players.add(new Player("Player "+i));
        }

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

    public void incrementResources(String tileType) {
        Location[] tilesOfType = findAllTiles(tileType);
        for( Location location: tilesOfType){
            tileResources[location.xlocation][location.ylocation] +=1;
        }

    }

    public Location[] findAllTiles(String tileType){
    ArrayList<Location> locations = new ArrayList<>();
        for (int i = 0 ;i< tileNames.length; i++){
            for(int j=0; j< tileNames[0].length; j++){
                if (tileNames[i][j].compareToIgnoreCase(tileType) ==0){
                    locations.add(new Location(i,j));
                }
            }
        }
        return  (Location[]) locations.toArray();
    }
    public Location[] findAllTiles(){
        ArrayList<Location> locations = new ArrayList<>();
        for (int i = 0 ;i< tileNames.length; i++){
            for(int j=0; j< tileNames[0].length; j++){
                if (tileNames[i][j]!= null){
                    locations.add(new Location(i,j));
                }
            }
        }
        return  (Location[]) locations.toArray();
    }

    public int[] getPlayerResources(int index){
        return players.get(index).playerResources();
    }
}
