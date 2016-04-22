package edu.umt.csdept.survivorsoftheapocalypse;

import android.graphics.PointF;

/**
 * Created by jeff on 2/26/16.
 */
public class HexTest extends ApplicationTest {
    public static final float EPSILON = .001f;
    public void testRectCoordToHexOrigin() {
        // (0, 0) mapped to (0,0)
        PointF result = Hex.rectCoordsToHex(0, 0);
        assertEquals(0f, result.x, EPSILON);
        assertEquals(0f, result.y, EPSILON);
    }

    public void testHexCoordToRectUpperRight() {
        PointF result = Hex.hexCoordsToRect(1, 0);
        // r = 1, g = 0 is above and to the right of 0,0
        assertEquals(Hex.hexWidth * .75f, result.x, EPSILON);
        assertEquals(-Hex.hexHeight / 2, result.y, EPSILON);
    }

    public void testHexCoordsToRectUpper() {
        PointF result = Hex.hexCoordsToRect(0, 1);
        // r = 0, g = 0 is directly above 0,0
        assertEquals(0, result.x, EPSILON);
        assertEquals(-Hex.hexHeight, result.y, EPSILON);
    }

    public void testHexCoordToRectUpperLeft() {
        PointF result = Hex.hexCoordsToRect(-1, 1);
        // r = -1, g = 1 is above and to the left of 0,0
        assertEquals(-Hex.hexWidth * .75f, result.x, EPSILON);
        assertEquals(-Hex.hexHeight / 2, result.y, EPSILON);
    }

    public void testHexToRectToHex() {
        for (int i = -5; i < 5; i++) {
            for (int j = -5; j < 5; j++) {
                PointF rectResult = Hex.hexCoordsToRect(i,j);
                PointF hexResult = Hex.rectCoordsToHex(rectResult.x, rectResult.y);
                assertEquals(i, hexResult.x, EPSILON);
                assertEquals(j, hexResult.y, EPSILON);
            }
        }
    }

    /**
     * Additionally, in the app, the interface can be checked by zooming in very close. Clicking a
     * hex outputs the coordinates of the hex in the console. The correct coordinates should be
     * output right up to the border of the hex and immediately after crossing the edge, the
     * coordinates should change
     *
     * The rotation of the screen should not effect which hex coordinates are output. Clicking a
     * hex, rotating, clicking, rotating, etc. Each time the hex should show the same output
     *
     * Zooming should also not change the coordinates of a hex. To test this, first go to a moderate
     * zoom level, click a hex, zoom in and click the same hex. Zooming out should still yield the
     * same output.
     */
}
