package com.FourTheFlat.activities;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.FourTheFlat.ActiveUser;
import com.FourTheFlat.Alarm;
import com.FourTheFlat.ConnectionManager;
import com.FourTheFlat.Cryptography;
import com.FourTheFlat.HttpRequest;
import com.FourTheFlat.PojoMapper;
import com.FourTheFlat.R;
import com.FourTheFlat.Settings;
import com.FourTheFlat.stores.User;

import java.util.concurrent.ExecutionException;

public class LoginActivity extends Activity 
{
	TableLayout layout;
	
    EditText inputUsername;
    EditText inputPassword;
    TextView error;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        //Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
        boolean hasLoggedIn = Settings.getSharedPreferences(this.getApplicationContext()).getBoolean("hasLoggedIn", false);
        ActiveUser.initialise(getApplicationContext());
        
        if(hasLoggedIn && ActiveUser.getActiveUser() != null)
        {
        	Intent registered = new Intent(getApplicationContext(), com.FourTheFlat.TabCreator.class);
            registered.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(registered);
            finish();
        }
        
        getActionBar().setTitle("4TheFlat: Login"); 
        
        setContentView(R.layout.login);
        
        layout = (TableLayout)this.findViewById(R.id.layout);
        
        TableRow[] rows = new TableRow[5];  
        for (int i=0; i<rows.length; i++)
        {
        	rows[i] = new TableRow(this);
        }
        
        TextView username = new TextView(this);
        username.setText("Username: ");
        username.setTextSize(22f);
        rows[0].addView(username);
        
        inputUsername = new EditText(this);
        inputUsername.setHint("Username");
        inputUsername.setBackgroundColor(Color.rgb(248,248,248));
        inputUsername.setTypeface(Typeface.MONOSPACE);
        rows[0].addView(inputUsername);
        
        rows[0].setPadding(20, 60, 20, 0);
        
        TextView password = new TextView(this);
        password.setText("Password: ");
        password.setTextSize(22f);
        rows[1].addView(password);
        
	    inputPassword = new EditText(this);
	    inputPassword.setHint("Password");
	    inputPassword.setBackgroundColor(Color.rgb(248,248,248));
	    inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
	    inputPassword.setTypeface(Typeface.MONOSPACE);
        rows[1].addView(inputPassword);
        
        rows[1].setPadding(20, 60, 20, 60);
        
        Button login = new Button(this);
        login.setText("Login");
        login.setTextSize(22f);
        login.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View view) 
            {
                if ((!inputUsername.getText().toString().equals("")) && (!inputPassword.getText().toString().equals("")))
                {
                	NetAsync(view);
                }
                else if ((!inputUsername.getText().toString().equals("")))
                {
                    Toast.makeText(getApplicationContext(), "Password field empty", Toast.LENGTH_SHORT).show();
                }
                else if ((!inputPassword.getText().toString().equals("")))
                {
                    Toast.makeText(getApplicationContext(), "Username field empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Username and password field are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });        
        TableRow.LayoutParams loginParams = new TableRow.LayoutParams();
        loginParams.span = 2;
        loginParams.setMargins(30, 0, 30, 0);
		rows[2].addView(login, loginParams);
        
        Button register = new Button(this);
        register.setText("Go to Register");
        register.setTextSize(22f);
        register.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View view) 
            {
                Intent myIntent = new Intent(view.getContext(), RegisterActivity.class);
                startActivityForResult(myIntent, 0);
                finish();
             }
        });
        TableRow.LayoutParams registerParams = new TableRow.LayoutParams();
        registerParams.span = 2;
        registerParams.setMargins(30, 30, 30, 0);
        rows[3].addView(register, registerParams);
                
	    error = new TextView(this);  
	    error.setTextSize(16f);
	    error.setTextColor(Color.RED);
	    error.setGravity(Gravity.CENTER);
	    TableRow.LayoutParams errorParams = new TableRow.LayoutParams();
	    errorParams.span = 2;
        rows[4].addView(error, errorParams);        
        
        for (int i=0; i<rows.length; i++)
        {
        	layout.addView(rows[i]);
        }
    }

    private class NetCheck extends AsyncTask<String, String, Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            nDialog = new ProgressDialog(LoginActivity.this);
            nDialog.setTitle("Checking Network");
            nDialog.setMessage("Loading...");
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
                new ProcessLogin().execute();
            }
            else
            {
                nDialog.dismiss();
                error.setText("Error in Network Connection");
            }
        }
    }
    
    //Async Task to get and send data to cassandra database.
    private class ProcessLogin extends AsyncTask<String, String, Boolean> 
    {
        private ProgressDialog pDialog;
        String username, password;

        @Override
        protected void onPreExecute() 
        {
            super.onPreExecute();

            username = inputUsername.getText().toString();
            password = inputPassword.getText().toString();
            
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Logging in ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args) 
        {
        	String httpResponse = "";
        	password = Cryptography.computeSHAHash(password);
        	Boolean test;
        	User user = new User();
        	
			try 
			{
				httpResponse = new HttpRequest().execute("http://group1.cloudapp.net:8080/ServerSide/user/"+username+"/"+password+"/").get();
			} 
			catch (InterruptedException e1) 
			{
				e1.printStackTrace();
			} 
			catch (ExecutionException e1) 
			{
				e1.printStackTrace();
			}
			try{
					user = (User) PojoMapper.fromJson(httpResponse, User.class);
					SharedPreferences.Editor editor = Settings.getSharedPreferencesEditor(getApplicationContext());
					editor.putString("user", httpResponse);
					editor.putString("hashedPassword", password);
					editor.commit();
					LoginActivity lA = LoginActivity.this;
					Alarm.startRepeatingTimer(lA);
			}
			catch (Exception e)
			{
				Log.d("Error", e.toString());
			}
			
			if (user.getUsername() != null)
			{
				test = true;
			}
			else
			{
				test = false;			
			}
			
			return test;
        }

        @Override
        protected void onPostExecute(Boolean test) 
        {
	        if(test)
	        {
	            pDialog.setMessage("Loading User Space");
	            pDialog.setTitle("Getting Data");
	            Intent mainScreen = new Intent(getApplicationContext(), com.FourTheFlat.TabCreator.class);
	            SharedPreferences.Editor editor = Settings.getSharedPreferencesEditor(getApplicationContext());
	            ActiveUser.initialise(getApplicationContext());
	            editor.putBoolean("hasLoggedIn", true);
	            // Commit the edits!	
	            editor.commit();
	            pDialog.dismiss();
	            startActivity(mainScreen);
	            finish();
	        }
	        else
	        {
	            pDialog.dismiss();
	            error.setText("Incorrect username/password");
	        }
        }
    }
    
    public void NetAsync(View view)
    {
        new NetCheck().execute();
    }    
}