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

    public PlayerCard makeCard(String cardName){
        PlayerCard playerCard = null;
        if (cardName.equalsIgnoreCase("Bumper Crop")) {
            playerCard = new BumperCrop();
        }
        else if (cardName.equalsIgnoreCase("New Growth")) {
            playerCard = new NewGrowth();
        }
        else if (cardName.equalsIgnoreCase("Hidden Stores")) {
            playerCard = new HiddenStores();
        }
        else if (cardName.equalsIgnoreCase("Forest Fire")){
            playerCard = new ForestFire();
        }
        else if(cardName.equalsIgnoreCase("Locust Swarm")){
            playerCard = new LocustSwarm();
        }
        else if(cardName.equalsIgnoreCase("Raiders")){
            playerCard = new LocustSwarm();
        }
        else if(cardName.equalsIgnoreCase("Zombies")){
            playerCard = new LocustSwarm();
        }

        return playerCard;
    }
}
