package com.FourTheFlat;

import java.io.IOException;
import java.util.UUID;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.FourTheFlat.stores.Group;
import com.FourTheFlat.stores.User;

public class ActiveUser {

	private static User user;
	private static String shop="";

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

	public static String getShop()
	{
		return shop;
	}

	public static void setShop(String shopName)
	{
		shop = shopName;
	}

	public static boolean leaveGroup(Context context)
	{
		String response;
		UUID groupID = ActiveUser.getActiveUser().getGroupID();
		String username = ActiveUser.getActiveUser().getUsername();

		try {
			response = new HttpRequest().execute("http://group1b.cloudapp.net:8080/ServerSide/group/"+groupID+"/"+username,"delete").get();
		} catch (Exception e)
		{
			e.printStackTrace();
			Toast.makeText(context, "Unable to leave group", Toast.LENGTH_LONG);
			return false;
		}
		String userJSON;
		SharedPreferences sp = Settings.getSharedPreferences(context);
		String hashedPassword = sp.getString("hashedPassword", "");
		try {
			userJSON = new HttpRequest().execute("http://group1b.cloudapp.net:8080/ServerSide/user/"+ActiveUser.getActiveUser().getUsername()+"/"+hashedPassword).get();
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		SharedPreferences.Editor Editor = Settings.getSharedPreferencesEditor(context);
		Editor.putString("user", userJSON);
		Editor.commit();
		ActiveUser.getActiveUser().setGroupID(null);
		return true;
	}

	public static boolean createGroup(Context context, String address)
	{
		String response;
		UUID groupID = ActiveUser.getActiveUser().getGroupID();
		String username = ActiveUser.getActiveUser().getUsername();
		Group g;
		try {
			response = new HttpRequest().execute("http://group1b.cloudapp.net:8080/ServerSide/group/"+username+"/"+address,"post").get();
		} catch (Exception e)
		{
			e.printStackTrace();
			Toast.makeText(context, "Unable to create group", Toast.LENGTH_LONG);
			return false;
		}
		String userJSON;
		SharedPreferences sp = Settings.getSharedPreferences(context);
		String hashedPassword = sp.getString("hashedPassword", "");
		User u;
		try {
			userJSON = new HttpRequest().execute("http://group1b.cloudapp.net:8080/ServerSide/user/"+ActiveUser.getActiveUser().getUsername()+"/"+hashedPassword).get();
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		SharedPreferences.Editor Editor = Settings.getSharedPreferencesEditor(context);
		Editor.putString("user", userJSON);
		Editor.commit();
		initialise(context);
		return true;
	}

	public static boolean isGroupMemberShopping()
	{
		String response;
		Group g;
		try
		{
			response = new HttpRequest().execute("http://group1b.cloudapp.net:8080/ServerSide/group/"+ActiveUser.getActiveUser().getUsername(),"get").get();
			g = (Group)PojoMapper.fromJson(response, Group.class);
			if(!g.getuserShopping().equals(null))
			{
				return true;
			}
			return false;			
		}
		catch(Exception e)
		{
			return false;
		}
	}
}