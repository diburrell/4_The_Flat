package com.stocktake.activities;

import java.util.Collections;

import com.stocktake.*;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/*
 * Creates the rocket activity tab. Called initially AgileProjectActivity.
 */
public class RocketActivity extends Activity
{
		/** Called when the activity is first created. */

		
		@Override
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			
			
			setContentView(R.layout.rocket);
		}	
		
		
		@Override
		public void onPause()
		{
		    super.onPause();  // Always call the superclass method first
		    Log.w("Pause", "Activity Paused");
		}
		
		@Override
		public void onResume() {
		    super.onResume();  // Always call the superclass method first
		    Log.w("Resume", "Activity Resumed");
		}
	}