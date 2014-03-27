package com.FourTheFlat.activities;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import com.FourTheFlat.ActiveUser;
import com.FourTheFlat.HttpRequest;
import com.FourTheFlat.Main;
import com.FourTheFlat.PojoMapper;
import com.FourTheFlat.R;
import com.FourTheFlat.TabCreator;
import com.FourTheFlat.stores.MapStore;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ShopActivity extends Activity implements View.OnClickListener
{
	TableLayout layout;
	TableLayout buttonHolder;
	
	Map<String, Integer> list;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop);
		createDisplay(this, getList());
	}
	
<<<<<<< HEAD
	private void createDisplay(Activity contextActivity, Map<String, Integer> currList)
	{	
		
		list = getList();
			
		Map<String, Integer> newList = currList; 
		
		list.putAll(newList);
		
		layout = (TableLayout) contextActivity.findViewById(R.id.tableLayout1);
		buttonHolder = (TableLayout) contextActivity.findViewById(R.id.tableLayout2);

		TableRow[] row = new TableRow[currList.size()];
		TextView[] product = new TextView[currList.size()];
		TextView[] cost = new TextView[currList.size()];
=======
	private void createDisplay(Activity contextActivity)
	{		
		layout = (TableLayout) contextActivity.findViewById(R.id.layout);
	
		//Send signal to start shop (LOCK EVERY ONE ELSE OUT!) 
		MapStore store = new MapStore();
		try {
			store = (MapStore) PojoMapper.fromJson(new HttpRequest().execute(Main.URL + "usershopping/"+ActiveUser.getActiveUser().getUsername()).get(), MapStore.class);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, Integer> list = store.getMap();
		TableRow[] row = new TableRow[list.size()];
		TextView[] product = new TextView[list.size()];
		TextView[] cost = new TextView[list.size()];
>>>>>>> origin/error
		
		int i =0;
		for (Map.Entry<String, Integer> m : currList.entrySet())
		{
			row[i] = new TableRow(contextActivity);
			product[i] = new TextView(contextActivity);
			product[i].setText(m.getKey());
			product[i].setTextSize(24f);
			product[i].setTextColor(Color.BLACK);
			if (i == 0)
				product[i].setPadding(0, 60, 0, 0);				

			cost[i] = new TextView(contextActivity);	
			if(m.getValue() > 0)
			{
				cost[i].setText(m.getValue().toString());
				cost[i].setTextSize(24f);
				cost[i].setTextColor(Color.BLACK);
			}
			
			row[i].addView(product[i]);
			row[i].addView(cost[i]);

			row[i].setOnClickListener(this);
			
			layout.addView(row[i]);
			i++;
		}

		Button msg = new Button(contextActivity);
		msg.setText("End shop!");
		msg.setOnClickListener(this);
		buttonHolder.addView(msg);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (view instanceof Button)
		{		
			buttonClick(view);
		}
		else if (view instanceof TableRow)
		{
			tableRowClick(view);
		}
	}

	private void tableRowClick(View view) {
		TableRow tR = (TableRow) view;
		TextView child = (TextView) tR.getChildAt(0);
		TextView child2 = (TextView) tR.getChildAt(1);
		final String product = child.getText().toString();
		final String price = child2.getText().toString();
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
	    alert.setTitle(product+"'s Price");
	    alert.setMessage("Input the cost for "+product+" in pence:");

	    // Set an EditText view to get user input
	    final EditText pence = new EditText(this);
	    
	    alert.setView(pence);
	    pence.setText(price);
	    pence.setFilters(new InputFilter[] {
	    	    // Maximum 2 characters.
	    	    new InputFilter.LengthFilter(5),
	    	    // Digits only.
	    	    DigitsKeyListener.getInstance(),
	    	});
	    	// Digits only & use numeric soft-keyboard.
	    	pence.setKeyListener(DigitsKeyListener.getInstance());
	    alert.setPositiveButton("Confirm",
	            new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                    String price = pence.getText().toString();               
	                    list.put(product, Integer.parseInt(price));
	                    buttonHolder.removeAllViews();
	                    layout.removeAllViews();
	                    ShopActivity.this.createDisplay(ShopActivity.this, list);
	                }
	            });
	    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	            // do nothing
	        }
	    });
	    alert.show();
		
	}

	private void buttonClick(View view) {
		for (Map.Entry<String, Integer> m : list.entrySet())
		{
			if(m.getValue() > 0)
			{
				try {
					new HttpRequest().execute("http://group1.cloudapp.net:8080/ServerSide/usershopping/"+ActiveUser.getActiveUser().getGroupID()+"/"+m.getKey()+"/"
												+m.getValue(),"put").get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}			
		}
		
		try {
			new HttpRequest().execute("http://group1.cloudapp.net:8080/ServerSide/usershopping/"+ActiveUser.getActiveUser().getUsername()+"/"+ActiveUser.getShop(),"delete").get();	
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Intent resetIntent = new Intent(getApplicationContext(), TabCreator.class);
		resetIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(resetIntent);
		finish();
	}
	
	private Map<String, Integer> getList()
	{

		MapStore store = new MapStore();
		try {
			store = (MapStore) PojoMapper.fromJson(new HttpRequest().execute("http://group1.cloudapp.net:8080/ServerSide/usershopping/"+ActiveUser.getActiveUser().getUsername(),"post").get(), MapStore.class);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return store.getMap();
	}
	
	@Override
	public void onBackPressed()
	{
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder.setTitle("Do you want to cancel shopping?");
			alertDialogBuilder.setCancelable(false).setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									try {
										new HttpRequest().execute("http://group1.cloudapp.net:8080/ServerSide/usershopping/"+ActiveUser.getActiveUser().getUsername()+"/Test Shop","delete").get();
										Intent resetIntent = new Intent(getApplicationContext(), TabCreator.class);
										resetIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
										startActivity(resetIntent);
										finish();
									} catch (InterruptedException e) {
										e.printStackTrace();
									} catch (ExecutionException e) {
										e.printStackTrace();
									}
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
	}
}