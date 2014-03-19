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


    /**
     *  JSON Response node names.
     **/


    private static String KEY_SUCCESS = "success";
    private static String KEY_UID = "uid";
    private static String KEY_USERNAME = "uname";
    private static String KEY_CREATED_AT = "created_at";
    private static String KEY_ERROR = "error";
    static String response = "";

    /**
     * Defining layout items.
     **/
    EditText inputUsername;
    EditText inputPassword;
    Button btnRegister;
    TextView registerErrorMsg;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.register);

    /**
     * Defining all layout items
     **/
        inputUsername = (EditText) findViewById(R.id.uname);
        inputPassword = (EditText) findViewById(R.id.pword);
        btnRegister = (Button) findViewById(R.id.register);
        registerErrorMsg = (TextView) findViewById(R.id.register_error);



/**
 * Button which Switches back to the login screen on clicked
 **/

        Button login = (Button) findViewById(R.id.bktologin);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), LoginActivity.class);
                startActivityForResult(myIntent, 0);
                finish();
            }

        });

        /**
         * Register Button click event.
         * A Toast is set to alert when the fields are empty.
         * Another toast is set to alert Username must be 5 characters.
         **/

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (  ( !inputUsername.getText().toString().equals("")) && ( !inputPassword.getText().toString().equals("")))
                {
                    if ( inputUsername.getText().toString().length() > 4 ){
                    NetAsync(view);

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),
                                "Username should be minimum 5 characters", Toast.LENGTH_SHORT).show();
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
    /**
     * Async Task to check whether internet connection is working
     **/

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


/**
 * Gets current device state and checks for working internet connection by trying Google.
 **/
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;

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

/**
 * Defining Process dialog
 **/
        private ProgressDialog pDialog;

        String email,password,fname,lname,uname;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            inputUsername = (EditText) findViewById(R.id.uname);
            inputPassword = (EditText) findViewById(R.id.pword);
                uname= inputUsername.getText().toString();
                password = inputPassword.getText().toString();
                password = computeSHAHash(password);                
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
				response = httpResponse;
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ExecutionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try{
					user.setUsername(uname);
					SharedPreferences.Editor editor = Settings.getSharedPreferencesEditor(getApplicationContext());
					editor.putString("activeUser", httpResponse);
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

        //UserFunctions userFunction = new UserFunctions();
        //JSONObject json = userFunction.registerUser(fname, lname, email, uname, password);        	
            return json;
        }
       @Override
        protected void onPostExecute(JSONObject json) {
       /**
        * Checks for success message.
        **/
                try {
                    if (json.getString(KEY_SUCCESS) != null) {
                        String res = "1";

                        //String red = json.getString(KEY_ERROR);

                        if(Integer.parseInt(res) == 1){
                            pDialog.setTitle("Getting Data");
                            pDialog.setMessage("Loading Info");

                            registerErrorMsg.setText("Successfully Registered");


                            //DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                            //JSONObject json_user = json.getJSONObject("user");

                            /**
                             * Removes all the previous data in the SQlite database
                             **/

                            //UserFunctions logout = new UserFunctions();
                            //logout.logoutUser(getApplicationContext());
                            //db.addUser(json_user.getString(KEY_FIRSTNAME),json_user.getString(KEY_LASTNAME),json_user.getString(KEY_EMAIL),json_user.getString(KEY_USERNAME),json_user.getString(KEY_UID),json_user.getString(KEY_CREATED_AT));
                            /**
                             * Stores registered data in SQlite Database
                             * Launch Registered screen
                             **/

                            Intent registered = new Intent(getApplicationContext(), RegisteredActivity.class);

                            /**
                             * Close all views before launching Registered screen
                            **/
                            registered.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            pDialog.dismiss();
                            startActivity(registered);


                              finish();
                        }

                        else if (response.equals("Username already registered")){
                            pDialog.dismiss();
                            registerErrorMsg.setText("User already exists");
                        }
                        //else if (Integer.parseInt(red) ==3){
                        //    pDialog.dismiss();
                        //    registerErrorMsg.setText("Invalid Email id");
                        //}

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
        
        public String convertToHex(byte[] data) throws java.io.IOException
        {
        	StringBuffer sb = new StringBuffer();
        	String hex = null;
        	hex = Base64.encodeToString(data, 0, data.length, 0);
        	sb.append(hex);
        	return sb.toString();        	
        }
        
        public String computeSHAHash(String input)
        {
        	StringBuffer output = new StringBuffer();
        	MessageDigest mdSha256 = null;
            try{
            	mdSha256 = MessageDigest.getInstance("SHA-256");
            }
            catch(NoSuchAlgorithmException e){
            	Log.e("myapp", "SHA-256 ERROR!");
            }
            try{
            	mdSha256.update(input.getBytes("ASCII"));
            }
            catch(Exception e)
            {
            	
            }
            byte[] data = mdSha256.digest();
            output.append(byteArrayToString(data));
            return output.toString();
        	
        }
        
        /**
    	 * Taken from http://stackoverflow.com/questions/4895523/java-string-to-sha1
    	 * Allows us to convert a SHA-1 Byte Array into a Hex String
    	 */
    	public static String byteArrayToString(byte[] b) 
    	{
    		String result = "";

    		for (int i=0; i < b.length; i++) 
    		{
    			result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
    		}

    		return result;
    	}
        
}



