package com.FourTheFlat;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONObject;

import com.FourTheFlat.stores.User;

import android.app.Application;
import android.util.Log;

public class Main extends Application
{
	public static final String PREFS_NAME = "MyPrefsFile";
	public static final String URL = "http://group1.cloudapp.net:8080/ServerSide";
	private static String username = "test";
	private static String password = "test";

	public static String getUsername() { return username; }
	public static String getPassword() { return password; }
	
	public Main() throws JsonMappingException, JsonParseException, IOException
	{
		String jsonText = "";
		Boolean error = false;
		
		try
		{

		}
		catch(Exception e)
		{
			error = true;			
		}
		
		Log.w("json", jsonText);
		
		
		//User user = (User)PojoMapper.fromJson(jsonText, User.class);
		
		//Log.w("json", user.getUsername());
	}
}