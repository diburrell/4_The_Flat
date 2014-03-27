package com.FourTheFlat.activities;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import com.FourTheFlat.ActiveUser;
import com.FourTheFlat.Alarm;
import com.FourTheFlat.Cryptography;
import com.FourTheFlat.HttpRequest;
import com.FourTheFlat.Main;
import com.FourTheFlat.PojoMapper;
import com.FourTheFlat.R;
import com.FourTheFlat.Settings;
import com.FourTheFlat.TabCreator;
import com.FourTheFlat.stores.Group;
import com.FourTheFlat.stores.MapStore;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.text.method.TextKeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class AccountActivity extends Activity implements View.OnClickListener {

	TableLayout layout;

	EditText currentPasswordEdit;
	EditText newPasswordEdit;
	EditText confirmPasswordEdit;
	EditText createGroupAddressEdit;
	EditText userToAddEdit;
	EditText modifyFlatAddressEdit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account);
		createMainMenu(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		layout.removeAllViews();
	}

	@Override
	public void onResume() {
		super.onResume();
		createMainMenu(this);
	}

	/*
	 * Set up the account tab main display section
	 */
	private void createMainMenu(Activity contextActivity) {
		layout = (TableLayout) contextActivity.findViewById(R.id.layout);
		layout.removeAllViews();

		Button messages = new Button(contextActivity);
		messages.setText("Messages");
		messages.setId(12);
		messages.setOnClickListener(this);
		;
		layout.addView(messages);

		if (ActiveUser.getActiveUser().getGroupID() != null) {
			Button accountInfo = new Button(contextActivity);
			accountInfo.setText("Money Management");
			accountInfo.setId(0);
			accountInfo.setOnClickListener(this);
			TableLayout.LayoutParams params0 = new TableLayout.LayoutParams();
			params0.setMargins(0, 50, 0, 50);
			accountInfo.setLayoutParams(params0);
			layout.addView(accountInfo);
		}

		if (ActiveUser.getActiveUser().getGroupID() != null) {
			Button modifyFlatDetails = new Button(contextActivity);
			modifyFlatDetails.setText("Flat Details");
			modifyFlatDetails.setId(1);
			modifyFlatDetails.setOnClickListener(this);
			layout.addView(modifyFlatDetails);
		} else {
			Button createGroup = new Button(contextActivity);
			createGroup.setText("Create Group");
			createGroup.setId(9);
			TableLayout.LayoutParams params0 = new TableLayout.LayoutParams();
			params0.setMargins(0, 50, 0, 0);
			createGroup.setLayoutParams(params0);
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

	private void createAccountInformation(Activity contextActivity) {
		MapStore ms = new MapStore();
		try {
			ms = (MapStore) PojoMapper.fromJson(
					new HttpRequest().execute(
							Main.URL + "money/"
									+ ActiveUser.getActiveUser().getUsername())
							.get(), MapStore.class);

		} catch (Exception e) {
			Toast.makeText(this,
					"Unable to retrieve money information from the server",
					Toast.LENGTH_LONG).show();
			layout.removeAllViews();
			createMainMenu(this);
			return;
		}

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

		Map<String, Integer> books = ms.getMap();

		TableRow[] row = new TableRow[books.size()];
		TextView[] name = new TextView[books.size()];
		TextView[] owe = new TextView[books.size()];

		Log.w("size", Integer.toString(books.size()));
		Log.w("size", Integer.toString(books.entrySet().size()));

		if (books.entrySet().size() == 0) {
			TextView noInfo = new TextView(contextActivity);
			noInfo.setText("There is no information to display");
			noInfo.setGravity(Gravity.CENTER);
			noInfo.setTextSize(20f);
			noInfo.setTextColor(Color.BLACK);
			noInfo.setPadding(0, 30, 0, 30);
			layout.addView(noInfo);
			return;
		}

		// USER ROWS
		int i = 0;
		for (Map.Entry<String, Integer> m : books.entrySet()) {
			row[i] = new TableRow(contextActivity);

			name[i] = new TextView(contextActivity);
			name[i].setText(m.getKey());
			name[i].setTextSize(24f);
			name[i].setTextColor(Color.BLACK);
			if (i == 0)
				name[i].setPadding(0, 60, 0, 0);

			owe[i] = new TextView(contextActivity);

			if (m.getValue() < 0) {
				owe[i].setText(String.format("-£%.2f",
						((float) Math.abs(m.getValue())) / 100.0));
				owe[i].setTextColor(Color.RED);
				name[i].setOnClickListener(this);
			} else if (m.getValue() == 0.00) {
				owe[i].setText(String.format("£%.2f",
						((float) m.getValue()) / 100.0));
				owe[i].setTextColor(Color.BLACK);
			} else {
				owe[i].setText(String.format("£%.2f",
						((float) m.getValue()) / 100.0));
				owe[i].setTextColor(Color.GREEN);
			}

			owe[i].setGravity(Gravity.RIGHT);
			owe[i].setTextSize(24f);

			if (i == 0)
				owe[i].setPadding(0, 60, 0, 0);

			row[i].addView(name[i]);
			row[i].addView(owe[i]);

			layout.addView(row[i]);
			i++;
		}

		Button back = new Button(contextActivity);
		back.setText("Back to Account");
		back.setId(4);
		back.setOnClickListener(this);
		TableLayout.LayoutParams params = new TableLayout.LayoutParams();
		params.setMargins(0, 50, 0, 50);
		back.setLayoutParams(params);
		layout.addView(back);
	}

	private void createModifyFlatDetails(Activity contextActivity) {
		String response;
		Group grp;

		try {
			grp = (Group) PojoMapper.fromJson(
					new HttpRequest().execute(
							Main.URL + "group/"
									+ ActiveUser.getActiveUser().getUsername())
							.get(), Group.class);
		} catch (Exception e) {
			e.printStackTrace();
			layout.removeAllViews();
			createMainMenu(this);
			Toast.makeText(this, "Unable to get details from server.",
					Toast.LENGTH_LONG).show();
			return;
		}

		TextView header = new TextView(contextActivity);
		header.setText("Flat Details");
		header.setGravity(Gravity.CENTER);
		header.setTextSize(20f);
		header.setTextColor(Color.BLACK);
		header.setPadding(0, 30, 0, 30);
		layout.addView(header);

		View ruler = new View(contextActivity);
		ruler.setBackgroundColor(Color.WHITE);
		layout.addView(ruler, LayoutParams.FILL_PARENT, 5);

		String currentAddress = grp.getAddress();

		TextView address = new TextView(contextActivity);
		address.setText("Address: " + currentAddress);
		address.setTextSize(18f);
		address.setTextColor(Color.BLACK);
		layout.addView(address);

		/*
		 * modifyFlatAddressEdit = new EditText(contextActivity);
		 * modifyFlatAddressEdit.setTextSize(18f);
		 * modifyFlatAddressEdit.setHeight(180);
		 * modifyFlatAddressEdit.setText(currentAddress);
		 * modifyFlatAddressEdit.setGravity(Gravity.TOP | Gravity.LEFT);
		 * modifyFlatAddressEdit.setBackgroundColor(Color.rgb(248,248,248));
		 * layout.addView(modifyFlatAddressEdit);
		 */
		TextView members = new TextView(contextActivity);
		members.setText("Members");
		members.setTextSize(18f);
		members.setTextColor(Color.BLACK);
		layout.addView(members);

		Set<String> users = grp.getUsers();

		for (String u : users) {
			TextView user = new TextView(contextActivity);
			user.setText(u);
			user.setTextSize(18f);
			user.setTextColor(Color.BLACK);
			layout.addView(user);
		}

		Button requestAddressChange = new Button(contextActivity);
		requestAddressChange.setText("Request Address Change");
		requestAddressChange.setId(5);
		requestAddressChange.setOnClickListener(this);
		requestAddressChange.setTextColor(Color.BLACK);
		TableLayout.LayoutParams params = new TableLayout.LayoutParams();
		params.setMargins(0, 50, 0, 0);
		requestAddressChange.setLayoutParams(params);
		layout.addView(requestAddressChange);

		Button addUser = new Button(contextActivity);
		addUser.setText("Add User to Flat");
		addUser.setId(8);
		addUser.setOnClickListener(this);
		addUser.setTextColor(Color.BLACK);
		layout.addView(addUser);

		Button leave = new Button(contextActivity);
		leave.setText("Leave Flat");
		leave.setId(6);
		leave.setOnClickListener(this);
		leave.setTextColor(Color.BLACK);
		layout.addView(leave);
	}

	private void createChangePassword(Activity contextActivity) {
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
		currentPasswordEdit.setHint("Current Password");
		currentPasswordEdit.setTextSize(18f);
		currentPasswordEdit.setBackgroundColor(Color.rgb(248, 248, 248));
		currentPasswordEdit.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		currentPasswordEdit.setTypeface(Typeface.MONOSPACE);
		layout.addView(currentPasswordEdit);

		TextView newPassword = new TextView(contextActivity);
		newPassword.setText("New Password:");
		newPassword.setTextSize(18f);
		newPassword.setTextColor(Color.BLACK);
		newPassword.setPadding(0, 50, 0, 0);
		layout.addView(newPassword);

		newPasswordEdit = new EditText(contextActivity);
		newPasswordEdit.setHint("New Password");
		newPasswordEdit.setTextSize(18f);
		newPasswordEdit.setBackgroundColor(Color.rgb(248, 248, 248));
		newPasswordEdit.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		newPasswordEdit.setTypeface(Typeface.MONOSPACE);
		layout.addView(newPasswordEdit);

		TextView confirmPassword = new TextView(contextActivity);
		confirmPassword.setText("Confirm Password:");
		confirmPassword.setTextSize(18f);
		confirmPassword.setTextColor(Color.BLACK);
		confirmPassword.setPadding(0, 50, 0, 0);
		layout.addView(confirmPassword);

		confirmPasswordEdit = new EditText(contextActivity);
		confirmPasswordEdit.setHint("Confirm Password");
		confirmPasswordEdit.setTextSize(18f);
		confirmPasswordEdit.setBackgroundColor(Color.rgb(248, 248, 248));
		confirmPasswordEdit.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		confirmPasswordEdit.setTypeface(Typeface.MONOSPACE);
		layout.addView(confirmPasswordEdit);

		Button save = new Button(contextActivity);
		save.setText("Save");
		save.setId(7);
		save.setOnClickListener(this);

		TableLayout.LayoutParams params = new TableLayout.LayoutParams();
		params.setMargins(0, 40, 0, 0);
		save.setLayoutParams(params);
		save.setTextColor(Color.BLACK);
		layout.addView(save);

		Button cancel = new Button(contextActivity);
		cancel.setText("Cancel");
		cancel.setId(4);
		cancel.setOnClickListener(this);
		layout.addView(cancel);
	}

	public void loadAddUserToFlatLayout(Activity contextActivity) {
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

	public void createGroupLayout(Activity contextActivity) {
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

	public void onClick(View view) {
		if (view instanceof Button) {
			buttonClick(view);
		} else if (view instanceof TextView) {
			textViewClick(view);
		}
	}

	private void buttonClick(View view) {
		switch (view.getId()) {
		case 0:
			// move to account information screen
			layout.removeAllViews();
			createAccountInformation(this);
			break;

		case 1:
			// move to modify flat details screen
			layout.removeAllViews();
			createModifyFlatDetails(this);
			break;

		case 2:
			// move to change password screen
			layout.removeAllViews();
			createChangePassword(this);
			break;

		case 3:
			// log out of the app
			layout.removeAllViews();
			SharedPreferences.Editor editor = Settings
					.getSharedPreferencesEditor(getApplicationContext());
			editor.putBoolean("hasLoggedIn", false);
			editor.putString("user", "");
			// Commit the edits!
			editor.commit();
			Intent logout = new Intent(getApplicationContext(),
					com.FourTheFlat.activities.LoginActivity.class);
			startActivity(logout);
			finish();
			break;

		case 4:
			// back/cancel to main menu
			layout.removeAllViews();
			createMainMenu(this);
			break;

		// SUGGEST FLAT NAME CHANGE
		case 5:
			AlertDialog.Builder addressAlert = new AlertDialog.Builder(this);
			addressAlert.setTitle("Enter new flat address");
			// Set an EditText view to get user input
			final EditText address = new EditText(this);

			addressAlert.setView(address);

			// Digits only & use numeric soft-keyboard.
			address.setKeyListener(TextKeyListener.getInstance());
			addressAlert.setPositiveButton("Confirm",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							Log.w("NAME CHANGE", "SUGGESTED: "
									+ address.getText().toString());
							requestChangeAddress(address.getText().toString());
							Toast.makeText(AccountActivity.this,
									"Suggestion sent to other users",
									Toast.LENGTH_LONG).show();
						}
					});
			addressAlert.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// do nothing
						}
					});
			addressAlert.show();
			break;
		case 6:
			// leave the flat
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);

			alertDialogBuilder
					.setTitle("Are you sure you want to leave the group?");

			alertDialogBuilder
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									layout.removeAllViews();
									ActiveUser.leaveGroup(AccountActivity.this);
									AccountActivity.this
											.createMainMenu(AccountActivity.this);
									Toast.makeText(AccountActivity.this,
											"You have left the group!",
											Toast.LENGTH_LONG).show();
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});

			AlertDialog alertDialog = alertDialogBuilder.create();

			alertDialog.show();

			break;

		case 7:
			// save new password
			if (changePassword()) {
				layout.removeAllViews();
				createMainMenu(this);
			}
			break;

		case 8:
			// Load add user to flat layout
			AlertDialog.Builder userAlert = new AlertDialog.Builder(this);
			userAlert.setTitle("Enter new user's name");
			// Set an EditText view to get user input
			final EditText user = new EditText(this);

			userAlert.setView(user);

			// Digits only & use numeric soft-keyboard.
			user.setKeyListener(TextKeyListener.getInstance());
			userAlert.setPositiveButton("Confirm",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							Log.w("NAME CHANGE", "SUGGESTED: "
									+ user.getText().toString());
							requestAddUser(user.getText().toString());
							Toast.makeText(AccountActivity.this,
									"Suggestion sent to other users",
									Toast.LENGTH_LONG).show();
						}
					});
			userAlert.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// do nothing
						}
					});
			userAlert.show();
			break;

		case 9:
			// Load add user to flat layout
			AlertDialog.Builder groupAlert = new AlertDialog.Builder(this);
			groupAlert.setTitle("Enter the new group's address");
			// Set an EditText view to get user input
			final EditText newGroup = new EditText(this);

			groupAlert.setView(newGroup);

			// Digits only & use numeric soft-keyboard.
			newGroup.setKeyListener(TextKeyListener.getInstance());
			groupAlert.setPositiveButton("Confirm",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							Log.w("NAME CHANGE", "SUGGESTED: "
									+ newGroup.getText().toString());
							if (newGroup.getText().toString().equals("")) {
								Toast.makeText(AccountActivity.this,
										"You must enter an address!",
										Toast.LENGTH_LONG).show();
								return;
							}
							if (ActiveUser.createGroup(AccountActivity.this,
									newGroup.getText().toString())) {
								layout.removeAllViews();
								createModifyFlatDetails(AccountActivity.this);
							} else {
								Toast.makeText(AccountActivity.this,
										"Cannot access the server.",
										Toast.LENGTH_LONG).show();
								return;
							}
						}
					});
			groupAlert.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// do nothing
						}
					});
			groupAlert.show();

			break;

		case 10:
			// Click create group
			if (createGroupAddressEdit.getText().toString().equals("")) {
				Toast.makeText(this, "You must enter an address!",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (ActiveUser.createGroup(this, createGroupAddressEdit.getText()
					.toString())) {
				layout.removeAllViews();
				createMainMenu(this);
			} else {
				Toast.makeText(this, "Cannot access the server.",
						Toast.LENGTH_LONG).show();
				return;
			}
			break;

		case 11:
			if (userToAddEdit.getText().toString().equals("")) {
				Toast.makeText(this, "You must enter the name of a user!",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (!requestAddUser(userToAddEdit.getText().toString())) {
				return;
			}
			layout.removeAllViews();
			createMainMenu(this);
			break;

		case 12:
			onPause();
			Intent newSettingIntent = new Intent(this, MessageActivity.class);
			newSettingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(newSettingIntent);
			break;

		default:
			break;
		}
	}

	public boolean requestAddUser(String newUser) {
		String response;
		try {
			response = new HttpRequest().execute(
					Main.URL + "newsuggestion/"
							+ ActiveUser.getActiveUser().getUsername() + "/1/"
							+ newUser, "post").get();

			if (response.equals("User does not exist.")) {
				Toast.makeText(this, "User does not exist.", Toast.LENGTH_LONG)
						.show();
				return false;
			} else if (response.equals("User is already in a group.")) {
				Toast.makeText(this, "User is already in a group.",
						Toast.LENGTH_LONG).show();
				return false;
			} else if (response.equals("User added.")) {
				Toast.makeText(this, "User has been added to the group.",
						Toast.LENGTH_LONG).show();
				return true;
			} else if (response.equals("User request already pending.")) {
				Toast.makeText(this,
						"A request to add this user is already pending.",
						Toast.LENGTH_LONG).show();
				return false;
			} else if (response.equals("User suggested.")) {
				Toast.makeText(this, "User has been suggested.",
						Toast.LENGTH_LONG).show();
				return true;
			}
		} catch (Exception e) {
			Toast.makeText(this, "You do not have an active connection.",
					Toast.LENGTH_LONG).show();
			return false;
		}

		Toast.makeText(this, "Something went wrong.", Toast.LENGTH_LONG).show();
		return true;
	}

	public boolean requestChangeAddress(String newAddress) {
		String response;

		try {
Log.w("NAME CHANGE", "newsuggestion/"
		+ ActiveUser.getActiveUser().getUsername() + "/2/"
		+ newAddress);
			

response = new HttpRequest().execute(
					Main.URL + "newsuggestion/"
							+ ActiveUser.getActiveUser().getUsername() + "/2/"
							+ newAddress, "post").get();

			if (response.equals("Address changed.")) {
				return true;
			} else if (response.equals("Address change already pending.")) {
				Toast.makeText(this, "An address change is already pending.",
						Toast.LENGTH_SHORT).show();
				return false;
			}
		} catch (Exception e) {
			Toast.makeText(this, "Some thing went wrong!", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		return true;
	}

	public boolean changePassword() {
		String currentPassword = Cryptography
				.computeSHAHash(currentPasswordEdit.getText().toString());
		String newPassword = Cryptography.computeSHAHash(newPasswordEdit
				.getText().toString());
		String confirmPassword = Cryptography
				.computeSHAHash(confirmPasswordEdit.getText().toString());

		if (!newPassword.equals(confirmPassword)) {
			Toast.makeText(getApplicationContext(),
					"Both new passwords must match!", Toast.LENGTH_LONG).show();
			return false;
		}

		String responseCode;
		try {
			responseCode = new HttpRequest().execute(
					Main.URL + "user/"
							+ ActiveUser.getActiveUser().getUsername() + "/"
							+ currentPassword + "/" + newPassword, "post")
					.get();

			if (responseCode.equals("Incorrect username or password.")) {
				Toast.makeText(getApplicationContext(),
						"The current password entered is incorrect.",
						Toast.LENGTH_LONG).show();
				return false;
			} else if (responseCode.equals("An error has occurred.")) {
				Toast.makeText(getApplicationContext(),
						"An error has occurred.", Toast.LENGTH_LONG).show();
				return false;
			} else if (responseCode.equals("Password changed.")) {
				Toast.makeText(getApplicationContext(),
						"Password changed successfully.", Toast.LENGTH_LONG)
						.show();
				SharedPreferences.Editor editor = Settings
						.getSharedPreferencesEditor(this);
				editor.putString("hashedPassword", newPassword);
				editor.commit();
				AccountActivity aA = AccountActivity.this;
				Alarm.cancelRepeatingTimer(aA);
				return true;
			} else {
				Toast.makeText(getApplicationContext(),
						"An unknown error has occurred.", Toast.LENGTH_LONG)
						.show();
				return false;
			}
		} catch (Exception e) {
			Toast.makeText(
					getApplicationContext(),
					"Could not change password.  You do not have an active internet connection.",
					Toast.LENGTH_LONG).show();
			return false;
		}
	}

	private void textViewClick(View view) {
		final String username = ((TextView) view).getText().toString();

		if (ActiveUser.isGroupMemberShopping()) {
			Toast.makeText(
					getApplicationContext(),
					"You can't do that! Someone in your flat is currently shopping!",
					Toast.LENGTH_SHORT).show();
		} else {

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);

			alertDialogBuilder.setTitle("Do you want to clear " + username
					+ "'s debt?");

			alertDialogBuilder
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									try {
										String result = new HttpRequest()
												.execute(
														Main.URL
																+ "money/"
																+ ActiveUser
																		.getActiveUser()
																		.getUsername()
																+ "/"
																+ username,
														"post").get();
										Toast.makeText(AccountActivity.this,
												username + "'s debt cleared",
												Toast.LENGTH_LONG).show();
										layout.removeAllViews();
										AccountActivity ac = AccountActivity.this;
										ac.createAccountInformation(ac);
									} catch (InterruptedException e) {
										Toast.makeText(
												AccountActivity.this,
												"Something went wrong, unable to clear debt",
												Toast.LENGTH_LONG).show();
										e.printStackTrace();
									} catch (ExecutionException e) {
										Toast.makeText(
												AccountActivity.this,
												"Something went wrong, unable to clear debt",
												Toast.LENGTH_LONG).show();
										e.printStackTrace();
									}
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});

			AlertDialog alertDialog = alertDialogBuilder.create();

			alertDialog.show();
		}
	}

	@Override
	public void onBackPressed() {
		layout.removeAllViews();
		createMainMenu(this);
	}
}