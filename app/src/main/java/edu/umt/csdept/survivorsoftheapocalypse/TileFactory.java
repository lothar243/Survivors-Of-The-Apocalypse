package edu.umt.csdept.survivorsoftheapocalypse;

/**
 * Created by sinless on 3/16/16.
 */
public class TileFactory {
    private static TileFactory ourInstance = new TileFactory();

    public static TileFactory getInstance() {
        return ourInstance;
    }

    private TileFactory() {
    }

    public Tile getTile(String item){
    Tile tile = null;
        return tile;
    }
}
