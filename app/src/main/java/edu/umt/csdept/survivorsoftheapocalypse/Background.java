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

    final int verticalGap = 10, horizontalGap = 10;

    int hexWidth, hexHeight;


    public Background(Bitmap res) {
        image = res;
        hexWidth = image.getWidth();
        hexHeight = image.getHeight();
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
        int xOffset = (int) (colNum * (1.5f * hexWidth + 2 * horizontalGap));
        int yOffset = (int)(.5f * rowNum * (hexHeight + verticalGap));
        if(rowNum % 2 == 1) {
            xOffset -= (int)(.75f * hexWidth + horizontalGap);
        }
        return new Point(xOffset, yOffset);
    }
}
