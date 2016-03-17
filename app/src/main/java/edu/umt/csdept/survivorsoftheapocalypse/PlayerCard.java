package edu.umt.csdept.survivorsoftheapocalypse;

import android.graphics.Bitmap;

/**
 * Created by sinless on 2/25/16.
 */
public abstract class PlayerCard implements Card {

    String CardName;
    @Override
    public abstract void onAquire(GameState gameState);


    @Override
    public abstract void onPlay(GameState gameState);

    public String getCardName() {
        return CardName;
    }

    public void setCardName(String cardName) {
        CardName = cardName;
    }
}
