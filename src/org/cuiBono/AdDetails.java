package org.cuiBono;

import org.cuiBono.utils.ActionBarUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

public class AdDetails extends Activity implements ActionBarUtils.HasActionMenu {
	private AdHawkServer.Response details;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ad_details);
		
		details = (AdHawkServer.Response) getIntent().getSerializableExtra("details");
		
		setupControls();
	}
	
	public void setupControls() {
		ActionBarUtils.setTitle(this, R.string.app_name);
		
		ActionBarUtils.setActionButton(this, R.id.action_2, R.drawable.share, new View.OnClickListener() {
			public void onClick(View v) { 
				// nothing yet
			}
		});
		
		ActionBarUtils.setActionButton(this, R.id.action_1, R.drawable.about, new View.OnClickListener() {
			public void onClick(View v) { 
				startActivity(new Intent(AdDetails.this, About.class));
			}
		});
		
		ActionBarUtils.setActionMenu(this, R.menu.main);
		
		WebView results = (WebView) findViewById(R.id.content);
		results.loadUrl(details.resultUrl);
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
}