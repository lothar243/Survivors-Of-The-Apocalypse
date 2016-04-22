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
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

import java.util.ArrayList;

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

                    ______
             ______/ 0,1  \______
            / -1,1 \______/ 1, 0 \
            \______/ 0,0  \______/
            / -1,0 \______/ 1,-1 \
            \______/ 0,-1 \______/
                   \______/
    */
    private static final String NAME = "Hex";

    static final int horizontalGap = 10;
    static final int verticalGap = 6;
    static final int sideLength = 200;
    static final int hexWidth = Math.round(2f * sideLength + horizontalGap) + horizontalGap;
    static final int hexHeight = Math.round((float)Math.sqrt(3)* sideLength + verticalGap) + verticalGap;

    static final int maxImageWidth = 150, maxImageHeight = 150, imageX = 100, imageY = 10;
    static final int textX = 260, textY = 200;
    static final int maxMeepleWidth = 150, maxMeepleHeight = 150, meepleX = 100, meepleY = 180;

    static Resources resources;

    int rCoord, gCoord;
    Location location;
    private Bitmap image;
    private Bitmap meepleBitmap = null;
    ArrayList<Integer> playersPresent;

    int numResources;
    int resourceTypeColor;

    public Hex(int rCoord, int gCoord, Resources _resources, int resourceID, int numResources,
               int resourceTypeColor) {
        this.rCoord = rCoord;
        this.gCoord = gCoord;
        Bitmap hex = BitmapFactory.decodeResource(_resources, R.drawable.hex);
        this.image = Bitmap.createBitmap(hexWidth - horizontalGap, hexHeight - verticalGap, hex.getConfig());
        Canvas canvas = new Canvas(this.image);
        if(resources == null) {
            resources = _resources;
        }

        Matrix hexMatrix = new Matrix();
        float[] fromVector = {0f, 0f, hex.getWidth(), hex.getHeight()};
        float[] toVector = {0f, 0f, image.getWidth(), image.getHeight()};
        hexMatrix.setPolyToPoly(fromVector, 0, toVector, 0, 2);
        canvas.drawBitmap(hex, hexMatrix, null);

        Matrix imageMatrix = new Matrix();
        Bitmap picture = BitmapFactory.decodeResource(_resources, resourceID);
        float scale = Math.min((float)maxImageWidth / picture.getWidth(), (float)maxImageHeight / picture.getHeight());
        imageMatrix.setScale(scale, scale);
        imageMatrix.postTranslate(imageX, imageY);
        canvas.drawBitmap(picture, imageMatrix, null);

        this.numResources = numResources;
        this.resourceTypeColor = resourceTypeColor;
        location = new Location(rCoord, gCoord);
        playersPresent = new ArrayList<>();
    }

    public PointF getCenter() {
        return hexCoordsToRect(this.rCoord, this.gCoord);
    }

    public void draw(Canvas canvas) {
        if(image == null) return;
        PointF center = getCenter();
        float offsetX = center.x - .5f * hexWidth, offsetY = center.y - .5f * hexHeight;
        canvas.drawBitmap(image, offsetX, offsetY, null);
        Paint paint = new Paint();
        paint.setColor(resourceTypeColor);
        paint.setTextSize(150);
        canvas.drawText(numResources + "", offsetX + textX, offsetY + textY, paint);
        if(meepleBitmap != null) canvas.drawBitmap(meepleBitmap, offsetX + meepleX, offsetY + meepleY, null);
    }
    public Location getLocation() {
        return location;
    }

    public void setMeeple(int playerNumber) {
        int meepleResourceID;
        switch (playerNumber) {
            case 0:
                meepleResourceID = R.drawable.p1meeple;
                break;
            case 1:
                meepleResourceID = R.drawable.p2meeple;
                break;
            case 2:
                meepleResourceID = R.drawable.p3meeple;
                break;
            case 3:
                meepleResourceID = R.drawable.p4meeple;
                break;
            case 4:
                meepleResourceID = R.drawable.p5meeple;
                break;
            case 5:
                meepleResourceID = R.drawable.p6meeple;
                break;
            default:
                meepleResourceID = R.drawable.meeple;
        }
        meepleBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, meepleResourceID),
                maxImageWidth, maxMeepleHeight, false);
        if(playerNumber == -1) {
            meepleBitmap = null;
        }
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

    public static Point getHexIndex(float x, float y) {
        // determine the hex whose center is to the bottom left
        PointF hexCoords = rectCoordsToHex(x, y);
        int referenceR = (int)hexCoords.x;
        int referenceG = (int)hexCoords.y;

        // look through each hex (bottom left, bottom right, top right, top left) to see which one
        // the clicked point is closest to
        final int[] rDelta = {0, 1, 1, 0};
        final int[] gDelta = {0, 0, 1, 1};

        int currentClosest = 0;
        float minDist = Float.MAX_VALUE;
        for (int i = 0; i < 4; i++) {
            PointF currentTestPoint = hexCoordsToRect(referenceR + rDelta[i], referenceG + gDelta[i]);
            float currentDistance = distanceSquaredBetween(
                    x, y,
                    currentTestPoint.x, currentTestPoint.y

            );
            if(currentDistance < minDist) {
                currentClosest = i;
                minDist = currentDistance;
            }
        }
        return new Point(referenceR + rDelta[currentClosest], referenceG + gDelta[currentClosest]);
    }

    private static float distanceSquaredBetween(float x1, float y1, float x2, float y2) {
        float xDiff = x2 - x1;
        float yDiff = y2 - y1;
        return xDiff * xDiff + yDiff * yDiff;
    }

}
