package com.FourTheFlat.activities;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import com.FourTheFlat.ActiveUser;
import com.FourTheFlat.HttpRequest;
import com.FourTheFlat.Main;
import com.FourTheFlat.PojoMapper;
import com.FourTheFlat.R;
import com.FourTheFlat.stores.MapStore;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ShopActivity extends Activity implements View.OnClickListener
{
	TableLayout layout;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shoppinglist);
		createDisplay(this);
	}
	
	private void createDisplay(Activity contextActivity)
	{		
		layout = (TableLayout) contextActivity.findViewById(R.id.layout);
	
		//Send signal to start shop (LOCK EVERY ONE ELSE OUT!) 
		MapStore store = new MapStore();
		try {
			store = (MapStore) PojoMapper.fromJson(new HttpRequest().execute(Main.URL + "usershopping/"+ActiveUser.getActiveUser().getUsername()).get(), MapStore.class);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, Integer> list = store.getMap();
		TableRow[] row = new TableRow[list.size()];
		TextView[] product = new TextView[list.size()];
		TextView[] cost = new TextView[list.size()];
		
		int i =0;
		for (Map.Entry<String, Integer> m : list.entrySet())
		{
			row[i] = new TableRow(contextActivity);

			product[i] = new TextView(contextActivity);
			product[i].setText(m.getKey());
			product[i].setTextSize(24f);
			product[i].setTextColor(Color.BLACK);
			if (i == 0)
				product[i].setPadding(0, 60, 0, 0);				

			cost[i] = new TextView(contextActivity);		
			if (m.getValue() == 0.00)
			{
				cost[i].setText(String.format("£%.2f",((float) m.getValue())/100.0));
				cost[i].setTextColor(Color.BLACK);
			}
			else
			{
				cost[i].setText(String.format("£%.2f", ((float) m.getValue())/100.0));
				cost[i].setTextColor(Color.GREEN);
			}
			cost[i].setGravity(Gravity.RIGHT);
			cost[i].setTextSize(24f);
			if (i == 0)
				cost[i].setPadding(0, 60, 0, 0);

			row[i].addView(product[i]);
			row[i].addView(cost[i]);

			if(m.getValue() < 0)
			{
				row[i].setOnClickListener(this);
			}
			
			layout.addView(row[i]);
			i++;
		}

		
		
		
		
		Button msg = new Button(contextActivity);
		msg.setText("End shop!");
		layout.addView(msg);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (view instanceof Button)
		{		
			buttonClick(view);
		}
		else if (view instanceof TableRow)
		{
			tableRowClick(view);
		}
	}

	private void tableRowClick(View view) {
		// TODO Auto-generated method stub
		
	}

	private void buttonClick(View view) {
		// TODO Auto-generated method stub
		
	}
}