package com.FourTheFlat;

import android.app.Application;

public class Main extends Application
{			
	static public String URL = "http://group1d.cloudapp.net:8080/ServerSide/";
	
	static public int STORES = 9; //= 10; 
	
	static public double[][] locations =
	{
		{ 56.471517, 56.482722, 56.474530, 56.459811, 56.459032, 56.454624, 56.462902, 56.467256, 56.472329/*, 56.45843465*/ },
		{ -3.042506, -2.992598, -2.980187, -2.978250, -2.972137, -2.975493, -2.968766, -2.874610, -2.848669/*, -2.98278818*/ }
	};	
    
    static public String[] names = 
    {
    	"South Road Extra", //South Road, Dundee, Tayside, DD2 4SR
    	"Dundee Extra", //Kingsway, Dundee, DD3 8QB 
    	"Dundee Strathmart Express", //Unit 3, Strathmartine Road, Dundee, DD3 7SE 
    	"Dundee Hawkhill Express", //Unit 1, Hawkhill, Dundee, DD1 1NJ 
    	"Dundee Nethergate Express", //80 Nethergate, Dundee, Tayside, DD1 4ER 
    	"Dundee Riverside Extra", //Riverside Drive, Dundee, DD2 1UG 
    	"Dundee Metro", //60 Murraygate, Dundee, DD1 2BB
    	"Broughty Ferry Express", //229-231 Brook Street, Broughty Ferry, Dundee, Angus, DD5 2AG 
    	"Broughty Ferry Dalhousie Esso Express", //14a Dalhousie Road, Broughty Ferry, Dundee, DD5 2SQ
    	//"QMB Tesco" //test data
    };
}