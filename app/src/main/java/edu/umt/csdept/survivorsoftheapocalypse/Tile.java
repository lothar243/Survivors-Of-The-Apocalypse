package edu.umt.csdept.survivorsoftheapocalypse;

/**
 * Created by sinless on 2/25/16.
 */
public class Tile implements Card{

    @Override
    public void draw() {

    }

    @Override
    public void onAquire(GameState gameState) {
    onPlay(gameState);
    }

    @Override
    public void onPlay(GameState gameState) {
    //do what needs to be done to add tile to board
    }
}
