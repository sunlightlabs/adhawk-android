package org.cuiBono;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class AdHawk extends Activity {
	public static final String TAG = "AdHawk";
	 
	private TagAdTask task;
	private TextView results;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		
		setupControls();
	}
	
	public void setupControls() {
		this.findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				tagAd();
			}
		});
		
		results = (TextView) findViewById(R.id.response);
	}
	
	public void tagAd() {
		if (task == null)
			this.task = ((TagAdTask) new TagAdTask(this).execute());
	}
	
	public void onTag(String body) {
		results.setText(body);
	}
	
	public void onTag(AdHawkException exception) {
		results.setText("EXCEPTION: " + exception.getMessage());
	}

//	public JSONObject getAdArticles(String id) {
//
//		DefaultHttpClient client = new DefaultHttpClient();
//		HttpGet httpGet = new HttpGet(url + id);
//
//		try {
//			HttpResponse response = client.execute(httpGet);
//			InputStream content = response.getEntity().getContent();		
//			StringWriter writer = new StringWriter();
//			IOUtils.copy(content, writer, "utf-8");
//			return new JSONObject (writer.toString()); 
//			
//		} catch (ClientProtocolException e) {
//			Log.e(tag, "client problem:" + e.getMessage());
//			throw new AdHawkException("client problem",e);
//		} catch (IOException e) {
//			Log.e(tag, "IO problem:" + e.getMessage());
//			throw new AdHawkException("IO problem",e);
//		} catch (JSONException e) {
//			Log.e(tag, "JSON problem:" + e.getMessage());
//			throw new AdHawkException("JSON problem",e);
//		}
//	}
	 
	 private class TagAdTask extends AsyncTask<Void, String, String> {
		private AdHawkException exception;
		private AdHawk activity;
		
		public TagAdTask(AdHawk activity) {
			this.activity = activity;
		}
		
		@Override
		protected String doInBackground(Void... nothing) {

//				publishProgress("recording in progress!");
//				
//				String fname = record();
//				
//				Log.e(tag, "fname is " + fname);
//
//				publishProgress("generating fingerprint!");
//
//				String code = getCodeGen(fname);
//				Log.e(tag, "audio fingerprint is " + code);
			
			String fingerprint = "eJzFmGtubakOhKfEwzw8HMD2_IdwP9JS776RDvkRtVqR6iTr7LDArioXSSnlSA-o9QWyX9D1BeMJ87xg1Rdsf4GXF0Q8INfyAh0v2O0FNl_wbFFJ8oBU4gWtvuCHHq0XHH_BffcfIfP1gOovGPUF01-w4gWuDyhpvEDWA5LIC3p5gbYXnPwCjwfkKi9o9oKRXjD7C95dMH1A4esBNT8g5fOCH3oUL1B5wdYXvHuU1guqvuDdhV_VOT3ghzr3-QKtL9jrAT_USvoL2nnBsBes9YLzBB8P-F0X7AXNX_BDF-YLfuDzeUHJL_hVB_0FP3RwPuCHHt0h_Ef4SSnPfPXu0dov-I3n_NCj9oL_rEfxgt-4WYsX_ItzQV9Qygt-06OZX_Dv9ajMP4JKNsn9JD8nb5Krr2nwPHTOGCtFm7pTyVlbaa699Jxz86ItzmwlLNbaq_bto--6Z6s6j62uueuoMQkPkfjMmCXtXg_zcUyJyRrVzbb5almmznG6J7xITUOjWHKW00YurSI9L1bSe8FRqyMPbbvvxbQdLcnIpc2TzdxbtNZqHLNjZ8XK7tVUiZljc8oYI4qeNFN1SUdaHmG1ap0rdp2FD_sQFp3L2JLVsqY3adM2Z1_H2ypuTbRLV51e8hkSfgjI-lUICwbx1_FHidxU9LApW-WrBkf6MC8WxvFnObHPnHTI8u6z79OtjbjfhfCPnlb2ZpMGP05frRapHrGLy_lAzVn027N_wD5rD6-HODwoslunxnVTPt85SZnl5lWrJzhmcLYZ6slbsSWFmvHbxdHFktZLZccpamguy84W95lV3U6sPtlFmKw8xaQmo5t8bzFlD0rS1tlUXJpvbm39sqIu98ijy2hUupzeO80ZjSVParlygKxcW-D_Yp8DBohGzTCTS-M2yyKR94lc15I41LvKSHpoC_7TTDmRVisl-2Gv636si_fmbPKsTszn9ZDbx6wb57hEODs6b2F_MtiRuuaNd5czbhtqjLqkrl5QQhKbSAeduLR6dr9cYB0UxSV_f8AT_fz27B9gI7IuFd5Y56Vl2xle52JcF8qETWYcchQqnPlE4tE6yfbsTXw3SNT6Ep9ONWYSlZ26JEO9Q_i5ulKKQQUKV5t-yszskCbUEG-jGBsOqHqKhvVAmGlnKa3DLbYyfZ0MfxE_qxLaA2Ustto2PtHTPdoecvRAKm6_s2EWyfpg1MpQ5E3nEIv3cxavttzv7ez0q260cyx23hARXVf_euUaeuIKg6pf5zwbPfbSDqVhOSR0Th-uq6Y5hQ5CBkxoR2AZ3BtDtyAnm1DybyhL1_727AOKAwi1yfVWBadqmTpT7r0O5RvTN5uebDJ3EYevp7RrSl67Nmu0Qua0uk9lxZUgi_XVb4-35lOO3CrpwtqO-ZS2g9o3OeHmEnVbto0fXeVQ644x8cq2fVbfZR8miFXWwAmwv2SQUyqiUao6cD8sKHeokFvm9dh3scrtmv0P4jCNPT4wlrnuBZVlsaiQdc1_9Fh-dGDoEKqq2eAc6ExjMA-MV6xN89rSrKUep1My8erJge8fs2JNSPE3dNtJvj37wPW06_0zL69f9xZUm9RHgSsy_CDky0oqDgtXzNmNj421LEd0iGZHMfyNJw9mVKqjIY3JEZpFyYbBNPWO-4WXPc-erD1s9zxkUT3RYqVL9rnR87lzwAXXzVJ7qswKageHMetcHY7zX9g-R22bQ8-BwNfIGbPCu3knDUoLVo9EAYX5glY5ARQ5LEjnaa0sJtQqHan1Em0NuxuZEKRsyMRQSJwCosx5D6iRHIucVa8gcE1ZrFDimjRF095stPYX9VYg57waIsX1PlCgwfz27APsCouMDhNiKBoXBjNkWK5Tll9fTAq1FhM1l6-B0ca4vxvl3hk6yt1akD7R5jClOOTiY_LX33SqL0l4pXXMvK6EMSB8h-ERDP8DC4NxqnWoC05T4VZBu7jAWmX4xtgZtskT746rztLicsah17iuxySrdUGOhQveOVWwFTgaqdrALqpXPbM0zoTL37fws0N0ZDboadU1sDAxpkK9mVJwlNJJDSddm2SMk9rYA5aGaxAqBKrMdBlMQhg8adxDmma_QaS2Dk-u2x1RvNUxXVrZmbOTWwPKldTxuckxPhBW8vz27AO1oAHYy_QZIwU0tL3uMQbpAZckzGyyinFIw27VmdU2MEKbkTHLQbbahBNME-lVHCC18rXU4gCzZtm8AFoR7ryr3b8iBDkMKylUZrKDgbLwGc-FMcoE6QOXqOco85xdEbTwgpMGyYfikzLuxfwomZFJRYco11fEQOF7apM12urpkDM8R06FzQyKt5mzjAYcVAR32LHJSH3RAzQ65-UPZoU2DGdwaq460AQNxLtL0YW42iRTsFcL6VvsOmFFKlrvMM9MIerTh-Bfa5cyYNn1o9ratRZmOPLUWcdNCLv2DzCd8Mj_f_YBWI8UhMhLUl5BQNkuc5dJDsXA4OguDSK2hDczMggNuBEpd5G2yS-IwBFdOmwJxfNLhjHdEUSI2tQWL7o-A9fjNj8rdr6Ip1s31fUZvZ1Jqm7OyQuBm7RRxTEkRo2aMDX1KgVaEq8Zz_dnHVQfAqE3yk09S4y9uVUfcuuUEXAXQZH6WJby43LVOoERN8PngrFO4MkIIQ_CP-9DJTI2CRfnxSwW48IYS_060CTcUWZS_iLF-qQXLRPNkhmDjkxOqs27kBdP8AGmO1MXeRE8XDLjdya9RTl0jHZiL-c0qvIByHK-P_vA_wBkV2bO";
			
			try {
				return AdHawkServer.findAd(fingerprint);			
			} catch (AdHawkException e) {
				this.exception = e;
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			activity.task = null;
			
			if (result != null) {
				Log.d(TAG, result);
				activity.onTag(result);
			} else if (this.exception != null) {
				activity.onTag(this.exception);
			} else {
				activity.onTag(new AdHawkException("Unknown error."));
			}
		}
	}

}
