package com.example.boban.assignment4_ttt_multiplayer;

import android.content.Context;
import android.content.DialogInterface;
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

import java.util.concurrent.CountDownLatch;

public class SecondPlayer extends AppCompatActivity {
    public static final String P2_IMAGE =
            "PLAYER2_IMAGE";
    public static final String P2_NAME =
            "PLAYER2_NAME";

    ImageButton buttonGhost, buttonPumpkin, buttonCar = null;
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
        setContentView(R.layout.activity_second_player);
        Intent intent = getIntent();
        final String phoneNumber = intent.getStringExtra("phone_number");
        SecondPlayer.context = getApplicationContext();

        buttonGhost = findViewById(R.id.imageGhost);
        buttonPumpkin = findViewById(R.id.imagePumpkin);
        buttonCar = findViewById(R.id.imageCar);

        textName = findViewById(R.id.textName2);
        next = findViewById(R.id.buttonNext2);

        buttonGhost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonGhost.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.border));
                buttonCar.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.transparent_border));
                buttonPumpkin.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.transparent_border));
                imageName = "ghost";
            }
        });

        buttonPumpkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPumpkin.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.border));
                buttonCar.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.transparent_border));
                buttonGhost.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.transparent_border));
                imageName = "pumpkin";
            }
        });

        buttonCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonCar.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.border));
                buttonGhost.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.transparent_border));
                buttonPumpkin.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.transparent_border));
                imageName = "car";
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smsManager.sendTextMessage(phoneNumber, null, "new_start,p1,"+textName.getText().toString()+","+imageName+","+"second", null, null);
                checkOtherPlayer();
            }
        });

    }

    private void checkOtherPlayer(){
        if(received){
            Intent intentTTT = new Intent(this, TTT.class);
            intentTTT.putExtra(P2_NAME, textName.getText().toString());
            intentTTT.putExtra(P2_IMAGE, imageName);
            startActivity(intentTTT);
        }else{
            alertDialogShow = true;
            AlertDialog.Builder builder = new AlertDialog.Builder(SecondPlayer.this);
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
            intentTTT.putExtra(MainActivity.P1_NAME, vars[2]);
            intentTTT.putExtra(MainActivity.P1_IMAGE, vars[3]);
            intentTTT.putExtra(P2_NAME, textName.getText().toString());
            intentTTT.putExtra(P2_IMAGE, imageName);
            context.startActivity(intentTTT);
        }else{
            received = true;
        }
    }
}
