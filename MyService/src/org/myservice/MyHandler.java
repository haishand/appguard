package org.myservice;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class MyHandler extends Handler {

	public static final int ID_SMS_MESSAGE = 10;
	public static final int ID_PHONE_MESSAGE = 20;

	public MyHandler() {

	}

	public MyHandler(Looper looper) {
		super(looper);
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);

		switch (msg.what) {
		case ID_SMS_MESSAGE:
			Bundle bundle = msg.getData();

			String sender = bundle.getString("sender");
			String content = bundle.getString("content");
			Date date = new Date(bundle.getLong("timeStamp"));

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String time = dateFormat.format(date);

//			MyServiceUtils.saveToFile("sms_" + time, new SmsData(sender,
//					content, time));
			break;
		case ID_PHONE_MESSAGE:
			break;
			
		}

	}

}
