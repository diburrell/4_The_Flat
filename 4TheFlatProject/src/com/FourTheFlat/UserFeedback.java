package com.FourTheFlat;

import com.FourTheFlat.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public final class UserFeedback {

	public static void showNotification(Context context, String message) 
	{
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				new Intent(context, TabCreator.class), 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context)
				.setContentTitle("4TheFlat")
				.setContentText(message);
		mBuilder.setContentIntent(contentIntent);
		mBuilder.setDefaults(Notification.DEFAULT_SOUND);
		mBuilder.setAutoCancel(true);
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(1, mBuilder.build());
	}

}