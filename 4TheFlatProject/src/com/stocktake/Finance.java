package com.stocktake;

import java.io.Serializable;

import android.content.SharedPreferences;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class Finance implements Comparable<Finance>, Serializable
{
	public String name; // Stock name
	public String epic; // Stock code
	
	public float last = 0; // Last stock value
	public String market; // Market
	public float close = 0;
	public int volume = 0;
	public int instant_volume = 0;
	
	public int run = 50;
	public int plummet = 80;
	public int rocket = 10;
	
	public boolean is_run;
	public boolean is_rocket;
	public boolean is_plummet;

	public float currentValue = 0;

	public Finance() {
		name = "Default";
		last = 0;
	}
	
	public void getTriggers(Context context)
	{
		SharedPreferences preferences = context.getSharedPreferences("StockTriggers", Context.MODE_PRIVATE);
		run = preferences.getInt(epic+"_run", 50);
		plummet = preferences.getInt(epic+"_plummet", 80);
		rocket = preferences.getInt(epic+"_rocket", 10);
		Log.w(epic+"Run", Float.toString(run));
		Log.w(epic+"Plummet", Float.toString(plummet));
		Log.w(epic+"Rocket", Float.toString(rocket));
	}
	
	public void setTriggers(Context context, int run, int plummet, int rocket)
	{
		SharedPreferences preferences = context.getSharedPreferences("StockTriggers", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		
		editor.putInt(epic+"_run", run);
		editor.putInt(epic+"_plummet", plummet);
		editor.putInt(epic+"_rocket", rocket);
		editor.commit();
		
		this.run = run;
		this.plummet = plummet;
		this.rocket = rocket;
	}
	
	public void setEpic(String epic)
	{
		this.epic = epic;
	}
	
	public String getEpic()
	{
		return epic;
	}
	
	public void setLast(float newLast) {
		last = newLast;
	}

	public float getLast() {
		return last;
	}

	public void setName(String newName) {
		name = newName;
	}

	public void setCurrentValue(float currentValue) {
		this.currentValue = currentValue;
	}

	public float getCurrentValue() {
		return currentValue;
	}

	public String getName() {
		return name;
	}

	public void setMarket(String newMarket) {
		market = newMarket;
	}

	public String getMarket() {
		return market;
	}

	public String getSummary() {
		return name + ":  " + last;
	}

	public void setClose(float newClose) {
		close = newClose;
	}

	public float getClose() {
		return close;
	}

	public void setVolume(int newVol) {
		volume = newVol;
	}

	public int getVolume() {
		return volume;
	}

	public void setInstantVolume(int newVol) {
		instant_volume = newVol;
	}

	public int getInstantVolume() {
		return instant_volume;
	}

	public boolean isRun() {
		return is_run;
	}

	public boolean isRocket() {
		return is_rocket;
	}

	public boolean isPlummet() {
		return is_plummet;
	}

	public void calcRun()
	{
		if (volume != 0 && instant_volume != 0) {
			if (instant_volume >= ((((float)run/100)+1) * volume)) {
				is_run = true;
			} else {
				is_run = false;
			}
		}
	}

	public void calcRocketPlummet()
	{
		is_plummet = false;
		is_rocket = false;
		
		if (last != 0 && close != 0) {
			if (last >= ((((float)rocket/100)+1) * close)) {
				is_rocket = true;
			} else if (last <= (((float)plummet/100) * close)) {
				is_plummet = true;
			}
		}
	}

	@Override
	public int compareTo(Finance otherFinance) {
		float otherValue = otherFinance.currentValue;
		if (this.currentValue < otherValue) {
			return 1;
		} else if (this.currentValue == otherValue) {
			return 0;
		} else {
			return -1;
		}
	}


}