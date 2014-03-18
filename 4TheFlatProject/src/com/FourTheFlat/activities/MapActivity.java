package com.FourTheFlat.activities;
	
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
        
        int STORES = 9;
        
        double[][] locations = new double[STORES][2];
        locations[0][0] = 56.471517; locations[0][1] = -3.042506;
        locations[1][0] = 56.482722; locations[1][1] = -2.992598;
        locations[2][0] = 56.474530; locations[2][1] = -2.980187;
        locations[3][0] = 56.459811; locations[3][1] = -2.978250;
        locations[4][0] = 56.459032; locations[4][1] = -2.972137;
        locations[5][0] = 56.454624; locations[5][1] = -2.975493;
        locations[6][0] = 56.462902; locations[6][1] = -2.968766;
        locations[7][0] = 56.467256; locations[7][1] = -2.874610;
        locations[8][0] = 56.472329; locations[8][1] = -2.848669;
        
        String[] names = new String[STORES];
        names[0] = "South Road Extra"; //South Road, Dundee, Tayside, DD2 4SR
        names[1] = "Dundee Extra"; //Kingsway, Dundee, DD3 8QB 
        names[2] = "Dundee Strathmart Express"; //Unit 3, Strathmartine Road, Dundee, DD3 7SE 
        names[3] = "Dundee Hawkhill Express"; //Unit 1, Hawkhill, Dundee, DD1 1NJ 
        names[4] = "Dundee Nethergate Express"; //80 Nethergate, Dundee, Tayside, DD1 4ER 
        names[5] = "Dundee Riverside Extra"; //Riverside Drive, Dundee, DD2 1UG 
        names[6] = "Dundee Metro"; //60 Murraygate, Dundee, DD1 2BB
        names[7] = "Broughty Ferry Express"; //229-231 Brook Street, Broughty Ferry, Dundee, Angus, DD5 2AG 
        names[8] = "Broughty Ferry Dalhousie Esso Express"; //14a Dalhousie Road, Broughty Ferry, Dundee, DD5 2SQ
        
        LatLng[] tescos = new LatLng[STORES]; //stores the locations for all Dundee Tescos     
        for (int i=0; i<STORES; i++)
        {
	        tescos[i] = new LatLng(locations[i][0], locations[i][1]);
	        map.addMarker(new MarkerOptions().title(names[i]).position(tescos[i]));   
        }
        
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(tescos[3], 12));
        map.setMyLocationEnabled(true); //show user current location
    }
}