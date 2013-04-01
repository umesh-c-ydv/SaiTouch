package com.compassites.sai;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.R.color;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class OfflineAudioActivity extends Activity implements
		OnCompletionListener, OnClickListener, OnTouchListener,
		OnDismissListener {

	Button btnPlay, btnPrevious, btnNext, btnRadio, scrollFor, scrollBack;
	SongsManager songManager;
	MediaPlayer mediaPlayer;
	LinearLayout horScroll, bgLayout, musicPlayer, horizontalLinearLayout,
			tabButtons, musicPlayerText;
	HorizontalScrollView horScrollView;
	TableLayout tableContent;
	TextView songNameDisplay;
	Handler handler;
	Runnable runnable;
	TableRow row_1;
	int index;
	int songIndex = 0;
	int folderIndex = 0;

	PhotoCarousel carousel;
	ArrayList<HashMap<String, String>> folderList = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> songsListData = new ArrayList<HashMap<String, String>>();

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.offline_audio_activity);
		btnPlay = (Button) findViewById(R.id.ibMusicPlay);
		btnNext = (Button) findViewById(R.id.ibMusicNext);
		btnPrevious = (Button) findViewById(R.id.ibMusicPrev);
		btnRadio = (Button) findViewById(R.id.bRadio);
		horScroll = (LinearLayout) findViewById(R.id.hsvFolderList);
		horScrollView = (HorizontalScrollView) findViewById(R.id.Horizontalscroller);
		scrollFor = (Button) findViewById(R.id.scrollf);
		scrollBack = (Button) findViewById(R.id.scrollb);
		songNameDisplay = (TextView) findViewById(R.id.tvSongNameDisplay);
		bgLayout = (LinearLayout) findViewById(R.id.mainLayout);
		tableContent = (TableLayout) findViewById(R.id.tlTableContent);
		musicPlayer = (LinearLayout) findViewById(R.id.MusicPlayer);
		row_1 = (TableRow) findViewById(R.id.Row_1);
		horizontalLinearLayout = (LinearLayout) findViewById(R.id.llHorizontalScrollView);
		tabButtons = (LinearLayout) findViewById(R.id.llTabButtons);
		musicPlayerText = (LinearLayout) findViewById(R.id.MusicPlayerText);

		bgLayout.setOnTouchListener(this);
		btnPlay.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		btnPrevious.setOnClickListener(this);
		scrollFor.setOnClickListener(this);
		scrollBack.setOnClickListener(this);
		btnRadio.setOnClickListener(this);

		musicPlayer.setBackgroundColor(Color.BLACK | color.transparent);
		musicPlayer.setAlpha(0.8f);
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
		songManager = new SongsManager();
		mediaPlayer = new MediaPlayer() {
			@Override
			public void pause() throws IllegalStateException {
				// TODO Auto-generated method stub
				super.pause();
				btnPlay.setBackgroundResource(R.drawable.audio_play_btn);
			}

			@Override
			public void start() throws IllegalStateException {
				// TODO Auto-generated method stub
				super.start();
				btnPlay.setBackgroundResource(R.drawable.pause);
			}
		};
		String songFolderPath = Environment.getExternalStorageDirectory()
				.getPath() + "/" + "SaiMusic";
		folderList = (ArrayList<HashMap<String, String>>) songManager
				.getFolderList(songFolderPath);
		setUpHorizontalView();
		songsListData = songManager.getPlayList(folderList.get(0).get(
				"folderPath"));
		setup(songIndex);
		mediaPlayer.setOnCompletionListener(this);
		// mediaPlayer.pause();

		carousel = new PhotoCarousel();
		carousel.start();

		handler.removeCallbacks(runnable);
		handler.postDelayed(runnable, 8000);

	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		// TODO Auto-generated method stub

		if (songIndex < songsListData.size()) {
			// creating new HashMap
			// HashMap<String, String> song = songsList.get(i);
			// mp.stop();
			songIndex++;
			setup(songIndex);

		} else {
			songIndex = 0;
			setup(songIndex);
		}

	}

	private void setup(int index) {
		// TODO Auto-generated method stub
		try {
			if (index < songsListData.size()) {
				mediaPlayer.reset();
				mediaPlayer.setDataSource(songsListData.get(index).get(
						"songPath"));
				songNameDisplay.setText(songsListData.get(index).get(
						"songTitle"));
				mediaPlayer.prepare();
				makeFolderActive();
				mediaPlayer.start();
			} else {
				index = 0;
				mediaPlayer.reset();
				mediaPlayer.setDataSource(songsListData.get(index).get(
						"songPath"));
				songNameDisplay.setText(songsListData.get(index).get(
						"songTitle"));
				mediaPlayer.prepare();
				makeFolderActive();
				mediaPlayer.start();
			}
			songIndex = index;
		} catch (Exception e) {
			songIndex = 0;
			mediaPlayer.start();
			e.printStackTrace();
		}

	}

	private void makeFolderActive() {
		for (int i = 0; i < horScroll.getChildCount(); i++) {
			LinearLayout ll = (LinearLayout) horScroll.getChildAt(i);
			TextView tv = (TextView) ll.getChildAt(1);
			ImageView img = (ImageView) ll.getChildAt(0);
			if (i == folderIndex) {
				tv.setTextColor(Color.WHITE);
				img.setBackgroundResource(R.drawable.folder_active);
				ll.setBackgroundColor(Color.BLACK);
				continue;
			}
			tv.setTextColor(Color.GRAY);
			img.setBackgroundResource(R.drawable.folder_normal);
			ll.setBackgroundColor(Color.TRANSPARENT);
		}

	}

	private void setUpHorizontalView() {
		if (folderList.size() != 0) {
			for (HashMap<String, String> folderDetail : folderList) {
				LinearLayout layout = new LinearLayout(getApplicationContext());
				layout.setOrientation(1);
				layout.setLayoutParams(new LayoutParams(80, 80));
				layout.setGravity(Gravity.CENTER);
				layout.setClickable(true);
				ImageView img = new ImageView(OfflineAudioActivity.this);
				img.setBackgroundResource(R.drawable.custom_folder_btn);
				img.setLayoutParams(new LayoutParams(30, 30));
				img.setScaleType(ImageView.ScaleType.CENTER_CROP);
				TextView folderName = new TextView(OfflineAudioActivity.this);
				if (folderDetail.get("folderName").length() > 8) {
					folderName.setText(folderDetail.get("folderName")
							.substring(0, 7) + "..");
				} else {
					folderName.setText(folderDetail.get("folderName"));
				}
				folderName.setGravity(Gravity.BOTTOM
						| Gravity.CENTER_HORIZONTAL);
				folderName.setTextColor(Color.GRAY);
				layout.setClickable(true);
				layout.setId(folderList.indexOf(folderDetail));
				layout.addView(img);
				layout.addView(folderName);
				layout.setOnClickListener(OfflineAudioActivity.this);
				horScroll.addView(layout);
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ibMusicPlay:
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.pause();
			} else {
				mediaPlayer.start();
			}
			break;
		case R.id.ibMusicNext:
			if (songIndex < songsListData.size()) {
				songIndex++;
				setup(songIndex);
			} else {
				songIndex = 0;
				setup(songIndex);
			}
			break;
		case R.id.ibMusicPrev:
			if (songIndex > 0) {
				songIndex--;
				setup(songIndex);
			} else {
				songIndex = songsListData.size() - 1;
				setup(songIndex);
			}
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
		case R.id.bRadio:
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.pause();
			}
			startActivity(new Intent(getApplicationContext(),
					OnlineRadioActivity.class));
			this.finish();
			break;
		}

		if (v instanceof LinearLayout) {
			final ArrayList<HashMap<String, String>> songsListDataTemp;
			LinearLayout ll = (LinearLayout) v;
			ImageView img = (ImageView) ll.getChildAt(0);
			img.setBackgroundResource(R.drawable.folder_active);
			TextView tv = (TextView) ll.getChildAt(1);
			tv.setTextColor(Color.WHITE);
			index = ((LinearLayout) v).getId();
			songsListDataTemp = songManager.getPlayList(folderList.get(index)
					.get("folderPath"));
			CharSequence[] videoTitless = new CharSequence[songsListDataTemp
					.size()];
			int i = 0;
			for (HashMap<String, String> songDetail : songsListDataTemp) {
				videoTitless[i++] = songDetail.get("songTitle");
			}

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(folderList.get(index).get("folderName")
					+ " Playlist");
			builder.setItems(videoTitless,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							songIndex = which;
							folderIndex = index;
							songsListData.clear();
							songsListData = songsListDataTemp;
							setup(songIndex);
							mediaPlayer
									.setOnCompletionListener(OfflineAudioActivity.this);
							dialog.dismiss();
						}

					});

			AlertDialog alert = builder.create();
			WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
			lp.copyFrom(alert.getWindow().getAttributes());
			lp.gravity = Gravity.LEFT;
			lp.width = 100;
			lp.verticalMargin = 10;
			lp.height = 100;

			alert.getWindow().setAttributes(lp);
			alert.show();
			makeFolderActive();

		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		stopPlaying();
		try {
			carousel.interrupt();
		} catch (Exception e) {
			// TODO: handle exception
		}
		Intent intent = new Intent(this, Home.class);
		startActivity(intent);
		this.finish();
	}

	// Stopping Player
	private void stopPlaying() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
			mediaPlayer.release();
		}

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

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		if (arg0.getId() == bgLayout.getId()) {
			makeFolderActive();
			tableContent.setVisibility(View.VISIBLE);
			handler.removeCallbacks(runnable);
			handler.postDelayed(runnable, 8000);
		}
		return true;
	}

	@Override
	public void onDismiss(DialogInterface arg0) {
		// TODO Auto-generated method stub
		makeFolderActive();
	}
}
