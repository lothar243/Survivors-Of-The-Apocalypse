package edu.umt.csdept.survivorsoftheapocalypse;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

/**
 * Handle drawing the background
 */
public class Background {
    public final String NAME = "Background";

    private Bitmap image;
    private float x, y;
    public float dx = 0, dy = 0;


    public Background(Bitmap res) {
        image = res;
    }

    public void update() {
        x += dx;
        while(x < 0) {
            x += image.getWidth();
        }
        while(x >= image.getWidth()) {
            x -= image.getWidth();
        }
        y += dy;
        while(y < 0) {
            y += image.getHeight();
        }
        while(y >= image.getHeight()) {
            y -= image.getHeight();
        }

    }

    public void draw(Canvas canvas) {
        if(canvas != null) {
            int numCols = (int)(GamePanel.screenWidth / image.getWidth());
            int numRows = (int)(GamePanel.screenHeight / image.getHeight());
            for(int col = -1; col < numCols + 2; col++) {
                for (int row = -1; row < numRows + 2; row++) {
                    canvas.drawBitmap(image, x + col * image.getWidth(), y + row * image.getHeight(), null);
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
}
