package edu.umt.csdept.survivorsoftheapocalypse;

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
    Deck<String> tileDeck;
    Deck<Card> playerDeck;
    int currentPlayerIdx;

//make player object

    public GameState(int boardX, int boardY, int playerCount, ArrayList<Tile> tileList) {
       tileMap = new HashMap<>();


        tileNames = new String[boardX][boardY];

        tileResources = new int[boardX][boardY];
        players = new ArrayList<>();
        for(int i=0; i< playerCount; i++){
            players.add(new Player("Player "+i));
        }

        currentPlayerIdx = 0;

        wallLocations = new ArrayList<>();

        tileDeck = new Deck<>();
        playerDeck = new Deck<>();

        createTileMap(tileList);
        createTileDeck();
        createPlayerDeck();

    }
    public void createTileMap(ArrayList<Tile> tileList){

    }

    public void createTileDeck(){

    }

    public void createPlayerDeck(){

    }

    public int endTurn(){
       currentPlayerIdx =  ++currentPlayerIdx % players.size();

        return currentPlayerIdx;
    }

    public String getCurrentPlayerName(){
        return players.get(currentPlayerIdx).getPlayerName();
    }

    public int[] getCurrentPlayerResources(){
        return getPlayerResources(currentPlayerIdx);
    }

    public ArrayList<Location> getCurrentPlayerLocation(){
        ArrayList<Location> locations = players.get(currentPlayerIdx).getLocations();
        return locations;
    }

    public Tile drawTile(){
        String tileName =  tileDeck.draw();
        Tile tile  = tileMap.get(tileName);
        tile.onAquire(this);
        return (Tile)tile;
    }

    public PlayerCard drawPlayCard(){
        Card drawnCard = playerDeck.draw();
        drawnCard.onAquire(this);
        return (PlayerCard) drawnCard;
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

    public Tile[][] getBoardLayout(){
        Tile[][] layout = new Tile[tileNames.length][tileNames[0].length];
        for (int i = 0 ;i< tileNames.length; i++){
            for(int j=0; j< tileNames[0].length; j++){
                if (tileNames[i][j] == null)
                    layout[i][j] = null;
                else{
                    layout[i][j] = tileMap.get(tileNames[i][j]);
                }
            }
        }
        return layout;
    }
}
