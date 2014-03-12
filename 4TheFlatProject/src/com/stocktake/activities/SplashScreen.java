package com.stocktake.activities;

import com.stocktake.*;

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
               i.setClassName("com.stocktake",
                              "com.stocktake.AgileProjectActivity");
               startActivity(i);
         }
      };
      splashThread.start();
   }
}