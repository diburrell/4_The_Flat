package com.FourTheFlat.activities;
	
import com.FourTheFlat.R;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

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
        
        LatLng dundee = new LatLng(56.46204, -2.970711);         
        map.addMarker(new MarkerOptions().title("Dundee").position(dundee));
        
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(dundee, 12));
        map.setMyLocationEnabled(true);
    }
}