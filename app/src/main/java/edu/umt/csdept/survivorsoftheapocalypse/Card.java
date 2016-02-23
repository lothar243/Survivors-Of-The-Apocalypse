package edu.umt.csdept.survivorsoftheapocalypse;

public interface Card{

    public void draw();
    public void onAquire(GameState gameState);
    public void onPlay(GameState gameState);
}
