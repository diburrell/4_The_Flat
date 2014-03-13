package com.FourTheFlat.activities;

import com.FourTheFlat.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class ProductsActivity extends Activity implements View.OnClickListener {
	/** Called when the activity is first created. */

	TableLayout buttonHolder;

	Button moreProducts;

	TableLayout list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shoppinglist);

		moreProducts = new Button(this);
		moreProducts.setText("Add more products!");

		productTable(this);
	}

	public void productTable(Activity contextActivity) {

		buttonHolder = (TableLayout) contextActivity
				.findViewById(R.id.tableLayout1);
		list = (TableLayout) contextActivity.findViewById(R.id.tableLayout2);

		int productCount = 100;

		moreProducts.setOnClickListener(this);
		buttonHolder.addView(moreProducts);

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
			rowProduct[i].setOnClickListener(this);

			list.addView(rowProduct[i]);
		}

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

	@Override
	public void onClick(View v)
	{
			
		if (v instanceof Button)
		{
			Toast.makeText(this, "TO DO: SHOW MORE PRODUCTS!", Toast.LENGTH_SHORT).show();
		}
		else if (v instanceof TableRow)
		{
			TableRow tR = (TableRow)v;
			TextView child = (TextView) tR.getChildAt(0); 
			
			
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
 
			// set title
			alertDialogBuilder.setTitle("Do you want to add "+child.getText().toString()+" product to the shopping list?");
 
			// set dialog message
			alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						
						///POSITIVE INPUT!
						
						Toast.makeText(ProductsActivity.this, "TO DO: ADD ITEM TO SHOPPING LIST!", Toast.LENGTH_LONG).show();
					}
				  })
				.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
		}
	}
}