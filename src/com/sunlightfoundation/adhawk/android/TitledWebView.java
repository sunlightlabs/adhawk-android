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

public class TitledWebView extends Activity implements ActionBarUtils.HasActionMenu {
	public static final int SITE_CUSTOM = -1;
	public static final int SITE_TOP = 0;
	public static final int SITE_ABOUT = 1;
	public static final int SITE_WHY_NO_RESULTS = 2;
	
	private int type;
	private String title, url;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_with_title);
		
		type = getIntent().getIntExtra("type", SITE_CUSTOM);
		
		if (type == SITE_TOP) {
			title = getResources().getString(R.string.top_ads);
			url = getResources().getString(R.string.site_top_ads);
		} else if (type == SITE_ABOUT) {
			title = getResources().getString(R.string.app_name);
			url = getResources().getString(R.string.site_about);
		} else if (type == SITE_WHY_NO_RESULTS) {
			title = getResources().getString(R.string.why_no_results);
			url = getResources().getString(R.string.site_why_no_results);
		} else if (type == SITE_CUSTOM) {
			title = getIntent().getStringExtra("title");
			url = getIntent().getStringExtra("url");
		}
		
		setupControls();
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	public void setupControls() {
		ActionBarUtils.setTitle(this, title);
		
		if (type != SITE_ABOUT) {
			ActionBarUtils.setActionButton(this, R.id.action_1, R.drawable.about, new View.OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(TitledWebView.this, TitledWebView.class)
						.putExtra("type", SITE_ABOUT);
					startActivity(intent);
				}
			});
		}
		
		ActionBarUtils.setActionMenu(this, R.menu.main);
		
		Utils.loadUrl(Utils.webViewFor(this), url);
	}
	
	@Override 
	public boolean onCreateOptionsMenu(Menu menu) { 
		super.onCreateOptionsMenu(menu); 
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		menuSelected(item);
		return true;
	}
	
	@Override
	public void menuSelected(MenuItem item) {
		switch(item.getItemId()) { 
		case R.id.settings:
			startActivity(new Intent(this, Settings.class));
			break;
		case R.id.review:
			Utils.goReview(this);
		}
	}
}