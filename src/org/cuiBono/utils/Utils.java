package org.cuiBono.utils;

import org.cuiBono.AdHawkServer;
import org.cuiBono.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.webkit.WebSettings;
import android.webkit.WebView;


public class Utils {
	
	@SuppressLint("SetJavaScriptEnabled")
	public static WebView webViewFor(Activity activity) {
		WebView results = (WebView) activity.findViewById(R.id.content);
		WebSettings settings = results.getSettings();
		settings.setUserAgentString(AdHawkServer.USER_AGENT);
		settings.setJavaScriptEnabled(true);
		return results;
	}
}