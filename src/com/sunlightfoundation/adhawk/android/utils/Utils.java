package com.sunlightfoundation.adhawk.android.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.sunlightfoundation.adhawk.android.AdHawkServer;
import com.sunlightfoundation.adhawk.android.R;


public class Utils {
	
	public static WebView webViewFor(Activity activity) {
		return webViewFor(activity, -1);
	}
	
	// is this URL an AdHawk ad details URL?
	public static boolean isDetailsUrl(String url) {
		
		return true;
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	public static WebView webViewFor(Activity activity, int id) {
		if (id < 0)
			id = R.id.content;
		
		WebView results = (WebView) activity.findViewById(id);
		WebSettings settings = results.getSettings();
		settings.setUserAgentString(AdHawkServer.USER_AGENT);
		settings.setJavaScriptEnabled(true);
		return results;
	}
}