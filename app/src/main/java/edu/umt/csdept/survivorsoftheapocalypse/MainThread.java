package edu.umt.csdept.survivorsoftheapocalypse;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * The main game loop
 */
public class MainThread extends Thread {
    private static final String NAME = "MainThread";

    private int FPS = 30;
    private double averageFPS;
    private final SurfaceHolder surfaceHolder;
    private GameBoardView gameBoardView;
    private  boolean running;
    public static Canvas canvas;

    public MainThread(SurfaceHolder surfaceHolder, GameBoardView gameBoardView) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameBoardView = gameBoardView;
    }

    @Override
    public void run() {
        super.run();
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount = 0;
        long targetTime = 1000/FPS;

        while(running) {
            startTime = System.nanoTime();
            if(!gameBoardView.renderedAtLeastOnce) {
                canvas = null;

                // try locking the canvas for pixel editing
                try {
                    canvas = this.surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder) {
                        this.gameBoardView.update();
                        this.gameBoardView.draw(canvas);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (canvas != null) {
                        try {
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis; // adjust waitTime

            try {
                this.sleep(waitTime);
            }
            catch (Exception e) {}

            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if(frameCount == FPS) {
                averageFPS = 1000/((totalTime/frameCount)/1000000);
                frameCount = 0;
                totalTime = 0;
                Log.d(NAME, "Average FPS: " + averageFPS);
            }
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
