package com.FourTheFlat;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Settings {
	
	public static SharedPreferences getSharedPreferences(Context context)
	{
		SharedPreferences preferences = context.getSharedPreferences(
				"4TheFlat_Settings", context.MODE_PRIVATE);
		return preferences;
	}
	
	public static Editor getSharedPreferencesEditor(Context context)
	{
		SharedPreferences settings = context.getSharedPreferences(
				"4TheFlat_Settings", context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		return editor;
	}

}