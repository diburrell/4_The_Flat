package com.FourTheFlat.activities;

import java.util.concurrent.ExecutionException;

import com.FourTheFlat.ActiveUser;
import com.FourTheFlat.HttpRequest;
import com.FourTheFlat.R;
import com.FourTheFlat.Settings;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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
	EditText currentPasswordEdit;
	EditText newPasswordEdit;
	EditText confirmPasswordEdit;
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
		
		int USERS = 5;
		
		String[] users = new String[USERS];
		users[0] = "Adam";
		users[1] = "Brian";
		users[2] = "Claire";
		users[3] = "Denise";
		users[4] = "Eddie";
		
		double[] money = new double[USERS];
		money[0] = -14.59;
		money[1] = -0.01;
		money[2] = 0.00;
		money[3] = 0.01;
		money[4] = 19.31;
		
		TableRow[] row = new TableRow[USERS];
		TextView[] name = new TextView[USERS];
		TextView[] owe = new TextView[USERS];
		
		//USER ROWS
		for (int i=0; i<USERS; i++)
		{
			row[i] = new TableRow(contextActivity);
			
			name[i] = new TextView(contextActivity);
			name[i].setText(users[i]);
			name[i].setTextSize(24f);
			name[i].setTextColor(Color.BLACK);
			if (i == 0)
				name[i].setPadding(0, 60, 0, 0);				
			
			owe[i] = new TextView(contextActivity);		
			if (money[i] < 0.00)
			{
				owe[i].setText(String.format("-£%.2f", Math.abs(money[i])));
				owe[i].setTextColor(Color.RED);
			}
			else if (money[i] == 0.00)
			{
				owe[i].setText(String.format("£%.2f", money[i]));
				owe[i].setTextColor(Color.BLACK);
			}
			else
			{
				owe[i].setText(String.format("£%.2f", money[i]));
				owe[i].setTextColor(Color.GREEN);
				name[i].setOnClickListener(this);
			}
			owe[i].setGravity(Gravity.RIGHT);
			owe[i].setTextSize(24f);
			if (i == 0)
				owe[i].setPadding(0, 60, 0, 0);
			
			row[i].addView(name[i]);
			row[i].addView(owe[i]);
			
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
		
		currentPasswordEdit = new EditText(contextActivity);
		currentPasswordEdit.setTextSize(18f);
		layout.addView(currentPasswordEdit);
		
		TextView newPassword = new TextView(contextActivity);
		newPassword.setText("New Password:");
		newPassword.setTextSize(18f);
		newPassword.setTextColor(Color.BLACK);
		newPassword.setPadding(0, 50, 0, 0);
		layout.addView(newPassword);
		
		newPasswordEdit = new EditText(contextActivity);
		newPasswordEdit.setTextSize(18f);
		layout.addView(newPasswordEdit);
		
		TextView confirmPassword = new TextView(contextActivity);
		confirmPassword.setText("Confirm Password:");
		confirmPassword.setTextSize(18f);
		confirmPassword.setTextColor(Color.BLACK);
		confirmPassword.setPadding(0, 50, 0, 0);
		layout.addView(confirmPassword);	
		
		confirmPasswordEdit = new EditText(contextActivity);
		confirmPasswordEdit.setTextSize(18f);
		layout.addView(confirmPasswordEdit);
		
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
		if (view instanceof Button)
		{		
			buttonClick(view);
		}
		else if (view instanceof TextView)
		{
			textViewClick(view);
		}
	}
	
	private void buttonClick(View view)
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
				SharedPreferences.Editor editor = Settings.getSharedPreferencesEditor(getApplicationContext());
                editor.putBoolean("hasLoggedIn", false);
                editor.putString("user", "");
                // Commit the edits!	
                editor.commit();
            	Intent logout = new Intent(getApplicationContext(), com.FourTheFlat.activities.LoginActivity.class);
                startActivity(logout);
                finish();
				break;
				
			case 4:
				//back/cancel to main menu
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
				changePassword();
				createMainMenu(this);
				break;
				
			default:
				break;
		}
	}
	
	public void changePassword()
	{
		String currentPassword = currentPasswordEdit.getText().toString();
		String newPassword = newPasswordEdit.getText().toString();
		String confirmPassword = confirmPasswordEdit.getText().toString();
		
		if(!newPassword.equals(confirmPassword))
		{
			Toast.makeText(getApplicationContext(), "Both new passwords must match!", Toast.LENGTH_LONG).show();
			return;
		}
		
		String responseCode;
		try {
			responseCode = new HttpRequest().execute("http://group1.cloudapp.net:8080/ServerSide/user/"+ActiveUser.getActiveUser().getUsername()+"/"+currentPassword+"/"+newPassword,"post").get();
			if(responseCode.equals("Incorrect username or password."))
			{
				Toast.makeText(getApplicationContext(), "Both new passwords must match!", Toast.LENGTH_LONG).show();
				return;
			}
			else if(responseCode.equals("An error has occurred."))
			{
				Toast.makeText(getApplicationContext(), "An error has occurred.", Toast.LENGTH_LONG).show();
				return;
			}
			else if(responseCode.equals("Password changed."))
			{
				Toast.makeText(getApplicationContext(), "Password changed successfully.", Toast.LENGTH_LONG).show();
				return;
			}
			else
			{
				Toast.makeText(getApplicationContext(), "An unknown error has occurred.", Toast.LENGTH_LONG).show();
				return;
			}
		} catch (Exception e)
		{
			Toast.makeText(getApplicationContext(), "An unknown error has occurred.", Toast.LENGTH_LONG).show();
			return;
		}
	}

	private void textViewClick(View view)
	{
		String username = ((TextView)view).getText().toString();
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		alertDialogBuilder.setTitle("Do you want to clear " + username + "'s debt?");

		alertDialogBuilder.setCancelable(false)
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog,int id) 
				{					
					Toast.makeText(AccountActivity.this, "TO DO: CLEAR DEBT", Toast.LENGTH_LONG).show();
				}
			})
			.setNegativeButton("No", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog,int id) 
				{
					dialog.cancel();
				}
			});
		
			AlertDialog alertDialog = alertDialogBuilder.create();

			alertDialog.show();
	}
}