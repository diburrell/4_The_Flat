package com.FourTheFlat.activities;

import java.util.Collections;

import com.FourTheFlat.*;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ProductsActivity extends Activity
{
		/** Called when the activity is first created. */

		
		@Override
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			
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