package com.example.boban.assignment4_ttt_multiplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class TTT extends AppCompatActivity {
    TextView turnLabel = null;
    Button buttonRestart, buttonCancel;
    TTTButton tttButton[] = new TTTButton[9];
    Player player[] = new Player[2];
    int current = 0, playerNum = 0;
    SmsManager smsManager = SmsManager.getDefault();
    String phoneNumber;
    SMSReceiver smsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ttt);
        smsReceiver = new SMSReceiver(this, "ttt");
        Intent intent = getIntent();
        playerNum = intent.getIntExtra("PLAYER_NUM", -1);

        player[(playerNum == 1) ? 1:0] = new Player(intent.getStringExtra(PlayerSettings.PLAYER_IMAGE), intent.getStringExtra(PlayerSettings.PLAYER_NAME));
        player[(playerNum == 1) ? 0:1] = new Player("ball", intent.getStringExtra("SECOND_PLAYER_NAME"));

        phoneNumber = intent.getStringExtra("PHONE_NUMBER");

        if(current%2 == playerNum){
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }else{
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        turnLabel = findViewById(R.id.textViewStatus);
        buttonRestart = findViewById(R.id.buttonRestart);
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonRestart.setEnabled(false);

        tttButton[0] = findViewById(R.id.button00);
        tttButton[1] = findViewById(R.id.button01);
        tttButton[2] = findViewById(R.id.button02);
        tttButton[3] = findViewById(R.id.button10);
        tttButton[4] = findViewById(R.id.button11);
        tttButton[5] = findViewById(R.id.button12);
        tttButton[6] = findViewById(R.id.button20);
        tttButton[7] = findViewById(R.id.button21);
        tttButton[8] = findViewById(R.id.button22);

        String updateText = "It is " + player[current % 2].name + "'s turn";
        turnLabel.setText(updateText);

        buttonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "&&&TTT,REMATCH";
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                player[0].unRegister();
                player[1].unRegister();
                for (int i = 0; i < tttButton.length; i++) {
                    tttButton[i].setEnabled(true);
                }
                current = 0;
                String updateText = "It is " + player[current % 2].name + "'s turn";
                turnLabel.setText(updateText);
                if(current%2 == playerNum){
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }else{
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }

            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*for (int i = 0; i < tttButton.length; i++) {
                    tttButton[i].setEnabled(false);
                }*/
                String message = "&&&TTT,CANCEL";
                smsManager.sendTextMessage(phoneNumber, null, message , null, null);
                Intent sIntent = new Intent(v.getContext(), Invite.class);
                sIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                sIntent.putExtra(PlayerSettings.PLAYER_NAME, player[playerNum].getName());
                sIntent.putExtra(PlayerSettings.PLAYER_IMAGE, player[playerNum].getSymbol());
                unregisterReceiver(smsReceiver);
                startActivity(sIntent);
                finish();
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
                    if(current%2 == playerNum){
                        String message = "&&&TTT,IN_PROGRESS," + playerNum + "," + j;
                        smsManager.sendTextMessage(phoneNumber, null, message , null, null);
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                    if(tttButton[j].getText().toString().equals("")) {
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
            buttonRestart.setEnabled(true);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }else if(fullBoard()){
            turnLabel.setText("Cat's Game :|");
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            buttonRestart.setEnabled(true);
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

    public void receiveTurn(String player_num, String loc){
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        current = Integer.parseInt(player_num);
        tttButton[Integer.parseInt(loc)].performClick();
    }

    public void cancelGame(){
        Intent sIntent = new Intent(this, Invite.class);
        sIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sIntent.putExtra(PlayerSettings.PLAYER_NAME, player[playerNum].getName());
        sIntent.putExtra(PlayerSettings.PLAYER_IMAGE, player[playerNum].getSymbol());
        unregisterReceiver(smsReceiver);
        startActivity(sIntent);
        finish();
    }

    public void restartGame(){
        player[0].unRegister();
        player[1].unRegister();
        for (int i = 0; i < tttButton.length; i++) {
            tttButton[i].setEnabled(true);
        }
        current = 0;
        String updateText = "It is " + player[current % 2].name + "'s turn";
        turnLabel.setText(updateText);
        if(current%2 == playerNum){
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }else{
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }
}
