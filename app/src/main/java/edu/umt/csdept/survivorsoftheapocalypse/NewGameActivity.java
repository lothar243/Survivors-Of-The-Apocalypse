package edu.umt.csdept.survivorsoftheapocalypse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * The screen the user will see when starting a new game
 */
public class NewGameActivity extends Activity {
    Spinner numPlayersSpinner;
    EditText boardHeightEditText, boardWidthEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View mainLayout = getLayoutInflater().inflate(R.layout.new_game, null);

        numPlayersSpinner = (Spinner) mainLayout.findViewById(R.id.num_players_spinner);
        setupNumPlayersSpinner(numPlayersSpinner);

        boardHeightEditText = (EditText) mainLayout.findViewById(R.id.board_height);
        boardWidthEditText = (EditText) mainLayout.findViewById(R.id.board_width);

        Button startGameButton = (Button) mainLayout.findViewById(R.id.start_game_button);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewGame();
            }
        });

        setContentView(mainLayout);

    }

    private void setupNumPlayersSpinner(Spinner numPlayersSpinner) {
        ArrayList<String> numPlayers = new ArrayList<>();
        numPlayers.add("2");
        numPlayers.add("3");
        numPlayers.add("4");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, R.layout.spinner_item, numPlayers);
        adapter.setDropDownViewResource(R.layout.spinner_item);

        numPlayersSpinner.setAdapter(adapter);
    }

    private void startNewGame() {
        int boardHeight = 0;
        int boardWidth = 0;
        try {
            boardHeight = Integer.parseInt(boardHeightEditText.getText().toString());
            boardWidth = Integer.parseInt(boardWidthEditText.getText().toString());
        }
        catch (Exception e) { // do nothing
        }
        int numPlayers = numPlayersSpinner.getSelectedItemPosition() + 2;

        if(boardHeight <= 2 || boardWidth <= 2) {
            Toast.makeText(this, "Board dimensions must be at least 2x2", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent startGameIntent = new Intent(this, InGameActivity.class);
        startGameIntent.putExtra(InGameActivity.BOARD_HEIGHT, boardHeight);
        startGameIntent.putExtra(InGameActivity.BOARD_WIDTH, boardWidth);
        startGameIntent.putExtra(InGameActivity.NUM_PLAYERS, numPlayers);

        startActivity(startGameIntent);
    }
}
