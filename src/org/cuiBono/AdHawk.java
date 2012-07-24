package org.cuiBono;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteOrder;

import org.apache.commons.io.EndianUtils;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class AdHawk extends Activity {
	public static final String TAG = "AdHawk";
	
	// in milliseconds, how long to record for
	public static final int RECORD_TIME = 2000; // 15000
	
	// load in echo nest library
	static {
		System.loadLibrary("echonest-codegen");
	}
	private native String getCodeGen(String fname);
	
	 
	private TagAdTask task;
	private TextView error, progress;
	private WebView results;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		
		setupControls();
	}
	
	private class MyWebViewClient extends WebViewClient {
		
		@Override
		public void onReceivedHttpAuthRequest(WebView view,
		        HttpAuthHandler handler, String host, String realm) {

		    handler.proceed("blannon", "122cuibono!WWW");

		}
		
		public void onPageFinished(WebView view, String url) {
            Log.i(TAG, "Finished loading URL: " +url);
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Log.e(TAG, "Error: " + description);
        }
	}
	
	public void setupControls() {
		this.findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				tagAd();
			}
		});
		
		error = (TextView) findViewById(R.id.error);
		progress = (TextView) findViewById(R.id.progress);
		
		results = (WebView) findViewById(R.id.results);
		results.setBackgroundColor(0);
		
//		String host = getResources().getString(R.string.site_host);
//		String realm = getResources().getString(R.string.site_realm);
//		String username = getResources().getString(R.string.site_username);
//		String password = getResources().getString(R.string.site_password);
//		results.setHttpAuthUsernamePassword(host, realm, username, password);
		
//		results.setWebViewClient(new MyWebViewClient());
//		
//		results.loadUrl("http://adhawk.sunlightfoundation.com/ad/not_found.html");
	}
	
	public void tagAd() {
		if (task == null)
			this.task = ((TagAdTask) new TagAdTask(this).execute());
	}
	
	public void onTag(String url) {
		results.loadUrl(url);
	}
	
	public void onTag(AdHawkException exception) {
		error.setText(exception.getMessage());
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
			publishProgress("Recording...");
			
			String fname = record();
			
			Log.i(TAG, "Recording: " + fname);
			publishProgress("Generating fingerprint...");

//			String code = getCodeGen(fname);
//			Log.i(TAG, "Fingerprint: " + code);
			
			publishProgress("Done.");
			
			String fingerprint = "eJy1mVmSYzcORbdEcAK4HHDA_pfQh-oIp7sixPyoaLt87VIpnzjcCXJKsnJ6wNEH5OwvqOUF_Qk6X8A_DzjrASW1F-QntPICbS_wJ6zxgnvYX6FKfkGpL-DnH-D7BSseIEtfEPaALP0FNb-g1xeM8QI_L1hPCHlAkf2CKi9o9QV_w7qpL9j2gHqv4juUeIH6Czy94B72d_gI4hv84mZJXvD2urZeMPoLTjzgr5hzzfI7XIP_DltfEPMBNZ8XtPwC3S_4xZHKC3Z9wTkPaGk84JcM3fsB-cbOd8j9BcVfUOMFfb_AxgvcX7D6C-5xfoWSnlDyC2p_QfcXWH7BjazvEPkBNe8X9PGCWV7wV3zuD5C5X7DbA3KqLyj2AnvCzC9Y6wXvdL4i_g6_8Gq84EbWd3i3vkgP-MVju7zA9QW7vOCW6K_wd7yKB_zCq_yEv_G6X1g3X3AN4Cv8wrpbK77D7effodsLXF7w9rpPJfkG9V7yd3g7YfMX-Av-xq_kXsVX-H-y7u11-oIn60ryF_ySofaCt9e9M3ScFzzb5m-80hf8wqvxgqfX_V1KPkH8Bdfgv4PaC37xuvaCd8K-e520FxR5wS9e9-JkTfkFv2RofoGdFzx45Vj4THPvk32pWevhpZV8ejuL-dhXzdN769jiPlLWqWnrad66dmtryZ7V7UjEstrd0q7hbWy2lJMli-E21urd96oq0WwX8z3L8N6T9iUxIy0PfiQZeVIljscedfPv0kYdPq3rcq2nycj8BwafZa8y66JBzNmmltamzD6bfVbVxMPCqtKnp0_WWrRtigyv5ZZXIsXNl6ees50Z_MTZIcNza0et71FKGaPY4UT26P0sFTGdaiF-lqyzCsvTFmN4RN33IsYwaZPDYcejn75SG13CZWg_p20t04aUHuvkPbQcjqrgsKeMFRFz-ymzREt1JNHguRzcqt5CtPks-jmr3bRw_PuoemCjMdnpkLam7e1rHM0mZ0_GeVZMdx8WdYbOkgdmMEYbradjpW9PZ-_IFjPXddqYY-QpsTkuOcVmPSHN2j6bjcx86vqBz_n98doPaArplANhz3z6SJwsW8rptLxdm-7gTtMS27zcchv8tdnLPMaN98Y9cHFnn7XOXG3BEXOT6G3X3uXUuqfbnoutFukwgbdyhLO0WUV77tympm2JP3Q5MD31w8aVjXOxskbOuyifIMN653xTjxgTWxtHRLwmOS1O4ljFXbLCe-7tEFwniRvi6WXuapa1KtcZzRfsY-X3AKQ3HUt7csizZl_Hq9VyHy0sS3kQ3I3WS2kZ5WSdtdxvlfiTmqtYT2OappHTqjICXt0n24F9nyerjxzQqvfNhrVskXLSHrJ82GKztKCRvbjKWLZMkA5qYK1SA1XrYnNIocHXWjuSOqllR0ercY5ToTaygLecaRaErjYQj2yFGRMVB3JG-auuOgx18fI9ctFdODCf0tXnRl_mRyuCHUYXyccH-pqCZn0H8_-9cMU6ygrOdghsHn3Y3foojUaW7zahbctztNu_G1ZN523C9Dl_AImN-cdrP7CODshn6JYASo2TwRr6arVOzWGlriuiXmLF8ro2nKlj8YdN1Vh9USgjs5xSIbKnNcse4yA5Immw9k1P01NKamuz5w3ZtrBl5MeF4hmGKGGDLg0uDK3fC--Rzzynq8xVcNee2nE0d79jPNwTFJnQyaAv144nkoDZO4cAbXquGU9DE9wWrsd5q05sLWQ2Drf4To7LDFam7Khx2IjJd8azVu7Iva8-ccqG17EdfE5RL_bd-Rzz6r22Xnut8IezwRpmxo7R49q9heshNttnOcJp6pxjL3TC-Q0YcHqpiYtfQgVCpmxiCNcUh-eVKPdLdo4UOwj1vLkVdrOswICta0FS7Fd0Qi70NSYHkDAlwoGTJ6eiof85dyB19jynrYXeo2Onjl32Ynh7mStYzcSYMWO5SUMoeMPk520jktxyw-M2LQ6WbJvxLxA28udr_wBpxn4Vx6GXYjCFrfLoRqBpkzrKPbhEfuHExtXg5DXFCILAU5CgEAntFOHoL5v2gZFkbIGQ-frvCPzmbpUNrNpw5dJ7IWfIyClt97E6sWZnV2w6RvMjmkkoRY-FW68Fg5RQzmtu0kj06JnJ5v22GTPaxGZR-k0rnbFM8UfiA_fmaOpdGRaXPmQdWbEMSH5plXHOmrly3juS3pE7UBe5fiqKK4urwax4JrHSFAVldF8FOXc-VmvhXmq33KFSk1w08PXpG80HKWtqikYHZWQxpW62SQyMEZxbWu2yKugAZe3akJfj37DWLEJKxV0MofHJFcnyuGjTcwoSFm-Elt1rvw8tE8VeQ6XnELP4Tt5drgvWLHieMQLTT-DZEmpBLIrG9R4id8NsLnXdgCT2ys55HOf4pORiG8_nnRwIHBeazZUbCwvF-LpS7KRzAiw2PnT28THowvVllkOXQABpbEPs_A5JscnNU2g42xvBxy3UFhxtbCd5ziEJJ6NzzIKC4P8yIgdxhxuH8w9Qaer647Uf8EIpUjvUGkpJt9EYmnbXiWsg_3vVsK9v1nJIPSdw6I9MbGjw1nNMeAjLoBDSMAo5SODWljSZFN7YKneYaSgzNX7WuZqkivNS81qlC1B0kPptHzCPMMSN4Z8LyXUjD_blexPwa94MT1hTw6UPerdybDoNFL1Ti1qNjYME16PcU0mBYe0zqi0uBCFcOnxSA1N01rDLrhtvJAgOHEDRJyAkK2SNoxvSzMMPmyPusdt6_ZwVRuacCz410oRDvcIaCmLtt9PRO3cm8Ax_ua5ouDraUD4T-kmrBIwkrXfeQmUHateEkG61xRSD0YLIpqm1dNkYPp1jOsxfAclwsi0o7vrYDkO1A1vmRFDNQtdIB_8QvNfxgKDXpN4yYrs6wBypqZMmOvD_Tl-3bBBt8-aMc2-vtdF6jCqMyONG70BVRr1Bsc609S9IhN3647UfaOoJW_RBTkdANsyi8eGwDIrDDoRBU-LXRidYoiTiqVMYqOmcB3eOcLtRn0vWNuNGwsJy71eflD0c4CTo4ZD4liZ2VTHX0e__Ll_3Sz9OrkmnTlAEeEoiAye7AYts_Fu8U9QWxZLWaPih0L-TXoIevKdQ7GA_smDnDBzosCJSytY1PdtKrYIDuVCOGYWGICMcgr45l1G9aUwJhSTaNvvnZ1gWIj535MHXGJfwiWvxxpq4lzPyJ-BtoTfugIinY7AcRhBEjBce1FfHZxCoQTtorG8MWn0lTxkDKJWZhKKq31ZFDSD9GVr8NDWe7EQQtKZUH5yUd2g797tEWjk-R2VLzIZn3rKfbj2m_OH2lUim-WPadHSWrjQj6sX9WMpyrEZ40EP0sp3TZwmD86a5rM6UdC92Hsou8a6ZzrPPpFBg-ifhbWSDwwmjef_Auo7xx2s_UFAMExOzUL9JGdeobhtQBjphTuiVHFHSc9fKEM-gQ1LKuJdCY7ojHMbjOSi4dH_PjJ56aU7ucLHU5s6PBRURr6WfJN7J_GJZFkmDt16fZ0IUeMuRX9OlU7Nkykzulf0wjASd4zAC0G5oTnR2BlGG62XkBANCnhiI4bMb-5KaFCExYW8iQe6ir_IOA2AgCUoTK3fsZ36mA-NTG5PfIRCwUbwRM2No54ETp9m4J_2Vas7ps0X6eDK0PwbeRCCOxpHx8-iF6sLQJRvXG0ydDDqZwUhOZzRg0Gyx8m13E60y2l0nxDYgSGkcBicI-yHIWOyJzqhOhz-VHkZb9XEtleIfx4yVOyV2LcZezCnopxTkO4Ow_Js5FBdyK2XcnKrGQI5M1_2UxX1TcIS5DWUy30A3Gj3szDcSFouc1DoqICmZ4TWnh2vTAu63ZIwu9IScKvvsk18V-4TCxHOMtSEKFv4D2BNt6H9f-wFm1JqdvxP6xLsRz0C-VGYM57DGwxTeED--gMfkvApEMM5-y9E7UMP7RIOgrVOCWGUTWgrFCDYrU3zD0jGslPxGGatsNHVyROfVe2N8uGMAWQuNsPeMFWKO1wxyg6Vx57w79dA-UDvmM0Nv_SQNNRgI6HSmYnM6NttIWG4cFpE4odwebd7q4R0YAPMZAxEzQZDwTL63oZTE-E6YECj5XinD97E7mnI2NCXITSpRwDITO5mIXlLZiW0RwJ3RkokX-ZKMCHLQJncIwwmfgUPkIYLKaAnQmeunOnCVQTPC6_v9EkMKcXsg0UYKfh0Zhw0mhXYVtCrTVD7__Xrqs0skxmQ6Z6eGO3k7nLnVyy0WcxamukLCFQiNh0u5cVNw43Hn74JVbhhDxULZd9RIthAX1mkanPm6g-PI9gM0Va78f1_7gf8AZG_Eyw=="; 
					// "eJy1mVmSYzcORbdEcAK4HHDA_pfQh-oIp7sixPyoaLt87VIpnzjcCXJKsnJ6wNEH5OwvqOUF_Qk6X8A_DzjrASW1F-QntPICbS_wJ6zxgnvYX6FKfkGpL-DnH-D7BSseIEtfEPaALP0FNb-g1xeM8QI_L1hPCHlAkf2CKi9o9QV_w7qpL9j2gHqv4juUeIH6Czy94B72d_gI4hv84mZJXvD2urZeMPoLTjzgr5hzzfI7XIP_DltfEPMBNZ8XtPwC3S_4xZHKC3Z9wTkPaGk84JcM3fsB-cbOd8j9BcVfUOMFfb_AxgvcX7D6C-5xfoWSnlDyC2p_QfcXWH7BjazvEPkBNe8X9PGCWV7wV3zuD5C5X7DbA3KqLyj2AnvCzC9Y6wXvdL4i_g6_8Gq84EbWd3i3vkgP-MVju7zA9QW7vOCW6K_wd7yKB_zCq_yEv_G6X1g3X3AN4Cv8wrpbK77D7effodsLXF7w9rpPJfkG9V7yd3g7YfMX-Av-xq_kXsVX-H-y7u11-oIn60ryF_ySofaCt9e9M3ScFzzb5m-80hf8wqvxgqfX_V1KPkH8Bdfgv4PaC37xuvaCd8K-e520FxR5wS9e9-JkTfkFv2RofoGdFzx45Vj4THPvk32pWevhpZV8ejuL-dhXzdN769jiPlLWqWnrad66dmtryZ7V7UjEstrd0q7hbWy2lJMli-E21urd96oq0WwX8z3L8N6T9iUxIy0PfiQZeVIljscedfPv0kYdPq3rcq2nycj8BwafZa8y66JBzNmmltamzD6bfVbVxMPCqtKnp0_WWrRtigyv5ZZXIsXNl6ees50Z_MTZIcNza0et71FKGaPY4UT26P0sFTGdaiF-lqyzCsvTFmN4RN33IsYwaZPDYcejn75SG13CZWg_p20t04aUHuvkPbQcjqrgsKeMFRFz-ymzREt1JNHguRzcqt5CtPks-jmr3bRw_PuoemCjMdnpkLam7e1rHM0mZ0_GeVZMdx8WdYbOkgdmMEYbradjpW9PZ-_IFjPXddqYY-QpsTkuOcVmPSHN2j6bjcx86vqBz_n98doPaArplANhz3z6SJwsW8rptLxdm-7gTtMS27zcchv8tdnLPMaN98Y9cHFnn7XOXG3BEXOT6G3X3uXUuqfbnoutFukwgbdyhLO0WUV77tympm2JP3Q5MD31w8aVjXOxskbOuyifIMN653xTjxgTWxtHRLwmOS1O4ljFXbLCe-7tEFwniRvi6WXuapa1KtcZzRfsY-X3AKQ3HUt7csizZl_Hq9VyHy0sS3kQ3I3WS2kZ5WSdtdxvlfiTmqtYT2OappHTqjICXt0n24F9nyerjxzQqvfNhrVskXLSHrJ82GKztKCRvbjKWLZMkA5qYK1SA1XrYnNIocHXWjuSOqllR0ercY5ToTaygLecaRaErjYQj2yFGRMVB3JG-auuOgx18fI9ctFdODCf0tXnRl_mRyuCHUYXyccH-pqCZn0H8_-9cMU6ygrOdghsHn3Y3foojUaW7zahbctztNu_G1ZN523C9Dl_AImN-cdrP7CODshn6JYASo2TwRr6arVOzWGlriuiXmLF8ro2nKlj8YdN1Vh9USgjs5xSIbKnNcse4yA5Immw9k1P01NKamuz5w3ZtrBl5MeF4hmGKGGDLg0uDK3fC--Rzzynq8xVcNee2nE0d79jPNwTFJnQyaAv144nkoDZO4cAbXquGU9DE9wWrsd5q05sLWQ2Drf4To7LDFam7Khx2IjJd8azVu7Iva8-ccqG17EdfE5RL_bd-Rzz6r22Xnut8IezwRpmxo7R49q9heshNttnOcJp6pxjL3TC-Q0YcHqpiYtfQgVCpmxiCNcUh-eVKPdLdo4UOwj1vLkVdrOswICta0FS7Fd0Qi70NSYHkDAlwoGTJ6eiof85dyB19jynrYXeo2Onjl32Ynh7mStYzcSYMWO5SUMoeMPk520jktxyw-M2LQ6WbJvxLxA28udr_wBpxn4Vx6GXYjCFrfLoRqBpkzrKPbhEfuHExtXg5DXFCILAU5CgEAntFOHoL5v2gZFkbIGQ-frvCPzmbpUNrNpw5dJ7IWfIyClt97E6sWZnV2w6RvMjmkkoRY-FW68Fg5RQzmtu0kj06JnJ5v22GTPaxGZR-k0rnbFM8UfiA_fmaOpdGRaXPmQdWbEMSH5plXHOmrly3juS3pE7UBe5fiqKK4urwax4JrHSFAVldF8FOXc-VmvhXmq33KFSk1w08PXpG80HKWtqikYHZWQxpW62SQyMEZxbWu2yKugAZe3akJfj37DWLEJKxV0MofHJFcnyuGjTcwoSFm-Elt1rvw8tE8VeQ6XnELP4Tt5drgvWLHieMQLTT-DZEmpBLIrG9R4id8NsLnXdgCT2ys55HOf4pORiG8_nnRwIHBeazZUbCwvF-LpS7KRzAiw2PnT28THowvVllkOXQABpbEPs_A5JscnNU2g42xvBxy3UFhxtbCd5ziEJJ6NzzIKC4P8yIgdxhxuH8w9Qaer647Uf8EIpUjvUGkpJt9EYmnbXiWsg_3vVsK9v1nJIPSdw6I9MbGjw1nNMeAjLoBDSMAo5SODWljSZFN7YKneYaSgzNX7WuZqkivNS81qlC1B0kPptHzCPMMSN4Z8LyXUjD_blexPwa94MT1hTw6UPerdybDoNFL1Ti1qNjYME16PcU0mBYe0zqi0uBCFcOnxSA1N01rDLrhtvJAgOHEDRJyAkK2SNoxvSzMMPmyPusdt6_ZwVRuacCz410oRDvcIaCmLtt9PRO3cm8Ax_ua5ouDraUD4T-kmrBIwkrXfeQmUHateEkG61xRSD0YLIpqm1dNkYPp1jOsxfAclwsi0o7vrYDkO1A1vmRFDNQtdIB_8QvNfxgKDXpN4yYrs6wBypqZMmOvD_Tl-3bBBt8-aMc2-vtdF6jCqMyONG70BVRr1Bsc609S9IhN3647UfaOoJW_RBTkdANsyi8eGwDIrDDoRBU-LXRidYoiTiqVMYqOmcB3eOcLtRn0vWNuNGwsJy71eflD0c4CTo4ZD4liZ2VTHX0e__Ll_3Sz9OrkmnTlAEeEoiAye7AYts_Fu8U9QWxZLWaPih0L-TXoIevKdQ7GA_smDnDBzosCJSytY1PdtKrYIDuVCOGYWGICMcgr45l1G9aUwJhSTaNvvnZ1gWIj535MHXGJfwiWvxxpq4lzPyJ-BtoTfugIinY7AcRhBEjBce1FfHZxCoQTtorG8MWn0lTxkDKJWZhKKq31ZFDSD9GVr8NDWe7EQQtKZUH5yUd2g797tEWjk-R2VLzIZn3rKfbj2m_OH2lUim-WPadHSWrjQj6sX9WMpyrEZ40EP0sp3TZwmD86a5rM6UdC92Hsou8a6ZzrPPpFBg-ifhbWSDwwmjef_Auo7xx2s_UFAMExOzUL9JGdeobhtQBjphTuiVHFHSc9fKEM-gQ1LKuJdCY7ojHMbjOSi4dH_PjJ56aU7ucLHU5s6PBRURr6WfJN7J_GJZFkmDt16fZ0IUeMuRX9OlU7Nkykzulf0wjASd4zAC0G5oTnR2BlGG62XkBANCnhiI4bMb-5KaFCExYW8iQe6ir_IOA2AgCUoTK3fsZ36mA-NTG5PfIRCwUbwRM2No54ETp9m4J_2Vas7ps0X6eDK0PwbeRCCOxpHx8-iF6sLQJRvXG0ydDDqZwUhOZzRg0Gyx8m13E60y2l0nxDYgSGkcBicI-yHIWOyJzqhOhz-VHkZb9XEtleIfx4yVOyV2LcZezCnopxTkO4Ow_Js5FBdyK2XcnKrGQI5M1_2UxX1TcIS5DWUy30A3Gj3szDcSFouc1DoqICmZ4TWnh2vTAu63ZIwu9IScKvvsk18V-4TCxHOMtSEKFv4D2BNt6H9f-wFm1JqdvxP6xLsRz0C-VGYM57DGwxTeED--gMfkvApEMM5-y9E7UMP7RIOgrVOCWGUTWgrFCDYrU3zD0jGslPxGGatsNHVyROfVe2N8uGMAWQuNsPeMFWKO1wxyg6Vx57w79dA-UDvmM0Nv_SQNNRgI6HSmYnM6NttIWG4cFpE4odwebd7q4R0YAPMZAxEzQZDwTL63oZTE-E6YECj5XinD97E7mnI2NCXITSpRwDITO5mIXlLZiW0RwJ3RkokX-ZKMCHLQJncIwwmfgUPkIYLKaAnQmeunOnCVQTPC6_v9EkMKcXsg0UYKfh0Zhw0mhXYVtCrTVD7__Xrqs0skxmQ6Z6eGO3k7nLnVyy0WcxamukLCFQiNh0u5cVNw43Hn74JVbhhDxULZd9RIthAX1mkanPm6g-PI9gM0Va78f1_7gf8AZG_Eyw=="; 
			
			try {
				return AdHawkServer.findAd(fingerprint);
			} catch (AdHawkException e) {
				this.exception = e;
			}
			return null;
		}
		
		@Override
		protected void onProgressUpdate(String... message) {
			progress.setText(message[0]);				
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
	 
	 public String record() {

		// please note: the emulator only supports 8 khz sampling.
		// so in test mode, you need to change this to
		//int frequency = 8000;

		int frequency = 11025;

		int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
		int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
		File file = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/reverseme.pcm");

		// Delete any previous recording.
		if (file.exists())
			file.delete();

		// Create the new file.
		try {
			file.createNewFile();
		} catch (IOException e) {
			throw new IllegalStateException("Failed to create "
					+ file.toString());
		}

		try {
			// Create a DataOuputStream to write the audio data into the saved
			// file.
			OutputStream os = new FileOutputStream(file);
			BufferedOutputStream bos = new BufferedOutputStream(os);
			DataOutputStream dos = new DataOutputStream(bos);

			// Create a new AudioRecord object to record the audio.
			int bufferSize = 2 * AudioRecord.getMinBufferSize(frequency,
					channelConfiguration, audioEncoding);
			AudioRecord audioRecord = new AudioRecord(
					MediaRecorder.AudioSource.MIC, frequency,
					channelConfiguration, audioEncoding, bufferSize);

			short[] buffer = new short[bufferSize];
			audioRecord.startRecording();

			Log.i(TAG, "Recording started");

			long start = SystemClock.elapsedRealtime();
			long end = start + RECORD_TIME;
			
			while (SystemClock.elapsedRealtime() < end) {
				int bufferReadResult = audioRecord.read(buffer, 0, bufferSize);
				for (int i = 0; i < bufferReadResult; i++)
					
					if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
						dos.writeShort( EndianUtils.swapShort(buffer[i]));						
					} else {
						dos.writeShort( buffer[i] );								
					}
			}

			Log.i(TAG, "Recording stopped");

			audioRecord.stop();
			bos.flush();
			dos.close();
			return file.getAbsolutePath();

		} catch (Exception e) {
			Log.e(TAG, "Recording Failed:" + e.getMessage());
			throw new RuntimeException("Failed to create " + e.getMessage());

		}
	}

}
