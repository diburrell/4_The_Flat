package com.FourTheFlat.activities;
	
import com.FourTheFlat.Main;
import com.FourTheFlat.R;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.os.Bundle;

public class MapActivity extends Activity
{	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        createMap(this); 
    }
    
    private void createMap(Activity contextActivity)
    {
        GoogleMap map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();     

        LatLng[] tescos = new LatLng[Main.STORES]; //stores the locations for all Dundee Tescos     
        for (int i=0; i<Main.STORES; i++)
        {
	        tescos[i] = new LatLng(Main.locations[0][i], Main.locations[1][i]);
	        map.addMarker(new MarkerOptions().title(Main.names[i]).position(tescos[i]));   
        }
        
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(tescos[3], 12));
        map.setMyLocationEnabled(true); //show user current location
    }
}