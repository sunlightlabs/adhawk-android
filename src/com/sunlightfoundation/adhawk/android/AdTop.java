package com.sunlightfoundation.adhawk.android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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
		
		WebView webview = Utils.webViewFor(this);
		
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (!url.endsWith("/"))
					url = url + "/";
				
				Intent intent = new Intent(AdTop.this, AdDetails.class);
				intent.putExtra("details", new AdHawkServer.Response(url));
				startActivity(intent);
				return false;
			}
		});
		
		webview.loadUrl(getResources().getString(R.string.site_top_ads));
	}
}