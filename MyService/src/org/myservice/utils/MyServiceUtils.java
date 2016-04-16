package org.myservice.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.myservice.data.SmsData;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class MyServiceUtils {

	public static boolean isWifiConnected(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(wifiNetworkInfo.isConnected())
        {
            return true ;
        }
    
        return false ;
    }

	public static void saveSmsToFile(Context context, String name, SmsData smsData) {
		try {
			FileOutputStream fout = context.openFileOutput(name, Context.MODE_APPEND);
			fout.write(smsData.toString().getBytes());
			fout.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
