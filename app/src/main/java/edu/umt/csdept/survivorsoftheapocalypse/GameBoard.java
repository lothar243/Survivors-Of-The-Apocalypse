package edu.umt.csdept.survivorsoftheapocalypse;

import android.content.res.Resources;
import android.graphics.Bitmap;
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
