package com.sunlightfoundation.adhawk.android.utils;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sunlightfoundation.adhawk.android.AdDetails;
import com.sunlightfoundation.adhawk.android.AdHawkServer;
import com.sunlightfoundation.adhawk.android.R;


public class Utils {
	
	public static WebView webViewFor(Activity activity) {
		return webViewFor(activity, -1);
	}
	
	// is this URL an AdHawk ad details URL?
	public static boolean isDetailsUrl(String url) {
		return (url.indexOf("/ad/top/") > 0);
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	public static WebView webViewFor(final Activity activity, int id) {
		if (id < 0)
			id = R.id.content;
		
		WebView results = (WebView) activity.findViewById(id);
		WebSettings settings = results.getSettings();
		settings.setJavaScriptEnabled(true);
		
		results.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (Utils.isDetailsUrl(url)) {
					Intent intent = new Intent(activity, AdDetails.class);
					intent.putExtra("url", url);
					activity.startActivity(intent);
					return true;
				} else {
					// override even regular URLs, to force the X-Client-App header to get set each time
					Utils.loadUrl(view, url);
					return true;
				}
			}
		});
		
		return results;
	}
	
	public static void loadUrl(WebView webview, String url) {
		Map<String,String> headers = new HashMap<String,String>();
		headers.put("X-Client-App", AdHawkServer.USER_AGENT);
		webview.loadUrl(url, headers);
	}
}