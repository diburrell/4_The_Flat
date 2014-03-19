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

	
	public Main() throws JsonMappingException, JsonParseException, IOException
	{

	}
}