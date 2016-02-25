package edu.umt.csdept.survivorsoftheapocalypse;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;

/**
 * Handle drawing the background
 */
public class HexLayout {
    public final String NAME = "HexLayout";

    private Bitmap image;
    private float x, y;
    public float dx = 0, dy = 0;
    Hex[][] background;

    final int verticalGap = 8, horizontalGap = 10;

    int hexWidth, hexHeight;
    int horizontalHexOffset, verticalHexOffset;
    int oddRowOffset;


    public HexLayout(Resources resources) {
        background = new Hex[5][5];
        for (int i = 0; i < 5; i++) {
            background[i] = new Hex[5];
            for (int j = 0; j < 5; j++) {
                background[i][j] = new Hex(i, j, resources);
            }
        }
    }

    public void update() {
        x += dx;
        dx = 0;
        y += dy;
        dy = 0;

    }

    public void draw(Canvas canvas) {
        if(canvas != null) {
            canvas.drawColor(Color.WHITE);
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    background[i][j].draw(canvas);
                }
            }
        }
    }

    public int getImageWidth()  {
        if(image != null) {
            return image.getWidth();
        }
        return 0;
    }

    public int getImageHeight() {
        if(image != null) {
            return image.getHeight();
        }
        return 0;
    }

    public Point gridOffset(int rowNum, int colNum) {
        int xOffset = colNum * horizontalHexOffset;
        int yOffset = rowNum * verticalHexOffset;
        if(rowNum % 2 == 1) {
            xOffset -= oddRowOffset;
        }
        return new Point(xOffset, yOffset);
    }


}
