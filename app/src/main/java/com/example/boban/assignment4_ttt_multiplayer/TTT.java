package com.example.boban.assignment4_ttt_multiplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TTT extends AppCompatActivity {
    TextView turnLabel = null;
    Button buttonStart, buttonRestart, buttonCancel = null;
    TTTButton tttButton[] = new TTTButton[9];
    Player player[] = new Player[2];
    int current = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ttt);

        Intent intent = getIntent();

        player[0] = new Player(intent.getStringExtra(MainActivity.P1_IMAGE), intent.getStringExtra(MainActivity.P1_NAME));
        player[1] = new Player(intent.getStringExtra(SecondPlayer.P2_IMAGE), intent.getStringExtra(SecondPlayer.P2_NAME));

        turnLabel = findViewById(R.id.textViewStatus);
        buttonStart = findViewById(R.id.buttonStart);
        buttonRestart = findViewById(R.id.buttonRestart);
        buttonCancel = findViewById(R.id.buttonCancel);

        tttButton[0] = findViewById(R.id.button00);
        tttButton[1] = findViewById(R.id.button01);
        tttButton[2] = findViewById(R.id.button02);
        tttButton[3] = findViewById(R.id.button10);
        tttButton[4] = findViewById(R.id.button11);
        tttButton[5] = findViewById(R.id.button12);
        tttButton[6] = findViewById(R.id.button20);
        tttButton[7] = findViewById(R.id.button21);
        tttButton[8] = findViewById(R.id.button22);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonStart.setVisibility(View.GONE);
                String updateText = "It is " + player[current % 2].name + "'s turn";
                turnLabel.setText(updateText);
            }
        });

        buttonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Arrays.fill(player[0].dataCell, null);
                //Arrays.fill(player[1].dataCell, null);
                player[0].unRegister();
                player[1].unRegister();
                for (int i = 0; i < tttButton.length; i++) {
                    tttButton[i].setEnabled(true);
                }

                current = 0;
                String updateText = "It is " + player[current % 2].name + "'s turn";
                turnLabel.setText(updateText);
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < tttButton.length; i++) {
                    tttButton[i].setEnabled(false);
                }
                String updateText = "Cancelled.\n Press restart to play again.";
                turnLabel.setText(updateText);
                turnLabel.setGravity(Gravity.CENTER);
            }
        });

        //Initialize all buttons for the game
        for(int i = 0; i < 9; i++){
            tttButton[i].setIndex(i);
            tttButton[i].setContext(getApplicationContext());
            final int j = i;
            tttButton[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(tttButton[j].getId());
                    if(!buttonStart.isShown() && tttButton[j].getText().toString().equals("")) {
                        player[current%2].register(tttButton[j], j);
                        player[current%2].MarkCell(j);
                        current++;
                        String updateText = "It is " + player[current % 2].name + "'s turn";
                        turnLabel.setText(updateText);
                    }
                    checkEndGame();
                }
            });
        }
    }

    private void checkEndGame() {
        if((tttButton[0].getText().equals(tttButton[1].getText()) && tttButton[1].getText().equals(tttButton[2].getText()) && !tttButton[0].getText().equals("")) ||
                (tttButton[3].getText().equals(tttButton[4].getText()) && tttButton[4].getText().equals(tttButton[5].getText()) && !tttButton[3].getText().equals("")) ||
                (tttButton[6].getText().equals(tttButton[7].getText()) && tttButton[7].getText().equals(tttButton[8].getText()) && !tttButton[6].getText().equals("")) ||
                (tttButton[0].getText().equals(tttButton[3].getText()) && tttButton[3].getText().equals(tttButton[6].getText()) && !tttButton[0].getText().equals("")) ||
                (tttButton[1].getText().equals(tttButton[4].getText()) && tttButton[4].getText().equals(tttButton[7].getText()) && !tttButton[1].getText().equals("")) ||
                (tttButton[2].getText().equals(tttButton[5].getText()) && tttButton[5].getText().equals(tttButton[8].getText()) && !tttButton[2].getText().equals("")) ||
                (tttButton[0].getText().equals(tttButton[4].getText()) && tttButton[4].getText().equals(tttButton[8].getText()) && !tttButton[0].getText().equals("")) ||
                (tttButton[2].getText().equals(tttButton[4].getText()) && tttButton[4].getText().equals(tttButton[6].getText()) && !tttButton[2].getText().equals(""))) {

            turnLabel.setText(player[(current - 1) % 2].name + " Wins!");
            for(int i = 0; i < tttButton.length; i++) {
                tttButton[i].setEnabled(false);
            }
        }else if(fullBoard()){
            turnLabel.setText("Cat's Game :|");
        }

    }

    private boolean fullBoard() {
        boolean check = true;
        for(int i = 0; i < tttButton.length; i++) {
            if(tttButton[i].getText().equals("")) {
                check = false;
            }
        }
        return check;
    }

}
