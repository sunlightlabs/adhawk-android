package org.cuiBono;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class AdHawkServer {

	public static final String USER_AGENT= "src.org.cuiBono.AdHawkServer";
	public static final String TAG = "AdHawkServer";
	
	static class Response implements Serializable {
		private static final long serialVersionUID = 7L;
		
		public String resultUrl;
		
		public Response(JSONObject object) throws AdHawkException {
			try {
				resultUrl = object.getString("result_url");
			} catch (JSONException e) {
				throw new AdHawkException("No result URL in response body, or other problem", e);
			} catch (Exception e) {
				throw new AdHawkException("Some problem reading response from Ad Hawk", e);
			}
		}
	}
	
	public static AdHawkServer.Response findAd(String fingerprint) throws AdHawkException {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("fingerprint", fingerprint);
		params.put("lat", 0);
		params.put("lon", 0);
		
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
	
	public static JSONObject postTo(String url, Map<String,Object> params) throws AdHawkException {
		HttpPost request = new HttpPost(url);
        request.addHeader("User-Agent", USER_AGENT);
        
        String body = bodyFor(params);
        
        request.addHeader("Content-type", "application/json");
        
        try {
        	request.setEntity(new StringEntity(body));
        	
	        HttpResponse response = new DefaultHttpClient().execute(request);
	        int statusCode = response.getStatusLine().getStatusCode();
	        
	        if (statusCode == HttpStatus.SC_OK) {
	        	String responseBody = EntityUtils.toString(response.getEntity());
	        	Log.i(TAG, "Received: " + responseBody);
	        	return responseFor(responseBody);
	        } else if (statusCode == HttpStatus.SC_NOT_FOUND)
	        	throw new AdHawkException("404 Not Found from " + url);
	        else
	        	throw new AdHawkException("Bad status code " + statusCode + " on fetching JSON from " + url);
        } catch (JSONException e) {
        	throw new AdHawkException("Problem parsing response JSON", e);
        } catch (UnsupportedEncodingException e) {
        	throw new AdHawkException("Unsupported encoding for some reason", e);
		} catch (ClientProtocolException e) {
	    	throw new AdHawkException("Problem fetching JSON from " + url, e);
	    } catch (IOException e) {
	    	throw new AdHawkException("Problem fetching JSON from " + url, e);
	    }
	}
}