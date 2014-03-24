package com.FourTheFlat.activities;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import com.FourTheFlat.ActiveUser;
import com.FourTheFlat.Cryptography;
import com.FourTheFlat.HttpRequest;
import com.FourTheFlat.PojoMapper;
import com.FourTheFlat.R;
import com.FourTheFlat.Settings;
import com.FourTheFlat.stores.Group;
import com.FourTheFlat.stores.MapStore;

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
	EditText createGroupAddressEdit;
	EditText userToAddEdit;
	EditText modifyFlatAddressEdit;
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
		
		Button messages = new Button(contextActivity);
		messages.setText("Messages");
		messages.setId(12);
		messages.setOnClickListener(this);;
		layout.addView(messages);	
		
		
		if(ActiveUser.getActiveUser().getGroupID() != null)
		{
			Button accountInfo = new Button(contextActivity);
			accountInfo.setText("Account Information");
			accountInfo.setId(0);
			accountInfo.setOnClickListener(this);
			TableLayout.LayoutParams params0 = new TableLayout.LayoutParams();
			params0.setMargins(0, 50, 0, 50);
			accountInfo.setLayoutParams(params0);
			layout.addView(accountInfo);	
		}
		
		
		if(ActiveUser.getActiveUser().getGroupID() != null)
		{
			Button modifyFlatDetails = new Button(contextActivity);
			modifyFlatDetails.setText("Modify Flat Details");
			modifyFlatDetails.setId(1);
			modifyFlatDetails.setOnClickListener(this);
			layout.addView(modifyFlatDetails);
		}
		else
		{
			Button createGroup = new Button(contextActivity);
			createGroup.setText("Create Group");
			createGroup.setId(9);
			createGroup.setOnClickListener(this);
			layout.addView(createGroup);
		}

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

		MapStore ms = new MapStore();
		try {
			ms = (MapStore)PojoMapper.fromJson(new HttpRequest().execute("http://group1.cloudapp.net:8080/ServerSide/money/"+ActiveUser.getActiveUser().getUsername()).get(), MapStore.class);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Map<String, Integer> books = ms.getMap();
		
		TableRow[] row = new TableRow[books.size()];
		TextView[] name = new TextView[books.size()];
		TextView[] owe = new TextView[books.size()];

		
		//USER ROWS
		int i =0;
		for (Map.Entry<String, Integer> m : books.entrySet())
		{
			row[i] = new TableRow(contextActivity);

			name[i] = new TextView(contextActivity);
			name[i].setText(m.getKey());
			name[i].setTextSize(24f);
			name[i].setTextColor(Color.BLACK);
			if (i == 0)
				name[i].setPadding(0, 60, 0, 0);				

			owe[i] = new TextView(contextActivity);		
			if (m.getValue() < 0)
			{
				owe[i].setText(String.format("-£%.2f", ((float)Math.abs(m.getValue()))/100.0));
				owe[i].setTextColor(Color.RED);
			}
			else if (m.getValue() == 0.00)
			{
				owe[i].setText(String.format("£%.2f",((float) m.getValue())/100.0));
				owe[i].setTextColor(Color.BLACK);
			}
			else
			{
				owe[i].setText(String.format("£%.2f", ((float) m.getValue())/100.0));
				owe[i].setTextColor(Color.GREEN);
				name[i].setOnClickListener(this);
			}
			owe[i].setGravity(Gravity.RIGHT);
			owe[i].setTextSize(24f);
			if (i == 0)
				owe[i].setPadding(0, 60, 0, 0);

			row[i].addView(name[i]);
			row[i].addView(owe[i]);

			if(m.getValue() < 0)
			{
				row[i].setOnClickListener(this);
			}
			
			layout.addView(row[i]);
			i++;
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
		
		String response;
		Group grp;
		try {
			grp = (Group)PojoMapper.fromJson(new HttpRequest().execute("http://group1.cloudapp.net:8080/ServerSide/group/"+ActiveUser.getActiveUser().getUsername()).get(), Group.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			layout.removeAllViews();
			createMainMenu(this);
			Toast.makeText(this, "Unable to get details from server.", Toast.LENGTH_LONG).show();
			return;
		}
		
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

		String currentAddress = grp.getAddress();

		modifyFlatAddressEdit = new EditText(contextActivity);
		modifyFlatAddressEdit.setTextSize(18f);
		modifyFlatAddressEdit.setHeight(180);
		modifyFlatAddressEdit.setText(currentAddress);
		modifyFlatAddressEdit.setGravity(Gravity.TOP | Gravity.LEFT);
		layout.addView(modifyFlatAddressEdit);

		Button save = new Button(contextActivity);
		save.setText("Request Address Change");
		save.setId(5);
		save.setOnClickListener(this);
		TableLayout.LayoutParams params5 = new TableLayout.LayoutParams();
		params5.setMargins(2, 50, 0, 0);
		save.setLayoutParams(params5);
		save.setTextColor(Color.BLACK);
		layout.addView(save);
		
		Button addUser = new Button(contextActivity);
		addUser.setText("Add User to Flat");
		addUser.setId(8);
		addUser.setOnClickListener(this);
		TableLayout.LayoutParams params7 = new TableLayout.LayoutParams();
		params7.setMargins(0, 50, 0, 0);
		addUser.setLayoutParams(params7);
		addUser.setTextColor(Color.BLACK);
		layout.addView(addUser);

		Button leave = new Button(contextActivity);
		leave.setText("Leave this flat");
		leave.setId(6);
		leave.setOnClickListener(this);
		TableLayout.LayoutParams params6 = new TableLayout.LayoutParams();
		params6.setMargins(0, 50, 0, 50);
		leave.setLayoutParams(params6);
		leave.setTextColor(Color.BLACK);
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
		save.setTextColor(Color.BLACK);
		layout.addView(save);

		Button cancel = new Button(contextActivity);
		cancel.setText("Cancel");
		cancel.setId(4);
		cancel.setOnClickListener(this);
		layout.addView(cancel);
	}
	
	public void loadAddUserToFlatLayout(Activity contextActivity)
	{
		TextView header = new TextView(contextActivity);
		header.setText("Request User to Add");
		header.setGravity(Gravity.CENTER);
		header.setTextSize(20f);
		header.setTextColor(Color.BLACK);
		header.setPadding(0, 30, 0, 30);
		layout.addView(header);

		View ruler = new View(contextActivity); 
		ruler.setBackgroundColor(Color.WHITE);
		layout.addView(ruler, LayoutParams.FILL_PARENT, 5);

		TextView userToAdd = new TextView(contextActivity);
		userToAdd.setText("User to add:");
		userToAdd.setTextSize(18f);
		userToAdd.setTextColor(Color.BLACK);
		userToAdd.setPadding(0, 50, 0, 0);
		layout.addView(userToAdd);

		userToAddEdit = new EditText(contextActivity);
		userToAddEdit.setTextSize(18f);
		layout.addView(userToAddEdit);

		Button save = new Button(contextActivity);
		save.setText("Send Request");
		save.setId(11);
		save.setOnClickListener(this);

		TableLayout.LayoutParams params = new TableLayout.LayoutParams();
		params.setMargins(0, 40, 0, 30);
		save.setLayoutParams(params);
		save.setTextColor(Color.BLACK);
		layout.addView(save);

		Button cancel = new Button(contextActivity);
		cancel.setText("Cancel");
		cancel.setId(4);
		cancel.setOnClickListener(this);
		layout.addView(cancel);
	}
	
	public void createGroupLayout(Activity contextActivity)
	{
		
		TextView header = new TextView(contextActivity);
		header.setText("Setup Flat Details");
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

		createGroupAddressEdit = new EditText(contextActivity);
		createGroupAddressEdit.setTextSize(18f);
		createGroupAddressEdit.setHeight(180);
		createGroupAddressEdit.setText("");
		createGroupAddressEdit.setGravity(Gravity.TOP | Gravity.LEFT);
		layout.addView(createGroupAddressEdit);
		
		Button createGroup = new Button(contextActivity);
		createGroup.setText("Create Group");
		createGroup.setId(10);
		createGroup.setOnClickListener(this);
		layout.addView(createGroup);

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
		else if (view instanceof TableRow)
		{
			textViewClick(view);
		}
	}

	private void buttonClick(View view)
	{

		switch (view.getId())
		{
			case 0:
				//move to account information screen
				layout.removeAllViews();
				createAccountInformation(this);
				break;	

			case 1:
				//move to modify flat details screen
				layout.removeAllViews();
				createModifyFlatDetails(this);
				break;

			case 2:
				//move to change password screen
				layout.removeAllViews();
				createChangePassword(this);
				break;

			case 3:
				//log out of the app
				layout.removeAllViews();
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
				layout.removeAllViews();
				createMainMenu(this);
				break;

			case 5:
				//save new address
				
				requestChangeAddress(modifyFlatAddressEdit.getText().toString());
				layout.removeAllViews();
				createMainMenu(this);
				break;			

			case 6:
				//leave the flat
				layout.removeAllViews();
				ActiveUser.leaveGroup(this);
				createMainMenu(this);
				break;

			case 7:
				//save new password
				if(changePassword())
				{
					layout.removeAllViews();
					createMainMenu(this);
				}
				break;
				
			case 8:
			{
				//Load add user to flat layout
				layout.removeAllViews();
				loadAddUserToFlatLayout(this);
				break;
			}
			case 9:
			{
				//Create group layout
				layout.removeAllViews();
				createGroupLayout(this);
				break;
			}
			case 10:
			{
				//Click create group
				if(createGroupAddressEdit.getText().toString().equals(""))
				{
					Toast.makeText(this, "You must enter an address!", Toast.LENGTH_LONG).show();
					return;
				}
				if(ActiveUser.createGroup(this,createGroupAddressEdit.getText().toString()))
				{
					layout.removeAllViews();
					createMainMenu(this);
				}
				else
				{
					Toast.makeText(this, "Cannot access the server.", Toast.LENGTH_LONG).show();
					return;
				}
				break;
			}
			case 11:
			{
				if(!requestAddUser(userToAddEdit.getText().toString()))
				{
					Toast.makeText(this, "Unable to request this user to add", Toast.LENGTH_LONG).show();
				}
				layout.removeAllViews();
				createMainMenu(this);
				break;
			}

			case 12:
			{
				onPause();
				Intent newSettingIntent = new Intent(this, MessageActivity.class);

				newSettingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(newSettingIntent);
				break;	
			}
			default:
				break;
		}
	}

	public boolean requestAddUser(String newUser)
	{
		String response;
		try {
			response = new HttpRequest().execute("http://group1.cloudapp.net:8080/ServerSide/newsuggestion/"+ActiveUser.getActiveUser().getUsername()+"/1/"+newUser,"post").get();
			if(response.equals("User does not exist."))
			{
				Toast.makeText(this, "User does not exist.", Toast.LENGTH_LONG).show();
				return false;
			}
			if(response.equals("User is already in a group."))
			{
				
			}
		} catch (Exception e)
		{
			return false;
		}
		return true;
	}
	
	public boolean requestChangeAddress(String newAddress)
	{
		String response;
		try {
			response = new HttpRequest().execute("http://group1.cloudapp.net:8080/ServerSide/newsuggestion/"+ActiveUser.getActiveUser().getUsername()+"/2/"+newAddress,"post").get();
		} catch (Exception e)
		{
			return false;
		}
		return true;
	}
	


	public boolean changePassword()
	{
		String currentPassword = Cryptography.computeSHAHash(currentPasswordEdit.getText().toString());
		String newPassword = Cryptography.computeSHAHash(newPasswordEdit.getText().toString());
		String confirmPassword = Cryptography.computeSHAHash(confirmPasswordEdit.getText().toString());


		if(!newPassword.equals(confirmPassword))
		{
			Toast.makeText(getApplicationContext(), "Both new passwords must match!", Toast.LENGTH_LONG).show();
			return false;
		}

		String responseCode;
		try {
			responseCode = new HttpRequest().execute("http://group1.cloudapp.net:8080/ServerSide/user/"+ActiveUser.getActiveUser().getUsername()+"/"+currentPassword+"/"+newPassword,"post").get();
			if(responseCode.equals("Incorrect username or password."))
			{
				Toast.makeText(getApplicationContext(), "The current password entered is incorrect.", Toast.LENGTH_LONG).show();
				return false;
			}
			else if(responseCode.equals("An error has occurred."))
			{
				Toast.makeText(getApplicationContext(), "An error has occurred.", Toast.LENGTH_LONG).show();
				return false;
			}
			else if(responseCode.equals("Password changed."))
			{
				Toast.makeText(getApplicationContext(), "Password changed successfully.", Toast.LENGTH_LONG).show();
				SharedPreferences.Editor editor = Settings.getSharedPreferencesEditor(this);
				editor.putString("hashedPassword", newPassword);
				editor.commit();
				return true;
			}
			else
			{
				Toast.makeText(getApplicationContext(), "An unknown error has occurred.", Toast.LENGTH_LONG).show();
				return false;
			}
		} catch (Exception e)
		{
			Toast.makeText(getApplicationContext(), "An unknown error has occurred.", Toast.LENGTH_LONG).show();
			return false;
		}
	}

	private void textViewClick(View v)
	{
		TableRow tR = (TableRow)v;
		TextView child = (TextView) tR.getChildAt(0); 

		final String username = child.getText().toString();
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		alertDialogBuilder.setTitle("Do you want to clear " + username + "'s debt?");

		alertDialogBuilder.setCancelable(false)
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog,int id) 
				{					
					try {
						String result = new HttpRequest().execute("http://group1.cloudapp.net:8080/ServerSide/money/"+ActiveUser.getActiveUser().getUsername()+"/"+username, "post").get();
						Toast.makeText(AccountActivity.this, username+"'s debt cleared", Toast.LENGTH_LONG).show();
						
						layout.removeAllViews();
						AccountActivity ac = AccountActivity.this;	
						ac.createAccountInformation(ac);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						Toast.makeText(AccountActivity.this, "Something went wrong, unable to clear debt", Toast.LENGTH_LONG).show();
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						Toast.makeText(AccountActivity.this, "Something went wrong, unable to clear debt", Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}
					
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