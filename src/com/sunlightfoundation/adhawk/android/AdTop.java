package com.sunlightfoundation.adhawk.android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sunlightfoundation.adhawk.android.utils.ActionBarUtils;
import com.sunlightfoundation.adhawk.android.utils.Utils;

public class AdTop extends Activity {
	public static final int SITE_TOP = 0;
	public static final int SITE_ABOUT = 1;
	
	private String title, url;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_with_title);
		
		int type = getIntent().getIntExtra("type", SITE_TOP);
		
		if (type == SITE_TOP) {
			title = getResources().getString(R.string.top_ads);
			url = getResources().getString(R.string.site_top_ads);
		} else if (type == SITE_ABOUT) {
			title = getResources().getString(R.string.app_name);
			url = getResources().getString(R.string.site_about);
		}
		
		setupControls();
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	public void setupControls() {
		ActionBarUtils.setTitle(this, title);
		
		ActionBarUtils.setActionButton(this, R.id.action_1, R.drawable.about, new View.OnClickListener() {
			public void onClick(View v) { 
				startActivity(new Intent(AdTop.this, About.class));
			}
		});
		
		Utils.loadUrl(Utils.webViewFor(this), url);
	}
}