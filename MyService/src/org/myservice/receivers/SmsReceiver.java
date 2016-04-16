package org.myservice.receivers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;

import org.myservice.MyHandler;
import org.myservice.data.SmsData;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver{

	Handler handler;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Looper looper = Looper.myLooper();
		handler = new MyHandler(looper);

		Object[] pdus = (Object[]) intent.getExtras().get("pdus");
		for (Object pdu : pdus) {
			SmsMessage smsg = SmsMessage.createFromPdu((byte[])pdu);
			String sender = smsg.getOriginatingAddress();
			String content = smsg.getMessageBody();
			long timeStamp = smsg.getTimestampMillis();
			
			Message msg = Message.obtain();
			msg.what = MyHandler.ID_SMS_MESSAGE;
			Bundle bundle = new Bundle();
			bundle.putString("sender", sender);
			bundle.putString("content", content);
			bundle.putLong("timeStamp", timeStamp);
			msg.setData(bundle);
			handler.sendMessage(msg);
			
		}
	}

}
