package edu.umt.csdept.survivorsoftheapocalypse;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Calendar;

/**
 * The main surfaceview for the game
 */
public class GameBoardView extends SurfaceView implements SurfaceHolder.Callback {
    final String NAME = "MainSurfaceView";

    private MainThread thread;
    public GameBoard gameBoard;
    private GameState gameState;

    Matrix matrix = new Matrix();

    float touchX = 0, touchY = 0;
    float touchXSecond = 0, touchYSecond = 0;
    float averageX = 0, averageY = 0;
    int touchCount = 0;
    boolean moving = false;
    boolean hasLongPressed = false;
    final float DISTANCE_BEFORE_MOVING = 50;
    long timeOfInitialPress; // used for detecting a long onPress

    public GameBoardView(Context context, GameState gameState) {
        super(context);
        this.gameState = gameState;
        gameBoard = new GameBoard(getResources(), this.gameState);

        // add the game loop
        getHolder().addCallback(this);


        // make gamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        // we can safely start the game loop
        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // stop the thread when it's not displayed
        boolean retry = true;
        while(retry) {
            try {
                thread.setRunning(false);
                thread.join();
                retry = false;
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchX = event.getX();
                touchY = event.getY();
                moving = false;
                hasLongPressed = false;
                timeOfInitialPress = Calendar.getInstance().getTimeInMillis();
                return true;
            case MotionEvent.ACTION_UP:
                if(!moving)
                    onPress();
                return true;
            case MotionEvent.ACTION_POINTER_DOWN:
                averageX = (event.getX(0) + event.getX(1)) / 2;
                averageY = (event.getY(0) + event.getY(1)) / 2;
                moving = true;
                return true;
            case MotionEvent.ACTION_MOVE:
                if(!moving && (distanceFromInitialTouch(event) < DISTANCE_BEFORE_MOVING)) {
                    return true;
                }
                moving = true;
                if(event.getPointerCount() == 1) {
                    // only one finger is touching the screen
                    if(touchCount == 1) { // don't move when the user switches from 1-2 and vice versa
                        matrix.postTranslate(event.getX() - touchX, event.getY() - touchY);
                    }
                    touchX = event.getX();
                    touchY = event.getY();
                    touchCount = event.getPointerCount();

                    return true;
                }
                else {
                    if(touchCount == 2) { // don't move when the user switches from 1-2 and vice versa
                        // first, determine the location of the touches on the untranslated image
                        Matrix inverseMatrix = new Matrix();
                        if(!matrix.invert(inverseMatrix)) return true; // the matrix has no inverse somehow
                        float[] imagePoints = new float[]{touchX, touchY, touchXSecond, touchYSecond};
                        inverseMatrix.mapPoints(imagePoints); // the touch locations without any transformations

                        float[] touchPoints = new float[]{
                                event.getX(0), event.getY(0), event.getX(1), event.getY(1)};
                        // now, create a matrix that sends those points to the current touch locations
                        matrix.setPolyToPoly(imagePoints, 0, touchPoints, 0, 2);
                    }
                    touchX = event.getX(0);
                    touchY = event.getY(0);
                    touchXSecond = event.getX(1);
                    touchYSecond = event.getY(1);
                    touchCount = event.getPointerCount();

                    return true;
                }
            case MotionEvent.ACTION_POINTER_UP:
                if(event.getPointerCount() == 1) {
                    touchX = event.getX();
                    touchY = event.getY();
                }

                return true;
        }
        return true;
    }

    public float distanceFromInitialTouch(MotionEvent event) {
        float dx = touchX - event.getX();
        float dy = touchY - event.getY();
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public void onPress() {
        Matrix inverseMatrix = new Matrix();
        if(!matrix.invert(inverseMatrix)) {
            return;
        }
        float[] imageTouchPoints = {touchX, touchY};
        inverseMatrix.mapPoints(imageTouchPoints);
        Point clickedIndex = Hex.getHexIndex(imageTouchPoints[0], imageTouchPoints[1]);
//        Hex.rectCoordsToHex(imageTouchPoints[0], imageTouchPoints[1]);
        Log.d(NAME, "onPress at " + clickedIndex.x + ", " + clickedIndex.y);
    }

    public void update() {
        gameBoard.update();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.setMatrix(matrix);
        canvas.save();
        super.draw(canvas);
        gameBoard.draw(canvas);
    }
}
