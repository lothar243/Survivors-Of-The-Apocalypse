package edu.umt.csdept.survivorsoftheapocalypse;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class InGameActivity extends Activity {
    public static final String NAME = "InGameActivity";

    // these constants are used to identify extra arguments when starting the activity
    public static final String NUM_PLAYERS = "survivorsoftheapocalypse.NUM_PLAYERS";
    public static final String BOARD_WIDTH = "survivorsoftheapocalypse.BOARD_WIDTH";
    public static final String BOARD_HEIGHT = "survivorsoftheapocalypse.BOARD_HEIGHT";

    GameBoardView gameBoardView;
    GameState gameState;

    // various views
    Button endTurnButton;
    TextView woodCountView;
    TextView foodCountView;
    TextView playerNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // default values
        int boardWidth = 4;
        int boardHeight = 4;
        int numPlayers = 2;

        try {
            // read the arguments set when creating the intent
            Intent intent = getIntent();
            boardWidth = intent.getIntExtra(BOARD_WIDTH, 5);
            boardHeight = intent.getIntExtra(BOARD_HEIGHT, 5);
            numPlayers = intent.getIntExtra(NUM_PLAYERS, 2);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Resources resources = getResources();

        gameState = new GameState(boardWidth, boardHeight, numPlayers,
                GameXMLReader.readTileCards(resources),
                GameXMLReader.readCardCount(resources));


        // creating a gamestate for testing - temporary
        gameState.tileNames[1][1] = "Field";

        gameBoardView = new GameBoardView(this, gameState);

        // changing the bitmap of just the one tile, again this is only temporary
        gameBoardView.gameBoard.hexes.get(1).get(1).changeImage(BitmapFactory.decodeResource(getResources(), R.drawable.field));

        LayoutInflater layoutInflater = getLayoutInflater();
        ViewGroup mainPage = (ViewGroup) layoutInflater.inflate(R.layout.activity_main, null);
        setContentView(mainPage);
        ViewGroup boardPanel = (ViewGroup) mainPage.findViewById(R.id.board_panel);
        boardPanel.addView(gameBoardView);

        ViewGroup sidePanel = (ViewGroup)mainPage.findViewById(R.id.side_panel);
        // setup views
        foodCountView = (TextView)sidePanel.findViewById(R.id.food_amount);
        woodCountView = (TextView)sidePanel.findViewById(R.id.wood_amount);
        playerNameView = (TextView)sidePanel.findViewById(R.id.player_name);

        endTurnButton = (Button)sidePanel.findViewById(R.id.end_turn_button);
        endTurnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameState.endTurn();
                refreshViews();
            }
        });
        refreshViews();
    }

    public void refreshViews() {
        playerNameView.setText(gameState.getCurrentPlayerName());
        int [] currentPlayerResources = gameState.getCurrentPlayerResources();
        if(currentPlayerResources != null) {
            foodCountView.setText("" + currentPlayerResources[0]);
            woodCountView.setText("" + currentPlayerResources[1]);
        }
        else {
            foodCountView.setText("null");
            woodCountView.setText("null");
        }
    }




}
