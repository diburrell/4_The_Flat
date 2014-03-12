package com.stocktake;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.StringTokenizer;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class FeedParserTest
{
	public void parseJSON(Finance toPopulate, String currentStock)
			throws IOException, JSONException {
								
		
		//VALUES FOR BP
		if(currentStock.equals("BP"))
		{
			toPopulate.name = "BP"; 
			toPopulate.last = 70; //SIMULATE STOCK DECREASE BY 30%, TRIGGER MANUAL PLUMMET VALUE 
			toPopulate.market = "LON";
			toPopulate.volume = 100;
			toPopulate.instant_volume = 150; //SIMULATE INCREASSE IN TRADING BY 50%,  SET EQUAL TO THE PERCENTAGE TO TRIGGER DEFAULT RUN ALERT 
											 // (NO ALERT)
		}
		
		//VALUES FOR EXPERIAN
		if(currentStock.equals("EXPN"))
		{
			toPopulate.name ="Experian";
			toPopulate.last = 109; //STOCK INCREASED BY 9%, SIMULATES INCREASE BUT NO ROCKET
			toPopulate.market = "LON";			
			toPopulate.volume = 100;
			toPopulate.instant_volume = 140; //SIMULATE INCREASE IN TRADING OF 50%, SET AT THE EXACT PERCENTAGE TO TRIGGER DEFAULT RUN ALERT 
			 //(ALERT)
		}
		
		//VALUES FOR BOWLEVEN
		if(currentStock.equals("BLVN"))
		{
			toPopulate.name = "BowLeven";
			toPopulate.last = 100; //STOCK STAYS THE SAME
			toPopulate.market = "LON";
			toPopulate.volume = 100;
			toPopulate.instant_volume = 130; //SIMULATE INCREASE IN TRADING BY 30%,  SET LOWER THAN THE PERCENTAGE TO TRIGGER DEFAULT RUN ALERT 
											 // (ALERT)
			}
		
		//VALUES FOR SMITH AND NEPHEW
		if(currentStock.equals("SN"))
		{
			toPopulate.name = "Smith & Nephew";
			toPopulate.last = 0; //SIMULATES DATA NOT BEING AVAILABLE
			toPopulate.market = "LON";
			toPopulate.volume = 0;
		}
		
		//VALUES FOR HSBC
		if(currentStock.equals("HSBA"))
		{
			toPopulate.name = "HSBC";
			toPopulate.last = 135; //SIMULATE STOCK INCREASE OF 35%, TRIGGER MANUAL ROCKET TRIGGER
			toPopulate.market = "LON";
			toPopulate.volume = 100;
			toPopulate.instant_volume = 140; //SIMULATE INCREASSE IN TRADING BY 40%, TRIGGER MAULAL RUN TRIGGER
		}
		
		//VALUES FOR MARKS AND SPENCER
		if(currentStock.equals("MKS"))
		{
			toPopulate.name = "M&S";
			toPopulate.last = 70; //SIMULATES PLUMMET OF 30%
			toPopulate.market = "LON";
			toPopulate.volume = 100;
			toPopulate.instant_volume = 175;//SIMULATES INCREASE IN TRADING VOLUME OF 75%
		}
	}
	
	//EACH STOCK HAS HISTORIC PRICE AS 100
	public void getHistoric(Finance toPopulate, String stockToGet)
	{
		toPopulate.setClose(100);
	}
			
}