package com.example.boban.assignment4_ttt_multiplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {
    Invite activity;
    SecondPlayer secondPlayer;
    MainActivity mainActivity;
    TTT ttt;

    final String filter = "android.provider.Telephony.SMS_RECEIVED";
    IntentFilter intentFilter = new IntentFilter(filter);
    public SMSReceiver(Context context) {
        try{
           activity = (Invite) context;
        }catch(Exception e){
            System.out.println(e);

        }
        try{
            secondPlayer = (SecondPlayer) context;
        }catch (Exception e){
            System.out.println(e);

        }
        try{
            mainActivity = (MainActivity) context;
        }catch (Exception e){
            System.out.println(e);
        }
        try{
            ttt = (TTT) context;
        }catch (Exception e){
            System.out.println(e);

        }


        context.registerReceiver(this, intentFilter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage currentMessage = null;
        if(bundle != null){
            final Object[] pdusObj = (Object[]) bundle.get("pdus");

            for(int i = 0; i < pdusObj.length; i++){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    String format = bundle.getString("format");
                    currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i], format);
                }else{
                    currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                }
            }
            String senderNum = currentMessage.getDisplayOriginatingAddress();
            String message = currentMessage.getDisplayMessageBody();
            if(message.equals("start")){
                activity.displayAlert(senderNum);
            }else if(message.equals("deny") || message.equals("accept")){
                activity.displayStatus(message, senderNum);
            }else if(message.contains("new_start")){
                String[] breakDown = message.split(",");
                if(breakDown[4].equals("second")){
                    mainActivity.setReceived(message);
                }else{
                    secondPlayer.setReceived(message);
                }
            }else if(message.contains("in_progress")){
                ttt.receiveTurn(message);
            }else if(message.contains("cancel")){
                ttt.cancelGame();
            }else if(message.contains("restart")){
                ttt.restartGame();
            }
        }
    }


}
