package com.FourTheFlat;

import com.FourTheFlat.activities.AccountActivity;
import com.FourTheFlat.activities.ProductsActivity;
import com.FourTheFlat.activities.ShoppingListActivity;
import com.FourTheFlat.R;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

/*
 * This class creates the tabs for the app, and draws them to screen.
 */
public class TabCreator extends TabActivity
{	
	
	public void onCreate(Bundle savedInstanceState)
	{	
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);

	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, ShoppingListActivity.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("Shopping List").setIndicator("Shopping List",
	                      res.getDrawable(R.drawable.ic_tab_shoppinglist))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    
	 // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, ProductsActivity.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("Products").setIndicator("Products",
	                      res.getDrawable(R.drawable.ic_tab_products))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    
		 // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, AccountActivity.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("Account").setIndicator("Account",
	                      res.getDrawable(R.drawable.ic_tab_account))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(0);
	}
}


