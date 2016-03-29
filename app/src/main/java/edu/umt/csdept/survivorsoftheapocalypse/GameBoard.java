package edu.umt.csdept.survivorsoftheapocalypse;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
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
                int imageResourceID, numResources = 0;
                int resourceTypeColor;
                String name = "";
                if(currentTile != null) {
                    name = currentTile.getTitle();
                    numResources = currentTile.getResourceCount();
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
            }
        }
        upToDate = true;
    }

    public void update() {
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
