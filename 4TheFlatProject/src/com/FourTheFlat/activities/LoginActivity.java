package com.FourTheFlat.activities;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.FourTheFlat.ActiveUser;
import com.FourTheFlat.ConnectionManager;
import com.FourTheFlat.Cryptography;
import com.FourTheFlat.HttpRequest;
import com.FourTheFlat.Main;
import com.FourTheFlat.PojoMapper;
import com.FourTheFlat.R;
import com.FourTheFlat.Settings;
import com.FourTheFlat.TabCreator;
import com.FourTheFlat.R.id;
import com.FourTheFlat.R.layout;
import com.FourTheFlat.stores.User;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends Activity {

    Button btnLogin;
    Button Btnregister;
    Button passreset;
    EditText inputEmail;
    EditText inputPassword;
    private TextView loginErrorMsg;
    /**
     * Called when the activity is first created.
     */
    private static String KEY_SUCCESS = "success";
    private static String KEY_UID = "uid";
    private static String KEY_USERNAME = "uname";
    private static String KEY_FIRSTNAME = "fname";
    private static String KEY_LASTNAME = "lname";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
        boolean hasLoggedIn = Settings.getSharedPreferences(this.getApplicationContext()).getBoolean("hasLoggedIn", false);
        ActiveUser.initialise(getApplicationContext());
        if(hasLoggedIn && ActiveUser.getActiveUser() != null)
        {
        	Intent registered = new Intent(getApplicationContext(), com.FourTheFlat.TabCreator.class);
            
            /**
             * Close all views before launching Registered screen
            **/
            registered.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(registered);
            finish();
        }

        setContentView(R.layout.login);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.pword);
        Btnregister = (Button) findViewById(R.id.registerbtn);
        btnLogin = (Button) findViewById(R.id.login);
        passreset = (Button)findViewById(R.id.passres);
        loginErrorMsg = (TextView) findViewById(R.id.loginErrorMsg);

        passreset.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
        Intent myIntent = new Intent(view.getContext(), com.FourTheFlat.TabCreator.class);
        startActivityForResult(myIntent, 0);
        finish();
        }});


        Btnregister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), RegisterActivity.class);
                startActivityForResult(myIntent, 0);
                finish();
             }});

/**
 * Login button click event
 * A Toast is set to alert when the Email and Password field is empty
 **/
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                if (  ( !inputEmail.getText().toString().equals("")) && ( !inputPassword.getText().toString().equals("")) )
                {
                	doLogin();
                }
                else if ( ( !inputEmail.getText().toString().equals("")) )
                {
                    Toast.makeText(getApplicationContext(),
                            "Password field empty", Toast.LENGTH_SHORT).show();
                }
                else if ( ( !inputPassword.getText().toString().equals("")) )
                {
                    Toast.makeText(getApplicationContext(),
                            "Email field empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "Email and Password field are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    
    public void doLogin()
    {
    	String response = "";
    	try {
			response = new HttpRequest().execute("http://group1.cloudapp.net:8080/ServerSide/user/"+inputEmail.getText().toString()+"/"+Cryptography.computeSHAHash(inputPassword.getText().toString()),"get").get();
			if(processLoginResponse(response))
			{
				SharedPreferences.Editor editor = Settings.getSharedPreferencesEditor(getApplicationContext());
				editor.putString("user", response);
				editor.putBoolean("hasLoggedIn", true);
				editor.commit();
	        	Intent mainScreen = new Intent(getApplicationContext(), com.FourTheFlat.TabCreator.class);
	            startActivity(mainScreen);
	            return;
			}
			return;
    	} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }
    
    public boolean processLoginResponse(String response)
    {
    	if(response.equals("Invalid username or password."))
    	{
    		Toast.makeText(getApplicationContext(), "Invalid username and/or password", Toast.LENGTH_LONG).show();
    		return false;
    	}
    	else if(response.equals("Incorrect URL format."))
    	{
    		Toast.makeText(getApplicationContext(), "An error has occurred.", Toast.LENGTH_LONG).show();
    		return false;
    	}
    	else
    	{
    		User u = new User();
    		try {
				u = (User)PojoMapper.fromJson(response, User.class);
				return true;
			} catch (Exception e)
			{
				Log.w("login", "login unsuccessful");
				return false;
			}
    	}
    }
    
    
}
