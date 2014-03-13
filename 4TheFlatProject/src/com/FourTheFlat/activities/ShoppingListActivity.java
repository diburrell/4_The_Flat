package com.FourTheFlat.activities;

import com.FourTheFlat.*;
import com.FourTheFlat.activities.ShoppingListActivity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ShoppingListActivity extends Activity implements
		View.OnClickListener {

	TableLayout buttonHolder;

	/* Create Error Messages */
	TableRow errorRow;
	TextView error1;

	Button startShop;
	boolean byValue;

	TableLayout list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Alarm.startRepeatingTimer(getApplicationContext());

		setContentView(R.layout.shoppinglist);

		startShop = new Button(this);
		startShop.setText("Find a Shop!");

		listTable(this);
	}

	public void listTable(Activity contextActivity) {

		buttonHolder = (TableLayout) contextActivity
				.findViewById(R.id.tableLayout1);
		list = (TableLayout) contextActivity.findViewById(R.id.tableLayout2);

		int productCount = 100;

		startShop.setOnClickListener(this);
		buttonHolder.addView(startShop);

		TableRow[] rowProduct = new TableRow[productCount];
		TextView[] productName = new TextView[productCount];

		// FILL THE LIST WITH PRODUCTS ON THE LIST!!!!!!
		for (int i = 0; i < productCount - 1; i++) {

			rowProduct[i] = new TableRow(contextActivity);
			productName[i] = new TextView(contextActivity);

			productName[i].setText("Eggs");
			productName[i].setTypeface(Typeface.DEFAULT, Typeface.BOLD);
			productName[i].setTextColor(Color.BLACK);
			productName[i].setTextSize(25f);
			productName[i].setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

			rowProduct[i].addView(productName[i]);

			list.addView(rowProduct[i]);
		}
	}

	@Override
	public void onClick(View v) {
		Toast.makeText(this, "TO DO: SHOW A SHOP!", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onPause() {
		super.onPause(); // Always call the superclass method first
		Log.w("Pause", "Activity Paused");
	}

	@Override
	public void onResume() {
		super.onResume(); // Always call the superclass method first

		Log.w("Resume", "Activity Resumed");
	}
}