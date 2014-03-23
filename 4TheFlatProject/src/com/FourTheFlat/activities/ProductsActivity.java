package com.FourTheFlat.activities;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

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

	// Holds button
	TableLayout buttonHolder;
	// Holds list of products
	TableLayout list;

	Button moreProducts;
	boolean allProds = false;

	String[] allowedProducts;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shoppinglist);
		
		if(ActiveUser.getActiveUser().getGroupID() != null)
		{
			moreProducts = new Button(this);
			moreProducts.setText("See more products you can add!");
		}



		allowedProducts = getAllowedProducts();

		productTable(this);
	}

	public void productTable(Activity contextActivity) {

		buttonHolder = (TableLayout) contextActivity
				.findViewById(R.id.tableLayout1);
		list = (TableLayout) contextActivity.findViewById(R.id.tableLayout2);
		if(ActiveUser.getActiveUser().getGroupID() != null)
		{
			moreProducts.setOnClickListener(this);
			buttonHolder.addView(moreProducts);
		}

		if (!allProds) {

			TableRow[] rowProduct = new TableRow[allowedProducts.length];
			TextView[] productName = new TextView[allowedProducts.length];

			// FILL THE LIST WITH PRODUCTS ON THE LIST!!!!!!
			for (int i = 0; i < allowedProducts.length; i++) {

				rowProduct[i] = new TableRow(contextActivity);
				productName[i] = new TextView(contextActivity);

				productName[i].setText(allowedProducts[i]);
				productName[i].setTypeface(Typeface.DEFAULT, Typeface.BOLD);
				productName[i].setTextColor(Color.BLACK);
				productName[i].setTextSize(25f);
				productName[i].setGravity(Gravity.LEFT
						| Gravity.CENTER_VERTICAL);

				rowProduct[i].addView(productName[i]);
				rowProduct[i].setOnClickListener(this);

				list.addView(rowProduct[i]);
			}
		} else {
			String[] allProducts = null;

			try {
				String products = new HttpRequest()
						.execute(
								"http://group1.cloudapp.net:8080/ServerSide/allproducts")
						.get();
				allProducts = products.split("\n");

			} catch (Exception e) {
				Log.w("ALL_PROD", "Could not retrieve products!");
			}

			Arrays.sort(allProducts);
			TableRow[] rowProduct = new TableRow[allProducts.length];
			TextView[] productName = new TextView[allProducts.length];

			for (int i = 0; i < allProducts.length; i++) {
				rowProduct[i] = new TableRow(contextActivity);
				productName[i] = new TextView(contextActivity);

				productName[i].setText(allProducts[i]);
				productName[i].setTypeface(Typeface.DEFAULT, Typeface.BOLD);
				productName[i].setTextColor(Color.BLACK);
				productName[i].setTextSize(25f);
				productName[i].setGravity(Gravity.LEFT
						| Gravity.CENTER_VERTICAL);

				rowProduct[i].addView(productName[i]);
				rowProduct[i].setOnClickListener(this);

				list.addView(rowProduct[i]);
				// }
			}
		}

	}

	private String[] getAllowedProducts() {
		if(ActiveUser.getActiveUser().getGroupID() == null)
		{
			return new String[] { "You are not in a group."};
		}
		try {
			String allowed = new HttpRequest()
					.execute(
							"http://group1.cloudapp.net:8080/ServerSide/allowedproducts/"+ActiveUser.getActiveUser().getGroupID())
					.get();

			String[] allowedProducts = allowed.split("\n");

			Arrays.sort(allowedProducts);
			return allowedProducts;
		} catch (Exception e) {
			Log.w("ALL_PROD", "FAILED TO GET ALLOWED PRODUCTS!");
			return null;
		}
	}

	public void update() {
		list = (TableLayout) this.findViewById(R.id.tableLayout2);
		productTable(this);
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
	public void onClick(View v) {
		// if a button is passed in
		if (v instanceof Button) {
			if (!allProds) {
				allProds = true;
				moreProducts.setText("Go back to your allowed products");
			} else {
				allProds = false;
				allowedProducts = getAllowedProducts();
				moreProducts.setText("See more products you can add!");
			}

			onRestart();

			// if a table row passed in
		} else if (v instanceof TableRow) {
			TableRow tR = (TableRow) v;
			TextView child = (TextView) tR.getChildAt(0);

			final String product = child.getText().toString();

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);

			if (!allProds) {
				// set title
				alertDialogBuilder.setTitle("Do you want to add "
						+ child.getText().toString()
						+ " to the shopping list?");
			} else {
				alertDialogBuilder.setTitle("Do you want to suggest "
						+ child.getText().toString()
						+ " to be added to allowed products?");
			}

			// set dialog message
			alertDialogBuilder
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int id) {
									// /POSITIVE INPUT!

									if (!allProds) {
										Toast.makeText(ProductsActivity.this,
												"Item added to shopping list.",
												Toast.LENGTH_LONG).show();

										try {
											String completed = new HttpRequest()
													.execute(
															"http://group1.cloudapp.net:8080/ServerSide/shoppinglist/"+ActiveUser.getActiveUser().getGroupID()+"/"+ product,
															"put").get();
											Log.w("PUT COMPLETE", completed);

										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (ExecutionException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

									} else {
										Toast.makeText(
												ProductsActivity.this,
												"Product will be added if all other users agree!",
												Toast.LENGTH_LONG).show();
										
										try {
											String completed = new HttpRequest()
													.execute(
															"http://group1.cloudapp.net:8080/ServerSide/newsuggestion/"+ActiveUser.getActiveUser().getUsername()+"/0/"+ product,
															"post").get();
											Log.w("POST COMPLETE", completed);

										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (ExecutionException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
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
}