package edu.umt.csdept.survivorsoftheapocalypse;

import android.graphics.Bitmap;

public interface Card{

    public void onAquire(GameState gameState);
    public void onPlay(GameState gameState);
}
