package com.FourTheFlat.activities;

import com.FourTheFlat.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

/*
 * Creates the rocket activity tab. Called initially AgileProjectActivity.
 */
public class AccountActivity extends Activity implements View.OnClickListener
{
	TableLayout layout;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.account);
		createMainMenu(this);
	}	
	
	/*
	 * Set up the account tab main display section
	 */
	private void createMainMenu(Activity contextActivity)
	{		
		layout = (TableLayout)contextActivity.findViewById(R.id.layout);

		Button accountInfo = new Button(contextActivity);
		accountInfo.setText("Account Information");
		accountInfo.setId(0);
		accountInfo.setOnClickListener(this);
		layout.addView(accountInfo);		
		
		Button modifyFlatDetails = new Button(contextActivity);
		modifyFlatDetails.setText("Modify Flat Details");
		modifyFlatDetails.setId(1);
		modifyFlatDetails.setOnClickListener(this);
		layout.addView(modifyFlatDetails);
		
		Button changePassword = new Button(contextActivity);
		changePassword.setText("Change Password");
		changePassword.setId(2);
		changePassword.setOnClickListener(this);
		layout.addView(changePassword);
		
		Button logout = new Button(contextActivity);
		logout.setText("Logout");
		logout.setId(3);
		logout.setOnClickListener(this);
		layout.addView(logout);		
	}

	private void createAccountInformation(Activity contextActivity)
	{
		TextView header = new TextView(contextActivity);
		header.setText("Account Information");
		header.setTextSize(20f);
		layout.addView(header);
		
		String[] users = new String[3];
		users[0] = "Adam";
		users[1] = "Brian";
		users[2] = "Claire";
		
		TableRow[] row = new TableRow[users.length+1];
		TextView[] name = new TextView[users.length+1];
		TextView[] theyOwe = new TextView[users.length+1];
		TextView[] youOwe = new TextView[users.length+1];
		
		//HEADER ROW
		row[0] = new TableRow(contextActivity);
		
		name[0] = new TextView(contextActivity);
		name[0].setText("Name");
		name[0].setTextAppearance(contextActivity, android.R.style.TextAppearance_DeviceDefault_Widget_ActionBar_Title);
		row[0].addView(name[0]);
		
		theyOwe[0] = new TextView(contextActivity);
		theyOwe[0].setText("They Owe");
		theyOwe[0].setGravity(Gravity.RIGHT);
		theyOwe[0].setTextAppearance(contextActivity, android.R.style.TextAppearance_DeviceDefault_Widget_ActionBar_Title);
		row[0].addView(theyOwe[0]);
		
		youOwe[0] = new TextView(contextActivity);
		youOwe[0].setText("You Owe");
		youOwe[0].setGravity(Gravity.RIGHT);
		youOwe[0].setTextAppearance(contextActivity, android.R.style.TextAppearance_DeviceDefault_Widget_ActionBar_Title);
		row[0].addView(youOwe[0]);
		
		layout.addView(row[0]);

		//USER ROWS
		for (int i=1; i<users.length+1; i++)
		{
			row[i] = new TableRow(contextActivity);
			
			name[i] = new TextView(contextActivity);
			name[i].setText(users[i-1]);
			row[i].addView(name[i]);
			
			theyOwe[i] = new TextView(contextActivity);
			theyOwe[i].setText("£0");
			theyOwe[i].setGravity(Gravity.RIGHT);
			row[i].addView(theyOwe[i]);
			
			youOwe[i] = new TextView(contextActivity);
			youOwe[i].setText("£0");
			youOwe[i].setGravity(Gravity.RIGHT);
			row[i].addView(youOwe[i]);
			
			layout.addView(row[i]);
		}
		
		Button back = new Button(contextActivity);
		back.setText("Back to menu");
		back.setId(4);
		back.setOnClickListener(this);
		layout.addView(back);
	}
	
	private void createModifyFlatDetails(Activity contextActivity)
	{
		TextView header = new TextView(contextActivity);
		header.setText("Modify Flat Details");
		layout.addView(header);
		
		Button back = new Button(contextActivity);
		back.setText("Back to menu");
		back.setId(4);
		back.setOnClickListener(this);
		layout.addView(back);
	}
	
	private void createChangePassword(Activity contextActivity)
	{
		TextView header = new TextView(contextActivity);
		header.setText("Change Password");
		layout.addView(header);
		
		Button back = new Button(contextActivity);
		back.setText("Back to menu");
		back.setId(4);
		back.setOnClickListener(this);
		layout.addView(back);
	}
	
	/*
	 * The onClick handler for the main menu buttons
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View view)
	{
		layout.removeAllViews();
		
		switch (view.getId())
		{
			case 0:
				//move to account information screen
				createAccountInformation(this);
				break;	
				
			case 1:
				//move to modify flat details screen
				createModifyFlatDetails(this);
				break;
				
			case 2:
				//move to change password screen
				createChangePassword(this);
				break;
			
			case 3:
				//log out of the app
				Toast toast3 = Toast.makeText(getApplicationContext(), "TO DO: LOGOUT", Toast.LENGTH_SHORT);
				toast3.show();
				createMainMenu(this);
				break;
				
			case 4:
				//back to main menu
				createMainMenu(this);
				break;
				
			default:
				break;
		}
	}
}