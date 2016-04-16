package org.myservice.receivers;

import org.myservice.Constant;
import org.myservice.services.DaemonService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Constant.ACTION_START_DAEMON_SERVICE)) {
			Intent i = new Intent();
			i.setClass(context, DaemonService.class);
			context.startService(i);
		}

	}

}
