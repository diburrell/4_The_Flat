package com.FourTheFlat;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;

import com.FourTheFlat.stores.Group;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

	static boolean userShoppingNotificationSent = false;

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
			response = new HttpRequest().execute(Main.URL + "group/"+ActiveUser.getActiveUser().getUsername(),"get").get();
			g = (Group)PojoMapper.fromJson(response, Group.class);
			
			if(!g.getuserShopping().equals(null))
			{
				if(!userShoppingNotificationSent && !g.getuserShopping().equals(ActiveUser.getActiveUser().getUsername()))
				{
					UserFeedback.showNotification(context, "Someone is shopping, make sure to add any last minute items you need!");
				}
				
				userShoppingNotificationSent = true;
				return;
			}
			
			userShoppingNotificationSent = false;
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
			response = new HttpRequest().execute(Main.URL + "user/"+ActiveUser.getActiveUser().getUsername()+"/"+hashedPassword,"get").get();
			Log.w("activeUser",response);
			if(response.equals("Invalid username or password."))
			{
				SharedPreferences.Editor editor = Settings.getSharedPreferencesEditor(context.getApplicationContext());
                editor.putBoolean("hasLoggedIn", false);
                editor.putString("user", "");
                // Commit the edits!	
                editor.commit();
				return;
			}
			Settings.getSharedPreferencesEditor(context).putString("user", response);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return;
		}
	}

}