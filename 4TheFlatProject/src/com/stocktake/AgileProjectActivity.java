package com.stocktake;

import com.stocktake.R;
import com.stocktake.activities.RocketActivity;
import com.stocktake.activities.SummaryActivity;
import com.stocktake.activities.VolumeActivity;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

/*
 * This class creates the tabs for the app, and draws them to screen.
 */
public class AgileProjectActivity extends TabActivity
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
	    intent = new Intent().setClass(this, SummaryActivity.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("Summary").setIndicator("Summary",
	                      res.getDrawable(R.drawable.ic_tab_summary))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    
	 // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, VolumeActivity.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("Trade Volume").setIndicator("Trade Volume",
	                      res.getDrawable(R.drawable.ic_tab_volume))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    
		 // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, RocketActivity.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("Alerts").setIndicator("Alerts",
	                      res.getDrawable(R.drawable.ic_tab_alerts))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(0);
	}
}


