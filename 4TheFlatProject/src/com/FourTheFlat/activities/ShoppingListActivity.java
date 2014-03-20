package com.FourTheFlat.activities;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import com.FourTheFlat.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

public class ShoppingListActivity extends Activity implements View.OnClickListener 
{

	TableLayout buttonHolder;
	TableLayout list;

	Button startShop;

	String[] shoppingList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Alarm.startRepeatingTimer(getApplicationContext());

		setContentView(R.layout.shoppinglist);

		startShop = new Button(this);
		startShop.setText("Find a Tesco");

		shoppingList = getShoppingList();

		listTable(this);
	}

	public void listTable(Activity contextActivity) {

		buttonHolder = (TableLayout) contextActivity
				.findViewById(R.id.tableLayout1);
		list = (TableLayout) contextActivity.findViewById(R.id.tableLayout2);


		onPause();

		startShop.setOnClickListener(this);
		buttonHolder.addView(startShop);

		TableRow[] rowProduct = new TableRow[shoppingList.length];
		TextView[] productName = new TextView[shoppingList.length];

		// FILL THE LIST WITH PRODUCTS ON THE LIST!!!!!!
		for (int i = 0; i < shoppingList.length; i++) {

			rowProduct[i] = new TableRow(contextActivity);
			productName[i] = new TextView(contextActivity);

			productName[i].setText(shoppingList[i]);
			productName[i].setTypeface(Typeface.DEFAULT, Typeface.BOLD);
			productName[i].setTextColor(Color.BLACK);
			productName[i].setTextSize(25f);
			productName[i].setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

			rowProduct[i].addView(productName[i]);
			rowProduct[i].setOnClickListener(this);

			list.addView(rowProduct[i]);

		}
	}

	private String[] getShoppingList() {
		try {
			String list = new HttpRequest()
					.execute("http://group1.cloudapp.net:8080/ServerSide/shoppinglist/"+ActiveUser.getActiveUser().getGroupID())
					.get();

			String[] shoppingList = list.split("\n");
			Arrays.sort(shoppingList);
			return shoppingList;
		} catch (Exception e) {
			Log.w("ALL_PROD", "FAILED TO GET shopping list!");
			return null;
		}
	}


	public void update()
	{
		list = (TableLayout) this.findViewById(R.id.tableLayout2);
		shoppingList = getShoppingList();
		listTable(this);
	}

	@Override
	public void onClick(View v) 
	{	
		if(v instanceof Button)
		{
		Intent mapIntent = new Intent(this, MapActivity.class);
		mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(mapIntent);
		} else if (v instanceof TableRow) {
		TableRow tR = (TableRow) v;
		TextView child = (TextView) tR.getChildAt(0);

		final String product = child.getText().toString();

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);


			alertDialogBuilder.setTitle("Do you want to remove "
					+ child.getText().toString()
					+ " from the shopping list?");
		// set dialog message
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int id) {
								// /POSITIVE INPUT!

								try {
									String completed = new HttpRequest()
											.execute(
													"http://group1.cloudapp.net:8080/ServerSide/shoppinglist/"+ActiveUser.getActiveUser().getGroupID()+"/"+ product,
													"delete").get();
									Log.w("DELETE COMPLETE", completed);

									Toast.makeText(ShoppingListActivity.this,
											"ITEM REMOVED FROM SHOPPING LIST!",
											Toast.LENGTH_LONG).show();
									onRestart();

								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (ExecutionException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}								
							}
						})
				.setNegativeButton("No",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int id) {
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


	@Override
	protected void onRestart() {
		super.onRestart();
		list.removeAllViews();
		buttonHolder.removeAllViews();
		update();
		Log.w("Resume", "Activity Resumed");
	}

	@Override
	public void onPause() 
	{
		super.onPause(); //always call the superclass method first		
		list.removeAllViews();
		buttonHolder.removeAllViews();		
		Log.w("Pause", "Activity Paused");
	}

	@Override
	public void onResume() 
	{
		super.onResume(); //always call the superclass method first
		update();
		Log.w("Resume", "Activity Resumed");
	}
}