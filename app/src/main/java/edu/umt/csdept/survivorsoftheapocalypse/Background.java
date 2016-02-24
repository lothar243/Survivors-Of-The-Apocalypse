package edu.umt.csdept.survivorsoftheapocalypse;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;

/**
 * Handle drawing the background
 */
public class Background {
    public final String NAME = "Background";

    private Bitmap image;
    private float x, y;
    public float dx = 0, dy = 0;

    final int verticalGap = 8, horizontalGap = 10;

    int hexWidth, hexHeight;
    int horizontalHexOffset, verticalHexOffset;
    int oddRowOffset;

    public Background(Bitmap res) {
        image = res;
        hexWidth = image.getWidth();
        hexHeight = image.getHeight();
        horizontalHexOffset = (int)(1.5f * hexWidth + 2 * horizontalGap);
        verticalHexOffset = (int)(.5f * hexHeight + verticalGap);
        oddRowOffset = (int)(.75f * hexWidth + horizontalGap);
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
            for(int col = 0; col < 10; col++) {
                for (int row = 0; row < 10; row++) {
                    Point offset = gridOffset(row, col);
                    canvas.drawBitmap(image, x + offset.x, y + offset.y, null);
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
