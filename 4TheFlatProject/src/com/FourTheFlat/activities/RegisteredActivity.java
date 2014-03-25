package com.FourTheFlat.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.FourTheFlat.ActiveUser;
import com.FourTheFlat.R;
import com.FourTheFlat.Settings;
import com.FourTheFlat.stores.User;

public class RegisteredActivity extends Activity 
{
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
                
        setContentView(R.layout.registered);
                
        TableLayout layout = (TableLayout)this.findViewById(R.id.layout);
        
        TableRow[] rows = new TableRow[2];
        for (int i=0; i<rows.length; i++)
        {
        	rows[i] = new TableRow(this);
        }
                
        User current = ActiveUser.getActiveUser();
        String username = current.getUsername();
        
        TextView usernameView = new TextView(this);
        usernameView.setText("You have registered as: " + username);
        usernameView.setTextSize(22f);
        TableRow.LayoutParams usernameParams = new TableRow.LayoutParams();
        usernameParams.setMargins(20, 60, 20, 0);
        rows[0].addView(usernameView, usernameParams);
        
        Button proceed = new Button(this);
        proceed.setText("Proceed to application");
        proceed.setOnClickListener(new View.OnClickListener() 
        {
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
        TableRow.LayoutParams proceedParams = new TableRow.LayoutParams();
        proceedParams.setMargins(30, 50, 30, 0);
        rows[1].addView(proceed, proceedParams);

        for (int i=0; i<rows.length; i++)
        {
        	layout.addView(rows[i]);
        }
    } 
}