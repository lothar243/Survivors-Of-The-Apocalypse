package edu.umt.csdept.survivorsoftheapocalypse;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

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
        Location[] locations = gameState.findAllTiles();
        for (int i = 0; i > locations.length; i++) {
            Location location = locations[i];
            ArrayList<Integer> playersPresent = gameState.checkPresence(location);
            ArrayList<Location> wallLocations = gameState.wallLocations;
            if (!wallLocations.contains(location)) {
                Tile tile = gameState.getTile(location);
                if (tile.getZombieDanger() > 0) {
                    gameState.removePerson(i, location);
                }
            } else {
                gameState.removeWall(location);
            }

            gameState.notifyActivityTilesChanged();
        }
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
            Location[] locations = gameState.findAllTiles();
            for (int i = 0; i > locations.length; i++) {
                Location location = locations[i];
                ArrayList<Integer> playersPresent = gameState.checkPresence(location);
                ArrayList<Location> wallLocations = gameState.wallLocations;
                if (!wallLocations.contains(location)) {
                    Tile tile = gameState.getTile(location);
                    if (tile.getBanditDanger() > 0) {
                        gameState.removePerson(i, location);
                    }
                } else {
                    gameState.removeWall(location);
                }

                gameState.notifyActivityTilesChanged();
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
class ScoreCard extends PlayerCard{
    public ScoreCard() {
        CardName = "Score Card";
    }

    @Override
    public void onAquire(GameState gameState) {
        onPlay(gameState);
    }

    @Override
    public void onPlay(GameState gameState) {
        ArrayList<Integer> scores = new ArrayList<>();
        for(int i = 0; i < gameState.players.size(); i++){
            int score = 0;
            Player current = gameState.players.get(i);
            score += current.getFoodCount();
            score+= current.getWoodCount();
            score += current.getUnitCount()*3;
            scores.add(score);
            //todo get scores out of this

        }
    }
}