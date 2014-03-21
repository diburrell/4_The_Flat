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
	TableLayout listHolder;

	String[] shoppingList;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shoppinglist);	
		
		if (ActiveUser.getActiveUser().getGroupID() != null)
		{
			createDisplay(this);
		}
		else
		{
			displayEmpty(this);
		}
	}
	
	@Override
	public void onPause() 
	{
		super.onPause();		
		listHolder.removeAllViews();
		buttonHolder.removeAllViews();		
	}

	@Override
	public void onResume() 
	{
		super.onResume();
		createDisplay(this);
	}

	private void createDisplay(Activity contextActivity) 
	{
		shoppingList = getShoppingList();
		
		buttonHolder = (TableLayout) contextActivity.findViewById(R.id.tableLayout1);
		buttonHolder.removeAllViews();
		
		listHolder = (TableLayout) contextActivity.findViewById(R.id.tableLayout2);
		listHolder.removeAllViews();		

		Button findTesco = new Button(this);
		findTesco.setText("Find a Tesco");
		findTesco.setOnClickListener(this);
		buttonHolder.addView(findTesco);
		
		Button startShop = new Button(this);
		startShop.setText("Start shopping");
		startShop.setOnClickListener(this);
		buttonHolder.addView(startShop);

		TableRow[] rowProduct = new TableRow[shoppingList.length];
		TextView[] productName = new TextView[shoppingList.length];

		for (int i = 0; i < shoppingList.length; i++) 
		{
			rowProduct[i] = new TableRow(contextActivity);
			productName[i] = new TextView(contextActivity);

			productName[i].setText(shoppingList[i]);
			productName[i].setTypeface(Typeface.DEFAULT, Typeface.BOLD);
			productName[i].setTextColor(Color.BLACK);
			productName[i].setTextSize(25f);
			productName[i].setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
			productName[i].setOnClickListener(this);

			rowProduct[i].addView(productName[i]);
			
			listHolder.addView(rowProduct[i]);
		}
	}

	private void displayEmpty(Activity contextActivity)
	{
		buttonHolder = (TableLayout) contextActivity.findViewById(R.id.tableLayout1);
		buttonHolder.removeAllViews();
		
		TextView error = new TextView(contextActivity);
		error.setText("You are not the member of any group");
		error.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		error.setTextColor(Color.BLACK);
		error.setTextSize(25f);
		error.setGravity(Gravity.CENTER);
		buttonHolder.addView(error);
	}
	
	private String[] getShoppingList() 
	{
		try 
		{
			String list = new HttpRequest().execute("http://group1.cloudapp.net:8080/ServerSide/shoppinglist/cc4bcc90-ad52-11e3-a13d-74e543b5285b").get();

			String[] shoppingList = list.split("\n");
			Arrays.sort(shoppingList);
			return shoppingList;
		} 
		catch (Exception e) 
		{
			Log.w("ALL_PROD", "FAILED TO GET shopping list!");
			return null;
		}
	}
	
	@Override
	public void onClick(View view) 
	{	
		if(view instanceof Button)
		{
			buttonClick(view);
		} 
		else if (view instanceof TextView) 
		{
			textViewClick(view);
		}
	}
	
	private void buttonClick(View view)
	{
		if (((Button)view).getText() == "Find a Tesco")
		{
			Intent mapIntent = new Intent(this, MapActivity.class);
			mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(mapIntent);
		}
		else if (((Button)view).getText() == "Start shopping")
		{
			Toast.makeText(ShoppingListActivity.this, "START SHOPPING", Toast.LENGTH_LONG).show();
		}
	}

	private void textViewClick(View view)
	{
		final String product = ((TextView)view).getText().toString();

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		alertDialogBuilder.setTitle("Do you want to remove " + product + " from the shopping list?");

		alertDialogBuilder.setCancelable(false)
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int id) 
				{
					try 
					{
						String completed = new HttpRequest().execute("http://group1.cloudapp.net:8080/ServerSide/shoppinglist/cc4bcc90-ad52-11e3-a13d-74e543b5285b/"+ product, "delete").get();
						
						Log.w("PUT COMPLETE", completed);
									
						Toast.makeText(ShoppingListActivity.this, "ITEM REMOVED FROM SHOPPING LIST!", Toast.LENGTH_LONG).show();
						
						onResume();
					} 
					catch (InterruptedException e) 
					{
						e.printStackTrace();
					} 
					catch (ExecutionException e) 
					{
						e.printStackTrace();
					}								
				}
			})
			.setNegativeButton("No", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int id) 
				{
					dialog.cancel();
				}
			});

		AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();
	}
}