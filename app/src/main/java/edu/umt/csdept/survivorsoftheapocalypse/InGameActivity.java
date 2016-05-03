package edu.umt.csdept.survivorsoftheapocalypse;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class InGameActivity extends Activity {
    public static final String NAME = "InGameActivity";

    // these constants are used to identify extra arguments when starting the activity
    public static final String NUM_PLAYERS = "survivorsoftheapocalypse.NUM_PLAYERS";
    public static final String BOARD_WIDTH = "survivorsoftheapocalypse.BOARD_WIDTH";
    public static final String BOARD_HEIGHT = "survivorsoftheapocalypse.BOARD_HEIGHT";
    public static final String GAME_STATE = "survivorsoftheapocalypse.GAMESTATE";

    GameBoardView gameBoardView;
    GameState gameState;

    enum PossibleActions {decidingAction, buyingPerson, choosingCardLocation, harvesting, buildingWall}
    PossibleActions currentPlayerAction;

    PlayerCard currentCard;

    // various views
    ViewGroup sidePanel;
    Button endTurnButton;
    TextView woodCountView;
    TextView foodCountView;
    TextView playerNameView;

    TextView p0Score, p1Score, p2Score, p3Score, p4Score, p5Score;

    Button drawCardButton;
    ImageView buyPersonButton;
    ImageView buildWallButton;
    ImageView harvestButton;

    ViewGroup locationPrompt;

    int boardWidth = 4;
    int boardHeight = 4;
    int numPlayers = 2;

    int dialogChoice;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BOARD_WIDTH, boardWidth);
        outState.putInt(BOARD_HEIGHT, boardHeight);
        outState.putInt(NUM_PLAYERS, numPlayers);
        outState.putSerializable(GAME_STATE, gameState);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources resources = getResources();

        if(savedInstanceState == null) {
            // not resuming a previous instance state
            try {
                // read the arguments set when creating the intent
                Intent intent = getIntent();
                boardWidth = intent.getIntExtra(BOARD_WIDTH, 5);
                boardHeight = intent.getIntExtra(BOARD_HEIGHT, 5);
                numPlayers = intent.getIntExtra(NUM_PLAYERS, 2);
                gameState = new GameState(this, boardWidth, boardHeight, numPlayers,
                        GameXMLReader.readTileCards(resources),
                        GameXMLReader.readCardCount(resources));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            // resuming a previous instance
            Log.d(NAME, "Reloading a previous gamestate");
            gameState = (GameState)savedInstanceState.getSerializable(GAME_STATE);
            gameState.activity = this;
        }


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
        p0Score = (TextView)sidePanel.findViewById(R.id.p0_score);
        p1Score = (TextView)sidePanel.findViewById(R.id.p1_score);
        p2Score = (TextView)sidePanel.findViewById(R.id.p2_score);
        p3Score = (TextView)sidePanel.findViewById(R.id.p3_score);
        p4Score = (TextView)sidePanel.findViewById(R.id.p4_score);
        p5Score = (TextView)sidePanel.findViewById(R.id.p5_score);
        switch (numPlayers) {
            case 2:
                p2Score.setVisibility(View.GONE);
            case 3:
                p3Score.setVisibility(View.GONE);
            case 4:
                p4Score.setVisibility(View.GONE);
            case 5:
                p5Score.setVisibility(View.GONE);

        }

        endTurnButton = (Button)sidePanel.findViewById(R.id.end_turn_button);
        final Activity activity = this;
        endTurnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int numRemainingActions = gameState.getRemainingActions();
                if(numRemainingActions > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Really end your turn with " + numRemainingActions + " actions left?");
                    builder.setPositiveButton("Yes, I'm done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            endTurn();
                        }
                    });
                    builder.setNegativeButton("No, wait!", null);
                    builder.create().show();
                }
                else endTurn();
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
                startBuyingPerson();
            }
        });

        buildWallButton = (ImageView)sidePanel.findViewById(R.id.build_wall);
        buildWallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBuildingWall();
            }
        });

        harvestButton = (ImageView)sidePanel.findViewById(R.id.harvest);
        harvestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startHarvestingResources();
            }
        });
        currentPlayerAction = PossibleActions.decidingAction;

        locationPrompt = (ViewGroup)mainPage.findViewById(R.id.location_prompt);
        refreshViews();
    }

    public void refreshViews() {
        playerNameView.setText(gameState.getCurrentPlayerName() + ", Actions: " + gameState.getRemainingActions());
        switch (gameState.currentPlayerIdx) {
            case 0:
                playerNameView.setTextColor(ContextCompat.getColor(this, R.color.player0Color));
                break;
            case 1:
                playerNameView.setTextColor(ContextCompat.getColor(this, R.color.player1Color));
                break;
            case 2:
                playerNameView.setTextColor(ContextCompat.getColor(this, R.color.player2Color));
                break;
            case 3:
                playerNameView.setTextColor(ContextCompat.getColor(this, R.color.player3Color));
                break;
            case 4:
                playerNameView.setTextColor(ContextCompat.getColor(this, R.color.player4Color));
                break;
            case 5:
                playerNameView.setTextColor(ContextCompat.getColor(this, R.color.player5Color));
                break;
            default:
                playerNameView.setTextColor(Color.BLACK);
                break;
        }
        int [] currentPlayerResources = gameState.getCurrentPlayerResources();
        if(currentPlayerResources != null) {
            foodCountView.setText("" + currentPlayerResources[0]);
            woodCountView.setText("" + currentPlayerResources[1]);
        }
        else {
            foodCountView.setText("null");
            woodCountView.setText("null");
        }
        switch (numPlayers) {
            case 6:
                p5Score.setText("P5: " + gameState.scores[5]);
            case 5:
                p4Score.setText("P4: " + gameState.scores[4]);
            case 4:
                p3Score.setText("P3: " + gameState.scores[3]);
            case 3:
                p2Score.setText("P2: " + gameState.scores[2]);
            case 2:
                p1Score.setText("P1: " + gameState.scores[1]);
                p0Score.setText("P0: " + gameState.scores[0]);
        }
    }

    public void endTurn() {
        gameState.endTurn();
        String cardName = drawCard();
        refreshViews();
        final Activity activity = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(gameState.getCurrentPlayerName() + "'s turn");
        builder.setMessage(cardName + " drawn");
        builder.create().show();
    }

    public void startBuyingPerson() {
        sidePanel.setVisibility(View.INVISIBLE);
        locationPrompt.setVisibility(View.VISIBLE);
        currentPlayerAction = PossibleActions.buyingPerson;
    }

    public void startBuildingWall() {
        if(gameState.getRemainingActions() > 0) {
            setSideBarPromptForLocation(true);
            currentPlayerAction = PossibleActions.buildingWall;
        }
        else {
            tellUserNotEnoughActions();
        }
    }

    public void startHarvestingResources() {
        if(gameState.getRemainingActions() > 0) {
            sidePanel.setVisibility(View.INVISIBLE);
            locationPrompt.setVisibility(View.VISIBLE);
            currentPlayerAction = PossibleActions.harvesting;
        }
        else
            tellUserNotEnoughActions();
    }

    public void tellUserNotEnoughActions() {
        Toast.makeText(this, "Not enough actions left", Toast.LENGTH_SHORT).show();
    }

    public void onBoardPress(Location location) {
        switch (currentPlayerAction) {
            case choosingCardLocation:
                if(currentCard.onPlayAtLocation(gameState, location)) {
                    setSideBarPromptForLocation(false);
                    currentCard = null;
                }
                break;
            case buyingPerson:
                Log.d(NAME, "Placing person at " + location);
                if(gameState.tileAtLocation(location)) {
//                    gameState.placePerson(gameState.currentPlayerIdx, location);
                    if(!gameState.buyPerson(location)) {
                        Toast.makeText(this, "Not enough food", Toast.LENGTH_SHORT).show();
                    }
                }
                setSideBarPromptForLocation(false);
                gameBoardView.invalidateGameBoard();
                refreshViews();
                currentPlayerAction = PossibleActions.decidingAction;
                break;
            case harvesting:
                Log.d(NAME, "Collecting resources from " + location);
                if(gameState.tileAtLocation(location)) {
                    String resourceType = gameState.getTileResourceType(location);
                    if (resourceType.equals("Wild")) {
                        chooseResourceTypeDialog(location);
                    } else if (!gameState.collectResources(location, resourceType))
                        Toast.makeText(this, "Unable to collect", Toast.LENGTH_SHORT).show();
                }
                setSideBarPromptForLocation(false);
                gameBoardView.invalidateGameBoard();
                refreshViews();
                currentPlayerAction = PossibleActions.decidingAction;
                break;
            case buildingWall:
                if(gameState.buildWall(location)) {
                    Toast.makeText(this, "Wall built", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "Must have " + GameState.WALLCOST + " wood and be present to build", Toast.LENGTH_SHORT).show();
                }

                setSideBarPromptForLocation(false);
                currentPlayerAction = PossibleActions.decidingAction;
                refreshViews();
        }
    }

    private void setSideBarPromptForLocation(boolean prompting) {
        if(prompting) {
            sidePanel.setVisibility(View.INVISIBLE);
            locationPrompt.setVisibility(View.VISIBLE);
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

    public String drawCard() {
        PlayerCard drawnCard = gameState.drawPlayCard();
        gameBoardView.invalidateGameBoard();
//        Toast.makeText(this, drawnCard.getCardName() + " drawn", Toast.LENGTH_SHORT).show();
        return drawnCard.getCardName();
    }

    public void notifyTilesChanged() {
        if(gameBoardView != null && gameBoardView.gameBoard != null)
            gameBoardView.gameBoard.invalidateHexes();
    }

    private void chooseResourceTypeDialog(final Location location) {
        final int selectedBackgroundColor = getResources().getColor(R.color.selectResourceColor);
        final String[] choices = new String[]{"Food", "Wood"};
        dialogChoice = -1;
        final Activity activity = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a resource type to gather");
//        builder.setSingleChoiceItems(choices, -1, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialogChoice = which;
//            }
//        });
//        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if(dialogChoice == -1) Toast.makeText(activity, "No resource selected", Toast.LENGTH_SHORT).show();
//                else gameState.collectResources(location, choices[dialogChoice]);
//                gameBoardView.invalidateGameBoard();
//                refreshViews();
//            }
//        });
//        builder.setNegativeButton("Cancel", null);
        builder.setNegativeButton("Cancel", null);
        builder.setNeutralButton("Wood", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gameState.collectResources(location, "Wood");
                gameBoardView.invalidateGameBoard();
                refreshViews();
            }
        });
        builder.setPositiveButton("Food", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gameState.collectResources(location, "Food");
                gameBoardView.invalidateGameBoard();
                refreshViews();
            }
        });

//        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.resource_type_prompt, null);
//        builder.setView(viewGroup);
//        builder.setTitle("Choose a resource type");
//        final View chooseFood = viewGroup.findViewById(R.id.choose_food);
//        final View chooseWood = viewGroup.findViewById(R.id.choose_wood);
//
//        chooseFood.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                chooseFood.setBackgroundColor(selectedBackgroundColor);
//                chooseWood.setBackgroundColor(Color.TRANSPARENT);
//            }
//        });
//        chooseWood.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                chooseFood.setBackgroundColor(Color.TRANSPARENT);
//                chooseWood.setBackgroundColor(selectedBackgroundColor);
//            }
//        });
//        builder.setNegativeButton("Cancel", null);
//        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if()
//            }
//        });
        builder.create().show();
    }
}
