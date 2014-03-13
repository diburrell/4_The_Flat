package com.FourTheFlat;

import com.FourTheFlat.activities.AccountActivity;
import com.FourTheFlat.activities.ProductsActivity;
import com.FourTheFlat.activities.ShoppingListActivity;
import com.FourTheFlat.R;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

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
	    final TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, ShoppingListActivity.class);
	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("Shopping List").setIndicator("List",
	                      res.getDrawable(R.drawable.shoppinglist_tab))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    
	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, ProductsActivity.class);
	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("Products").setIndicator("Products",
	                      res.getDrawable(R.drawable.products_tab))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    
		 // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, AccountActivity.class);
	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("Account").setIndicator("Account",
	                      res.getDrawable(R.drawable.account_tab))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    
	    tabHost.setOnTabChangedListener(new OnTabChangeListener() 
	    {
	        public void onTabChanged(String tabId) 
	        {
	        	switch (tabHost.getCurrentTab()) 
	        	{
		            case 0:
		            	//click on tab 0
		                break;
		            case 1:
		            	//click on tab 1
		                break;
		            case 2:
		            	//click on tab 2
		                break;
		            default:
		                break;
	            }
	        	
	        	setTabColors(tabHost);
	        }
	    });
	    
	    setTabColors(tabHost);
	    
	    tabHost.setCurrentTab(0);
	}
	
	private void setTabColors(TabHost tabHost)
	{
		for (int i=0; i<tabHost.getTabWidget().getTabCount(); i++)
    	{
			TextView tv = (TextView)tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
	        tv.setTextSize(20);
			
    		if (tabHost.getTabWidget().getChildAt(i).isSelected()) //if this tab is currently selected
    		{
    			tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.rgb(12, 102, 146));
    			tv.setTextColor(Color.WHITE);
    		}
    		else //this tab is NOT currently selected
    		{
    			tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.rgb(51, 153, 204));
    			tv.setTextColor(Color.rgb(64, 64, 64));
    		}
    	}
	}
}


