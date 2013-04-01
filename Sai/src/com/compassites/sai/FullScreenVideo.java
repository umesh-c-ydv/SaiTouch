package com.compassites.sai;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

public class FullScreenVideo extends Activity implements OnTouchListener,
		OnClickListener {

	Button minimize_btn;
	VideoView videoView;
	LinearLayout layout, main;
	Handler handler;
	Runnable runnable;
	String videoPath;
	MediaController mediaController;
	Button minimizeButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.video_full_screen);
		videoView = (VideoView) findViewById(R.id.VideoFullScreen);

		main = (LinearLayout) findViewById(R.id.main);
		main.setBackgroundColor(Color.BLACK);
		handler = new Handler();

		Bundle bundle = getIntent().getExtras();
		CharSequence[] videoData = bundle.getCharSequenceArray("videoPlaying");
		videoPath = videoData[0].toString();
		videoView.setVideoPath(videoPath);
		videoView.seekTo(Integer.parseInt(videoData[1].toString()));
		videoView.setOnCompletionListener(completionListener);

		mediaController = new MediaController(this) {
			@Override
			public void setAnchorView(View view) {
				super.setAnchorView(view);

				minimizeButton = new Button(FullScreenVideo.this);
				minimizeButton.setBackgroundResource(R.drawable.icon_minimize);
				FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.gravity = Gravity.RIGHT;
				params.width = 30;
				params.height = 30;
				params.rightMargin = 40;
				params.topMargin = 10;
				addView(minimizeButton, params);
				minimizeButton.setOnClickListener(FullScreenVideo.this);
			}
		};
		videoView.setMediaController(mediaController);

		videoView.start();
	}

	OnCompletionListener completionListener = new OnCompletionListener() {

		@Override
		public void onCompletion(MediaPlayer mp) {
			// TODO Auto-generated method stub
			closeFullScreen();
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v instanceof Button) {
			closeFullScreen();
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if (v.getId() == videoView.getId()) {
			// layout.setVisibility(View.VISIBLE);
		}
		return false;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		closeFullScreen();
	}

	private void closeFullScreen() {
		int position = videoView.getCurrentPosition();
		CharSequence[] videoData = { videoPath, "" + position };
		Intent in = new Intent(getApplicationContext(), LocalVideo.class);
		// Sending songIndex to PlayerActivity
		in.putExtra("videoData", videoData);
		setResult(RESULT_OK, in);
		videoView.stopPlayback();
		// Closing PlayListView
		finish();
	}

}
