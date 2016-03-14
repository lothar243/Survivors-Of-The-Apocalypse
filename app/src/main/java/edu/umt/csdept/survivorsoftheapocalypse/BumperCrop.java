package edu.umt.csdept.survivorsoftheapocalypse;

/**
 * Created by sinless on 3/4/16.
 */
public class BumperCrop extends PlayerCard {
    public BumperCrop() {
        CardName = "BumperCrop";
    }


    @Override
    public void onAquire(GameState gameState) {

    }

    @Override
    public void onPlay(GameState gameState) {
        gameState.incrementResources("Field");
    }
}
