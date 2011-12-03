package org.cuiBono;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class cuiBono extends Activity {

	boolean isRecording = false;
	Button b;
	
	private OnClickListener record = new OnClickListener() {
		@Override
		public void onClick(View v) {
			isRecording = !isRecording;
			if (isRecording) {
				b.setText("Stop Recording");
				Thread t = new Thread() {
					@Override
					public void run() {
						record();
					}
				};
				t.start();
			} else {
				b.setText("Start Recording");

			}
		}
		
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		b =(Button) findViewById(R.id.RecordButton);
		b.setOnClickListener(record);
	}

	public void record() {
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

			Log.e("AudioRecord", "Recording started");

			while (isRecording) {
				int bufferReadResult = audioRecord.read(buffer, 0, bufferSize);
				for (int i = 0; i < bufferReadResult; i++)
					dos.writeShort(buffer[i]);
			}

			Log.e("AudioRecord", "Recording stopped");

			audioRecord.stop();
			bos.flush();
			dos.close();

		} catch (Throwable t) {
			Log.e("AudioRecord", "Recording Failed:" + t.getMessage());

		}
	}

}