package com.FourTheFlat.activities;

import com.FourTheFlat.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;

public class ShopActivity extends Activity
{
	TableLayout layout;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop);
		createDisplay(this);
	}
	
	private void createDisplay(Activity contextActivity)
	{
		layout = (TableLayout) contextActivity.findViewById(R.id.layout);
		
		Button msg = new Button(contextActivity);
		msg.setText("into shop activity");
		layout.addView(msg);
	}
}