package com.stocktake;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.StringTokenizer;

import android.os.AsyncTask;

public class HistoricFeedParser extends AsyncTask<String[], Void, String[]>
{
    protected String[] doInBackground(String[]... params)
    {
    	InputStream is = null;
   		BufferedReader rd = null;
   		StringTokenizer st = null;
   		URL url = null;
   		
   		int lineNumber = 0, tokenNumber = 0;
		String csvdata[] = new String[2];
       
   		try
   		{
			url = new URL(params[0][0]);
			
			is = url.openStream();
			rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			
			String strLine = "";
			
			while (((strLine = rd.readLine()) != null && lineNumber<7)) {
				lineNumber++;

				if (lineNumber == 2) {

					st = new StringTokenizer(strLine, ",");
					String token;

					while (st.hasMoreTokens()) {
						tokenNumber++;
						token = st.nextToken();
						if (tokenNumber == 5) {
							csvdata[0] = token;
							// Log.v("LOGCATZ", "Last Close: " + csvdata[0]);
						}					
					}
					tokenNumber = 0;
				}
				//Volume data from same day 1 week ago, so line 6 of csv
				if (lineNumber==6){
					
					st = new StringTokenizer(strLine, ",");
					String token;

					while (st.hasMoreTokens()) {
						tokenNumber++;
						token = st.nextToken();
						if (tokenNumber == 6) {
							csvdata[1] = token;
							// Log.v("LOGCATZ", "Last Volume: " + csvdata[1]);
						}
					}
				}
					
			}
				
			is.close();
   		} 
   		catch (MalformedURLException e)
   		{
           // TODO Auto-generated catch block
           e.printStackTrace();
   		}
   		catch (IOException e) 
   		{
           // TODO Auto-generated catch block
           e.printStackTrace();
   		}

       return csvdata;
    }
}
