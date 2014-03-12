package com.stocktake.activities;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;

import com.stocktake.*;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class SummaryActivity extends Activity {

	/** Called when the activity is first created. */

	GeneralManager myGenmanager;

	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Alarm.startRepeatingTimer(getApplicationContext());

	}

	
	@Override
	public void onPause()
	{
	    super.onPause();  // Always call the superclass method first
	    Log.w("Pause", "Activity Paused");
	}
	
	@Override
	public void onResume()
	{
	    super.onResume();  // Always call the superclass method first
	    
	    Log.w("Resume", "Activity Resumed");
	}
}