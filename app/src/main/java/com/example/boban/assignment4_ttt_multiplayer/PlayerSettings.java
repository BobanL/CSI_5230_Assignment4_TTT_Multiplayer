package com.example.boban.assignment4_ttt_multiplayer;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class PlayerSettings extends AppCompatActivity {
    public static final String PLAYER_IMAGE =
            "PLAYER_IMAGE";
    public static final String PLAYER_NAME =
            "PLAYER_NAME";
    Intent intent;
    ImageButton buttonGhost, buttonPumpkin, buttonCar = null;
    EditText textName = null;
    Button next = null;
    String imageName = "ghost";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_settings);
        intent = getIntent();
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
                Intent startIntent = new Intent(v.getContext(), Invite.class);
                startIntent.putExtra(PLAYER_IMAGE, imageName);
                startIntent.putExtra(PLAYER_NAME, textName.getText().toString());
                startActivity(startIntent);
            }
        });

        if(intent.hasExtra("PLAYER_NAME")){
            textName.setText(intent.getStringExtra(PLAYER_NAME));
            String image = intent.getStringExtra(PLAYER_IMAGE);
            if(image.equals("ghost")){
                buttonGhost.performClick();
            }else if(image.equals("pumpkin")){
                buttonPumpkin.performClick();
            }else if(image.equals("car")){
                buttonCar.performClick();
            }
        }
    }
}
