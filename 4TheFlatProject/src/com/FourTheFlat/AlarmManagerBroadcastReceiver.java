package com.FourTheFlat;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;

import com.FourTheFlat.stores.Group;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

	
	
	@Override
	/** This method is triggered when an alarm goes off. */
	public void onReceive(Context context, Intent intent)
	{
		Log.w("alarm", "alarm done");
		Calendar c = Calendar.getInstance();
		if(c.get(c.MINUTE) % 5 == 0)//Every 5 minutes
		{
			checkUserShopping(context);
		}
		else//Every minute
		{
			refreshActiveUser(context);
		}
	}
	
	public void checkUserShopping(Context context)
	{
		String response;
		Group g;
		try
		{
			response = new HttpRequest().execute("http://group1.cloudapp.net:8080/ServerSide/group/"+ActiveUser.getActiveUser().getUsername(),"get").get();
			g = (Group)PojoMapper.fromJson(response, Group.class);
			if(!g.getuserShopping().equals(null))
			{
				UserFeedback.showNotification(context, "Someone is shopping, make sure to add any last minute items you need!");
				return;
			}
		}
		catch(Exception e)
		{
			return;
		}
	}
	
	public void refreshActiveUser(Context context)
	{
		String response;
		String hashedPassword = Settings.getSharedPreferences(context).getString("hashedPassword", "");
		try
		{
			response = new HttpRequest().execute("http://group1.cloudapp.net:8080/ServerSide/user/"+ActiveUser.getActiveUser().getUsername()+"/"+hashedPassword,"get").get();
			Settings.getSharedPreferencesEditor(context).putString("user", response);
		}
		catch(Exception e)
		{
			return;
		}
	}
	
}