package com.as.mymessage.MessageReceiverClasses;

import static com.as.mymessage.util.ContactCheckerUtil.getContactNameFromNumber;
import static com.as.mymessage.util.ContactCheckerUtil.isNumberInContacts;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.as.mymessage.DatabasePackage.DatabaseHelper;
import com.as.mymessage.DatabasePackage.MessageTableModalClass;
import com.as.mymessage.R;
import com.as.mymessage.activities.MainActivity;
import com.as.mymessage.modals.RecyclerModalClass;
import com.as.mymessage.util.ContactCheckerUtil;
import com.as.mymessage.util.TimeStampUtil;


public class SmsReceiver extends BroadcastReceiver  {

   private static int messageImage = R.drawable.baseline_message_24;

    String messageReceived = null;
    String mobNumber = null;
    String contactName = null;
    long time;

    DatabaseHelper databaseHelper;


    @Override
    public void onReceive(Context context, Intent intent) {

        //Get The sms
        handleReceiveRequest(context,intent);

        // Notifying the user about the incoming message
        NotificationChannel mChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel("01", "notification", NotificationManager.IMPORTANCE_HIGH);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "01");
            mBuilder.setSmallIcon(messageImage);
            if(contactName != null) mBuilder.setContentTitle(contactName);
            else mBuilder.setContentTitle(mobNumber);
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
        DatabaseHelper databaseHelper = DatabaseHelper.getDB(context);
        databaseHelper.messageTableModalClassDao().addMessage(new MessageTableModalClass(messageImage,
                mobNumber,contactName,messageReceived,TimeStampUtil.convertToDate(time),TimeStampUtil.convertToTime(time), time));

    }
    public void handleReceiveRequest(Context context,Intent intent){

        Bundle bundle =intent.getExtras();
        //pdus (Protocol Data Units)
        Object[] pdus = (Object[]) bundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[pdus.length];

        for (int i = 0; i < pdus.length; i++) {
            messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
        }
        for (SmsMessage message : messages) {
           messageReceived = message.getMessageBody();
           mobNumber = message.getDisplayOriginatingAddress();
           time = message.getTimestampMillis();

           //Modifying the number

            if(mobNumber.charAt(0)=='+'){
                mobNumber = mobNumber.substring(mobNumber.length()-10);
                mobNumber = "0"+mobNumber;
            }
            else {
                if(mobNumber.matches("^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]\\d{3}[\\s.-]\\d{4}$")) {
                    mobNumber = "0"+mobNumber;
                }
            }

            if(isNumberInContacts(context,mobNumber)){
                contactName = getContactNameFromNumber(context,mobNumber);
            }
            Log.d("MsgDetails","MobNo:"+mobNumber+" , Msg: "+messageReceived+" , Time: "+time);

            //Sending intent in the form of Serializable
            Intent broadcastIntent = new Intent();
            Bundle args = new Bundle();
            RecyclerModalClass recyclerModalClass = new RecyclerModalClass(messageImage,mobNumber,contactName,messageReceived,TimeStampUtil.convertToDate(time), TimeStampUtil.convertToTime(time));
            args.putSerializable("object",recyclerModalClass);
            broadcastIntent.setAction("SMS_RECEIVED_ACTION");
            broadcastIntent.putExtra("sms",args);
            context.sendBroadcast(broadcastIntent);
        }
    }


}
