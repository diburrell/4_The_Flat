package com.FourTheFlat;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

	
	
	@Override
	/** This method is triggered when an alarm goes off. */
	public void onReceive(Context context, Intent intent)
	{
		boolean notifyChange = false;
		
		if (ConnectionManager.checkInternetConnection(context)) {
			}
		else
		{
		}
	}
	
}