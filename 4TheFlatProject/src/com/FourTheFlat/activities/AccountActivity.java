package com.FourTheFlat.activities;

import com.FourTheFlat.R;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
	
	@Override
	public void onPause()
	{
	    super.onPause();
	    layout.removeAllViews();
	}
	
	@Override
	public void onResume()
	{
	    super.onResume();  
	    createMainMenu(this);
	}
	
	/*
	 * Set up the account tab main display section
	 */
	private void createMainMenu(Activity contextActivity)
	{		
		layout = (TableLayout)contextActivity.findViewById(R.id.layout);
		
		layout.removeAllViews();
		
		Button accountInfo = new Button(contextActivity);
		accountInfo.setText("Account Information");
		accountInfo.setId(0);
		accountInfo.setOnClickListener(this);
		TableLayout.LayoutParams params0 = new TableLayout.LayoutParams();
		params0.setMargins(0, 50, 0, 50);
		accountInfo.setLayoutParams(params0);
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
		TableLayout.LayoutParams params2 = new TableLayout.LayoutParams();
		params2.setMargins(0, 50, 0, 50);
		changePassword.setLayoutParams(params2);
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
		header.setGravity(Gravity.CENTER);
		header.setTextSize(20f);
		header.setTextColor(Color.BLACK);
		header.setPadding(0, 30, 0, 30);
		layout.addView(header);
		
		View ruler = new View(contextActivity); 
		ruler.setBackgroundColor(Color.WHITE);
		layout.addView(ruler, LayoutParams.FILL_PARENT, 5);
		
		String[] users = new String[5];
		users[0] = "Adam";
		users[1] = "Brian";
		users[2] = "Claire";
		users[3] = "Denise";
		users[4] = "Eddie";
		
		TableRow[] row = new TableRow[users.length+1];
		TextView[] name = new TextView[users.length+1];
		TextView[] theyOwe = new TextView[users.length+1];
		TextView[] youOwe = new TextView[users.length+1];
		
		//HEADER ROW
		row[0] = new TableRow(contextActivity);
		row[0].setPadding(0, 30, 0, 0);
		
		name[0] = new TextView(contextActivity);
		SpannableString nameContent = new SpannableString("Name");
		nameContent.setSpan(new UnderlineSpan(), 0, nameContent.length(), 0);
		name[0].setText(nameContent);
		name[0].setTextSize(18f);
		name[0].setTextColor(Color.BLACK);
		row[0].addView(name[0]);
		
		theyOwe[0] = new TextView(contextActivity);
		SpannableString theyOweContent = new SpannableString("They Owe");
		theyOweContent.setSpan(new UnderlineSpan(), 0, theyOweContent.length(), 0);
		theyOwe[0].setText(theyOweContent);
		theyOwe[0].setGravity(Gravity.RIGHT);
		theyOwe[0].setTextSize(18f);
		theyOwe[0].setTextColor(Color.BLACK);
		row[0].addView(theyOwe[0]);
		
		youOwe[0] = new TextView(contextActivity);
		SpannableString youOweContent = new SpannableString("You Owe");
		youOweContent.setSpan(new UnderlineSpan(), 0, youOweContent.length(), 0);
		youOwe[0].setText(youOweContent);
		youOwe[0].setGravity(Gravity.RIGHT);
		youOwe[0].setTextSize(18f);
		youOwe[0].setTextColor(Color.BLACK);
		row[0].addView(youOwe[0]);
		
		layout.addView(row[0]);

		//USER ROWS
		for (int i=1; i<users.length+1; i++)
		{
			row[i] = new TableRow(contextActivity);
			
			name[i] = new TextView(contextActivity);
			name[i].setText(users[i-1]);
			name[i].setTextSize(18f);
			name[i].setTextColor(Color.BLACK);
			row[i].addView(name[i]);
			
			theyOwe[i] = new TextView(contextActivity);
			theyOwe[i].setText("£0");
			theyOwe[i].setGravity(Gravity.RIGHT);
			theyOwe[i].setTextSize(18f);
			theyOwe[i].setTextColor(Color.BLACK);
			row[i].addView(theyOwe[i]);
			
			youOwe[i] = new TextView(contextActivity);
			youOwe[i].setText("£0");
			youOwe[i].setGravity(Gravity.RIGHT);
			youOwe[i].setTextSize(18f);
			youOwe[i].setTextColor(Color.BLACK);
			row[i].addView(youOwe[i]);
			
			layout.addView(row[i]);
		}
		
		Button back = new Button(contextActivity);
		back.setText("Back to menu");
		back.setId(4);
		back.setOnClickListener(this);
		TableLayout.LayoutParams params = new TableLayout.LayoutParams();
		params.setMargins(0, 50, 0, 50);
		back.setLayoutParams(params);
		layout.addView(back);
	}
	
	private void createModifyFlatDetails(Activity contextActivity)
	{
		TextView header = new TextView(contextActivity);
		header.setText("Modify Flat Details");
		header.setGravity(Gravity.CENTER);
		header.setTextSize(20f);
		header.setTextColor(Color.BLACK);
		header.setPadding(0, 30, 0, 30);
		layout.addView(header);
		
		View ruler = new View(contextActivity); 
		ruler.setBackgroundColor(Color.WHITE);
		layout.addView(ruler, LayoutParams.FILL_PARENT, 5);
		
		TextView address = new TextView(contextActivity);
		address.setText("Address:");
		address.setTextSize(18f);
		address.setTextColor(Color.BLACK);
		address.setPadding(0, 50, 0, 0);
		layout.addView(address);
		
		String currentAddress = "123 Fake Street, Neverland";
		
		EditText addressEdit = new EditText(contextActivity);
		addressEdit.setTextSize(18f);
		addressEdit.setHeight(180);
		addressEdit.setText(currentAddress);
		addressEdit.setGravity(Gravity.TOP | Gravity.LEFT);
		layout.addView(addressEdit);
		
		Button save = new Button(contextActivity);
		save.setText("Save");
		save.setId(5);
		save.setOnClickListener(this);
		TableLayout.LayoutParams params5 = new TableLayout.LayoutParams();
		params5.setMargins(0, 50, 0, 0);
		save.setLayoutParams(params5);
		save.setTextColor(Color.GREEN);
		layout.addView(save);
		
		Button leave = new Button(contextActivity);
		leave.setText("Leave this flat");
		leave.setId(6);
		leave.setOnClickListener(this);
		TableLayout.LayoutParams params6 = new TableLayout.LayoutParams();
		params6.setMargins(0, 50, 0, 50);
		leave.setLayoutParams(params6);
		leave.setTextColor(Color.RED);
		layout.addView(leave);
		
		Button cancel = new Button(contextActivity);
		cancel.setText("Cancel");
		cancel.setId(4);
		cancel.setOnClickListener(this);
		layout.addView(cancel);
	}
	
	private void createChangePassword(Activity contextActivity)
	{
		TextView header = new TextView(contextActivity);
		header.setText("Change Password");
		header.setGravity(Gravity.CENTER);
		header.setTextSize(20f);
		header.setTextColor(Color.BLACK);
		header.setPadding(0, 30, 0, 30);
		layout.addView(header);
		
		View ruler = new View(contextActivity); 
		ruler.setBackgroundColor(Color.WHITE);
		layout.addView(ruler, LayoutParams.FILL_PARENT, 5);
		
		TextView currentPassword = new TextView(contextActivity);
		currentPassword.setText("Current Password:");
		currentPassword.setTextSize(18f);
		currentPassword.setTextColor(Color.BLACK);
		currentPassword.setPadding(0, 50, 0, 0);
		layout.addView(currentPassword);
		
		EditText currentEdit = new EditText(contextActivity);
		currentEdit.setTextSize(18f);
		layout.addView(currentEdit);
		
		TextView newPassword = new TextView(contextActivity);
		newPassword.setText("New Password:");
		newPassword.setTextSize(18f);
		newPassword.setTextColor(Color.BLACK);
		newPassword.setPadding(0, 50, 0, 0);
		layout.addView(newPassword);
		
		EditText newEdit = new EditText(contextActivity);
		newEdit.setTextSize(18f);
		layout.addView(newEdit);
		
		TextView confirmPassword = new TextView(contextActivity);
		confirmPassword.setText("Confirm Password:");
		confirmPassword.setTextSize(18f);
		confirmPassword.setTextColor(Color.BLACK);
		confirmPassword.setPadding(0, 50, 0, 0);
		layout.addView(confirmPassword);	
		
		EditText confirmEdit = new EditText(contextActivity);
		confirmEdit.setTextSize(18f);
		layout.addView(confirmEdit);
		
		Button save = new Button(contextActivity);
		save.setText("Save");
		save.setId(7);
		save.setOnClickListener(this);
		TableLayout.LayoutParams params = new TableLayout.LayoutParams();
		params.setMargins(0, 40, 0, 30);
		save.setLayoutParams(params);
		save.setTextColor(Color.GREEN);
		layout.addView(save);
		
		Button cancel = new Button(contextActivity);
		cancel.setText("Cancel");
		cancel.setId(4);
		cancel.setOnClickListener(this);
		layout.addView(cancel);
	}
	
	/*
	 * The onClick handler for the account buttons
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
				
			case 5:
				//save new address
				Toast toast5 = Toast.makeText(getApplicationContext(), "TO DO: SAVE NEW ADDRESS", Toast.LENGTH_SHORT);
				toast5.show();
				createMainMenu(this);
				break;			
				
			case 6:
				//leave the flat
				Toast toast6 = Toast.makeText(getApplicationContext(), "TO DO: LEAVE THE FLAT", Toast.LENGTH_SHORT);
				toast6.show();
				createMainMenu(this);
				break;
				
			case 7:
				//save new password
				Toast toast7 = Toast.makeText(getApplicationContext(), "TO DO: SAVE NEW PASSWORD", Toast.LENGTH_SHORT);
				toast7.show();
				createMainMenu(this);
				break;
				
			default:
				break;
		}
	}
}