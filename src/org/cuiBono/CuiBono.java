package org.cuiBono;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.nio.ByteOrder;

import org.apache.commons.io.EndianUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CuiBono extends Activity implements UncaughtExceptionHandler{

	static String tag = "CuiBono";
	
	static String url = "http://whopaidapp.org/api/ad/";
	
	static {
		System.loadLibrary("echonest-codegen");
	}

	private boolean isRecording = false;
	private Button recordButton;
	private NotificationManager notificationManager;
	
	 
	private native String getCodeGen(String fname);

	private class TagAdTask extends AsyncTask<String, String, String> {

		JSONObject response;
		@Override
		protected String doInBackground(String... urls) {

			publishProgress("recording in progress!");
			
			String fname = record();
			
			Log.e(tag, "fname is " + fname);

			publishProgress("generating fingerprint!");

			String code = getCodeGen(fname);
			Log.e(tag, "audio fingerprint is " + code);
			
			try {
				publishProgress("calling webservice!");
				response = getAdArticles(code);				
			} catch (Exception e) {
				publishProgress("problem with webservice");
				return "bad";
			}
			return "ok";
		}
				
		protected void onProgressUpdate(String... problem) {
			
			TextView text = (TextView) findViewById(R.id.status);
			text.setText(problem[0]);			
			sendNotify(problem[0]);		
			
		}

		@Override
		protected void onPostExecute(String result) {
			
			if (result.equals("bad")) {
				return;
			}
			recordButton.setText("Start Recording");
			Intent adinfo = new Intent(CuiBono.this, AdInfo.class);
			Bundle extras = new Bundle();
			try {

				extras.putString("title",  response.getString("title"));
				extras.putString("funder", response.getString("funder"));
				extras.putString("url",  response.getString("url"));
				extras.putString("transcript",  response.getString("transcript"));
						
				adinfo.putExtras(extras);
				startActivity(adinfo);

			} catch (JSONException e) {
				Log.e(tag, "JSON problem:" + e.getMessage());
				throw new CuiBonoException("JSON problem",e);
			}
		}
	}

	private OnClickListener record = new OnClickListener() {
		public void onClick(View v) {
			if (isRecording)
				return;
			isRecording = true;
			TagAdTask task = new TagAdTask();
			task.execute(new String[] { "http://blaaa.bla" });
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		recordButton = (Button) findViewById(R.id.RecordButton);
		recordButton.setOnClickListener(record);
		
		Thread.setDefaultUncaughtExceptionHandler(this);
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

			Log.e(tag, "Recording started");

			long start = SystemClock.elapsedRealtime();
			long end = start + 15000;
			while (SystemClock.elapsedRealtime() < end) {
				int bufferReadResult = audioRecord.read(buffer, 0, bufferSize);
				for (int i = 0; i < bufferReadResult; i++)
					
					if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
						dos.writeShort( EndianUtils.swapShort(buffer[i]));						
					} else {
						dos.writeShort( buffer[i] );								
					}
			}

			Log.e(tag, "Recording stopped");

			audioRecord.stop();
			bos.flush();
			dos.close();
			isRecording = false;
			return file.getAbsolutePath();

		} catch (Exception e) {
			Log.e(tag, "Recording Failed:" + e.getMessage());
			throw new RuntimeException("Failed to create " + e.getMessage());

		}
	}

	public JSONObject getAdArticles(String id) {

		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url + id);

		try {
			HttpResponse response = client.execute(httpGet);
			InputStream content = response.getEntity().getContent();		
			StringWriter writer = new StringWriter();
			IOUtils.copy(content, writer, "utf-8");
			return new JSONObject (writer.toString()); 
			
		} catch (ClientProtocolException e) {
			Log.e(tag, "client problem:" + e.getMessage());
			throw new CuiBonoException("client problem",e);
		} catch (IOException e) {
			Log.e(tag, "IO problem:" + e.getMessage());
			throw new CuiBonoException("IO problem",e);
		} catch (JSONException e) {
			Log.e(tag, "JSON problem:" + e.getMessage());
			throw new CuiBonoException("JSON problem",e);
		}
	}

	public void uncaughtException(Thread thread, Throwable exception) {
		Log.e(tag, "uncaught exception:" + exception.getMessage());
		sendNotify("problem:" + exception.getMessage());
	}

	
	 protected void sendNotify(String msg) {
         Context context = getApplicationContext();
         Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();         
 }

}
