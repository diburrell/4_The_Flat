package com.FourTheFlat.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.FourTheFlat.R;
import com.FourTheFlat.TabCreator;

import java.util.HashMap;

public class RegisteredActivity extends Activity {



    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registered);

        HashMap<String,String> user = new HashMap<String, String>();
        //db.getUserDetails();

        /**
         * Displays the registration details in Text view
         **/
        final TextView uname = (TextView)findViewById(R.id.uname);
        final TextView email = (TextView)findViewById(R.id.email);
        uname.setText(user.get("uname"));
        email.setText(user.get("email"));

        Button login = (Button) findViewById(R.id.Main);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), TabCreator.class);
                startActivityForResult(myIntent, 0);
                finish();
            }

        });


    } 
}
