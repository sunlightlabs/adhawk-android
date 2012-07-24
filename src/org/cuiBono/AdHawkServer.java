package org.cuiBono;

import java.io.IOException;
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
	
	public static String findAd(String fingerprint) throws AdHawkException {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("fingerprint", fingerprint);
		params.put("lat", 0);
		params.put("lon", 0);
		JSONObject response = postTo("http://adhawk.sunlightfoundation.com/api/ad/", params);
		
		String url;
		try {
			url = response.getString("result_url");
		} catch (JSONException e) {
			throw new AdHawkException("No result URL in response body, or other problem", e);
		} catch (Exception e) {
			throw new AdHawkException("Some problem reading response from Ad Hawk", e);
		}
		
		return url;
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
		//Log.d(TAG, "Fetching: " + url);
		
		HttpPost request = new HttpPost(url);
        request.addHeader("User-Agent", USER_AGENT);
        
        //String body = "{\"fingerprint\":\"eJzFmGtubakOhKfEwzw8HMD2_IdwP9JS776RDvkRtVqR6iTr7LDArioXSSnlSA-o9QWyX9D1BeMJ87xg1Rdsf4GXF0Q8INfyAh0v2O0FNl_wbFFJ8oBU4gWtvuCHHq0XHH_BffcfIfP1gOovGPUF01-w4gWuDyhpvEDWA5LIC3p5gbYXnPwCjwfkKi9o9oKRXjD7C95dMH1A4esBNT8g5fOCH3oUL1B5wdYXvHuU1guqvuDdhV_VOT3ghzr3-QKtL9jrAT_USvoL2nnBsBes9YLzBB8P-F0X7AXNX_BDF-YLfuDzeUHJL_hVB_0FP3RwPuCHHt0h_Ef4SSnPfPXu0dov-I3n_NCj9oL_rEfxgt-4WYsX_ItzQV9Qygt-06OZX_Dv9ajMP4JKNsn9JD8nb5Krr2nwPHTOGCtFm7pTyVlbaa699Jxz86ItzmwlLNbaq_bto--6Z6s6j62uueuoMQkPkfjMmCXtXg_zcUyJyRrVzbb5almmznG6J7xITUOjWHKW00YurSI9L1bSe8FRqyMPbbvvxbQdLcnIpc2TzdxbtNZqHLNjZ8XK7tVUiZljc8oYI4qeNFN1SUdaHmG1ap0rdp2FD_sQFp3L2JLVsqY3adM2Z1_H2ypuTbRLV51e8hkSfgjI-lUICwbx1_FHidxU9LApW-WrBkf6MC8WxvFnObHPnHTI8u6z79OtjbjfhfCPnlb2ZpMGP05frRapHrGLy_lAzVn027N_wD5rD6-HODwoslunxnVTPt85SZnl5lWrJzhmcLYZ6slbsSWFmvHbxdHFktZLZccpamguy84W95lV3U6sPtlFmKw8xaQmo5t8bzFlD0rS1tlUXJpvbm39sqIu98ijy2hUupzeO80ZjSVParlygKxcW-D_Yp8DBohGzTCTS-M2yyKR94lc15I41LvKSHpoC_7TTDmRVisl-2Gv636si_fmbPKsTszn9ZDbx6wb57hEODs6b2F_MtiRuuaNd5czbhtqjLqkrl5QQhKbSAeduLR6dr9cYB0UxSV_f8AT_fz27B9gI7IuFd5Y56Vl2xle52JcF8qETWYcchQqnPlE4tE6yfbsTXw3SNT6Ep9ONWYSlZ26JEO9Q_i5ulKKQQUKV5t-yszskCbUEG-jGBsOqHqKhvVAmGlnKa3DLbYyfZ0MfxE_qxLaA2Ustto2PtHTPdoecvRAKm6_s2EWyfpg1MpQ5E3nEIv3cxavttzv7ez0q260cyx23hARXVf_euUaeuIKg6pf5zwbPfbSDqVhOSR0Th-uq6Y5hQ5CBkxoR2AZ3BtDtyAnm1DybyhL1_727AOKAwi1yfVWBadqmTpT7r0O5RvTN5uebDJ3EYevp7RrSl67Nmu0Qua0uk9lxZUgi_XVb4-35lOO3CrpwtqO-ZS2g9o3OeHmEnVbto0fXeVQ644x8cq2fVbfZR8miFXWwAmwv2SQUyqiUao6cD8sKHeokFvm9dh3scrtmv0P4jCNPT4wlrnuBZVlsaiQdc1_9Fh-dGDoEKqq2eAc6ExjMA-MV6xN89rSrKUep1My8erJge8fs2JNSPE3dNtJvj37wPW06_0zL69f9xZUm9RHgSsy_CDky0oqDgtXzNmNj421LEd0iGZHMfyNJw9mVKqjIY3JEZpFyYbBNPWO-4WXPc-erD1s9zxkUT3RYqVL9rnR87lzwAXXzVJ7qswKageHMetcHY7zX9g-R22bQ8-BwNfIGbPCu3knDUoLVo9EAYX5glY5ARQ5LEjnaa0sJtQqHan1Em0NuxuZEKRsyMRQSJwCosx5D6iRHIucVa8gcE1ZrFDimjRF095stPYX9VYg57waIsX1PlCgwfz27APsCouMDhNiKBoXBjNkWK5Tll9fTAq1FhM1l6-B0ca4vxvl3hk6yt1akD7R5jClOOTiY_LX33SqL0l4pXXMvK6EMSB8h-ERDP8DC4NxqnWoC05T4VZBu7jAWmX4xtgZtskT746rztLicsah17iuxySrdUGOhQveOVWwFTgaqdrALqpXPbM0zoTL37fws0N0ZDboadU1sDAxpkK9mVJwlNJJDSddm2SMk9rYA5aGaxAqBKrMdBlMQhg8adxDmma_QaS2Dk-u2x1RvNUxXVrZmbOTWwPKldTxuckxPhBW8vz27AO1oAHYy_QZIwU0tL3uMQbpAZckzGyyinFIw27VmdU2MEKbkTHLQbbahBNME-lVHCC18rXU4gCzZtm8AFoR7ryr3b8iBDkMKylUZrKDgbLwGc-FMcoE6QOXqOco85xdEbTwgpMGyYfikzLuxfwomZFJRYco11fEQOF7apM12urpkDM8R06FzQyKt5mzjAYcVAR32LHJSH3RAzQ65-UPZoU2DGdwaq460AQNxLtL0YW42iRTsFcL6VvsOmFFKlrvMM9MIerTh-Bfa5cyYNn1o9ratRZmOPLUWcdNCLv2DzCd8Mj_f_YBWI8UhMhLUl5BQNkuc5dJDsXA4OguDSK2hDczMggNuBEpd5G2yS-IwBFdOmwJxfNLhjHdEUSI2tQWL7o-A9fjNj8rdr6Ip1s31fUZvZ1Jqm7OyQuBm7RRxTEkRo2aMDX1KgVaEq8Zz_dnHVQfAqE3yk09S4y9uVUfcuuUEXAXQZH6WJby43LVOoERN8PngrFO4MkIIQ_CP-9DJTI2CRfnxSwW48IYS_060CTcUWZS_iLF-qQXLRPNkhmDjkxOqs27kBdP8AGmO1MXeRE8XDLjdya9RTl0jHZiL-c0qvIByHK-P_vA_wBkV2bO\",\"lat\": \"-47.9\", \"lon\": \"-123.43\"}";
        String body = bodyFor(params);
        
        request.addHeader("Content-type", "application/json");
        
        //Log.d(TAG, body);
        
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