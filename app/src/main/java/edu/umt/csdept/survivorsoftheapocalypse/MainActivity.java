package edu.umt.csdept.survivorsoftheapocalypse;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class MainActivity extends Activity {
    GamePanel gamePanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gamePanel = new GamePanel(this);
        setContentView(gamePanel);
    }




}
