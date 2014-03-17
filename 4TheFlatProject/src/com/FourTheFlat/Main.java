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
	private static String username = "test";
	private static String password = "test";

	public static String getUsername() { return username; }
	public static String getPassword() { return password; }
	
	public Main() throws JsonMappingException, JsonParseException, IOException
	{
		JSONObject jObject;
		String jsonText = "";
		Boolean error = false;
		
		try
		{
			jsonText = new JSONGetter().execute("http://group1.cloudapp.net:8080/ServerSide/user/test/test").get();
		}
		catch(Exception e)
		{
			error = true;			
		}
		
		User user = (User)PojoMapper.fromJson(jsonText, User.class);
		
		Log.w("jason", user.getUsername());
	}
}