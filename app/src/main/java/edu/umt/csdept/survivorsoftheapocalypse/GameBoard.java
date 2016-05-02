package edu.umt.csdept.survivorsoftheapocalypse;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Handle drawing the background
 */
public class GameBoard {
    private static final String NAME = "GameBoard";
    boolean upToDate = false;
    ArrayList<ArrayList<Hex>> hexes;
    Resources resources;
    GameState gameState;

    public GameBoard(Resources resources, GameState gameState) {
        this.resources = resources;
        this.gameState = gameState;
        refreshHexes();
    }

    private void refreshHexes() {
//        Log.d(NAME, "refreshHexes()");
        Tile[][] boardLayout = gameState.getBoardLayout();
        if(hexes == null) hexes = new ArrayList<>();
        for (int i = 0; i < boardLayout.length; i++) {
            if(hexes.size() <= i) hexes.add(new ArrayList<Hex>());
            for (int j = 0; j < boardLayout[i].length; j++) {
                ArrayList<Hex> currentRow = hexes.get(i);
                Tile currentTile = boardLayout[i][j];
                int imageResourceID, numResources = 0;
                int resourceTypeColor;
                String name = "";
                if(currentTile != null) {
                    name = currentTile.getTitle();
                    numResources = gameState.getTileResources(new Location(i, j));
                }
                switch (name) {
                    case "Field":
                        imageResourceID = R.drawable.field;
                        resourceTypeColor = resources.getColor(R.color.foodTextColor);
                        break;
                    case "Forest":
                        imageResourceID = R.drawable.forest;
                        resourceTypeColor = resources.getColor(R.color.woodTextColor);
                        break;
                    case "City":
                        imageResourceID = R.drawable.city;
                        resourceTypeColor = resources.getColor(R.color.wildTextColor);
                        break;
                    case "Mountain":
                        imageResourceID = R.drawable.mountain;
                        resourceTypeColor = Color.TRANSPARENT;
                        break;
                    default:
                        imageResourceID = R.drawable.hex;
                        resourceTypeColor = Color.BLACK;
                        break;
                }

                if(currentRow.size() <= j) currentRow.add(
                        new Hex(i, j, resources, imageResourceID, numResources, resourceTypeColor));
                else(currentRow.get(j)).setResourceCount(numResources);
            }
        }
        // place meeples on hexes
        for (int playerNum = 0; playerNum < gameState.players.size(); playerNum++) {
            // create a boolean array to update player locations
            boolean[][] locationArray = new boolean[hexes.size()][];
            for (int i = 0; i < locationArray.length; i++) {
                locationArray[i] = new boolean[hexes.get(i).size()];
            }

            ArrayList<Location> playerLocations = gameState.getPlayerLocation(playerNum);
            for(Location location: playerLocations) {
                locationArray[location.xlocation][location.ylocation] = true;
            }

            for (int i = 0; i < locationArray.length; i++) {
                for (int j = 0; j < locationArray[i].length; j++) {
                    hexes.get(i).get(j).setPlayerPresence(playerNum, locationArray[i][j]);
                }
            }
        }
        // update walls
        boolean[][] wallArray = new boolean[hexes.size()][];
        for(int i = 0; i < wallArray.length; i++) {
            wallArray[i] = new boolean[hexes.get(i).size()];
        }
        for(Location wallLocation: gameState.wallLocations) {
            wallArray[wallLocation.xlocation][wallLocation.ylocation] = true;
        }
        for (int i = 0; i < wallArray.length; i++) {
            ArrayList<Hex> row = hexes.get(i);
            for (int j = 0; j < wallArray[i].length; j++) {
                row.get(j).setWall(wallArray[i][j]);
            }
        }
        upToDate = true;
    }

    public void update() {
        if(!upToDate)
            refreshHexes();
    }

    public void draw(Canvas canvas) {
        if(!upToDate) {
            refreshHexes();
        }
        if(canvas != null) {
            canvas.drawColor(Color.WHITE);
            for(ArrayList<Hex> row: hexes) {
                for(Hex hex: row) {
                    hex.draw(canvas);
                }
            }
        }
    }

    public void invalidateBoard() {
        upToDate = false;
    }
    public void invalidateHexes() {
        for(ArrayList<Hex> row: hexes) {
            for(Hex hex: row) {
                hex.invalidate();
            }
        }
    }
}
