package edu.umt.csdept.survivorsoftheapocalypse;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class InGameActivity extends Activity {
    public static final String NAME = "InGameActivity";

    // these constants are used to identify extra arguments when starting the activity
    public static final String NUM_PLAYERS = "survivorsoftheapocalypse.NUM_PLAYERS";
    public static final String BOARD_WIDTH = "survivorsoftheapocalypse.BOARD_WIDTH";
    public static final String BOARD_HEIGHT = "survivorsoftheapocalypse.BOARD_HEIGHT";

    GameBoardView gameBoardView;
    GameState gameState;

    enum PossibleActions {decidingAction, buyingPerson, choosingCardLocation, harvesting}
    PossibleActions currentPlayerAction;

    PlayerCard currentCard;

    // various views
    ViewGroup sidePanel;
    Button endTurnButton;
    TextView woodCountView;
    TextView foodCountView;
    TextView playerNameView;

    Button drawCardButton;
    ImageView buyPersonButton;
    ImageView harvestButton;

    ViewGroup locationPrompt;


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
        ArrayList<CardCount> testCounts = GameXMLReader.readCardCount(resources);
        Log.d(NAME, "Reading cardCounts");
        for (int i = 0; i < testCounts.size(); i++) {
            Log.d(NAME, testCounts.get(i).toString());
        }

        gameState = new GameState(this, boardWidth, boardHeight, numPlayers,
                GameXMLReader.readTileCards(resources),
                GameXMLReader.readCardCount(resources));

        Tile[][] boardLayout = gameState.getBoardLayout();

        // place all the tiles on the board
        for(int i = 0; i < boardLayout.length; i++) {
            for(int j = 0; j < boardLayout[i].length; j++) {
                Tile drawnTile = gameState.drawTile();
                if(drawnTile != null)
                    gameState.PlaceTile(drawnTile, new Location(i, j));
            }
        }

        gameBoardView = new GameBoardView(this, gameState);

        LayoutInflater layoutInflater = getLayoutInflater();
        ViewGroup mainPage = (ViewGroup) layoutInflater.inflate(R.layout.activity_main, null);
        setContentView(mainPage);
        ViewGroup boardPanel = (ViewGroup) mainPage.findViewById(R.id.board_panel);
        boardPanel.addView(gameBoardView);

        sidePanel = (ViewGroup)mainPage.findViewById(R.id.side_panel);
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
        drawCardButton = (Button)sidePanel.findViewById(R.id.draw_card_button);
        drawCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawCard();
            }
        });

        buyPersonButton = (ImageView)sidePanel.findViewById(R.id.buy_person);
        buyPersonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyPerson();
            }
        });

        harvestButton = (ImageView)sidePanel.findViewById(R.id.harvest);
        harvestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                harvestResource();
            }
        });
        currentPlayerAction = PossibleActions.decidingAction;

        locationPrompt = (ViewGroup)mainPage.findViewById(R.id.location_prompt);
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

    public void buyPerson() {
        sidePanel.setVisibility(View.INVISIBLE);
        locationPrompt.setVisibility(View.VISIBLE);
        currentPlayerAction = PossibleActions.buyingPerson;
    }

    public void harvestResource() {
        sidePanel.setVisibility(View.INVISIBLE);
        locationPrompt.setVisibility(View.VISIBLE);
        currentPlayerAction = PossibleActions.harvesting;
    }

    public void onBoardPress(Point indices) {
        switch (currentPlayerAction) {
            case choosingCardLocation:
                String tileName = gameState.getTileNameAtLocation(new Location(indices));
                if(!(tileName == null || tileName.equals(""))) {
                    currentCard.onPlay(gameState, new Location(indices));
                    setSideBarPromptForLocation(false);
                    currentCard = null;
                }
                break;
            case buyingPerson:
                Log.d(NAME, "Placing person at " + indices);
                gameState.placePerson(gameState.currentPlayerIdx, new Location(indices));
                setSideBarPromptForLocation(false);
                gameBoardView.invalidateGameBoard();
                refreshViews();
                currentPlayerAction = PossibleActions.decidingAction;
                break;
            case harvesting:
                Log.d(NAME, "Collecting resources from " + indices);
                if(!gameState.collectResources(new Location(indices)))
                    Toast.makeText(this, "Unable to collect", Toast.LENGTH_SHORT).show();
                setSideBarPromptForLocation(false);
                gameBoardView.invalidateGameBoard();
                refreshViews();
                currentPlayerAction = PossibleActions.decidingAction;
                break;

        }
    }

    private void setSideBarPromptForLocation(boolean prompting) {
        if(prompting) {

        }
        else {
            sidePanel.setVisibility(View.VISIBLE);
            locationPrompt.setVisibility(View.INVISIBLE);
        }
    }

    public void promptForLocation(PlayerCard cardBeingPlayed) {
        currentPlayerAction = PossibleActions.choosingCardLocation;
        currentCard = cardBeingPlayed;
        locationPrompt.setVisibility(View.VISIBLE);
        sidePanel.setVisibility(View.INVISIBLE);
    }

    public void drawCard() {
        PlayerCard drawnCard = gameState.drawPlayCard();
        gameBoardView.invalidateGameBoard();
        Toast.makeText(this, drawnCard.getCardName() + " drawn", Toast.LENGTH_SHORT).show();
    }


}
