package com.FourTheFlat.activities;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import com.FourTheFlat.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
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
	TableLayout buttonLayout;
	TableLayout listLayout;

	String[] shoppingList;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shoppinglist);
		
		if(ActiveUser.getActiveUser().getGroupID() != null)
		{
			createDisplay(this);
		}
		else
		{
			Log.w("hello","hello");
			emptyDisplay(this);		
		}
	}
	
	@Override
	public void onPause() 
	{
		super.onPause();
		buttonLayout.removeAllViews();
		if(ActiveUser.getActiveUser().getGroupID() != null)
		{
			listLayout.removeAllViews();	
		}
	}

	@Override
	public void onResume() 
	{
		super.onResume();
		if(ActiveUser.getActiveUser().getGroupID() != null)
		{
			createDisplay(this);
		}
	}

	public void createDisplay(Activity contextActivity) 
	{
		shoppingList = getShoppingList();
		
		buttonLayout = (TableLayout) contextActivity.findViewById(R.id.tableLayout1);
		buttonLayout.removeAllViews();
		
		listLayout = (TableLayout) contextActivity.findViewById(R.id.tableLayout2);
		listLayout.removeAllViews();

		Button shop = new Button(this);
		shop.setText("Go Shop");
		shop.setOnClickListener(this);
		buttonLayout.addView(shop);

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

			listLayout.addView(rowProduct[i]);
		}
	}
	
	private String[] getShoppingList() 
	{
		try 
		{
			String list = new HttpRequest().execute("http://group1.cloudapp.net:8080/ServerSide/shoppinglist/" + ActiveUser.getActiveUser().getGroupID()).get();

			String[] shoppingList = list.split("\n");
			Arrays.sort(shoppingList);
			return shoppingList;
		} 
		catch (Exception e) 
		{
			Log.w("ALL_PROD", "FAILED TO GET shopping list");
			return null;
		}
	}
	
	private void emptyDisplay(Activity contextActivity)
	{
		buttonLayout = (TableLayout) contextActivity.findViewById(R.id.tableLayout1);
		buttonLayout.removeAllViews();

		TextView error = new TextView(contextActivity);
		error.setText("You are not a member of any group");
		error.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		error.setTextColor(Color.BLACK);
		error.setTextSize(25f);
		error.setGravity(Gravity.CENTER);
		buttonLayout.addView(error);
	}

	@Override
	public void onClick(View view) 
	{	
		if(view instanceof Button)
		{
			LocationAsync(view);
		} 
		else if (view instanceof TextView) 
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
							String completed = new HttpRequest().execute("http://group1.cloudapp.net:8080/ServerSide/shoppinglist/"+ActiveUser.getActiveUser().getGroupID()+"/" + product, "delete").get();
							
							Log.w("DELETE COMPLETE", completed);

							Toast.makeText(ShoppingListActivity.this, product + "removed from shopping list", Toast.LENGTH_LONG).show();

							onRestart();
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
					public void onClick(DialogInterface dialog,	int id) 
					{
						dialog.cancel();
					}
				});

			AlertDialog alertDialog = alertDialogBuilder.create();

			alertDialog.show();
		}
	}	
	
	private class ProcessLocation extends AsyncTask<String, String, Boolean> 
	{
	    private ProgressDialog progress;
	
	    @Override
	    protected void onPreExecute() 
	    {
	        super.onPreExecute();
	        progress = new ProgressDialog(ShoppingListActivity.this);
	        progress.setTitle("Contacting GPS");
	        progress.setMessage("Checking location...");
	        progress.setIndeterminate(false);
	        progress.setCancelable(true);
	        progress.show();
	    }
	
	    @Override
	    protected Boolean doInBackground(String... args) 
	    {
	    	return false;
	    	//return nearTesco();
	    }
	
	    @Override
	    protected void onPostExecute(Boolean nearTesco) 
	    {
	        if (nearTesco)
	        {
	        	Intent shopIntent = new Intent(getApplicationContext(), ShopActivity.class);
	        	shopIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    		startActivity(shopIntent);
	        }
	        else
	        {
	        	Intent mapIntent = new Intent(getApplicationContext(), MapActivity.class);
	    		mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    		startActivity(mapIntent);
	        }
	    } 
	}
	
	public void LocationAsync(View view)
    {
        new ProcessLocation().execute();
    }   
	
	public boolean nearTesco()
	{			
	    double earthRadius = 3958.75;
	    int meterConversion = 1609;
	    
	    LatLng current = new LatLng(56.459782,-2.979114); //point near hawkhill tesco
	    
	    for (int i=0; i<Main.STORES; i++)
	    {
		    double dLat = Math.toRadians(current.latitude - Main.locations[0][i]);
		    double dLng = Math.toRadians(current.longitude - Main.locations[1][i]);
		    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
		               Math.cos(Math.toRadians(Main.locations[0][i])) * Math.cos(Math.toRadians(current.latitude)) *
		               Math.sin(dLng/2) * Math.sin(dLng/2);
		    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		    double dist = earthRadius * c;
	
		    Log.w("DISTANCE", i + ": " + dist * meterConversion + "m");
		    
		    if ((dist * meterConversion) <= 250) //if within 250m of a tesco
		    	return true;
	    }
		
		return false;
	}
}