package com.FourTheFlat;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

/*
 * class to handle internet connection
 */
public final class ConnectionManager extends Activity
{
	public static boolean checkInternetConnection(Context context) 
	{	
		ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	
		//is the user connected to the internet?
		if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) 
			return true;
	    else
			return false;
	}	
}