package com.FourTheFlat;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;


public class Alarm {

	public static void startRepeatingTimer(Context context) {
		setAlarm(context);
	}

	public static void cancelRepeatingTimer(Context context) {
		cancelAlarm(context);
	}

	/** Sets the alarms that need to be scheduled. */
	private static void setAlarm(Context context) {
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
			Intent intent = new Intent(context,
					AlarmManagerBroadcastReceiver.class);
			PendingIntent pi = PendingIntent.getBroadcast(context, 50,
					intent, PendingIntent.FLAG_UPDATE_CURRENT);
			// If the alarm should have already gone off today then schedule it
			// starting from tomorrow.
			//Set the alarm to go off 15 minutes before the end of the trading day at 4:15
			Calendar c = Calendar.getInstance();
			c.set(c.HOUR_OF_DAY, 16); // 16
			c.set(c.MINUTE, 15); // 15
			c.set(c.SECOND,0);
			if ((c.getTimeInMillis()) - System.currentTimeMillis() > 0) {
				am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), am.INTERVAL_DAY, pi);
			} else {
				am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis() + am.INTERVAL_DAY, am.INTERVAL_DAY, pi);
			}

	}

	/** Cancels all alarms */
	private static void cancelAlarm(Context context) {
			Intent intent = new Intent(context,
					AlarmManagerBroadcastReceiver.class);
			PendingIntent sender = PendingIntent.getBroadcast(context, 50,
					intent, 0);
			AlarmManager alarmManager = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);
			alarmManager.cancel(sender);
	}
}
