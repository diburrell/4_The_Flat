package com.stocktake;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

	FeedParser feed = new FeedParser();
	
	private final String[] epicCode = {"BP","BLVN","HSBA","EXPN","MKS","SN"};
	Finance[] portfolio = new Finance[6];
	
	@Override
	/** This method is triggered when an alarm goes off. */
	public void onReceive(Context context, Intent intent)
	{
		boolean notifyChange = false;
		
		if (GeneralManager.checkInternetConnection(context)) {
			try {
				for (int i=0; i<5; i++)
				{
					portfolio[i] = new Finance();
					feed.parseJSON(portfolio[i], epicCode[i]);
					feed.getHistoric(portfolio[i], epicCode[i]);

					portfolio[i].calcRocketPlummet();
					portfolio[i].calcRun();

					Calendar c = Calendar.getInstance();
					
					if((portfolio[i].is_plummet | portfolio[i].is_rocket) && !notifyChange && isWeekDay(c))
					{
						GeneralManager.showNotification(context, "One or more of your stocks has changed today.");
						notifyChange = true;
					}				
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		else
		{
			GeneralManager.showNotification(context, "Couldn't get rockets or plummets because you do not have an active Internet connection.");
		}
	}
	
	private boolean isWeekDay(Calendar c)
	{
		/*
		 * 	Sunday = 1
			Monday = 2
			Tuesday = 3
			Wednesday = 4
			Thursday = 5
			Friday = 6
			Saturday = 7
		 */
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		return (dayOfWeek != 7 && dayOfWeek != 1);
	}
}