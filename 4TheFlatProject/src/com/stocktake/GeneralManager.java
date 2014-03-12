package com.stocktake;

import java.util.Locale;

import com.stocktake.R;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v4.app.NotificationCompat;

//new class to handle general things
public final class GeneralManager extends Activity
{
	public static boolean checkInternetConnection(Context c) 
	{	
		ConnectivityManager conMgr = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
	
		// ARE WE CONNECTED TO THE INTERNET
		if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) 
			return true;
	    else
			return false;
	}
	
	public static void showNotification(Context context, String message) 
	{
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				new Intent(context, AgileProjectActivity.class), 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(R.drawable.ic_tab_alerts_on)
				.setContentTitle("Shares")
				.setContentText(message);
		mBuilder.setContentIntent(contentIntent);
		mBuilder.setDefaults(Notification.DEFAULT_SOUND);
		mBuilder.setAutoCancel(true);
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(1, mBuilder.build());
	}
	
	public static int volCharToInt(String amount) {
		float convertedVal = 0;
		int multiplier = 1;
		int returnValue = 0;
		try {
			amount = amount.replaceAll(",", "");
			String valComponent = amount.substring(0, amount.length() - 1);
			String multComponent = amount.substring(amount.length() - 1);
			convertedVal = Float.parseFloat(valComponent);
			multComponent = multComponent.toUpperCase();
			if (multComponent.equals("M")) {
				multiplier = 1000000;
			}
			if (multComponent.equals("K")) {
				multiplier = 1000;
			}
			convertedVal = convertedVal * (float) multiplier;
			returnValue = (int) convertedVal;
		} catch (Exception e) {
			returnValue = 0;
		}
		return returnValue;
	}
}
