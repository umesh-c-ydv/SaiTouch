package com.compassites.sai;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.R.color;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class OnlineRadioActivity extends Activity implements OnClickListener,
		OnTouchListener {

	Button radioPlayer, scrollFor, scrollBack, musicPlayer;
	LinearLayout horScroll, bgLayout, horizontalLinearLayout, tabButtons,
			musicPlayerText;
	HorizontalScrollView horScrollView;
	private ProgressBar playSeekBar;
	TextView radioNameDisplay;

	TableLayout tableContent;

	MediaPlayer mediaPlayer;
	List<HashMap<String, String>> radioStations = new ArrayList<HashMap<String, String>>();
	private static String RADIO_STATION_URL;
	Handler handler;
	Runnable runnable;
	boolean flag = false;
	PhotoCarousel carousel;
	int radioIndex = 0;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.online_radio_activity);
		horScroll = (LinearLayout) findViewById(R.id.hsvFolderList);
		scrollBack = (Button) findViewById(R.id.scrollb);
		scrollFor = (Button) findViewById(R.id.scrollf);
		horScrollView = (HorizontalScrollView) findViewById(R.id.Horizontalscroller);
		playSeekBar = (ProgressBar) findViewById(R.id.pbRadio);
		playSeekBar.setMax(100);
		playSeekBar.setVisibility(View.INVISIBLE);
		radioPlayer = (Button) findViewById(R.id.ibRadioPlay);
		musicPlayer = (Button) findViewById(R.id.bLocalAudio);
		radioNameDisplay = (TextView) findViewById(R.id.tvRadioNameDisplay);
		bgLayout = (LinearLayout) findViewById(R.id.mainLayout);
		tableContent = (TableLayout) findViewById(R.id.tlTableContent);
		horizontalLinearLayout = (LinearLayout) findViewById(R.id.llHorizontalScrollView);
		tabButtons = (LinearLayout) findViewById(R.id.llTabButtons);
		musicPlayerText = (LinearLayout) findViewById(R.id.MusicPlayerText);

		horizontalLinearLayout.setBackgroundColor(Color.BLACK
				| color.transparent);
		horizontalLinearLayout.setAlpha(0.8f);
		tabButtons.setBackgroundColor(Color.BLACK | color.transparent);
		tabButtons.setAlpha(0.8f);
		musicPlayerText.setBackgroundColor(Color.BLACK | color.transparent);
		musicPlayerText.setAlpha(0.8f);

		handler = new Handler();
		runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				tableContent.setVisibility(View.GONE);
			}
		};

		bgLayout.setOnTouchListener(this);
		scrollBack.setOnClickListener(this);
		scrollFor.setOnClickListener(this);
		radioPlayer.setOnClickListener(this);
		musicPlayer.setOnClickListener(this);

		PropertyReader propReader = new PropertyReader();
		radioStations = propReader.getRadioLists();
		setUpHorizontalView();
		initializeMediaPlayer(radioIndex);
		startPlaying();
		carousel = new PhotoCarousel();
		carousel.start();

		handler.removeCallbacks(runnable);
		handler.postDelayed(runnable, 8000);

		if (!checkConnectivity()) {
			Toast.makeText(getApplicationContext(),
					"Check Your Internet Connection To Play Radio !!",
					Toast.LENGTH_LONG).show();
			onBackPressed();
			startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ibRadioPlay:
			startPlaying();
			break;

		case R.id.scrollb:
			horScrollView.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					horScrollView.smoothScrollBy(-80, 0);
				}
			});
			break;
		case R.id.scrollf:
			horScrollView.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					horScrollView.smoothScrollBy(80, 0);
				}
			});
			break;
		case R.id.bLocalAudio:
			stopPlaying();
			startActivity(new Intent("com.compassites.sai.OFFLINEAUDIOACTIVITY"));
			this.finish();
			break;

		}

		if (v instanceof LinearLayout) {
			LinearLayout ll = (LinearLayout) v;
			ImageView img = (ImageView) ll.getChildAt(0);
			img.setBackgroundResource(R.drawable.icon_stop);
			TextView tv = (TextView) ll.getChildAt(1);
			tv.setTextColor(Color.WHITE);
			radioIndex = ((LinearLayout) v).getId();
			stopPlaying();
			initializeMediaPlayer(radioIndex);
			startPlaying();

		}

	}

	private void setUpHorizontalView() {
		if (radioStations.size() != 0) {

			for (HashMap<String, String> radioDetail : radioStations) {
				LinearLayout layout = new LinearLayout(getApplicationContext());
				layout.setOrientation(1);
				layout.setLayoutParams(new LayoutParams(80, 80));
				layout.setGravity(Gravity.CENTER);
				layout.setClickable(true);
				ImageView img = new ImageView(OnlineRadioActivity.this);
				img.setBackgroundResource(R.drawable.icon_play);
				img.setLayoutParams(new LayoutParams(25, 25));
				img.setScaleType(ImageView.ScaleType.CENTER_CROP);
				TextView folderName = new TextView(OnlineRadioActivity.this);
				folderName.setText(radioDetail.get("radioName").toUpperCase());
				folderName.setGravity(Gravity.BOTTOM
						| Gravity.CENTER_HORIZONTAL);
				folderName.setTextColor(Color.GRAY);
				layout.setClickable(true);
				layout.setId(radioStations.indexOf(radioDetail));
				layout.addView(img);
				layout.addView(folderName);
				layout.setOnClickListener(OnlineRadioActivity.this);
				horScroll.addView(layout);
			}
			/*
			 * ImageButton buttonForward = new ImageButton(this);
			 * buttonBack.setBackgroundResource(R.drawable.scroll_forward);
			 * horScroll.addView(buttonForward);
			 */
		}
	}

	private void initializeMediaPlayer(int index) {

		RADIO_STATION_URL = radioStations.get(index).get("radioURL");
		radioNameDisplay.setText(radioStations.get(index).get("radioMsg"));
		makeFolderActive(index);
		flag = true;
		mediaPlayer = new MediaPlayer();
		try {
			mediaPlayer.setDataSource(RADIO_STATION_URL);

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		mediaPlayer
				.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {

					public void onBufferingUpdate(MediaPlayer mp, int percent) {
						playSeekBar.setSecondaryProgress(percent);
						Log.i("Buffering", "" + percent);
					}
				});
	}

	private void makeFolderActive(int playingRadioIndex) {

		for (int i = 0; i < horScroll.getChildCount(); i++) {
			LinearLayout ll = (LinearLayout) horScroll.getChildAt(i);
			TextView tv = (TextView) ll.getChildAt(1);
			ImageView img = (ImageView) ll.getChildAt(0);
			if (i == playingRadioIndex) {
				tv.setTextColor(Color.WHITE);
				img.setBackgroundResource(R.drawable.icon_stop);
				ll.setBackgroundColor(Color.BLACK);
				continue;
			}
			tv.setTextColor(Color.GRAY);
			img.setBackgroundResource(R.drawable.icon_play);
			ll.setBackgroundColor(Color.TRANSPARENT);
		}

	}

	private void startPlaying() {
		if (!mediaPlayer.isPlaying()) {
			try {
				radioNameDisplay.setText("Streaming...");
				mediaPlayer.prepareAsync();
				mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

					public void onPrepared(MediaPlayer mp) {
						mediaPlayer.start();
						radioPlayer.setEnabled(true);
						radioNameDisplay.setText(radioStations.get(radioIndex)
								.get("radioMsg"));
					}
				});
				radioPlayer.setBackgroundResource(R.drawable.audio_stop_btn);
				radioPlayer.setEnabled(false);
			} catch (Exception e) {

			}
		} else {
			try {
				mediaPlayer.stop();
				radioPlayer.setBackgroundResource(R.drawable.audio_play_btn);
			} catch (Exception e) {
				mediaPlayer.prepareAsync();

				mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

					public void onPrepared(MediaPlayer mp) {
						mediaPlayer.start();
						radioPlayer.setEnabled(true);
						radioNameDisplay.setText(radioStations.get(radioIndex)
								.get("radioMsg"));

					}
				});
				radioPlayer.setBackgroundResource(R.drawable.audio_stop_btn);
				radioPlayer.setEnabled(false);
			}
		}

	}

	// Stopping Player
	private void stopPlaying() {
		if (flag == true) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;

			flag = false;
		}
		playSeekBar.setVisibility(View.INVISIBLE);

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		try {
			carousel.interrupt();
		} catch (Exception e) {
			// TODO: handle exception
		}
		stopPlaying();
		Intent intent = new Intent(this, Home.class);
		startActivity(intent);
		this.finish();
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		if (arg0.getId() == bgLayout.getId()) {
			tableContent.setVisibility(View.VISIBLE);
			handler.removeCallbacks(runnable);
			handler.postDelayed(runnable, 8000);
		}
		return true;
	}

	class PhotoCarousel extends Thread {
		int photoIndex = 0;

		private String photofilepath = Environment
				.getExternalStorageDirectory().getPath() + "/SaiPhotos";
		protected int splashTime = 4000;
		ArrayList<String> photoList;

		public PhotoCarousel() {
			photoList = (ArrayList<String>) getPhotos(photofilepath);
		}

		public List<String> getPhotos(String directoryPath) {
			List<String> photoList = new ArrayList<String>();
			File file = new File(directoryPath);
			File[] photos = file.listFiles();
			if (photos != null) {
				for (File photoPath : photos) {
					photoList.add(photoPath.getPath());
				}
			}
			return photoList;
		}

		public void run() {
			{
				int count = 0;
				// TODO Auto-generated method stub
				try {
					while (true) {
						bgLayout.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								Bitmap bitmap = BitmapFactory
										.decodeFile(photoList.get(photoIndex));
								Drawable drawableImg = new BitmapDrawable(
										bitmap);
								bgLayout.setBackgroundDrawable(drawableImg);
							}
						});

						photoIndex++;
						if (photoIndex == photoList.size() - 1) {
							photoIndex = 0;
						}
						if (count == 0 || count == 1) {
							Thread.sleep(8000);
						} else {
							Thread.sleep(4000);
						}

						count++;

					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

			}

		}
	}

	public boolean checkConnectivity() {
		ConnectivityManager manager = (ConnectivityManager) getApplicationContext()
				.getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);

		if (manager != null) {
			try {
				NetworkInfo[] info = manager.getAllNetworkInfo();
				if (info != null) {
					for (NetworkInfo netInfo : info) {
						if (netInfo.getState() == NetworkInfo.State.CONNECTED) {
							return true;
						}
					}

				}
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		return false;
	}
}
