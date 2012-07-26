package org.cuiBono;

import org.cuiBono.utils.ActionBarUtils;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

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
}