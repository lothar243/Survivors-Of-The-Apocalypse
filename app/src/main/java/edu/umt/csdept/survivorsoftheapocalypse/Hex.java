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

            my system:
            hex coordinates descriptions
            r horizontal, positive right
            g upper left to lower right, positive up-left

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

    static final int horizontalGap = 10;
    static final int verticalGap = 6;
    static final int sideLength = 200;
    static final int hexWidth = Math.round(2f * sideLength + horizontalGap) + horizontalGap;
    static final int hexHeight = Math.round((float)Math.sqrt(3)* sideLength + verticalGap) + verticalGap;


    int rCoord, gCoord;
    private Bitmap image;



    public Hex(int rCoord, int gCoord, Resources resources) {
        this.rCoord = rCoord;
        this.gCoord = gCoord;
        image = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.hex),
                hexWidth - horizontalGap, hexHeight - verticalGap, false);

    }
    public Hex(int rCoord, int gCoord, Resources resources, int resourceID) {
        this.rCoord = rCoord;
        this.gCoord = gCoord;
        this.image = BitmapFactory.decodeResource(resources, resourceID);
    }

    public void changeImage(Resources resources, int resourceID) {
        this.image = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, resourceID),
                hexWidth - horizontalGap, hexHeight - verticalGap, false);
    }

    public void changeImage(Bitmap image) {
        this.image = Bitmap.createScaledBitmap(image,
                hexWidth - horizontalGap, hexHeight - verticalGap, false);
    }

    public PointF getCenter() {
        return hexCoordsToRect(this.rCoord, this.gCoord);
    }

    public void draw(Canvas canvas) {
        if(image == null) return;
        PointF center = getCenter();
        canvas.drawBitmap(image, center.x - .5f * hexWidth,  center.y - .5f * hexHeight, null);
    }

    public static PointF rectCoordsToHex(float x, float y) {
        float r = (x * 4) / (3 * hexWidth);
        float g = -y / hexHeight - (2 * x) / (3 * hexWidth);
        Log.d(NAME, "r = " + r + ", g = " + g);
        return new PointF(r, g);
    }

    public static PointF hexCoordsToRect(float r, float g) {
        // as r increases, x increases by 3/4 of a hex width
        // g doesn't effect x
        float x = r * .75f * hexWidth;
        // as r increases, y decreases by half the hex height
        // as g increases, y decreases by a full hex height
        float y = - (.5f * r + g) * hexHeight;
        return new PointF(x, y);
    }

}
