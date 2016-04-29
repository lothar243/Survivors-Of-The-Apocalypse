package edu.umt.csdept.survivorsoftheapocalypse;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by sinless on 2/25/16.
 */
public class Tile implements Card, Serializable{
    private String title;
    private String resource;
    private int banditDanger;
    private int zombieDanger;
    private int resourceCount;
    private int deckCount;


    @Override
    public void onAquire(GameState gameState) {

        onPlay(gameState);
    }

    @Override
    public void onPlay(GameState gameState) {
    //do what needs to be done to add tile to board

    }

    public void setTile(GameState gameState , Location location){
        gameState.PlaceTile(this, location);
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public int getBanditDanger() {
        return banditDanger;
    }

    public void setBanditDanger(int banditDanger) {
        this.banditDanger = banditDanger;
    }

    public int getZombieDanger() {
        return zombieDanger;
    }

    public void setZombieDanger(int zombieDanger) {
        this.zombieDanger = zombieDanger;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "title='" + title + '\'' +
                ", resource='" + resource + '\'' +
                ", banditDanger=" + banditDanger +
                ", zombieDanger=" + zombieDanger +
                ", resourceCount=" + resourceCount +
                ", deckCount=" + deckCount +
                '}';
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getResourceCount() {
        return resourceCount;
    }

    public void setResourceCount(int resourceCount) {
        this.resourceCount = resourceCount;
    }

    public int getDeckCount() {
        return deckCount;
    }

    public void setDeckCount(int deckCount) {
        this.deckCount = deckCount;
    }
}
