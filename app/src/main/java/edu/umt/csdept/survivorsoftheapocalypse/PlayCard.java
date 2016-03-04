package edu.umt.csdept.survivorsoftheapocalypse;

/**
 * Created by sinless on 2/25/16.
 */
public abstract class PlayCard implements Card {

    @Override
    public void draw() {

    }

    @Override
    public abstract void onAquire(GameState gameState);


    @Override
    public abstract void onPlay(GameState gameState);
}
