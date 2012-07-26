package org.cuiBono;

import org.cuiBono.utils.ActionBarUtils;
import org.cuiBono.utils.Utils;

import android.app.Activity;
import android.os.Bundle;

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