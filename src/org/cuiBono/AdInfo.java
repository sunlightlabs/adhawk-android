package org.cuiBono;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AdInfo extends Activity{

	Button b;

	private OnClickListener getCoverage = new OnClickListener() {
		public void onClick(View v) {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://news.google.com"));
			startActivity(browserIntent);
		}
		
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adinfo);
		b =(Button) findViewById(R.id.adinfo1);
		b.setOnClickListener(getCoverage);
		Bundle bundle = getIntent().getExtras();
		
		TextView trans = (TextView) findViewById(R.id.ad_transcript_view);
		trans.setText( bundle.getString("transcript") );
		
	}
}
