package edu.umt.csdept.survivorsoftheapocalypse;

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
        gameState.promptActivityForLocation(this); // use this to cause the activity to prompt the user
        gameState.incrementResources("City", 1);
    }

    @Override
    public void onPlay(GameState gameState, Location location) {
        // custom behavior after the location has been selected
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