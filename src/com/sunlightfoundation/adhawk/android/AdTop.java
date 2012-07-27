package com.sunlightfoundation.adhawk.android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.sunlightfoundation.adhawk.android.utils.ActionBarUtils;
import com.sunlightfoundation.adhawk.android.utils.Utils;

public class AdTop extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_with_title);
		
		setupControls();
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	public void setupControls() {
		ActionBarUtils.setTitle(this, R.string.top_ads);
		
		ActionBarUtils.setActionButton(this, R.id.action_1, R.drawable.about, new View.OnClickListener() {
			public void onClick(View v) { 
				startActivity(new Intent(AdTop.this, About.class));
			}
		});
		
		Utils.webViewFor(this).loadUrl(getResources().getString(R.string.site_top_ads));
	}
}