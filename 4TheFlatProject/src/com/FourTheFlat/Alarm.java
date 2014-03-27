package com.FourTheFlat;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class Alarm 
{
	public static void startRepeatingTimer(Context context)
	{
		setAlarm(context);
	}

	public static void cancelRepeatingTimer(Context context) 
	{
		cancelAlarm(context);
	}

	/** Sets the alarms that need to be scheduled. */
	private static void setAlarm(Context context) 
	{
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context,	AlarmManagerBroadcastReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, 50, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		Calendar c = Calendar.getInstance();
		c.set(c.HOUR_OF_DAY, 0);
		c.set(c.MINUTE, 0);
		c.set(c.MINUTE, 0);
		am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 60000, pi);
	}

	/** Cancels all alarms */
	private static void cancelAlarm(Context context) 
	{
		Intent intent = new Intent(context,	AlarmManagerBroadcastReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(context, 50, intent, 0);
		AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
	}
}