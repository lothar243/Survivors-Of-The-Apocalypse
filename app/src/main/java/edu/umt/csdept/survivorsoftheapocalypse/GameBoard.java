package edu.umt.csdept.survivorsoftheapocalypse;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;

/**
 * Handle drawing the background
 */
public class GameBoard {
    ArrayList<ArrayList<Hex>> hexes;
    enum BoardShape {DIAMOND, BIG_HEX, SQUARE}

    public GameBoard(Resources resources, BoardShape boardShape, int sideLength) {
        hexes = new ArrayList<>();
        switch (boardShape) {
            case DIAMOND:
                for (int i = 0; i < sideLength; i++) {
                    ArrayList<Hex> row = new ArrayList<>();
                    for (int j = 0; j < sideLength; j++) {
                        row.add(new Hex(i, j, resources));
                    }
                    hexes.add(row);
                }
                break;
            case BIG_HEX:
                break;
            case SQUARE:
                break;
        }
    }
    public GameBoard(Resources resources, GameState gameState) {
        Tile[][] boardLayout = gameState.getBoardLayout();
        hexes = new ArrayList<>();
        for (int i = 0; i < boardLayout.length; i++) {
            ArrayList<Hex> hexRow = new ArrayList<>();
            for (int j = 0; j < boardLayout[i].length; j++) {
                Tile tile = boardLayout[i][j];
                if(tile == null) {
                    tile = new Tile();
                }
                int imageResourceID;
                if(tile.getTitle() == null) tile.setTitle("");
                switch (tile.getTitle()) {
                    case "Field":
                        imageResourceID = R.drawable.field;
                        break;
                    case "Forest":
                        imageResourceID = R.drawable.forest;
                        break;
                    case "City":
                        imageResourceID = R.drawable.city;
                        break;
                    default:
                        imageResourceID = R.drawable.hex;
                        break;
                }
                Bitmap tileBitmap = BitmapFactory.decodeResource(resources, imageResourceID);
                hexRow.add(new Hex(i,j,tileBitmap));
            }
            hexes.add(hexRow);
        }

    }

    public void update() {
    }

    public void draw(Canvas canvas) {
        if(canvas != null) {
            canvas.drawColor(Color.WHITE);
            for(ArrayList<Hex> row: hexes) {
                for(Hex hex: row) {
                    hex.draw(canvas);
                }
            }
        }
    }

}
