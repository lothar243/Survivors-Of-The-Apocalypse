package edu.umt.csdept.survivorsoftheapocalypse;

import android.graphics.Point;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sinless on 2/11/16.
 */
class Location implements Serializable{

    int xlocation;
    int ylocation;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (xlocation != location.xlocation) return false;
        return ylocation == location.ylocation;

    }

    @Override
    public int hashCode() {
        int result = xlocation;
        result = 31 * result + ylocation;
        return result;
    }

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

}

class CardCount implements Serializable{
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

public class GameState implements Serializable{
    public static final int WALLCOST = 2;
    private static final int TURNCOUNT = 4;
    transient InGameActivity activity;
    HashMap<String, Tile>  tileMap;
    String[][] tileNames;
    int[][] tileResources;
    ArrayList<Player> players;
    ArrayList<Location> wallLocations;
    Deck<String> tileDeck;
    Deck<Card> playerDeck;
    int currentPlayerIdx;
    int currentPlayerActionsTaken;
    static final String NAME = "GameState";

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
        currentPlayerActionsTaken = 0;

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
        currentPlayerActionsTaken = 0;
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
    public ArrayList<Location> getPlayerLocation(int playerIdx) {
        ArrayList<Location> locations = players.get(playerIdx).getLocations();
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

    public boolean GatherResources( int playerIdx, Location location){
        if( players.get(playerIdx).checkPresence(location)){
            if (tileNames[location.xlocation][location.ylocation]!= null){
                Tile tile = tileMap.get(tileNames[location.xlocation][location.ylocation]);
                Player player = players.get(playerIdx);
                player.gatherResource(tile.getResource(), 1);
                tileResources[location.xlocation][location.ylocation] -=1;
                return true;
            }
        }
        return false;
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
        return TURNCOUNT - currentPlayerActionsTaken;
    }
    public int spendAction(){
        return currentPlayerActionsTaken +=1;
    }

    public boolean collectResources(Location resourceLocation){
        boolean validLocation = false;
            boolean present =checkPresence(currentPlayerIdx, resourceLocation);

            if (present) {
                if (getTileResources(resourceLocation)>=1){
                    validLocation = GatherResources(currentPlayerIdx, resourceLocation);

                }

            }
        return validLocation;
    }

    private boolean checkPresence(int playerIdx, Location queryLocation) {
        ArrayList<Location> locations = getCurrentPlayerLocation();
        if (locations.contains(queryLocation)){
            return true;
        }
        return false;
    }

    public int[][] getTileResources() {
        return tileResources;
    }

    public int getTileResources(Location location){
        return tileResources[location.xlocation][location.ylocation];
    }

    public boolean addPerson(Location location){

        boolean validAction = false;

        if(!checkPresence(currentPlayerIdx, location)){
            addPerson(location);
            validAction = true;
        }

        return validAction;
    }

    public  boolean movePerson(int playerIdx, Location startLocation, Location endLocation){
        boolean finished = false;
         Player currentPlayer = players.get(playerIdx);
        if( currentPlayer.checkPresence(startLocation)) {
            if (tileNames[startLocation.xlocation][startLocation.ylocation] != null) {
                removePerson(currentPlayerIdx,startLocation);
                addPerson(endLocation);
            }
        }
        return finished;
    }

    public boolean buildWall(Location location){
        boolean validAction = false;

        if(checkPresence(currentPlayerIdx, location)){
            if (players.get(currentPlayerIdx).woodCount >= WALLCOST) {
                addWall(location);
                players.get(currentPlayerIdx).woodCount -= WALLCOST;
                validAction = true;
            }
        }

        return validAction;
    }

    public void addWall(Location location) {
        wallLocations.add(location);
    }

    public int getPlayerCount() {
        return players.size();
    }
}
