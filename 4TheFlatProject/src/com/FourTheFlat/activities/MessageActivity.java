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
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.FourTheFlat.ActiveUser;
import com.FourTheFlat.HttpRequest;
import com.FourTheFlat.Main;
import com.FourTheFlat.PojoMapper;
import com.FourTheFlat.R;
import com.FourTheFlat.stores.Message;

public class MessageActivity extends Activity implements View.OnClickListener 
{
	TableLayout upperLayout;
	TableLayout lowerLayout;

	Message[] messages;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messages);
		messages = getMessages();
		loadMessageList(this);
	}
	
	@Override
	protected void onRestart() 
	{
		super.onRestart();
		loadMessageList(this);
	}

	public void loadMessageList(Activity contextActivity)
	{	
		lowerLayout = (TableLayout) contextActivity.findViewById(R.id.tableLayout2);
		lowerLayout.removeAllViews();

		if (messages != null)
		{
			displayMessages(this);
			return;
		}
		else
		{
			noConnectionDisplay(this);
			return;
		}
	}

	private void noConnectionDisplay(Activity contextActivity)
	{
		lowerLayout = (TableLayout) contextActivity.findViewById(R.id.tableLayout2);
		lowerLayout.removeAllViews();

		TextView noMessages = new TextView(this);
		noMessages.setText("You don't have an internet connection");
		noMessages.setGravity(Gravity.CENTER);
		noMessages.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		noMessages.setTextColor(Color.BLACK);
		noMessages.setTextSize(24f);
		lowerLayout.addView((View)noMessages);
	}

	private void displayMessages(Activity contextActivity) 
	{		
		lowerLayout = (TableLayout) contextActivity.findViewById(R.id.tableLayout2);
		lowerLayout.removeAllViews();

		TextView[] groupTitle = new TextView[4];

		groupTitle[0] = new TextView(this);
		SpannableString span0 = new SpannableString("Product Requests");
		span0.setSpan(new UnderlineSpan(), 0, span0.length(), 0);
		groupTitle[0].setText(span0);
		groupTitle[0].setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		groupTitle[0].setTextColor(Color.BLACK);
		groupTitle[0].setTextSize(30f);

		groupTitle[1] = new TextView(this);
		SpannableString span1 = new SpannableString("New User Requests");
		span1.setSpan(new UnderlineSpan(), 0, span1.length(), 0);
		groupTitle[1].setText(span1);
		groupTitle[1].setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		groupTitle[1].setTextColor(Color.BLACK);
		groupTitle[1].setTextSize(30f);

		groupTitle[2] = new TextView(this);
		SpannableString span2 = new SpannableString("Address Change Requests");
		span2.setSpan(new UnderlineSpan(), 0, span2.length(), 0);
		groupTitle[2].setText(span2);
		groupTitle[2].setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		groupTitle[2].setTextColor(Color.BLACK);
		groupTitle[2].setTextSize(30f);

		groupTitle[3] = new TextView(this);
		SpannableString span3 = new SpannableString("Suggestion Outcomes");
		span3.setSpan(new UnderlineSpan(), 0, span3.length(), 0);
		groupTitle[3].setText(span3);
		groupTitle[3].setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		groupTitle[3].setTextColor(Color.BLACK);
		groupTitle[3].setTextSize(30f);

		LinkedList[] messageGroups = new LinkedList[4];	
		for (int i = 0; i < 4; i++) 
		{
			messageGroups[i] = new LinkedList<Message>();

			TableRow tr = new TableRow(this);
			tr.setPadding(10, 30, 10, 0);
			tr.addView(groupTitle[i]);

			messageGroups[i].add(tr);
		}

		//fill the list with messages
		for (int i = 0; i < messages.length; i++) 
		{
			TableRow tr = new TableRow(this);
			TextView tv = new TextView(this);

			tv = new TextView(contextActivity);

			tv.setText(messages[i].getMessage());
			tv.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
			tv.setTextColor(Color.BLACK);
			tv.setTextSize(20f);
			tv.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
			tv.setOnClickListener(this);

			tr.setPadding(10, 0, 10, 0);
			tr.addView(tv);
			messageGroups[messages[i].getType()].add(tr);
		}

		boolean newMessages = false;

		//all all rows
		for (int i = 0; i < 4; i++) 
		{
			if (messageGroups[i].size() > 1)
			{
				for (Object tr : messageGroups[i])
				{
					lowerLayout.addView((View) tr);
					newMessages = true;
				}
			}
		}
		
		if (!newMessages)
		{
			TextView noMessages = new TextView(this);
			noMessages.setText("No new messages");
			noMessages.setGravity(Gravity.CENTER);
			noMessages.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
			noMessages.setTextColor(Color.BLACK);
			noMessages.setTextSize(24f);
			lowerLayout.addView((View)noMessages);
		}
	}

	private Message[] getMessages() 
	{
		String allMessages = null;

		try 
		{
			allMessages = new HttpRequest().execute(Main.URL + "messages/" + ActiveUser.getActiveUser().getUsername()).get();
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
			return null;
		} 
		catch (ExecutionException e) 
		{
			e.printStackTrace();
			return null;
		}
		
		if (allMessages == null)
		{
			return null;
		}
		
		String[] messageStrings = allMessages.split(Pattern.quote("}"));
		Message[] messages = new Message[messageStrings.length - 1];

		for (int i = 0; i < messages.length; i++) 
		{
			messageStrings[i] += "}";
			
			try 
			{
				Message m = (Message) PojoMapper.fromJson(messageStrings[i], Message.class);
				messages[i] = m;
			} 
			catch (JsonMappingException e) 
			{
				e.printStackTrace();
			} 
			catch (JsonParseException e) 
			{
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}

		return messages;
	}

	@Override
	public void onClick(View view) 
	{
		if(view instanceof Button)
		{
			MessageActivity.this.finish();
		} 
		else if (view instanceof TextView) 
		{
			final String subject = ((TextView)view).getText().toString();
	
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
	
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	
			if (type == 0)
			{
				alertDialogBuilder.setTitle("Do you want " + subject + " to be an approved product?");
			}
			else if (type == 1)
			{
				alertDialogBuilder.setTitle("Do you want " + subject + " to be added to the group?");
			}
			else if (type == 2)
			{
				alertDialogBuilder.setTitle("Do you want " + subject + " to be the group address?");
			}
			else if(type == 3)
			{
				alertDialogBuilder.setTitle(thisMessage.getMessage());
			}
	
			if(type != 3)
			{
				alertDialogBuilder.setCancelable(false)
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() 
					{
						public void onClick(DialogInterface dialog,	int id) 
						{
							String completed = null;
							
							try 
							{
								Log.w("ACCOUNT_ACTIVITY","URL: "+ Main.URL + "messages/"+ActiveUser.getActiveUser().getUsername()+"/"+thisMessage.getMessageID()+"/yes");
								
								completed = new HttpRequest().execute(Main.URL + "messages/"+ActiveUser.getActiveUser().getUsername()+"/"+thisMessage.getMessageID()+"/yes","post").get();
							}
							catch (ExecutionException e) 
							{
								e.printStackTrace();
							} 
							catch (InterruptedException e) 
							{
								e.printStackTrace();
							}
							
							Log.w("ACCOUNT_ACTIVITY","POSITIVE SEND: "+ completed);
						}
					})
					.setNegativeButton("No", new DialogInterface.OnClickListener() 
					{
						public void onClick(DialogInterface dialog,	int id) 
						{
							String completed = null;

							try 
							{
								Log.w("ACCOUNT_ACTIVITY","URL: "+ Main.URL + "messages/"+ActiveUser.getActiveUser().getUsername()+"/"+thisMessage.getMessageID()+"/no");
								
								completed = new HttpRequest().execute(Main.URL + "messages/"+ActiveUser.getActiveUser().getUsername()+"/"+thisMessage.getMessageID()+"/no", "post").get();
							}
							catch (ExecutionException e) 
							{
								e.printStackTrace();
							} 
							catch (InterruptedException e) 
							{
								e.printStackTrace();
							}
							
							Log.w("ACCOUNT_ACTIVITY","NEGITIVE SEND: "+ completed);
						}
					});
			}
			else
			{
				alertDialogBuilder.setCancelable(false)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() 
					{
						public void onClick(DialogInterface dialog,	int id) 
						{
							String completed = null;
							
							try 
							{
								Log.w("ACCOUNT_ACTIVITY","URL: "+ Main.URL + "messages/"+ActiveUser.getActiveUser().getUsername()+"/"+thisMessage.getMessageID());
								
								completed = new HttpRequest().execute(Main.URL + "messages/"+ActiveUser.getActiveUser().getUsername()+"/"+thisMessage.getMessageID(),"delete").get();
							}
							catch (ExecutionException e) 
							{
								e.printStackTrace();
							} 
							catch (InterruptedException e) 
							{
								e.printStackTrace();
							}
							
							Log.w("ACCOUNT_ACTIVITY","POSITIVE SEND: "+ completed);
	
						}
					});	
			}

			AlertDialog alertDialog = alertDialogBuilder.create();
	
			alertDialog.show();
		}
		
		onRestart();
	}
}