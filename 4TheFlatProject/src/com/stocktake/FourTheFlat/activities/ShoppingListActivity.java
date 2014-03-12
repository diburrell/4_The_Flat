package com.stocktake.FourTheFlat.activities;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;

import com.FourTheFlat.*;
import com.stocktake.R;

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

public class ShoppingListActivity extends Activity {

	
	/* Create Error Messages */
	TableLayout table;
	TableRow errorRow;
	TextView error1;
	TableRow.LayoutParams params;

	Button toggleView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Alarm.startRepeatingTimer(getApplicationContext());

		setContentView(R.layout.shoppinglist);
		
		summaryTable(this);
	}

	public void summaryTable(Activity contextActivity) {

		table = (TableLayout) contextActivity.findViewById(R.id.tableLayout1); // Find	

		int productCount = 4;
		
		TableRow[] rowProduct = new TableRow[productCount];
	 
		TextView[] productName = new TextView[productCount];
		 
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
		
		TableLayout.LayoutParams params2 = new TableLayout.LayoutParams();
		params2.topMargin = 100;			
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