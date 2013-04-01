package com.compassites.sai;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class Photo extends Activity implements OnTouchListener, OnClickListener {
	int timer = 4000;
	protected int splashTime = 4000;

	private RelativeLayout bgLayout;
	private static boolean stoped = false;
	private Button button, next, prev;
	int index = 0;
	Runnable runnable;
	Handler handler;
	PhotoCarousel carousel;
	Thread thread;
	LinearLayout controller;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_photo);
		bgLayout = (RelativeLayout) findViewById(R.id.llBackground);
		button = (Button) findViewById(R.id.ib_stop_and_play);
		controller = (LinearLayout) findViewById(R.id.photoControlLayout);
		next = (Button) findViewById(R.id.next);
		prev = (Button) findViewById(R.id.prev);

		// button.setVisibility(View.GONE);
		bgLayout.setOnTouchListener(this);
		button.setOnClickListener(this);
		next.setOnClickListener(this);
		prev.setOnClickListener(this);
		handler = new Handler();
		runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				controller.setVisibility(View.GONE);
			}
		};

		carousel = new PhotoCarousel(bgLayout);
		thread = new Thread(carousel);
		thread.start();

		handler.removeCallbacks(runnable);
		handler.postDelayed(runnable, 5000);

	}

	public boolean isButtonPressed() {
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.ib_stop_and_play:
			if (carousel.suspendFlag == true) {
				button.setBackgroundResource(R.drawable.audio_pause);
				carousel.myresume();
			} else {
				button.setBackgroundResource(R.drawable.audio_play);
				carousel.mysuspend();
			}
			break;

		case R.id.next:
			if (carousel.suspendFlag == true) {
				carousel.nextPic();
			} else {
				carousel.mysuspend();
				button.setBackgroundResource(R.drawable.audio_play);
				carousel.nextPic();
			}
			break;

		case R.id.prev:
			if (carousel.suspendFlag == true) {
				carousel.previousPic();
			} else {
				carousel.mysuspend();
				button.setBackgroundResource(R.drawable.audio_play);
				carousel.previousPic();
			}
			break;
		}
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		if (arg0.getId() == bgLayout.getId()) {

			controller.setVisibility(View.VISIBLE);
			handler.removeCallbacks(runnable);
			handler.postDelayed(runnable, 5000); // 5 sec

		}

		return true;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		try {
			thread.interrupt();
		} catch (Exception e) {

		}
		Intent intent = new Intent(this, Home.class);
		startActivity(intent);
		this.finish();
	}

}
