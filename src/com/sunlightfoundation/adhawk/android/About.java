package com.sunlightfoundation.adhawk.android;

import android.app.Activity;
import android.os.Bundle;

import com.sunlightfoundation.adhawk.android.utils.ActionBarUtils;
import com.sunlightfoundation.adhawk.android.utils.Utils;

public class About extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		setupControls();
	}
	
	public void setupControls() {
		ActionBarUtils.setTitle(this, R.string.app_name);
		
		Utils.webViewFor(this).loadUrl(getResources().getString(R.string.site_about));
	}
}