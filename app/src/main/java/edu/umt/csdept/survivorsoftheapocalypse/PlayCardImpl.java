package edu.umt.csdept.survivorsoftheapocalypse;

import android.util.Log;

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
        Log.d(CardName, "Start");
        Log.d(CardName, locations.toString());
        for (int i = 0; i > locations.length; i++) {
            Location location = locations[i];
            ArrayList<Integer> playersPresent = gameState.checkPresence(location);
            Log.d(CardName, playersPresent.toString());
            ArrayList<Location> wallLocations = gameState.wallLocations;
            if (!wallLocations.contains(location)) {
                Log.d(CardName, "Start");
                Tile tile = gameState.getTile(location);
                if (tile.getZombieDanger() > 0) {
                    for( int j = 0; j< playersPresent.size(); j++) {
                        Log.d(CardName, playersPresent.get(j).toString());
                        Log.d(CardName, location.toString());
                        gameState.removePerson(playersPresent.get(j), location);
                    }
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
            Log.d(CardName, "Start");
            Log.d(CardName, locations.toString());
            for (int i = 0; i > locations.length; i++) {
                Location location = locations[i];
                ArrayList<Integer> playersPresent = gameState.checkPresence(location);
                Log.d(CardName, playersPresent.toString());
                ArrayList<Location> wallLocations = gameState.wallLocations;
                if (!wallLocations.contains(location)) {
                    System.out.println();
                    Tile tile = gameState.getTile(location);
                    if (tile.getBanditDanger() > 0) {
                        for( int j = 0; j< playersPresent.size(); j++) {
                            Log.d(CardName,playersPresent.get(j).toString());
                            Log.d(CardName, location.toString());
                            gameState.removePerson(playersPresent.get(j), location);
                        }
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
    public boolean onPlayAtLocation(GameState gameState, Location location) {
        // this is called as a result of the gamestate.promptActivityForLocation after the location has been chosen
        if(gameState.tileAtLocation(location) && !gameState.wallAtLocation(location)) {
            gameState.addWall(location);
            return true;
        }
        else return false;
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