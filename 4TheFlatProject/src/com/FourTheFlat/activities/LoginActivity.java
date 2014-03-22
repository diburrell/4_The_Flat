package com.FourTheFlat.activities;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

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
    private static String SUCCESS = "success";
    private static String KEY_UID = "uid";
    private static String KEY_USERNAME = "uname";
    private static String KEY_CREATED_AT = "created_at";
    private Boolean loggedIn=false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        setContentView(R.layout.login);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.pword);
        Btnregister = (Button) findViewById(R.id.registerbtn);
        btnLogin = (Button) findViewById(R.id.login);
        passreset = (Button)findViewById(R.id.passres);
        loginErrorMsg = (TextView) findViewById(R.id.loginErrorMsg);

        passreset.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
        	Toast.makeText(getApplicationContext(), "Ain't no functionality for this yet!", Toast.LENGTH_LONG).show();
        }});


        Btnregister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), RegisterActivity.class);
                startActivityForResult(myIntent, 0);
                finish();
             }});
        
        
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                if (  ( !inputEmail.getText().toString().equals("")) && ( !inputPassword.getText().toString().equals("")) )
                {
                	NetAsync(view);
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

    private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(LoginActivity.this);
            nDialog.setTitle("Checking Network");
            nDialog.setMessage("Loading..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        
        @Override
        protected Boolean doInBackground(String... args){
        	
            return ConnectionManager.checkInternetConnection(getApplicationContext());

        }
        @Override
        protected void onPostExecute(Boolean th){

            if(th == true){
                nDialog.dismiss();
                new ProcessLogin().execute();
            }
            else{
                nDialog.dismiss();
                loginErrorMsg.setText("Error in Network Connection");
            }
        }
    }

    
    //Async Task to get and send data to cassandra database.
    private class ProcessLogin extends AsyncTask<String, String, JSONObject> {


        private ProgressDialog pDialog;

        String email,password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            inputEmail = (EditText) findViewById(R.id.email);
            inputPassword = (EditText) findViewById(R.id.pword);
            email = inputEmail.getText().toString();
            password = inputPassword.getText().toString();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Logging in ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
        	//TODO: LOGIN LOGIC HERE
        	String httpResponse = "";
        	password=Cryptography.computeSHAHash(password);
        	User user = new User();
			try {
				httpResponse = new HttpRequest().execute("http://group1.cloudapp.net:8080/ServerSide/user/"+email+"/"+password+"/").get();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ExecutionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try{
					user = (User) PojoMapper.fromJson(httpResponse, User.class);
					SharedPreferences.Editor editor = Settings.getSharedPreferencesEditor(getApplicationContext());
					editor.putString("user", httpResponse);
					editor.putString("hashedPassword", password);
					editor.commit();
			}
			catch(Exception e)
			{
				Log.d("Error", e.toString());
			}
			JSONObject json = new JSONObject();
			if(user.getUsername()!=null)
			{            
				try{
					json.put(SUCCESS, "1");
				}
				catch(JSONException e){            	
				}
			}
			else{
				try {
					json.put(SUCCESS, "0");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
			return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
            	Log.w("test",json.toString());
               if (json.getString(SUCCESS) != null) {
                    String res = json.getString(SUCCESS);
                    if(Integer.parseInt(res) == 1)
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
                    }else{
                        pDialog.dismiss();
                        loginErrorMsg.setText("Incorrect username/password");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
       }
    }
    
    public void NetAsync(View view){
        new NetCheck().execute();
    }    
}