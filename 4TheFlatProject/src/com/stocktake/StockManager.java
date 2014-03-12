package com.stocktake;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.text.NumberFormat;

import org.json.JSONException;

import android.content.Context;
import android.app.Activity;
import android.app.Application;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class StockManager extends Application
{
	// private Finance stockObj;
	private static HashMap<Finance, Float> portfolio = new HashMap<Finance, Float>();
	static List<String> stockCodes = new ArrayList<String>();
	static List<String> displayNames = new LinkedList<String>();
	static FeedParserTest newParse = new FeedParserTest();

	//not currently used
	private static String myState;

	public static String getState()
	{
		return myState;
	}

	public static void setState(String s)
	{
		myState = s;
	}
	
	public static HashMap<Finance,Float> getPortfolio()
	{
		return portfolio;
	}
	
	public static List<String> getStockCodes()
	{
		return stockCodes;
	}

	public static void clearPortfolio()
	{
		portfolio.clear();
		stockCodes.clear();
	}

	public static Finance createFinanceObject(String stockCode, Context context) throws IOException, JSONException
	{
		Finance newStock = new Finance();

		newStock.setEpic(stockCode);
		newStock.getTriggers(context);
		newParse.parseJSON(newStock, stockCode);
		newParse.getHistoric(newStock, stockCode);
		
		//Skip tests for rocket/plummet and run if EPIC code no longer exists
		if(newStock.getLast()!=0)
		{
			newStock.calcRun();
			newStock.calcRocketPlummet();
		}

		return newStock;
	}

	public static boolean addPortfolioEntry(String stockCode,String shortName, int numberOfShares, Context context)
			throws IOException, JSONException
	{

		float shareQuantity = (float) numberOfShares;
		Finance stockObj = createFinanceObject(stockCode, context);
		if (portfolio.containsKey(stockObj))
		{
			return false;
		}
		
		portfolio.put(stockObj, shareQuantity);
		displayNames.add(shortName);
		stockCodes.add(stockObj.getName());
		return true;
	}

	public static float getPortfolioTotal()
	{
		float value = 0;
		if (portfolio.isEmpty())
		{
			return 0;
		}
		for (Finance stockObj : portfolio.keySet())
		{
			value += stockObj.getLast() * portfolio.get(stockObj);
		}
		return value;
	}
}


