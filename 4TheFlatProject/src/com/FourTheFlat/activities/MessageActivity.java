package com.FourTheFlat.activities;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.FourTheFlat.ActiveUser;
import com.FourTheFlat.HttpRequest;
import com.FourTheFlat.PojoMapper;
import com.FourTheFlat.R;
import com.FourTheFlat.activities.MapActivity;
import com.FourTheFlat.activities.ShoppingListActivity;
import com.FourTheFlat.stores.Message;

public class MessageActivity extends Activity implements View.OnClickListener {

	TableLayout buttonHolder;
	TableLayout list;

	Button goBack;

	Message[] messages;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messages);

		goBack = new Button(this);
		goBack.setText("Go back!");

		messages = getMessages();
		displayMessages(this);

	}

	private void displayMessages(Activity contextActivity) {
		buttonHolder = (TableLayout) contextActivity
				.findViewById(R.id.tableLayout1);
		list = (TableLayout) contextActivity.findViewById(R.id.tableLayout2);

		goBack.setOnClickListener(this);
		buttonHolder.addView(goBack);

		LinkedList[] messageGroups = new LinkedList[3];

		TextView[] groupTitle = new TextView[3];

		groupTitle[0] = new TextView(this);
		groupTitle[0].setText("Products");
		groupTitle[0].setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		groupTitle[0].setTextColor(Color.BLACK);
		groupTitle[0].setTextSize(24f);

		groupTitle[1] = new TextView(this);
		groupTitle[1].setText("Users");
		groupTitle[1].setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		groupTitle[1].setTextColor(Color.BLACK);
		groupTitle[1].setTextSize(24f);

		groupTitle[2] = new TextView(this);
		groupTitle[2].setText("Address");
		groupTitle[2].setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		groupTitle[2].setTextColor(Color.BLACK);
		groupTitle[2].setTextSize(24f);

		for (int i = 0; i < 3; i++) {
			messageGroups[i] = new LinkedList<Message>();

			TableRow tr = new TableRow(this);
			tr.addView(groupTitle[i]);

			messageGroups[i].add(tr);
		}

		// FILL THE LIST WITH MESSAGES!!
		for (int i = 0; i < messages.length; i++) {
			TableRow tr = new TableRow(this);
			TextView tv = new TextView(this);

			// rowProduct[i] = new TableRow(contextActivity);
			tv = new TextView(contextActivity);

			tv.setText(messages[i].getMessage());
			tv.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
			tv.setTextColor(Color.BLACK);
			tv.setTextSize(20f);
			tv.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

			tr.addView(tv);
			tr.setOnClickListener(this);
			messageGroups[messages[i].getType()].add(tr);
		}

		// ADD ALL ROWS
		for (int i = 0; i < 3; i++) {
			for (Object tr : messageGroups[i])

			{
				list.addView((View) tr);

			}

		}

	}

	private Message[] getMessages() {
		String allMessages = null;

		try {
			allMessages = new HttpRequest().execute(
					"http://group1.cloudapp.net:8080/ServerSide/messages/"
							+ ActiveUser.getActiveUser().getUsername()).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String[] messageStrings = allMessages.split(Pattern.quote("}"));
		Message[] messages = new Message[messageStrings.length - 1];

		for (int i = 0; i < messages.length; i++) {
			messageStrings[i] += "}";
			try {
				Message m = (Message) PojoMapper.fromJson(messageStrings[i],
						Message.class);
				messages[i] = m;
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return messages;

	}

	@Override
	public void onClick(View v) {
		if(v instanceof Button)
		{
		  MessageActivity.this.finish();
		} else if (v instanceof TableRow) {
		TableRow tR = (TableRow) v;
		TextView child = (TextView) tR.getChildAt(0);

		final String subject = child.getText().toString();

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);

			alertDialogBuilder.setTitle("Do you want to aprove "
					+ subject
					+ "?");
		// set dialog message
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int id) {
								//POSITIVE INPUT!
								for(Message m : MessageActivity.this.messages)
								{
									if(m.getMessage().equals(subject))
									{
										String completed = null;
										try {
						Log.w("ACCOUNT_ACTIVITY","URL: "+ "http://group1.cloudapp.net:8080/ServerSide/messages/"+ActiveUser.getActiveUser().getUsername()+"/"+m.getMessageID()+"/yes");
											completed = new HttpRequest()
											.execute(
											"http://group1.cloudapp.net:8080/ServerSide/messages/"+ActiveUser.getActiveUser().getUsername()+"/"+m.getMessageID()+"/yes",
											"post").get();
										}
											catch (ExecutionException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (InterruptedException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
						Log.w("ACCOUNT_ACTIVITY","POSITIVE SEND: "+ completed);
										break;
									}
								}
													
							}
						})
				.setNegativeButton("No",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.cancel();
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	}

}
