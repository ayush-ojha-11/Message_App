package com.as.mymessage;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.as.mymessage.DatabasePackage.DatabaseHelper;
import com.as.mymessage.DatabasePackage.MessageTableModalClass;
import com.as.mymessage.activities.ComposeSmsActivity;
import com.as.mymessage.activities.MainActivity;
import com.as.mymessage.modals.RecyclerModalClass;
import com.as.mymessage.util.TimeStampUtil;

import java.sql.Timestamp;


public class SmsReceiver extends BroadcastReceiver  {

    static String messageReceived = null;
    static String mobNumber = null;
    static long time;


    @Override
    public void onReceive(Context context, Intent intent) {

        //Get The sms
        handleReceiveRequest(context,intent);


        // Notifying the user about the incoming message

        NotificationChannel mChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel("01", "notification", NotificationManager.IMPORTANCE_HIGH);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "01");
            mBuilder.setSmallIcon(R.drawable.ic_launcher_foreground);
            mBuilder.setContentTitle(mobNumber);
            mBuilder.setContentText(messageReceived);
            mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(messageReceived));
            Intent notificationIntent = new Intent(context, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_MUTABLE);
            mBuilder.setContentIntent(contentIntent);

            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(mChannel);
            manager.notify(0, mBuilder.build());
        }

        // Adding Messages to database when the application is not active and sms is received

        DatabaseHelper databaseHelper = DatabaseHelper.getDB(context);
        databaseHelper.messageTableModalClassDao().addMessage(new MessageTableModalClass(R.drawable.ic_launcher_foreground,
                mobNumber,messageReceived,TimeStampUtil.convert(time)));
    }

    public static void handleReceiveRequest(Context context,Intent intent){


        Bundle bundle =intent.getExtras();

        //pdus (Protocol Data Units)
        Object[] smsObj = (Object[]) bundle.get("pdus");
        String format = bundle.getString("format");

        for(Object obj: smsObj){
            SmsMessage message = SmsMessage.createFromPdu((byte[]) obj, format);
            mobNumber = message.getDisplayOriginatingAddress();
            messageReceived = message.getDisplayMessageBody();
            time = message.getTimestampMillis();
            Log.d("MsgDetails","MobNo:"+mobNumber+" , Msg: "+messageReceived+" , Time: "+time);

            //Sending intent in the form of Serializable

            Intent broadcastIntent = new Intent("MessageReceiver");
            Bundle args = new Bundle();
            RecyclerModalClass recyclerModalClass = new RecyclerModalClass(1,R.drawable.ic_launcher_foreground,mobNumber,messageReceived, TimeStampUtil.convert(time));
            args.putSerializable("object",recyclerModalClass);
            broadcastIntent.setAction("SMS_RECEIVED_ACTION");
            broadcastIntent.putExtra("sms",args);

            context.sendBroadcast(broadcastIntent);
        }
    }
}
