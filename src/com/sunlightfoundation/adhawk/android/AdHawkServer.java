package com.sunlightfoundation.adhawk.android;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class AdHawkServer {

	public static final String USER_AGENT= "com.sunlightfoundation.adhawk.android";
	public static final String TAG = "AdHawkServer";
	
	static class Response implements Serializable {
		private static final long serialVersionUID = 7L;
		
		public String resultUrl, shareText;
				
		public Response(JSONObject object) throws AdHawkException {
			try {
				resultUrl = object.getString("result_url");
				shareText = object.getString("share_text");
				
				if (resultUrl == "null")
					resultUrl = null;
				
				// fix in case the server drops the trailing slash
				if (resultUrl != null && !resultUrl.endsWith("/"))
					resultUrl += "/";
				
				if (shareText == "null")
					shareText = null;
				
			} catch (JSONException e) {
				throw new AdHawkException("No result URL in response body, or other problem", e);
			} catch (Exception e) {
				throw new AdHawkException("Some problem reading response from Ad Hawk", e);
			}
		}
	}
	
	public static AdHawkServer.Response getAd(String jsonUrl) throws AdHawkException {
		return new AdHawkServer.Response(getFrom(jsonUrl));
	}
	
	public static AdHawkServer.Response findAd(String fingerprint) throws AdHawkException {
		return findAd(fingerprint, 0, 0);
	}
	
	public static AdHawkServer.Response findAd(String fingerprint, double lat, double lon) throws AdHawkException {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("fingerprint", fingerprint);
		params.put("lat", lat);
		params.put("lon", lon);
		
		return new AdHawkServer.Response(postTo("http://adhawk.sunlightfoundation.com/api/ad/", params));
	}
	
	public static String bodyFor(Map<String,Object> params, boolean debug) {
		return new JSONObject(params).toString();
	}
	
	public static String bodyFor(Map<String,Object> params) {
		return bodyFor(params, false);
	}
	
	public static JSONObject responseFor(String body) throws JSONException {
		return new JSONObject(body);
	}
	
	public static JSONObject getFrom(String url) throws AdHawkException {
		HttpGet request = new HttpGet(url);
        return makeRequest(request, url);
	}
	
	public static JSONObject postTo(String url, Map<String,Object> params) throws AdHawkException {
		HttpPost request = new HttpPost(url);
        String body = bodyFor(params);
        
        Log.d(TAG, "Including body: " + body);
        
        try {
        	request.setEntity(new StringEntity(body));
        } catch (UnsupportedEncodingException e) {
        	throw new AdHawkException("Unsupported encoding for some reason: " + body, e);
		}
        
        return makeRequest(request, url);
	}
	
	public static JSONObject makeRequest(HttpUriRequest request, String url) throws AdHawkException {
		try {
        	request.addHeader("User-Agent", USER_AGENT);
        	request.addHeader("Content-type", "application/json");
        	
        	Log.d(TAG, "Requesting: " + request.getClass().getSimpleName() + " - " + url + "\n\n");
        	
	        HttpResponse response = new DefaultHttpClient().execute(request);
	        int statusCode = response.getStatusLine().getStatusCode();
	        
	        String responseBody = EntityUtils.toString(response.getEntity());
        	Log.d(TAG, "Responsing: " + responseBody);
        	
	        if (statusCode == HttpStatus.SC_OK)
	        	return responseFor(responseBody);
	        else if (statusCode == HttpStatus.SC_NOT_FOUND)
	        	throw new AdHawkException("404 Not Found from " + url);
	        else
	        	throw new AdHawkException("Bad status code " + statusCode + " on fetching JSON from " + url);
        } catch (JSONException e) {
        	throw new AdHawkException("Problem parsing response JSON", e);
        } catch (ClientProtocolException e) {
	    	throw new AdHawkException("Problem fetching JSON from " + url, e);
	    } catch (IOException e) {
	    	throw new AdHawkException("Problem fetching JSON from " + url, e);
	    }
	}
}