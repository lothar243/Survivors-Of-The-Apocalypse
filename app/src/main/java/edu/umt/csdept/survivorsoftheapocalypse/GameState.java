package edu.umt.csdept.survivorsoftheapocalypse;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sinless on 2/11/16.
 */
class Location{
    public Location(Point point) {
        xlocation = point.x;
        ylocation = point.y;
    }

    public Location(int xlocation, int ylocation) {
        this.xlocation = xlocation;
        this.ylocation = ylocation;
    }

    public String toString() {
        return "(" + xlocation + ", " + ylocation + ")";
    }

    int xlocation;
    int ylocation;
}

class CardCount {
    String name;
    int count;

    public CardCount(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public String toString() {
        return "name = " + name + ", count = " + count;
    }
}

public class GameState {
    int TURNCOUNT = 4;
    InGameActivity activity;
    HashMap<String, Tile>  tileMap;
    String[][] tileNames;
    int[][] tileResources;
    ArrayList<Player> players;
    ArrayList<Location> wallLocations;
    Deck<String> tileDeck;
    Deck<Card> playerDeck;
    int currentPlayerIdx;
    int currentPlayerTurnsTaken;

//make player object

    public GameState(InGameActivity activity, int boardX, int boardY, int playerCount, ArrayList<Tile> tileList, ArrayList<CardCount> cardCounts) {
        this.activity = activity;
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
        createTileDeck(tileList);
        createPlayerDeck(cardCounts);
        currentPlayerTurnsTaken = 0;

    }
    public void createTileMap(ArrayList<Tile> tileList){
        for (Tile tile : tileList){
            tileMap.put(tile.getTitle(), tile);
        }
    }

    public void createTileDeck(ArrayList<Tile> tileList){
        for (Tile tile : tileList){
            String tileName = tile.getTitle();
            for(int i = 0; i < tile.getDeckCount(); i++){
                tileDeck.add(tileName, "bottom");
            }
        }
        tileDeck.shuffle();
    }

    public void promptActivityForLocation(PlayerCard cardToBePlayed) {
        activity.promptForLocation(cardToBePlayed);
    }

    public void createPlayerDeck(ArrayList<CardCount> cardList){
        PlayCardFactory cardFactory = PlayCardFactory.getInstance();
        for (CardCount cardCount : cardList) {
            String cardName = cardCount.getName();
            for (int i = 0; i < cardCount.getCount(); i++) {
                PlayerCard card = cardFactory.makeCard(cardName);
                playerDeck.add(card, "bottom");
            }
            playerDeck.shuffle();
        }
        playerDeck.shuffle();
    }
    public int endTurn(){
       currentPlayerIdx =  ++currentPlayerIdx % players.size();
        currentPlayerTurnsTaken = 0;
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

    public String drawTileName(){
        if (tileDeck.count()==0)
            createTileDeck(new ArrayList<Tile>(tileMap.values()));
        String tileName =  tileDeck.draw();
        return tileName;
    }

    public Tile drawTile(){
        String tileName =  drawTileName();
        Tile tile  = tileMap.get(tileName);
        tile.onAquire(this);
        return (Tile)tile;
    }

    public PlayerCard drawPlayCard(){
        Card drawnCard = playerDeck.draw();
        drawnCard.onAquire(this);
        return (PlayerCard) drawnCard;
    }

    public void incrementResources(String tileType, int amount) {
        Location[] tilesOfType = findAllTiles(tileType);
        for( Location location: tilesOfType){
            tileResources[location.xlocation][location.ylocation] +=amount;
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
        return  locations.toArray(new Location[locations.size()]);
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
        return   locations.toArray(new Location[locations.size()]);
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

    public void PlaceTile(Tile tile, Location location){
        int xVal = location.xlocation;
        int yVal = location.ylocation;
        tileNames[xVal][yVal] = tile.getTitle();
        tileResources[xVal][yVal] = tile.getResourceCount();
    }

    public void GatherResources( int playerIdx, Location location){
        if( players.get(playerIdx).checkPresence(location)){
            if (tileNames[location.xlocation][location.ylocation]!= null){
                Tile tile = tileMap.get(tileNames[location.xlocation][location.ylocation]);
                Player player = players.get(playerIdx);
                player.gatherResource(tile.getResource(), 1);
                tileResources[location.xlocation][location.ylocation] -=1;

            }
        }
    }

    public void placePerson(int playerIdx, Location location){
        players.get(playerIdx).placePerson(location);
    }

    public void removePerson(int playerIdx, Location location){
        players.get(playerIdx).removePerson(location);
    }

    public int getSurvivorCount(int playerIdx){
        return players.get(playerIdx).getUnitCount();

    }

    public String getTileNameAtLocation(Location location) {
        return tileNames[location.xlocation][location.ylocation];
    }

    public int getRemainingActions(){
        return TURNCOUNT -currentPlayerTurnsTaken;
    }
    public int spendAction(){
        return currentPlayerTurnsTaken +=1;
    }
}
