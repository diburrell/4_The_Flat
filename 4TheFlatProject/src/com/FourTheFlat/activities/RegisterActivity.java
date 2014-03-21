package com.FourTheFlat.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.FourTheFlat.activities.LoginActivity;
import com.FourTheFlat.stores.User;
import com.FourTheFlat.ActiveUser;
import com.FourTheFlat.ConnectionManager;
import com.FourTheFlat.Cryptography;
import com.FourTheFlat.HttpRequest;
import com.FourTheFlat.PojoMapper;
import com.FourTheFlat.R;
import com.FourTheFlat.Settings;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class RegisterActivity extends Activity {


    private static String KEY_SUCCESS = "success";
    private static String KEY_RESPONSE = "response";

    //Layout items
    EditText inputUsername;
    EditText inputPassword;
    EditText inputPasswordConfirm;
    Button btnRegister;
    TextView registerErrorMsg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.register);
       
        inputUsername = (EditText) findViewById(R.id.uname);
        inputPassword = (EditText) findViewById(R.id.pword);
        inputPasswordConfirm = (EditText) findViewById(R.id.pwordConfirm);
        btnRegister = (Button) findViewById(R.id.register);
        registerErrorMsg = (TextView) findViewById(R.id.register_error);
        Button login = (Button) findViewById(R.id.bktologin);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), LoginActivity.class);
                startActivityForResult(myIntent, 0);
                finish();
            }

        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (  ( !inputUsername.getText().toString().equals("")) && ( !inputPassword.getText().toString().equals("")))
                {
                    if ( inputPassword.getText().toString().length() > 5)
                    {
                    	if(inputPassword.getText().toString().equals(inputPasswordConfirm.getText().toString()))
                    	{
                    		NetAsync(view);
                    	}
                    	else
                    	{
                    		Toast.makeText(getApplicationContext(), "Password and password confirmation do not match.", Toast.LENGTH_SHORT).show();
                    	}

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),
                                "Password should be minimum 6 characters", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "One or more fields are empty", Toast.LENGTH_SHORT).show();
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
            nDialog = new ProgressDialog(RegisterActivity.this);
            nDialog.setMessage("Loading..");
            nDialog.setTitle("Checking Network");
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
                new ProcessRegister().execute();
            }
            else{
                nDialog.dismiss();
                registerErrorMsg.setText("Error in Network Connection");
            }
        }
    }

    private class ProcessRegister extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog pDialog;

        String password = "";
        String uname = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            inputUsername = (EditText) findViewById(R.id.uname);
            inputPassword = (EditText) findViewById(R.id.pword);
            uname= inputUsername.getText().toString();
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
        protected JSONObject doInBackground(String... args) {
        	//TODO: REGISTRATION LOGIC HERE
        	String httpResponse = "";
        	JSONObject json = new JSONObject();
        	
        	User user = new User();
			try {
				httpResponse = new HttpRequest().execute("http://group1.cloudapp.net:8080/ServerSide/user/"+uname+"/"+password+"/", "put").get();
				try{
	        		json.put(KEY_SUCCESS, "0");
	        		json.put(KEY_RESPONSE, httpResponse);
	        	}
	        	catch(JSONException e)
	        	{        		
	        	}
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ExecutionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(httpResponse.contains("groupID"))
			{
				try{
						user.setUsername(uname);
						SharedPreferences.Editor editor = Settings.getSharedPreferencesEditor(getApplicationContext());
						editor.putString("user", httpResponse);
						ActiveUser.initialise(getApplicationContext());
						editor.putBoolean("hasLoggedIn", true);
						editor.commit();
						try{
			            	json.put(KEY_SUCCESS, "1");
			            }
			            catch(JSONException e){            	
			            }
				}
				catch(Exception e)
				{
				} 
			}
            return json;
        }
       @Override
        protected void onPostExecute(JSONObject json) {
       /**
        * Checks for success message.
        **/
                try {
                    if (json.getString(KEY_SUCCESS) == "1") {
                        registerErrorMsg.setText("Successfully Registered");
                        Intent registered = new Intent(getApplicationContext(), RegisteredActivity.class);
                        registered.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        ActiveUser.initialise(getApplicationContext());
                        pDialog.dismiss();
                        startActivity(registered);
                        finish();                        
                    }
                    else if (json.getString(KEY_RESPONSE).equals("Username already registered.")){
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "That Username already exists, please try a new one!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        pDialog.dismiss();
                        registerErrorMsg.setText("Error occured in registration");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }}
    
        public void NetAsync(View view){
            new NetCheck().execute();
        }  
}