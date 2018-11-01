package com.example.boban.assignment4_ttt_multiplayer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    public static final String P1_IMAGE =
            "PLAYER1_IMAGE";
    public static final String P1_NAME =
            "PLAYER1_NAME";
    ImageButton buttonBall, buttonMusic, buttonMoon = null;
    static EditText textName = null;
    Button next = null;
    static String imageName = "ghost";
    SmsManager smsManager = SmsManager.getDefault();
    static Context context;
    static boolean received = false;
    static boolean alertDialogShow = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        final String phoneNumber = intent.getStringExtra("phone_number");
        SecondPlayer.context = getApplicationContext();

        buttonBall = findViewById(R.id.imageBall);
        buttonMusic = findViewById(R.id.imageMusic);
        buttonMoon = findViewById(R.id.imageMoon);

        textName = findViewById(R.id.textName);
        next = findViewById(R.id.buttonNext1);

        buttonBall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonBall.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.border));
                buttonMoon.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.transparent_border));
                buttonMusic.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.transparent_border));
                imageName = "ball";
            }
        });

        buttonMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonMusic.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.border));
                buttonMoon.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.transparent_border));
                buttonBall.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.transparent_border));
                imageName = "music";
            }
        });

        buttonMoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonMoon.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.border));
                buttonBall.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.transparent_border));
                buttonMusic.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.transparent_border));
                imageName = "moon";
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smsManager.sendTextMessage(phoneNumber, null, "new_start,p1,"+textName.getText().toString()+","+imageName+","+"main", null, null);
                checkOtherPlayer();
            }
        });
    }

    private void checkOtherPlayer(){
        if(received){
            Intent intentTTT = new Intent(this, TTT.class);
            intentTTT.putExtra(P1_NAME, textName.getText().toString());
            intentTTT.putExtra(P1_IMAGE, imageName);
            startActivity(intentTTT);
        }else{
            alertDialogShow = true;
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Waiting on other player ").setTitle("Waiting!");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public static void setReceived(String message){
        if(alertDialogShow){
            String[] vars = message.split(",");
            Intent intentTTT = new Intent(context, TTT.class);
            intentTTT.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intentTTT.putExtra(SecondPlayer.P2_NAME, vars[2]);
            intentTTT.putExtra(SecondPlayer.P2_IMAGE, vars[3]);
            intentTTT.putExtra(P1_NAME, textName.getText().toString());
            intentTTT.putExtra(P1_IMAGE, imageName);
            context.startActivity(intentTTT);
        }else{
            received = true;
        }
    }
}
