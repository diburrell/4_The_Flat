package com.FourTheFlat.activities;

import com.FourTheFlat.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

/*
 * Creates the rocket activity tab. Called initially AgileProjectActivity.
 */
public class AccountActivity extends Activity
{
	/** Called when the activity is first created. */
	
	TableLayout table;
	
	Button accountInfo;
	Button modifyFlatDetails;
	Button changePassword;
	Button logout;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.account);
		accountDisplay(this);
	}	
	
	public void accountDisplay(Activity contextActivity)
	{		
		table = (TableLayout) contextActivity.findViewById(R.id.tableLayout1);
		
		accountInfo = new Button(contextActivity);
		accountInfo.setText("Account Information");
		table.addView(accountInfo);		
		
		modifyFlatDetails = new Button(contextActivity);
		modifyFlatDetails.setText("Modify Flat Details");
		table.addView(modifyFlatDetails);
		
		changePassword = new Button(contextActivity);
		changePassword.setText("Change Password");
		table.addView(changePassword);
		
		logout = new Button(contextActivity);
		logout.setText("Logout");
		table.addView(logout);		
	}
}