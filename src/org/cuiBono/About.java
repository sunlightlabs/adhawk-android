package org.cuiBono;

import org.cuiBono.utils.ActionBarUtils;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class About extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		setupControls();
	}
	
	public void setupControls() {
		ActionBarUtils.setTitle(this, R.string.app_name);
		
		WebView content = (WebView) findViewById(R.id.content);
		WebSettings settings = content.getSettings();
		settings.setUserAgentString(AdHawkServer.USER_AGENT);
		settings.setJavaScriptEnabled(true);
		content.loadUrl(getResources().getString(R.string.site_about));
	}
}