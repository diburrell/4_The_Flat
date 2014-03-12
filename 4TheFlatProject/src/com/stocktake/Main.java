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

public class Main extends Application
{
	// private Finance stockObj;
	static List<String> stockCodes = new ArrayList<String>();
	static List<String> displayNames = new LinkedList<String>();

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
		
	public static List<String> getStockCodes()
	{
		return stockCodes;
	}


	
}


