package edu.umt.csdept.survivorsoftheapocalypse;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.Serializable;

/**
 * Created by sinless on 2/25/16.
 */
public abstract class PlayerCard implements Card, Serializable {
    protected static final String NAME = "PlayerCard";

    String CardName;
    @Override
    public abstract void onAquire(GameState gameState);


    @Override
    public abstract void onPlay(GameState gameState);

    public boolean onPlayAtLocation(GameState gameState, Location location) {
        Log.d(NAME, "played " + CardName + " at " + location.toString());
        return true;
    }

    public String getCardName() {
        return CardName;
    }

    public void setCardName(String cardName) {
        CardName = cardName;
    }
}
