package org.cuiBono.utils;

import org.cuiBono.AdHawkServer;
import org.cuiBono.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.webkit.WebSettings;
import android.webkit.WebView;


public class Utils {
	
	public static WebView webViewFor(Activity activity) {
		return webViewFor(activity, -1);
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