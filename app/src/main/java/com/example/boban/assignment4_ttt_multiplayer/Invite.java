package com.example.boban.assignment4_ttt_multiplayer;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Invite extends AppCompatActivity {
    TextView statusText = null;
    EditText phoneNumberText = null;
    Button inviteButton = null;
    SmsManager smsManager = SmsManager.getDefault();
    Button backButton = null;
    String pname, image;
    SMSReceiver smsReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Intent intent = getIntent();
        smsReceiver = new SMSReceiver(this, "invite");
        pname = intent.getStringExtra(PlayerSettings.PLAYER_NAME);
        image = intent.getStringExtra(PlayerSettings.PLAYER_IMAGE);
        statusText = findViewById(R.id.statusText);
        phoneNumberText = findViewById(R.id.phoneNumberText);
        inviteButton = findViewById(R.id.inviteButton);
        backButton = findViewById(R.id.buttonBackSettings);

        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusText.setText("Status: Pending");
                String message = "&&&TTT,INVITE," + pname;
                String phoneNumber = phoneNumberText.getText().toString();
                smsManager.sendTextMessage(phoneNumber, null, message,null, null);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(v.getContext(), PlayerSettings.class);
                settingsIntent.putExtra(PlayerSettings.PLAYER_NAME, pname);
                settingsIntent.putExtra(PlayerSettings.PLAYER_IMAGE, image);
                startActivity(settingsIntent);
            }
        });
        if(!isSmsPermissionGranted()){
            requestReadAndSendSmsPermission();
        }
    }


    public void displayAlert(final String inviterName, final String senderNumber){
        AlertDialog.Builder builder = new AlertDialog.Builder(Invite.this);
        builder.setMessage("You've been invited by " + inviterName).setTitle("Invited!");
        builder.setPositiveButton("Accept Invite", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String message = "&&&TTT,ACCEPT," + pname;
                smsManager.sendTextMessage(senderNumber, null, message, null, null);
                startTTT(senderNumber, inviterName, true);
            }
        });
        builder.setNegativeButton("Deny Invite", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String message = "&&&TTT,REJECT," + pname;
                smsManager.sendTextMessage(senderNumber, null, message, null, null);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void displayStatus(String message, String number, String inviterName){
        if(message.equals("REJECT")){
            statusText.setText("Status: Invite Rejected!");
        }
        if(message.equals("ACCEPT")){
            startTTT(number, inviterName, false);
        }
    }

    private void startTTT(final String number, String inviterName, Boolean invited){
        unregisterReceiver(smsReceiver);
        Intent intent = new Intent(Invite.this, TTT.class);
        intent.putExtra("PHONE_NUMBER", number);
        intent.putExtra(PlayerSettings.PLAYER_NAME, pname);
        intent.putExtra(PlayerSettings.PLAYER_IMAGE, image);
        intent.putExtra("SECOND_PLAYER_NAME", inviterName);
        intent.putExtra("PLAYER_NUM", (invited ? 1 : 0));
        startActivity(intent);
        finish();
    }

    public boolean isSmsPermissionGranted(){
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
    }

    private  void requestReadAndSendSmsPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)){

        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_PHONE_STATE}, 1);
    }
}
