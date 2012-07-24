package org.cuiBono;

import org.cuiBono.utils.ActionBarUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
		content.loadUrl(getResources().getString(R.string.site_about));
	}
}