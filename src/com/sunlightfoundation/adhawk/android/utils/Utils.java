package com.sunlightfoundation.adhawk.android.utils;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sunlightfoundation.adhawk.android.AdDetails;
import com.sunlightfoundation.adhawk.android.AdHawkServer;
import com.sunlightfoundation.adhawk.android.R;
import com.sunlightfoundation.adhawk.android.TitledWebView;


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
		
		final String aboutUrl = activity.getResources().getString(R.string.site_about);
		
		results.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (Utils.isDetailsUrl(url)) {
					Intent intent = new Intent(activity, AdDetails.class);
					intent.putExtra("url", url);
					activity.startActivity(intent);
					return true;
				} else if (url.equals(aboutUrl)) {
					activity.startActivity(new Intent(activity, TitledWebView.class)
						.putExtra("type", TitledWebView.SITE_ABOUT)
					);
					return true;
				} else {
					activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
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
	
	public static void doFeedback(Activity activity) {
		Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", activity.getResources().getString(R.string.contact_email), null));
		intent.putExtra(Intent.EXTRA_SUBJECT, activity.getResources().getString(R.string.contact_subject));
		activity.startActivity(intent);
	}
	
	public static void goReview(Activity activity) {
		String packageName = activity.getResources().getString(R.string.package_name);
		String uri = "market://details?id=" + packageName;
		try {	
			activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
		} catch(ActivityNotFoundException e) {
			// swallow
		}
	}
}