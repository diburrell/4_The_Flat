package com.FourTheFlat;

import java.io.IOException;
import java.util.UUID;

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
	
	public static boolean leaveFlat(Context context)
	{
		String response;
		UUID groupID = ActiveUser.getActiveUser().getGroupID();
		String username = ActiveUser.getActiveUser().getUsername();
		
		try {
			response = new HttpRequest().execute("http://group1.cloudapp.net:8080/ServerSide/group/"+groupID+"/"+username,"delete").get();
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		String userJSON;
		SharedPreferences sp = Settings.getSharedPreferences(context);
		String hashedPassword = sp.getString("hashedPassword", "");
		try {
			userJSON = new HttpRequest().execute("http://group1.cloudapp.net:8080/ServerSide/user/"+ActiveUser.getActiveUser().getUsername()+"/"+hashedPassword).get();
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		SharedPreferences.Editor Editor = Settings.getSharedPreferencesEditor(context);
		Editor.putString("activeUser", userJSON);
		Editor.commit();
		ActiveUser.getActiveUser().setGroupID(null);
		return true;
	}

}
