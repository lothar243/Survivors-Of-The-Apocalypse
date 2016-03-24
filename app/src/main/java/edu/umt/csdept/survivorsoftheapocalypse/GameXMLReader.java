package edu.umt.csdept.survivorsoftheapocalypse;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;

import java.util.ArrayList;

/**
 * Read in the various XML files for the game including its cards
 */
public class GameXMLReader {
    public static final String NAME = "GameXMLReader";
    public static final String DEFAULT_XML_STRING = "unspecified";
    public static final int DEFAULT_INT_UNSPECIFIED_ATTRIBUTE = -10001;
    public static final int DEFAULT_INT_READ_ERROR = -10002;

    public static ArrayList<Tile> readTileCards(Resources resources) {
        XmlResourceParser parser = resources.getXml(R.xml.tile);
        try {
            ArrayList<Tile> tiles = new ArrayList<>();
            int eventType = parser.getEventType();
            while(eventType != XmlResourceParser.END_DOCUMENT) {
                try {
                    if (eventType == XmlResourceParser.START_TAG
                            && parser.getName().equalsIgnoreCase("tile")) {
                        String name = DEFAULT_XML_STRING;
                        String resourceType = DEFAULT_XML_STRING;
                        int resourceCount = DEFAULT_INT_UNSPECIFIED_ATTRIBUTE;
                        int zombieThreat = DEFAULT_INT_UNSPECIFIED_ATTRIBUTE;
                        int raiderThreat = DEFAULT_INT_UNSPECIFIED_ATTRIBUTE;
                        int tileDeckCount = DEFAULT_INT_UNSPECIFIED_ATTRIBUTE;
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            switch (parser.getAttributeName(i)) {
                                case "Name":
                                    name = parser.getAttributeValue(i);
                                    break;
                                case "ResourceType":
                                    resourceType = parser.getAttributeValue(i);
                                    break;
                                case "ResourceCount":
                                    resourceCount = parser.getAttributeIntValue(i, DEFAULT_INT_READ_ERROR);
                                    break;
                                case "ZombieThreat":
                                    zombieThreat = parser.getAttributeIntValue(i, DEFAULT_INT_READ_ERROR);
                                    break;
                                case "RaiderThreat":
                                    raiderThreat = parser.getAttributeIntValue(i, DEFAULT_INT_READ_ERROR);
                                    break;
                                case "TileDeckCount":
                                    tileDeckCount = parser.getAttributeIntValue(i, DEFAULT_INT_READ_ERROR);
                                    break;
                                default:
                                    break;
                            }
                        }
                        Tile currentTile = new Tile();
                        currentTile.setTitle(name);
                        currentTile.setResource(resourceType);
                        currentTile.setDeckCount(tileDeckCount);
                        currentTile.setResourceCount(resourceCount);
                        currentTile.setZombieDanger(zombieThreat);
                        currentTile.setBanditDanger(raiderThreat);
                        tiles.add(currentTile);
                    }
                }
                catch (Exception e) {
                    Log.e(NAME, "Error creating tile");
                    e.printStackTrace();
                }
                eventType = parser.next();
            }
            return tiles;
        }
        catch (Exception e) {
            Log.e(NAME, "Error reading tile XML file");
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<CardCount> readCardCount(Resources resources) {
        XmlResourceParser parser = resources.getXml(R.xml.play_deck);
        try {
            ArrayList<CardCount> deck = new ArrayList<>();
            int eventType = parser.getEventType();
            while(eventType != XmlResourceParser.END_DOCUMENT) {
                try {
                    if (eventType == XmlResourceParser.START_TAG
                            && parser.getName().equalsIgnoreCase("tile")) {
                        String name = DEFAULT_XML_STRING;
                        int count = DEFAULT_INT_UNSPECIFIED_ATTRIBUTE;
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            switch (parser.getAttributeName(i)) {
                                case "Name":
                                    name = parser.getAttributeValue(i);
                                    break;
                                case "ResourceCount":
                                    count = parser.getAttributeIntValue(i, DEFAULT_INT_READ_ERROR);
                                    break;
                                default:
                                    break;
                            }
                        }
                        CardCount cardCount = new CardCount(name, count);
                        deck.add(cardCount);
                    }
                }
                catch (Exception e) {
                    Log.e(NAME, "Error creating tile");
                    e.printStackTrace();
                }
                eventType = parser.next();
            }
            return deck;
        }
        catch (Exception e) {
            Log.e(NAME, "Error reading tile XML file");
            e.printStackTrace();
        }
        return null;
    }

    public static void testXMLReader(Resources resources) {
        ArrayList<Tile> tiles = readTileCards(resources);
        Log.d(NAME, "Reading the tile.xml file");
        for(Tile tile: tiles) {
            Log.d(NAME, tile.toString());
        }
    }
}
