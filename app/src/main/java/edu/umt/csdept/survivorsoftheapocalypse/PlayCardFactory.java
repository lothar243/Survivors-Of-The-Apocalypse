package edu.umt.csdept.survivorsoftheapocalypse;

/**
 * Created by sinless on 3/4/16.
 */
public class PlayCardFactory {
    private static PlayCardFactory ourInstance = new PlayCardFactory();

    public static PlayCardFactory getInstance() {
        return ourInstance;
    }

    private PlayCardFactory() {
    }

    public PlayCard makeCard(String cardName){
        PlayCard playCard = null;
        if (cardName.equalsIgnoreCase("Bumper Crop")) {
            playCard = new BumperCrop();
        }
        
        return playCard;
    }
}
