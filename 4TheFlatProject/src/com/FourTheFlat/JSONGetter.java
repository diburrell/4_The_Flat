package com.FourTheFlat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import android.os.AsyncTask;

public class JSONGetter extends AsyncTask<String, Void, String>
{
    protected String doInBackground(String... params)
    {
    	InputStream is = null;
   		BufferedReader rd = null;
   		StringBuilder sb = null;
   		URL url = null;
   		
   		String returnedString = null;
       
   		try
   		{
			url = new URL(params[0]);
			   
			int cp;
			is = url.openStream();
			rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			sb = new StringBuilder();
			
			while ((cp = rd.read()) != -1)
			{
				sb.append((char) cp);
			}
			
			if (sb != null)
			{
				returnedString = sb.toString();
				
			}
			
			is.close();
   		} 
   		catch (MalformedURLException e)
   		{
           e.printStackTrace();
   		}
   		catch (IOException e) 
   		{
           e.printStackTrace();
   		}		

        return returnedString;
    }
}