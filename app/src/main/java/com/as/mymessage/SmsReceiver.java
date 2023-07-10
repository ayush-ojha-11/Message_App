package com.as.mymessage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.as.mymessage.modals.RecyclerModalClass;
import com.as.mymessage.util.TimeStampUtil;

import java.sql.Timestamp;


public class SmsReceiver extends BroadcastReceiver  {


    @Override
    public void onReceive(Context context, Intent intent) {

        String messageReceived = null;
        String mobNumber = null;
        long time;

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
