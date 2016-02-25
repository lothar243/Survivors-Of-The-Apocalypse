package edu.umt.csdept.survivorsoftheapocalypse;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * The main surfaceview for the game
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    final String NAME = "MainSurfaceView";

    public static float backgroundWidth;
    public static float backgroundHeight;
    private MainThread thread;
    private HexLayout hexLayout;
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

        hexLayout = new HexLayout(getResources());
        backgroundWidth = hexLayout.getImageWidth();
        backgroundHeight = hexLayout.getImageHeight();
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
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchX = event.getX();
                touchY = event.getY();
                Log.d(NAME, touchX + ", " + touchY);
                return true;
            case MotionEvent.ACTION_MOVE:
                hexLayout.dx += event.getX() - touchX;
                hexLayout.dy += event.getY() - touchY;
                touchX = event.getX();
                touchY = event.getY();
//                Log.d(NAME, hexLayout.dx + " x " + hexLayout.dy);
                return true;
            case MotionEvent.ACTION_UP:
                hexLayout.dx = 0;
                hexLayout.dy = 0;
                Log.d(NAME, touchX + ", " + touchY);
                touchX = -1;
                touchY = -1;
                return true;
        }

        return super.onTouchEvent(event);
    }

    public void update() {
        hexLayout.update();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.save();
        final float scaleFactorX = getWidth()/ backgroundWidth;
        final float scaleFactorY = getHeight()/ backgroundHeight;
        super.draw(canvas);
        hexLayout.draw(canvas);
    }
}

//todo 3 layouts: board (scrollable and zoomable), player stats, player actions (scrollable)