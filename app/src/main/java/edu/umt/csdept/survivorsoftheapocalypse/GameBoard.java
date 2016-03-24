package edu.umt.csdept.survivorsoftheapocalypse;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;

/**
 * Handle drawing the background
 */
public class GameBoard {
    private static final String NAME = "GameBoard";
    boolean upToDate = false;
    ArrayList<ArrayList<Hex>> hexes;
    enum BoardShape {DIAMOND, BIG_HEX, SQUARE}
    Resources resources;
    GameState gameState;

    public GameBoard(Resources resources, GameState gameState) {
//        Tile[][] boardLayout = gameState.getBoardLayout();
        this.resources = resources;
        this.gameState = gameState;
        refreshHexes();
//        hexes = new ArrayList<>();
//        for (int i = 0; i < boardLayout.length; i++) {
//            ArrayList<Hex> hexRow = new ArrayList<>();
//            for (int j = 0; j < boardLayout[i].length; j++) {
//                Tile tile = boardLayout[i][j];
//                if(tile == null) {
//                    tile = new Tile();
//                }
//                int imageResourceID = imageResourceID(tile.getTitle());
//                if(tile.getTitle() == null) tile.setTitle("");
//
//                Bitmap tileBitmap = BitmapFactory.decodeResource(resources, imageResourceID);
//                hexRow.add(new Hex(i,j,tileBitmap));
//            }
//            hexes.add(hexRow);
//        }

    }

    private void refreshHexes() {
        Log.d(NAME, "refreshHexes()");
        Tile[][] boardLayout = gameState.getBoardLayout();
        if(hexes == null) hexes = new ArrayList<>();
        for (int i = 0; i < boardLayout.length; i++) {
            if(hexes.size() <= i) hexes.add(new ArrayList<Hex>());
            for (int j = 0; j < boardLayout[i].length; j++) {
                ArrayList<Hex> currentRow = hexes.get(i);
                Tile currentTile = boardLayout[i][j];
                int resourceID;
                if(currentTile == null) {
                    resourceID = imageResourceID("");
//                    Log.d(NAME, "null tile");
                }
                else {
                    resourceID = imageResourceID(currentTile.getTitle());
//                    Log.d(NAME, currentTile.toString());
                }
                if(currentRow.size() <= j) currentRow.add(
                        new Hex(i, j, resources, resourceID));
                else currentRow.get(j).changeImage(resources, resourceID);
            }
        }
        upToDate = true;
    }

    public void update() {
    }

    public int imageResourceID(String name) {
        switch (name) {
            case "Field":
                return R.drawable.field;
            case "Forest":
                return R.drawable.forest;
            case "City":
                return R.drawable.city;
            case "Mountain":
                return R.drawable.mountain;
            default:
                return R.drawable.hex;
        }
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

    public void invalidate() {
        upToDate = false;
    }
}
