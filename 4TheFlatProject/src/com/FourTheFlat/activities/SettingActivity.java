package com.FourTheFlat.activities;

import com.FourTheFlat.*;
import com.FourTheFlat.R;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends Activity 
{
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Alarm.startRepeatingTimer(getApplicationContext());
		
		Bundle bundle = this.getIntent().getExtras();
		
		setContentView(R.layout.settings);
		
	}
	
	
}
