package edu.umt.csdept.survivorsoftheapocalypse;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;

public class MainActivity extends Activity {
    GamePanel gamePanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gamePanel = new GamePanel(this);
        LayoutInflater layoutInflater = getLayoutInflater();
        ViewGroup mainPage = (ViewGroup) layoutInflater.inflate(R.layout.activity_main, null);
        setContentView(mainPage);
        ViewGroup boardPanel = (ViewGroup) mainPage.findViewById(R.id.board_panel);
        boardPanel.addView(gamePanel);
    }




}
