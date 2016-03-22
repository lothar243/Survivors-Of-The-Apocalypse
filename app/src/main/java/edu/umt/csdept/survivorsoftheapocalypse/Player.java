package edu.umt.csdept.survivorsoftheapocalypse;

import java.util.ArrayList;

/**
 * Created by sinless on 3/7/16.
 */
public class Player {
    ArrayList<Location> locations;
    int woodCount;
    int foodCount;
    private String playerName;

    public Player(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public ArrayList<Location> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<Location> locations) {
        this.locations = locations;
    }

    public int getWoodCount() {
        return woodCount;
    }

    public void setWoodCount(int woodCount) {
        this.woodCount = woodCount;
    }

    public int getFoodCount() {
        return foodCount;
    }

    public void setFoodCount(int foodCount) {
        this.foodCount = foodCount;
    }

    public int[] playerResources(){
        int[] resources = new int[2];
        resources[0] = foodCount;
        resources[1] = woodCount;
        return  resources;
    }

    public boolean checkPresence(Location location) {
        if (locations.contains(location))
            return true;

        return false;
    }

    public void gatherResource(String resource, int amount) {
        if ("Wood".compareToIgnoreCase(resource)==0){
            woodCount+=amount;
        }
        else if("Food".compareToIgnoreCase(resource)==0){
            foodCount+=amount;
        }
    }

    public void spendResources(String resource, int amount){
        if ("Wood".compareToIgnoreCase(resource)==0){
            woodCount-=amount;
        }
        else if("Food".compareToIgnoreCase(resource)==0){
            foodCount-=amount;
        }
    }

    public void placePerson(Location location) {
        locations.add(location);
    }
}
