package edu.umt.csdept.survivorsoftheapocalypse;

import java.util.ArrayList;

/**
 * Created by sinless on 3/4/16.
 */
class BumperCrop extends PlayerCard {
    public BumperCrop() {
        CardName = "Bumper Crop";
    }


    @Override
    public void onAquire(GameState gameState) {
        onPlay(gameState);
    }

    @Override
    public void onPlay(GameState gameState) {
        gameState.incrementResources("Field",1);
    }
}

class NewGrowth extends PlayerCard{

    public NewGrowth() { CardName = "New Growth";
    }

    @Override
    public void onAquire(GameState gameState) {
        onPlay(gameState);
    }

    @Override
    public void onPlay(GameState gameState) {
        gameState.incrementResources("Forest",1);
    }
}

class HiddenStores extends PlayerCard{


    public HiddenStores() {
        CardName = "Hidden Stores";
    }

    @Override
    public void onAquire(GameState gameState) {
        onPlay(gameState);
    }

    @Override
    public void onPlay(GameState gameState) {
        gameState.incrementResources("City", 1);
    }

}

class LocustSwarm extends PlayerCard{
    public LocustSwarm() {
        CardName = "Locust Swarm";
    }

    @Override
    public void onAquire(GameState gameState) {
        onPlay(gameState);
    }

    @Override
    public void onPlay(GameState gameState) {
        gameState.incrementResources("Field", -1);
    }
}

class ForestFire extends PlayerCard{
    public ForestFire() {
        CardName = "Forest Fire";
    }

    @Override
    public void onAquire(GameState gameState) {
        onPlay(gameState);
    }

    @Override
    public void onPlay(GameState gameState) {
        gameState.incrementResources("Forest", -1);
    }
}

class Zombies extends PlayerCard {

    public Zombies() {
        CardName = "ZOMBIES!!";
    }

    @Override
    public void onAquire(GameState gameState) {
        onPlay(gameState);
    }

    @Override
    public void onPlay(GameState gameState) {
        for (int i = 0; i < gameState.getPlayerCount(); i++) {
            ArrayList<Location> playerLocations = gameState.getPlayerLocation(i);
            ArrayList<Location> wallLocations = gameState.wallLocations;
            for (Location location : playerLocations) {
                if (!wallLocations.contains(location)) {
                    gameState.removePerson(i, location);
                }
            }

        }
        gameState.notifyActivityTilesChanged();
    }
}
    class Raiders extends PlayerCard {

        public Raiders() {
            CardName = "RAIDERS!!";
        }

        @Override
        public void onAquire(GameState gameState) {
            onPlay(gameState);
        }

        @Override
        public void onPlay(GameState gameState) {
            for (int i = 0; i < gameState.getPlayerCount(); i++) {
                ArrayList<Location> playerLocations = gameState.getPlayerLocation(i);
                ArrayList<Location> wallLocations = gameState.wallLocations;
                for (Location location : playerLocations) {
                    if (!wallLocations.contains(location)) {
                        gameState.removePerson(i, location);
                    }
                }

            }
        }

}

class GreatWall extends PlayerCard{
    public GreatWall() {
        CardName = "The Great Wall Rises";
    }

    @Override
    public void onAquire(GameState gameState) {
        onPlay(gameState);
    }

    @Override
    public void onPlay(GameState gameState) {
        //todo this needs to create a wall on selected Location
        gameState.promptActivityForLocation(this);

    }

    @Override
    public void onPlay(GameState gameState, Location location) {
        // this is called as a result of the gamestate.promptActivityForLocation after the location has been chosen
        gameState.addWall(location);
    }

}