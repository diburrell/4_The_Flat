package com.FourTheFlat.activities;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import com.FourTheFlat.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ActionBar.LayoutParams;
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

public class ProductsActivity extends Activity implements View.OnClickListener 
{
	TableLayout upperLayout;
	TableLayout lowerLayout;

	String[] allowedProducts;
	
	int state = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shoppinglist);
		loadProductsList(this);	
	}
	
	@Override
	public void onPause() 
	{
		super.onPause();
		upperLayout.removeAllViews();
		lowerLayout.removeAllViews();
	}

	@Override
	public void onResume() 
	{
		super.onResume();
		state = 0;		
		loadProductsList(this);
	}

	public void productTable(Activity contextActivity) 
	{
		upperLayout = (TableLayout) contextActivity.findViewById(R.id.tableLayout1);
		upperLayout.removeAllViews();
		
		lowerLayout = (TableLayout) contextActivity.findViewById(R.id.tableLayout2);
		lowerLayout.removeAllViews();

		if (state == 0) 
		{
			Button moreProducts = new Button(contextActivity);
			moreProducts.setText("Add more products");
			moreProducts.setOnClickListener(this);
			upperLayout.addView(moreProducts);
			
			TextView header = new TextView(contextActivity);
			header.setText("Allowed Products");
			header.setGravity(Gravity.CENTER);
			header.setTextSize(20f);
			header.setTextColor(Color.BLACK);
			header.setPadding(0, 30, 0, 30);
			upperLayout.addView(header);
			
			View ruler = new View(contextActivity); 
			ruler.setBackgroundColor(Color.WHITE);
			upperLayout.addView(ruler, LayoutParams.FILL_PARENT, 5);
			
			TableRow[] rowProduct = new TableRow[allowedProducts.length];
			TextView[] productName = new TextView[allowedProducts.length];

			for (int i = 0; i < allowedProducts.length; i++) 
			{
				rowProduct[i] = new TableRow(contextActivity);
				productName[i] = new TextView(contextActivity);

				productName[i].setText(allowedProducts[i]);
				productName[i].setTypeface(Typeface.DEFAULT, Typeface.BOLD);
				productName[i].setTextColor(Color.BLACK);
				productName[i].setTextSize(25f);
				productName[i].setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
				productName[i].setOnClickListener(this);

				rowProduct[i].addView(productName[i]);

				lowerLayout.addView(rowProduct[i]);
			}
		} 
		else if (state == 1) 
		{
			Button moreProducts = new Button(contextActivity);
			moreProducts.setText("Go back");
			moreProducts.setOnClickListener(this);
			upperLayout.addView(moreProducts);
			
			TextView header = new TextView(contextActivity);
			header.setText("All Products");
			header.setGravity(Gravity.CENTER);
			header.setTextSize(20f);
			header.setTextColor(Color.BLACK);
			header.setPadding(0, 30, 0, 30);
			upperLayout.addView(header);
			
			View ruler = new View(contextActivity); 
			ruler.setBackgroundColor(Color.WHITE);
			upperLayout.addView(ruler, LayoutParams.FILL_PARENT, 5);
			
			String[] allProducts = null;

			try 
			{
				String products = new HttpRequest().execute("http://group1.cloudapp.net:8080/ServerSide/allproducts").get();
				allProducts = products.split("\n");
			} 
			catch (Exception e) 
			{
				Log.w("ALL_PROD", "FAILED TO GET all products");
			}

			Arrays.sort(allProducts);
			
			TableRow[] rowProduct = new TableRow[allProducts.length];
			TextView[] productName = new TextView[allProducts.length];

			outerloop:
			for (int i = 0; i < allProducts.length; i++) 
			{
				for(int j = 0; j < allowedProducts.length; j++)
				{
					if(allProducts[i].equals(allowedProducts[j]))
					{
						continue outerloop;
					}
				}
				
				rowProduct[i] = new TableRow(contextActivity);
				productName[i] = new TextView(contextActivity);

				productName[i].setText(allProducts[i]);
				productName[i].setTypeface(Typeface.DEFAULT, Typeface.BOLD);
				productName[i].setTextColor(Color.BLACK);
				productName[i].setTextSize(25f);
				productName[i].setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
				productName[i].setOnClickListener(this);

				rowProduct[i].addView(productName[i]);

				lowerLayout.addView(rowProduct[i]);
			}
		}
	}
	
	private void noConnectionDisplay(Activity contextActivity)
	{
		upperLayout = (TableLayout) contextActivity.findViewById(R.id.tableLayout1);
		upperLayout.removeAllViews();
		
		lowerLayout = (TableLayout) contextActivity.findViewById(R.id.tableLayout2);
		lowerLayout.removeAllViews();

		TextView error = new TextView(contextActivity);
		error.setText("You do not have an active connection.");
		error.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		error.setTextColor(Color.BLACK);
		error.setTextSize(25f);
		error.setGravity(Gravity.CENTER);
		lowerLayout.addView(error);
	}
	
	private void noGroupDisplay(Activity contextActivity)
	{
		upperLayout = (TableLayout) contextActivity.findViewById(R.id.tableLayout1);
		upperLayout.removeAllViews();
		
		lowerLayout = (TableLayout) contextActivity.findViewById(R.id.tableLayout2);
		lowerLayout.removeAllViews();

		TextView error = new TextView(contextActivity);
		error.setText("You are not in a group");
		error.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		error.setTextColor(Color.BLACK);
		error.setTextSize(25f);
		error.setGravity(Gravity.CENTER);
		lowerLayout.addView(error);
	}

	private String[] getAllowedProducts() 
	{
		try 
		{
			String allowed = new HttpRequest().execute("http://group1.cloudapp.net:8080/ServerSide/allowedproducts/"+ActiveUser.getActiveUser().getGroupID()).get();

			String[] allowedProducts = allowed.split("\n");
			Arrays.sort(allowedProducts);
			return allowedProducts;
		} 
		catch (Exception e) 
		{
			Log.w("ALL_PROD", "FAILED TO GET allowed products");
			return null;
		}
	}

	public void loadProductsList(Activity contextActivity) 
	{
		upperLayout = (TableLayout) contextActivity.findViewById(R.id.tableLayout1);
		upperLayout.removeAllViews();
		
		lowerLayout = (TableLayout) contextActivity.findViewById(R.id.tableLayout2);
		lowerLayout.removeAllViews();
		
		if (ActiveUser.getActiveUser().getGroupID() != null)
		{
			allowedProducts = getAllowedProducts();
		
			if (allowedProducts != null)
			{
				productTable(this);
			}
			else
			{
				noConnectionDisplay(this);
			}
		}
		else
		{
			noGroupDisplay(this);
		}		
	}

	@Override
	public void onClick(View view) 
	{
		if (view instanceof Button) 
		{
			if (state == 0) 
			{
				state = 1;
			} 
			else if (state == 1)
			{
				state = 0;
			}

			loadProductsList(this);
		} 
		else if (view instanceof TextView) 
		{
			final String product = ((TextView)view).getText().toString();

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

			if (state == 0) 
			{
				alertDialogBuilder.setTitle("Do you want to add " + product + " to the shopping list?");
			} 
			else 
			{
				alertDialogBuilder.setTitle("Do you want to suggest " + product + " to be added to allowed products?");
			}

			alertDialogBuilder.setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int id) 
					{
						if (state == 0) 
						{
							Toast.makeText(ProductsActivity.this, product + " added to shopping list.",	Toast.LENGTH_LONG).show();

							try 
							{
								String completed = new HttpRequest().execute("http://group1.cloudapp.net:8080/ServerSide/shoppinglist/"+ActiveUser.getActiveUser().getGroupID() + "/" + product, "put").get();
								Log.w("PUT COMPLETE", completed);
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
						else 
						{
							Toast.makeText(ProductsActivity.this, product + " will be added if all other users agree", Toast.LENGTH_LONG).show();

							try 
							{
								String completed = new HttpRequest().execute("http://group1.cloudapp.net:8080/ServerSide/newsuggestion/"+ActiveUser.getActiveUser().getUsername()+"/0/" + product, "post").get();
								Log.w("POST COMPLETE", completed);
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
}