package com.FourTheFlat;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import android.content.Context;
import android.content.SharedPreferences;

import com.FourTheFlat.stores.User;

public class ActiveUser {
	
	private static User user;
	
	public static boolean initialise(Context context)
	{
		SharedPreferences settings = Settings.getSharedPreferences(context);
		String userJSON = settings.getString("user", "");
		try {
			user = (User)PojoMapper.fromJson(userJSON, User.class);
			return true;
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public static User getActiveUser()
	{
		return user;
	}

}
