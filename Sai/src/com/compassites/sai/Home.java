package com.compassites.sai;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Home extends Activity implements OnClickListener {

	Button audio, video, photos;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// mainActivity = this;
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		audio = (Button) findViewById(R.id.bAudio);
		video = (Button) findViewById(R.id.bVideo);
		photos = (Button) findViewById(R.id.bPhotos);

		video.setOnClickListener(this);
		audio.setOnClickListener(this);
		photos.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bAudio:
			startActivity(new Intent("com.compassites.sai.OFFLINEAUDIOACTIVITY"));
			this.finish();
			break;
		case R.id.bVideo:
			startActivity(new Intent("com.compassites.sai.OFFLINEVIDEO"));
			this.finish();
			break;
		case R.id.bPhotos:
			startActivity(new Intent("com.compassites.sai.PHOTOS"));
			this.finish();
			break;

		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		this.finish();

	}

}
