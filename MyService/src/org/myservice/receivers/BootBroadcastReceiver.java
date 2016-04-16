package org.myservice.receivers;

import org.myservice.Constant;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.view.Gravity;
import android.widget.Toast;

public class BootBroadcastReceiver extends BroadcastReceiver {


	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Constant.ACTION_BOOT)) {
			
			Intent i = new Intent(context, AlarmReceiver.class);
			i.setAction(Constant.ACTION_START_DAEMON_SERVICE);
			PendingIntent sender = PendingIntent.getBroadcast(context, 0,
					i, 0);
			long firstime = SystemClock.elapsedRealtime();
			AlarmManager am = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);

			// 10秒一个周期，不停的发送广播
			am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime,
					10 * 1000, sender);			
		}
	}

}
