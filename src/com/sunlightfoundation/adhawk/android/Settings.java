package com.sunlightfoundation.adhawk.android;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.sunlightfoundation.adhawk.android.utils.ActionBarUtils;

public class Settings extends PreferenceActivity {
	
	public static final String LOCATION_ENABLED_KEY = "location_enabled";
	public static final boolean LOCATION_ENABLED_DEFAULT = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		setupControls();
		setupPreferences();
	}
	
	public void setupControls() {
		ActionBarUtils.setTitle(this, R.string.app_name);
	}
	
	public void setupPreferences() {
		addPreferencesFromResource(R.xml.settings);
		PreferenceManager.setDefaultValues(this, R.xml.settings, false);
	}
	
	public static boolean locationEnabled(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(LOCATION_ENABLED_KEY, LOCATION_ENABLED_DEFAULT);
	}
	
}