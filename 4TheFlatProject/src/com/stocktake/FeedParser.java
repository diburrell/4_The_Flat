package com.stocktake;

import java.io.IOException;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class FeedParser {

	public void parseJSON(Finance toPopulate, String currentStock) throws IOException, JSONException
	{
		// Create JSON and Finance objects
		JSONObject jObject;
		String jsonText = "";
		Boolean error = false;
		
		Log.w("StockCode", currentStock);
	
		try
		{
			jsonText = new CurrentFeedParser().execute("http://finance.google.com/finance/info?client=ig&infotype=infoquoteall&q=LON:"+currentStock).get();
		}
		catch(Exception e)
		{
			error = true;			
		}
	
		if (!error || jsonText.equals(""))
		{
			// Init object
			jObject = new JSONObject(jsonText);

			// Set 'Last' value
			String lastValue = jObject.getString("l");
			lastValue = lastValue.replace(",", "");
			toPopulate.setLast(Float.parseFloat(lastValue) / 100f);

			// Set 'Market'
			toPopulate.setMarket(jObject.getString("e"));

			// Set 'Instant Volume'
			int instantVolume = GeneralManager.volCharToInt(jObject.getString("vo"));
			toPopulate.setInstantVolume(instantVolume);

		}
		else
		{
			Log.w("testTag", currentStock + " got in hereS");
			toPopulate.setLast(0);
		}

		String name = "";
		// Set 'Company' name
		if (currentStock.equals("BLVN")) {
			name = "BowLeven";
		} else if (currentStock.equals("BP")) {
			name = "BP";
		} else if (currentStock.equals("EXPN")) {
			name = "Experian";
		} else if (currentStock.equals("HSBA")) {
			name = "HSBC";
		} else if (currentStock.equals("MKS")) {
			name = "M&S";
		} else if (currentStock.equals("SN")) {
			name = "Smith & Nephew";
		}
		toPopulate.setName(name);

	}

	public void getHistoric(Finance toPopulate, String stockSymbol) throws IOException
	{
		// Check dates
		Calendar cal = Calendar.getInstance();
		String csvdata[] = new String[2];
		
		int day = cal.get(Calendar.DATE) - 1;
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);

		Log.w("CSVParse", "Date: " + day + "/" + month + "/" + year);

		/*
		 * Generate URL for previous data
		 * S
		 * s - ticker symbol a - start month b - start day c - start year d -
		 * end month e - end day f - end year
		 */
		
		try
		{
			String[] URL = {"http://ichart.finance.yahoo.com/table.csv?s="+ stockSymbol + ".L" + "&d=" + month + "&e=" + day + "&f=" + year};
			csvdata = new HistoricFeedParser().execute(URL).get();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		toPopulate.setClose(Float.parseFloat(csvdata[0]) / 100f);
		toPopulate.setVolume(Integer.parseInt(csvdata[1]));
	}

}
