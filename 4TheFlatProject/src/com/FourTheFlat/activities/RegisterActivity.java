package com.FourTheFlat.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.FourTheFlat.activities.LoginActivity;
import com.FourTheFlat.stores.User;
import com.FourTheFlat.ActiveUser;
import com.FourTheFlat.Alarm;
import com.FourTheFlat.ConnectionManager;
import com.FourTheFlat.Cryptography;
import com.FourTheFlat.HttpRequest;
import com.FourTheFlat.R;
import com.FourTheFlat.Settings;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class RegisterActivity extends Activity 
{
    private static String KEY_SUCCESS = "success";
    private static String KEY_RESPONSE = "response";

    TableLayout layout;
    
    EditText inputUsername;
    EditText inputPassword;
    EditText inputConfirm;
    TextView error;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        getActionBar().setTitle("4TheFlat: Register"); 
        
        setContentView(R.layout.register);
        
        layout = (TableLayout)this.findViewById(R.id.layout);
        
        TableRow[] rows = new TableRow[6];  
        for (int i=0; i<rows.length; i++)
        {
        	rows[i] = new TableRow(this);
        }
       
        TextView username = new TextView(this);
        username.setText("Username: ");
        username.setTextSize(18f);
        rows[0].addView(username);
        
        inputUsername = new EditText(this);
        inputUsername.setHint("Username");
        inputUsername.setTextSize(15f);
        inputUsername.setBackgroundColor(Color.rgb(248,248,248));
        inputUsername.setTypeface(Typeface.MONOSPACE);
        rows[0].addView(inputUsername);
        
        rows[0].setPadding(20, 60, 20, 0);
        
        TextView password = new TextView(this);
        password.setText("Password: ");
        password.setTextSize(18f);
        rows[1].addView(password);
        
	    inputPassword = new EditText(this);
	    inputPassword.setHint("Password");
	    inputPassword.setTextSize(15f);
	    inputPassword.setBackgroundColor(Color.rgb(248,248,248));
	    inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
	    inputPassword.setTypeface(Typeface.MONOSPACE);
        rows[1].addView(inputPassword);
        
        rows[1].setPadding(20, 60, 20, 60);
        
        TextView confirm = new TextView(this);
        confirm.setText("Confirm Password: ");
        confirm.setTextSize(18f);
        rows[2].addView(confirm);
        
	    inputConfirm = new EditText(this);
	    inputConfirm.setHint("Confirm Password");
	    inputConfirm.setTextSize(15f);
	    inputConfirm.setBackgroundColor(Color.rgb(248,248,248));
	    inputConfirm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
	    inputConfirm.setTypeface(Typeface.MONOSPACE);
        rows[2].addView(inputConfirm);
        
        rows[2].setPadding(20, 0, 20, 60);
        
        Button register = new Button(this);
        register.setText("Register");
        register.setTextSize(22f);
        register.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View view) 
            {
                if ((!inputUsername.getText().toString().equals("")) && (!inputPassword.getText().toString().equals("")))
                {
                    if ( inputPassword.getText().toString().length() > 5)
                    {
                    	if(inputPassword.getText().toString().equals(inputConfirm.getText().toString()))
                    	{
                    		NetAsync(view);
                    	}
                    	else
                    	{
                    		Toast.makeText(getApplicationContext(), "Password and password confirmation do not match", Toast.LENGTH_SHORT).show();
                    	}

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Password should be minimum 6 characters", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "One or more fields are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        TableRow.LayoutParams registerParams = new TableRow.LayoutParams();
        registerParams.span = 2;
        registerParams.setMargins(30, 0, 30, 0);
        rows[3].addView(register, registerParams);
        
        Button login = new Button(this);
        login.setText("Back to Login");
        login.setTextSize(22f);
        login.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View view) 
            {
                Intent myIntent = new Intent(view.getContext(), LoginActivity.class);
                startActivityForResult(myIntent, 0);
                finish();
            }
        });
        TableRow.LayoutParams loginParams = new TableRow.LayoutParams();
        loginParams.span = 2;
        loginParams.setMargins(30, 30, 30, 0);
		rows[4].addView(login, loginParams);
        
		error = new TextView(this);    
        rows[5].addView(error);   
        
        for (int i=0; i<rows.length; i++)
        {
        	layout.addView(rows[i]);
        }
    }
    
    private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            nDialog = new ProgressDialog(RegisterActivity.this);
            nDialog.setMessage("Loading..");
            nDialog.setTitle("Checking Network");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args)
        {
        	return ConnectionManager.checkInternetConnection(getApplicationContext());
        }
        
        @Override
        protected void onPostExecute(Boolean th)
        {
            if(th == true)
            {
                nDialog.dismiss();
                new ProcessRegister().execute();
            }
            else
            {
                nDialog.dismiss();
                error.setText("Error in Network Connection");
            }
        }
    }

    private class ProcessRegister extends AsyncTask<String, String, JSONObject> 
    {
        private ProgressDialog pDialog;

        String password = "";
        String username = "";
        @Override
        protected void onPreExecute() 
        {
            super.onPreExecute();

            username= inputUsername.getText().toString();
            password = inputPassword.getText().toString();
            
            password = Cryptography.computeSHAHash(password);                
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Registering ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) 
        {
        	String httpResponse = "";
        	JSONObject json = new JSONObject();
        	
        	User user = new User();
			try 
			{
				httpResponse = new HttpRequest().execute("http://group1.cloudapp.net:8080/ServerSide/user/"+username+"/"+password+"/", "put").get();
				
				try
				{
	        		json.put(KEY_SUCCESS, "0");
	        		json.put(KEY_RESPONSE, httpResponse);
	        	}
	        	catch(JSONException e)
	        	{     
	        		e.printStackTrace();
	        	}
			} 
			catch (InterruptedException e1) 
			{
				e1.printStackTrace();
			} 
			catch (ExecutionException e1) 
			{
				e1.printStackTrace();
			}
			if(httpResponse.contains("groupID"))
			{
				try{
						user.setUsername(username);
						SharedPreferences.Editor editor = Settings.getSharedPreferencesEditor(getApplicationContext());
						editor.putString("user", httpResponse);
						ActiveUser.initialise(getApplicationContext());
						editor.putBoolean("hasLoggedIn", true);
						editor.putString("hashedPassword", Cryptography.computeSHAHash(inputPassword.getText().toString()));
						editor.commit();
<<<<<<< HEAD
						RegisterActivity rA = RegisterActivity.this;
						Alarm.startRepeatingTimer(rA);
						try{
=======
						try
						{
>>>>>>> origin/refactor
			            	json.put(KEY_SUCCESS, "1");
			            }
			            catch(JSONException e){            	
			            }
				}
				catch(Exception e)
				{
					e.printStackTrace();
				} 
			}
            return json;
        }
       @Override
        protected void onPostExecute(JSONObject json) 
       {
    	   //Checks for success message
            try 
            {
                if (json.getString(KEY_SUCCESS) == "1") 
                {
                    error.setText("Successfully Registered");
                    Intent registered = new Intent(getApplicationContext(), RegisteredActivity.class);
                    registered.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ActiveUser.initialise(getApplicationContext());
                    pDialog.dismiss();
                    startActivity(registered);
                    finish();                        
                }
                else if (json.getString(KEY_RESPONSE).equals("Username already registered."))
                {
                    pDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "That username already exists, please try a new one!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    pDialog.dismiss();
                    error.setText("Error occured in registration");
                }

            } 
            catch (JSONException e) 
            {
                e.printStackTrace();
            }
        }
    }
    
    public void NetAsync(View view)
    {
        new NetCheck().execute();
    }  
}