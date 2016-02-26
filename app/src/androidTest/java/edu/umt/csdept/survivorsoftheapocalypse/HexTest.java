package edu.umt.csdept.survivorsoftheapocalypse;

import android.graphics.PointF;

/**
 * Created by jeff on 2/26/16.
 */
public class HexTest extends ApplicationTest {
    public static final float tollerance = .001f;
    public void testRectCoordToHexOrigin() {
        // (0, 0) mapped to (0,0)
        PointF result = Hex.rectCoordsToHex(0, 0);
        assertEquals(0f, result.x, tollerance);
        assertEquals(0f, result.y, tollerance);
    }

    public void testHexCoordToRectUpperRight() {
        PointF result = Hex.hexCoordsToRect(1, 0);
        // r = 1, g = 0 is above and to the right of 0,0
        assertEquals(Hex.hexWidth * .75f, result.x, tollerance);
        assertEquals(-Hex.hexHeight / 2, result.y, tollerance);
    }

    public void testHexCoordsToRectUpper() {
        PointF result = Hex.hexCoordsToRect(0, 1);
        // r = 0, g = 0 is directly above 0,0
        assertEquals(0, result.x, tollerance);
        assertEquals(-Hex.hexHeight, result.y, tollerance);
    }

    public void testHexCoordToRectUpperLeft() {
        PointF result = Hex.hexCoordsToRect(-1, 1);
        // r = -1, g = 1 is above and to the left of 0,0
        assertEquals(-Hex.hexWidth * .75f, result.x, tollerance);
        assertEquals(-Hex.hexHeight / 2, result.y, tollerance);
    }

    public void testHexToRectToHex() {
        for (int i = -5; i < 5; i++) {
            for (int j = -5; j < 5; j++) {
                PointF rectResult = Hex.hexCoordsToRect(i,j);
                PointF hexResult = Hex.rectCoordsToHex(rectResult.x, rectResult.y);
                assertEquals(i, hexResult.x, tollerance);
                assertEquals(j, hexResult.y, tollerance);
            }
        }
    }
}
