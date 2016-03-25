package edu.umt.csdept.survivorsoftheapocalypse;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by sinless on 2/25/16.
 */
public abstract class PlayerCard implements Card {
    private static final String NAME = "PlayerCard";

    String CardName;
    @Override
    public abstract void onAquire(GameState gameState);


    @Override
    public abstract void onPlay(GameState gameState);

    public void onPlay(GameState gameState, Location location) {
        Log.d(NAME, "played " + CardName + " at " + location.toString());
    }

    public String getCardName() {
        return CardName;
    }

    public void setCardName(String cardName) {
        CardName = cardName;
    }
}
