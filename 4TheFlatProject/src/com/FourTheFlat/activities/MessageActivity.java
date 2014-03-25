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
	public void onCreate(Bundle savedInstanceState) 
	{
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

		LinkedList[] messageGroups = new LinkedList[4];

		TextView[] groupTitle = new TextView[4];

		groupTitle[0] = new TextView(this);
		groupTitle[0].setText("Product Requests");
		groupTitle[0].setGravity(Gravity.CENTER);
		groupTitle[0].setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		groupTitle[0].setTextColor(Color.BLACK);
		groupTitle[0].setTextSize(30f);

		groupTitle[1] = new TextView(this);
		groupTitle[1].setText("New User Requests");
		groupTitle[1].setGravity(Gravity.CENTER);
		groupTitle[1].setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		groupTitle[1].setTextColor(Color.BLACK);
		groupTitle[1].setTextSize(30f);

		groupTitle[2] = new TextView(this);
		groupTitle[2].setText("Address Change Requests");
		groupTitle[2].setGravity(Gravity.CENTER);
		groupTitle[2].setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		groupTitle[2].setTextColor(Color.BLACK);
		groupTitle[2].setTextSize(30f);


		groupTitle[3] = new TextView(this);
		groupTitle[3].setText("Suggestion Outcomes");
		groupTitle[3].setGravity(Gravity.CENTER);
		groupTitle[3].setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		groupTitle[3].setTextColor(Color.BLACK);
		groupTitle[3].setTextSize(30f);

		for (int i = 0; i < 4; i++) {
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

		boolean newMessages = false;

		// ADD ALL ROWS
		for (int i = 0; i < 4; i++) {
			if (messageGroups[i].size() > 1)
			{
				for (Object tr : messageGroups[i])
				{
					list.addView((View) tr);
					newMessages = true;
				}
			}

		}
		if(!newMessages)
		{
			TextView noMessages = new TextView(this);
			noMessages.setText("No new messages!");
			noMessages.setGravity(Gravity.CENTER);
			noMessages.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
			noMessages.setTextColor(Color.BLACK);
			noMessages.setTextSize(24f);
			list.addView((View)noMessages);

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


	public void update() {
		list = (TableLayout) this.findViewById(R.id.tableLayout2);
		displayMessages(this);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		list.removeAllViews();
		buttonHolder.removeAllViews();
		update();
		Log.w("Resume", "Activity Resumed");

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

		Message findMessage = null;

		Message[] newList = new Message[MessageActivity.this.messages.length - 1];

		int i = 0;
		for(Message m : MessageActivity.this.messages)
		{
			if(m.getMessage().equals(subject))
			{
			  findMessage = m; 
			}
			else
			{
				newList[i] = m;
				i++;
			}
		}

		MessageActivity.this.messages = newList;

		//MESSAGE BEING REFERED TO IN TEXT
		final Message thisMessage = findMessage;
		final int type = thisMessage.getType();

		//CREATE DIALOGUE BOX
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);

		if(type == 0)
		{
			alertDialogBuilder.setTitle("Do you want "
					+ subject
					+ " to be an approved product?");
		}
		else if (type == 1)
		{
			alertDialogBuilder.setTitle("Do you want "
					+ subject
					+ " to be added to the group?");
		}
		else if(type == 2)
		{
			alertDialogBuilder.setTitle("Do you want "
					+ subject
					+ " to be the group address?");
		}
		else if(type == 3)
		{
			alertDialogBuilder.setTitle(thisMessage.getMessage());
		}

		if(type != 3)
		{
		// set dialog message
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int id) {
								//POSITIVE INPUT!

										String completed = null;
										try {
						Log.w("ACCOUNT_ACTIVITY","URL: "+ "http://group1.cloudapp.net:8080/ServerSide/messages/"+ActiveUser.getActiveUser().getUsername()+"/"+thisMessage.getMessageID()+"/yes");
											completed = new HttpRequest()
											.execute(
											"http://group1.cloudapp.net:8080/ServerSide/messages/"+ActiveUser.getActiveUser().getUsername()+"/"+thisMessage.getMessageID()+"/yes",
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

							}
						})
				.setNegativeButton("No",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int id) {
								//NEGITIVE INPUT!
										String completed = null;
										try {
						Log.w("ACCOUNT_ACTIVITY","URL: "+ "http://group1.cloudapp.net:8080/ServerSide/messages/"+ActiveUser.getActiveUser().getUsername()+"/"+thisMessage.getMessageID()+"/no");
											completed = new HttpRequest()
											.execute(
											"http://group1.cloudapp.net:8080/ServerSide/messages/"+ActiveUser.getActiveUser().getUsername()+"/"+thisMessage.getMessageID()+"/no",
											"post").get();
										}
											catch (ExecutionException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (InterruptedException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
						Log.w("ACCOUNT_ACTIVITY","NEGITIVE SEND: "+ completed);
							}
						});
		}
		else
		{
			// set dialog message
			alertDialogBuilder
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int id) {
											String completed = null;
											try {
							Log.w("ACCOUNT_ACTIVITY","URL: "+ "http://group1.cloudapp.net:8080/ServerSide/messages/"+ActiveUser.getActiveUser().getUsername()+"/"+thisMessage.getMessageID());
												completed = new HttpRequest()
												.execute(
												"http://group1.cloudapp.net:8080/ServerSide/messages/"+ActiveUser.getActiveUser().getUsername()+"/"+thisMessage.getMessageID(),
												"delete").get();
											}
												catch (ExecutionException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											} catch (InterruptedException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
							Log.w("ACCOUNT_ACTIVITY","POSITIVE SEND: "+ completed);

								}
							});	
		}

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}
		onRestart();
	}

}