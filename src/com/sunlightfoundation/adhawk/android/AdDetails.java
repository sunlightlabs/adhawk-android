package com.sunlightfoundation.adhawk.android;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.sunlightfoundation.adhawk.android.utils.ActionBarUtils;
import com.sunlightfoundation.adhawk.android.utils.Utils;

public class AdDetails extends Activity implements ActionBarUtils.HasActionMenu {
	private AdHawkServer.Response details;
	private String url;
	private GetAdTask task;
	private View spinner, content;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_with_title);
		
		details = (AdHawkServer.Response) getIntent().getSerializableExtra("details");
		url = getIntent().getStringExtra("url");
		
		setupControls();
		
		if (details != null)
			setupAd();
		else
			getAd(url);
	}
	
	public void setupControls() {
		Log.i(AdHawk.TAG, "setupControls()");
		
		spinner = findViewById(R.id.spinner);
		content = findViewById(R.id.main);
	}
	
	public void setupAd() {
		Log.i(AdHawk.TAG, "setupAd()");
		
		ActionBarUtils.setTitle(this, R.string.app_name);
		
		ActionBarUtils.setActionButton(this, R.id.action_2, R.drawable.share, new View.OnClickListener() {
			public void onClick(View v) { 
				String shareText = getResources().getString(R.string.share_text) + 
						" " + details.resultUrl + " " +
						getResources().getString(R.string.hashtag);
				
				Intent intent = new Intent(Intent.ACTION_SEND).setType("text/plain")
						.putExtra(Intent.EXTRA_TEXT, shareText)
						.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.share_email_subject));
	    		startActivity(Intent.createChooser(intent, "Share this ad:"));
			}
		});
		
		ActionBarUtils.setActionButton(this, R.id.action_1, R.drawable.about, new View.OnClickListener() {
			public void onClick(View v) { 
				startActivity(new Intent(AdDetails.this, About.class));
			}
		});
		
		ActionBarUtils.setActionMenu(this, R.menu.main);
		
		Utils.webViewFor(this).loadUrl(details.resultUrl);
	}
	
	public void loadAd() {
		Utils.webViewFor(this).loadUrl(details.resultUrl);
	}
	
	public void getAd(String url) {
		if (this.task == null) {
			content.setVisibility(View.GONE);
			spinner.setVisibility(View.VISIBLE);
			
			this.task = (GetAdTask) new GetAdTask(this).execute(url);
		}
	}
	
	public void onGetAd(AdHawkServer.Response details) {
		this.details = details;
		spinner.setVisibility(View.GONE);
		content.setVisibility(View.VISIBLE);
		setupAd();
	}
	
	public void onGetAd(AdHawkException exception) {
		// er
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
		}
	}
	
	private class GetAdTask extends AsyncTask<String, Void, AdHawkServer.Response> {
		private AdHawkException exception;
		private AdDetails activity;
		
		public GetAdTask(AdDetails activity) {
			this.activity = activity;
		}
		
		@Override
		protected AdHawkServer.Response doInBackground(String... url) {
			try {
				return AdHawkServer.getAd(url[0]);
			} catch (AdHawkException e) {
				this.exception = e;
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(AdHawkServer.Response result) {
			activity.task = null;
			
			if (result != null)
				activity.onGetAd(result);
			else if (this.exception != null)
				activity.onGetAd(this.exception);
			else
				activity.onGetAd(new AdHawkException("Unknown error."));
		}
	}
}