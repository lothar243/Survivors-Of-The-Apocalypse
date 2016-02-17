package edu.umt.csdept.survivorsoftheapocalypse;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * The main surfaceview for the game
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    final String NAME = "MainSurfaceView";

    public static float backgoundWidth;
    public static float backgroundHeight;
    public static final int MOVESPEED = -5;
    private MainThread thread;
    private Background background;
    public static float screenWidth;
    public static float screenHeight;

    public float touchX = -1, touchY = -1;

    public GamePanel(Context context) {
        super(context);


        // add the game loop
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        // make gamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        background = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.hexgrid));
        backgoundWidth = background.getImageWidth();
        backgroundHeight = background.getImageHeight();
        screenWidth = getWidth();
        screenHeight = getHeight();

        // we can safely start the game loop
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
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchX = event.getX();
                touchY = event.getY();
                Log.d(NAME, touchX + ", " + touchY);
                return true;
            case MotionEvent.ACTION_MOVE:
                background.dx += event.getX() - touchX;
                background.dy += event.getY() - touchY;
                touchX = event.getX();
                touchY = event.getY();
//                Log.d(NAME, background.dx + " x " + background.dy);
                return true;
            case MotionEvent.ACTION_UP:
                background.dx = 0;
                background.dy = 0;
                Log.d(NAME, touchX + ", " + touchY);
                touchX = -1;
                touchY = -1;
                return true;
        }

        return super.onTouchEvent(event);
    }

    public void update() {
        background.update();
    }

    @Override
    public void draw(Canvas canvas) {
        final float scaleFactorX = getWidth()/ backgoundWidth;
        final float scaleFactorY = getHeight()/ backgroundHeight;
        super.draw(canvas);
        if(canvas != null) {
//            final int savedState = canvas.save();
//            canvas.scale(scaleFactorX, scaleFactorY);
            background.draw(canvas);
//            canvas.restoreToCount(savedState);
        }
    }
}

