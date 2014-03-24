package com.FourTheFlat.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.FourTheFlat.ActiveUser;
import com.FourTheFlat.R;
import com.FourTheFlat.Settings;
import com.FourTheFlat.TabCreator;
import com.FourTheFlat.stores.User;

import java.util.HashMap;

public class RegisteredActivity extends Activity {



    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registered);
        User current = ActiveUser.getActiveUser();
        String username = current.getUsername();
        final TextView uname = (TextView)findViewById(R.id.uname);
        uname.setText(username);
        
        Button login = (Button) findViewById(R.id.Main);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) 
            {
            	Intent mainScreen = new Intent(getApplicationContext(), com.FourTheFlat.TabCreator.class);
                SharedPreferences.Editor editor = Settings.getSharedPreferencesEditor(getApplicationContext());
                ActiveUser.initialise(getApplicationContext());
                editor.putBoolean("hasLoggedIn", true);
                // Commit the edits!	
                editor.commit();                
                startActivity(mainScreen);
                finish();
            }

        });


    } 
}
