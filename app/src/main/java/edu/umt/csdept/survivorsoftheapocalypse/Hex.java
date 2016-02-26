package edu.umt.csdept.survivorsoftheapocalypse;

/**
 * I'm using a hex coordinate idea taken from the following url:
 * http://stackoverflow.com/questions/2459402/hexagonal-grid-coordinates-to-pixel-coordinates
 *
 * summary of their system
 * y = 3/2 * s * b
 * b = 2/3 * y / s
 * x = sqrt(3) * s * ( b/2 + r)
 * x = - sqrt(3) * s * ( b/2 + g )
 * r = (sqrt(3)/3 * x - y/3 ) / s
 * g = -(sqrt(3)/3 * x + y/3 ) / s
 *
 * r + b + g = 0
 *
 * my system:
 * I want the hexes to have a flat spot on top and bottom, so I'll switch the x and y
 *
 * hex coordinates descriptions
 * r vertical, positive up
 * g lower right to upper left, positive up
 * b lower-left to upper-right, positive up
 *
 * x =
 */

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

public class Hex {
    /*
            I'm using a hex coordinate idea taken from the following url:
            http://stackoverflow.com/questions/2459402/hexagonal-grid-coordinates-to-pixel-coordinates

            summary of their system
            y = 3/2 * s * b
            b = 2/3 * y / s
            x = sqrt(3) * s * ( b/2 + r)
            x = - sqrt(3) * s * ( b/2 + g )
            r = (sqrt(3)/3 * x - y/3 ) / s
            g = -(sqrt(3)/3 * x + y/3 ) / s

            r + b + g = 0

            my system:
            I want the hexes to have a flat spot on top and bottom, so I'll switch the x and y

            hex coordinates descriptions
            r horizontal, positive right
            g upper right to lower left, positive down-left
            b lower right to upper left, positive up-left

            x and y describe the center of each hex
            y = -sqrt(3) * s * (

                      ________
             ________/ 0,1    \________
            / -1,1   \________/ 1,   0 \
            \________/ 0,0    \________/
            / -1,0   \________/ 1,  -1 \
            \________/ 0,  -1 \________/
                     \________/
    */
    private static final String NAME = "Hex";

    static int sideLength = 200;
    static int horizontalGap = 10;
    static int verticalGap = 6;
    static final float sqrtThree = (float)Math.sqrt(3);
    int rCoord, gCoord;
    private Bitmap image;


    public Hex(int rCoord, int gCoord, Resources resources) {
        this.rCoord = rCoord;
        this.gCoord = gCoord;
        image = BitmapFactory.decodeResource(resources, R.drawable.hex);
        sideLength = image.getWidth() / 2;
    }
    public Hex(int rCoord, int gCoord, Resources resources, int resourceID) {
        this.rCoord = rCoord;
        this.gCoord = gCoord;
        this.image = BitmapFactory.decodeResource(resources, resourceID);
    }

    public void changeImage(Resources resources, int resourceID) {
        this.image = BitmapFactory.decodeResource(resources, resourceID);
    }

    public PointF getCenter() {
        float xCoord = 1.5f * (sideLength + horizontalGap) * rCoord;
        // as the rCoord increases, y increases by sqrt(3) / 2 * sideLength
        // as the gCoord increases, y increases by sqrt(3) * sideLength
        float yCoord =  sqrtThree * (sideLength + verticalGap) * (.5f * rCoord + gCoord);
        return new PointF(xCoord, yCoord);
    }

    public void draw(Canvas canvas) {
        if(image == null) return;
        PointF center = getCenter();
        canvas.drawBitmap(image, center.x - sideLength,  center.y - sqrtThree * sideLength, null);
    }

    public static Point rectangularCoordsToHexCoords(float x, float y) {
        int r = (int)((2 * x) / (3 * sideLength) + .5);
        int g = (int)(y / (sqrtThree * sideLength) - x / (3 * sideLength) + 1);
        Log.d(NAME, "r = " + r + ", g = " + g);
        return new Point(r, g);
    }
}
