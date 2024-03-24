package com.as.mymessage.MessageReceiverClasses;

import static com.as.mymessage.util.ContactCheckerUtil.getContactNameFromNumber;
import static com.as.mymessage.util.ContactCheckerUtil.isNumberInContacts;


import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.as.mymessage.DatabasePackage.DatabaseHelper;
import com.as.mymessage.DatabasePackage.MessageTableModalClass;
import com.as.mymessage.R;
import com.as.mymessage.activities.MainActivity;
import com.as.mymessage.modals.RecyclerModalClass;
import com.as.mymessage.util.TimeStampUtil;


public class SmsReceiver extends BroadcastReceiver {

    String messageReceived = null;
    String mobNumber = null;
    String contactName = null;
    long time;

    DatabaseHelper databaseHelper;


    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {

        //Get The sms
        handleReceiveRequest(context, intent);

        // Notifying the user about the incoming message
        NotificationChannel mChannel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel("01", "SMS Notifications", NotificationManager.IMPORTANCE_HIGH);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "01");
            //icon
            mBuilder.setSmallIcon(R.drawable.ic_stat_name);
            mBuilder.setAutoCancel(true);
            //title
            if (contactName != null)
                mBuilder.setContentTitle(contactName);
            else
                mBuilder.setContentTitle(mobNumber);
            //content
            mBuilder.setContentText(messageReceived);
            mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(messageReceived));
            Intent notificationIntent = new Intent(context, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_MUTABLE);
            mBuilder.setContentIntent(contentIntent);

            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(mChannel);
            manager.notify(0, mBuilder.build());
        }

        // Adding Messages to database
        databaseHelper = DatabaseHelper.getDB(context);
        databaseHelper.messageTableModalClassDao().addMessage(new MessageTableModalClass(mobNumber, contactName, messageReceived, TimeStampUtil.convertToDate(time), TimeStampUtil.convertToTime(time), time));

    }

    public void handleReceiveRequest(Context context, Intent intent) {
        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);

        if (messages != null && messages.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (SmsMessage smsMessage : messages) {
                sb.append(smsMessage.getMessageBody());
                mobNumber = smsMessage.getOriginatingAddress();
                time = smsMessage.getTimestampMillis();
            }
            messageReceived = sb.toString();
            if(isNumberInContacts(context,mobNumber)){
                contactName = getContactNameFromNumber(context,mobNumber);
            }
            Log.d("MsgDetails","MobNo:"+mobNumber+" , Msg: "+messageReceived+" , Time: "+time);

            //Sending intent in the form of Serializable
            Intent broadcastIntent = new Intent();
            Bundle args = new Bundle();
            RecyclerModalClass recyclerModalClass = new RecyclerModalClass(mobNumber,contactName,messageReceived,TimeStampUtil.convertToDate(time), TimeStampUtil.convertToTime(time));
            args.putSerializable("object",recyclerModalClass);
            broadcastIntent.setAction("SMS_RECEIVED_ACTION");
            broadcastIntent.putExtra("sms",args);
            context.sendBroadcast(broadcastIntent);
        }

    }
}








