package com.FourTheFlat.activities;

import com.FourTheFlat.*;
import com.FourTheFlat.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
 
public class SplashScreen extends Activity {
   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.splash);
      
      Thread splashThread = new Thread() {
         @Override
         public void run() {
               finish();
               Intent i = new Intent();
               if(!loggedIn())
               {
            	   i.setClassName("com.FourTheFlat", "com.FourTheFlat.LoginActivity");
               }
               else
               {
            	   i.setClassName("com.FourTheFlat",
                              "com.FourTheFlat.AgileProjectActivity");
               }
               startActivity(i);
         }
      };
      splashThread.start();
   }
   
   public boolean loggedIn()
   {
	   return false;
   }
}