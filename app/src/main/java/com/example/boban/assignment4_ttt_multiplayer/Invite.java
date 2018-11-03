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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        statusText = findViewById(R.id.statusText);
        phoneNumberText = findViewById(R.id.phoneNumberText);
        inviteButton = findViewById(R.id.inviteButton);

        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusText.setText("Status: Pending");
                String message = "start";
                String phoneNumber = phoneNumberText.getText().toString();
                smsManager.sendTextMessage(phoneNumber, null, message,null, null);
            }
        });
        SMSReceiver smsReceiver = new SMSReceiver(this);
        if(!isSmsPermissionGranted()){
            requestReadAndSendSmsPermission();
        }

    }


    public void displayAlert(final String number){
        AlertDialog.Builder builder = new AlertDialog.Builder(Invite.this);
        builder.setMessage("You've been invited by " + number).setTitle("Invited!");
        builder.setPositiveButton("Accept Invite", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                smsManager.sendTextMessage(number, null, "accept", null, null);
                Intent intent = new Intent(Invite.this, SecondPlayer.class);
                intent.putExtra("phone_number", number);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("Deny Invite", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                smsManager.sendTextMessage(number, null, "deny", null, null);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void displayStatus(String message, String number){
        if(message.equals("deny")){
            statusText.setText("Status: Invite Rejected!");
        }
        if(message.equals("accept")){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("phone_number", number);
            startActivity(intent);
        }
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
