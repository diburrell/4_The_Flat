package com.FourTheFlat.activities;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;

import com.FourTheFlat.*;
import com.FourTheFlat.R;
import com.FourTheFlat.activities.ShoppingListActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ShoppingListActivity extends Activity implements View.OnClickListener {

	
	/* Create Error Messages */
	TableLayout table;
	TableRow errorRow;
	TextView error1;
	TableRow.LayoutParams params;

	Button startShop;
	boolean byValue;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Alarm.startRepeatingTimer(getApplicationContext());

		setContentView(R.layout.shoppinglist);
		
		startShop = new Button(this);
		
		summaryTable(this);
	}

	public void summaryTable(Activity contextActivity) {

		table = (TableLayout) contextActivity.findViewById(R.id.tableLayout1); // Find	

		int productCount = 4;
		
		TableRow[] rowProduct = new TableRow[productCount];
	 
		TextView[] productName = new TextView[productCount];
		
				
		startShop.setOnClickListener(this);
		table.addView(startShop);
		
		 
			for (int i = 0; i < 4 ; i++) {
					
			rowProduct[i] = new TableRow(contextActivity);
			productName[i] = new TextView(contextActivity);
	
			productName[i].setText("Eggs");
			productName[i].setTypeface(Typeface.DEFAULT, Typeface.BOLD);
			productName[i].setTextColor(Color.rgb(255, 255, 255));
			productName[i].setTextSize(25f);
			productName[i].setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

			rowProduct[i].addView(productName[i]);

			table.addView(rowProduct[i]);
		}
		
	}

	public void update()
	{
		table = (TableLayout) this.findViewById(R.id.tableLayout1);

		errorRow = new TableRow(this);
		error1 = new TextView(this);
		params = new TableRow.LayoutParams();
		params.span = 4;

	
	}

	
	@Override
	public void onClick(View v)
	{
		if (v instanceof Button)
		{
			if (byValue)
			{
				startShop.setText("321...");
				byValue = false;
			}
			else
			{
				startShop.setText("ABC...");
				byValue = true;
			}
		}
	}
	
	
	@Override
	public void onPause()
	{
	    super.onPause();  // Always call the superclass method first
	    Log.w("Pause", "Activity Paused");
	}
	
	@Override
	public void onResume()
	{
	    super.onResume();  // Always call the superclass method first
	    
	    Log.w("Resume", "Activity Resumed");
	}
}