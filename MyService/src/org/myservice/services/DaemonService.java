package org.myservice.services;

import org.myservice.utils.MyServiceUtils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class DaemonService extends Service {

	Intent intent;
	AlarmManager alarmManager;
	PendingIntent operation;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public void onCreate() {
		alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
		super.onCreate();
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
//		Toast t = Toast.makeText(getApplicationContext(), "MyService Test2", Toast.LENGTH_LONG);
//		t.setGravity(Gravity.CENTER, 0, 0);
//		t.show();
		
		this.intent = intent;
		Log.i("DeamonService", "DaemonService:onStartCommand()");
		long triggerAtTime = System.currentTimeMillis();
		
		long interval = 1000 *60 * 1;
//		Intent intent1 = new Intent()

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}


}
