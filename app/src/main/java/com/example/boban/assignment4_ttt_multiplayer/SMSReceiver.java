package com.example.boban.assignment4_ttt_multiplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {
    Invite invite;
    TTT ttt;
    final String filter = "android.provider.Telephony.SMS_RECEIVED";
    IntentFilter intentFilter = new IntentFilter(filter);

    public SMSReceiver(Context context, String cName) {
        if(cName.equals("invite")){
            invite = (Invite) context;
        }else if(cName.equals("ttt")){
            ttt = (TTT) context;
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
            String[] decoded = message.split(",");

            if(decoded[0].equals("&&&TTT")){
                if(decoded[1].equals("INVITE")){
                    invite.displayAlert(decoded[2],senderNum);
                }else if(decoded[1].equals("ACCEPT") || decoded[1].equals("REJECT")){
                    invite.displayStatus(decoded[1], senderNum, decoded[2]);
                }else if(decoded[1].equals("IN_PROGRESS")){
                    ttt.receiveTurn(decoded[2], decoded[3]);
                }
            }


            /*if(message.equals("start")){
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
            }*/
        }
    }


}
