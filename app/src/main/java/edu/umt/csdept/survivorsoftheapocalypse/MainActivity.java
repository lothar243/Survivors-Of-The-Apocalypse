package edu.umt.csdept.survivorsoftheapocalypse;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class MainActivity extends Activity {
    GameBoardView gameBoardView;
    GameState gameState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // creating a gamestate for testing - temporary
        gameState = new GameState(4, 4, 2);
        gameState.tileNames[1][1] = "Field";

        gameBoardView = new GameBoardView(this, gameState);

        // changing the bitmap of just the one tile, again this is only temporary
        gameBoardView.gameBoard.hexes.get(1).get(1).changeImage(BitmapFactory.decodeResource(getResources(), R.drawable.field));

        LayoutInflater layoutInflater = getLayoutInflater();
        ViewGroup mainPage = (ViewGroup) layoutInflater.inflate(R.layout.activity_main, null);
        setContentView(mainPage);
        ViewGroup boardPanel = (ViewGroup) mainPage.findViewById(R.id.board_panel);
        boardPanel.addView(gameBoardView);
    }




}
