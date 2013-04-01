package com.compassites.sai;

import java.util.ArrayList;
import java.util.HashMap;


import android.R.color;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class LocalVideo extends Activity implements OnClickListener, SurfaceHolder.Callback, OnTouchListener {

	MyVideoView videoView;
	//MediaController mediaController;
	VideoManager videoManager;
	Button btnYoutube , scrollFor, scrollBack, fullScreen_btn, maximizeButton;
	TextView videoTitle;
	LinearLayout horScroll , mediaControllerView, horizontalLinearLayout, tabButtons;
	HorizontalScrollView horScrollView;
	MediaController controller;

	 int videoIndex = 0;
	 int folderIndex = 0;

	public ArrayList<HashMap<String, String>> videosList = new ArrayList<HashMap<String, String>>();
	public ArrayList<HashMap<String, String>> folderList = new ArrayList<HashMap<String, String>>();
	private ArrayList<HashMap<String, String>> videosListDemo = new ArrayList<HashMap<String, String>>();
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.offline_video_activity);
		
		videoView = (MyVideoView) findViewById(R.id.video_view);
		videoView.setDimensions(500, 280);
		videoView.setKeepScreenOn(true);
		btnYoutube = (Button) findViewById(R.id.bYouTube);
		horScroll = (LinearLayout) findViewById(R.id.hsvFolderList);
		mediaControllerView = (LinearLayout) findViewById(R.id.mediaController);
		horScrollView = (HorizontalScrollView) findViewById(R.id.Horizontalscroller);
		scrollFor = (Button) findViewById(R.id.scrollf);
		scrollBack = (Button) findViewById(R.id.scrollb);
		videoTitle = (TextView) findViewById(R.id.tvVideoName);
		fullScreen_btn = (Button) findViewById(R.id.bFullScreen);
		horizontalLinearLayout = (LinearLayout) findViewById(R.id.llHorizontalScrollView);
		tabButtons = (LinearLayout) findViewById(R.id.llTabButtons);
		fullScreen_btn.setVisibility(View.GONE);
		
		fullScreen_btn.setOnClickListener(this);
		//videoView.setOnTouchListener(this);
		scrollFor.setOnClickListener(this);
		scrollBack.setOnClickListener(this);
		btnYoutube.setOnClickListener(this);
		
		
		horizontalLinearLayout.setBackgroundColor(Color.BLACK|color.transparent);
		horizontalLinearLayout.setAlpha(0.8f);
		tabButtons.setBackgroundColor(Color.BLACK|color.transparent);
		tabButtons.setAlpha(0.8f);
		
		videoManager = new VideoManager();
		String videoPath = Environment.getExternalStorageDirectory().getPath() + "/" + "SaiVideos";
		folderList = (ArrayList<HashMap<String, String>>) videoManager.getFolderList(videoPath);
		setUpHorizontalView();
		controller = new MediaController(this){
			@Override 
			 public void setAnchorView(View view) {
			     super.setAnchorView(view);

			     maximizeButton = new Button(LocalVideo.this);
			     maximizeButton.setBackgroundResource(R.drawable.icon_maximize);
			     FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			     params.gravity = Gravity.RIGHT;
			     params.width = 30;
			     params.height = 30;
			     params.rightMargin = 40;
			     params.topMargin = 10;
			     addView(maximizeButton, params);
			     maximizeButton.setOnClickListener(LocalVideo.this);
			}
		};
		videosList = videoManager.getPlayList(folderList.get(0).get("folderPath"));
		Bitmap thumb = ThumbnailUtils.createVideoThumbnail(videosList.get(0).get("videoPath"),
			    MediaStore.Images.Thumbnails.MINI_KIND);
		
		setup(videoIndex);
		BitmapDrawable drawable = new BitmapDrawable(thumb);
		videoView.setBackgroundDrawable(drawable);
		//videoView.pause();
		videoView.setOnCompletionListener(completionListener);
		makeFolderActive();
	}
	
	
	
	private void setUpHorizontalView(){
		if(folderList.size() !=0){
			for(HashMap<String, String> folderDetail : folderList){
				LinearLayout layout = new LinearLayout(getApplicationContext());
				layout.setOrientation(1);
			    layout.setLayoutParams(new LayoutParams(60, 60));
			    layout.setGravity(Gravity.CENTER);
			    layout.setClickable(true);
				ImageView img = new ImageView(LocalVideo.this);
				img.setBackgroundResource(R.drawable.custom_folder_btn);
				img.setLayoutParams(new LayoutParams(30, 30));
				img.setScaleType(ImageView.ScaleType.CENTER_CROP);
				TextView folderName = new TextView(LocalVideo.this);
				if(folderDetail.get("folderName").length() > 7){
					folderName.setText(folderDetail.get("folderName").substring(0, 6)+"..");
				} else {
					folderName.setText(folderDetail.get("folderName"));
				}
				folderName.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
				folderName.setTextColor(Color.GRAY);
				layout.setClickable(true);
				layout.setId(folderList.indexOf(folderDetail));
				layout.addView(img);
				layout.addView(folderName);
				layout.setOnClickListener(LocalVideo.this);
				horScroll.addView(layout);
			}
		}
	}

	private void setup(int index) {
		// TODO Auto-generated method stub
		try{
			videoView.setVideoPath(videosList.get(index).get("videoPath"));
			makeFolderActive();
			videoTitle.setText(videosList.get(index).get("videoTitle"));
			videoTitle.setTextColor(Color.GRAY);
			Window window = getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			SurfaceHolder holder = videoView.getHolder();
		     holder.addCallback(this);
		 	
		 	controller.setMediaPlayer(videoView);
		 	controller.setAnchorView(videoView);
		 	videoView.setMediaController(controller);
		 	videoView.requestFocus();
			videoView.start();
			videoIndex = index;
			}catch(Exception e){
				videoIndex = 0;
				videoView.start();
				e.printStackTrace();
			}

	}
	
	OnCompletionListener completionListener = new OnCompletionListener() {

		@Override
		public void onCompletion(MediaPlayer mp) {
			// TODO Auto-generated method stub

			if (videoIndex < videosList.size() - 1) {
				// creating new HashMap
				// HashMap<String, String> song = songsList.get(i);
				// mp.stop();
				videoIndex++;
				setup(videoIndex);
			} else {
				videoIndex = 0;
				setup(videoIndex);
			}

		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v instanceof LinearLayout){
			LinearLayout ll = (LinearLayout) v;
			ImageView img = (ImageView) ll.getChildAt(0);
			img.setBackgroundResource(R.drawable.folder_active);
			TextView tv = (TextView) ll.getChildAt(1);
			tv.setTextColor(Color.WHITE);
			final int index = ((LinearLayout) v).getId();
			
			videosListDemo.clear();
			videosListDemo = videoManager.getPlayList(folderList.get(index).get("folderPath"));
			CharSequence[] videoTitless = new CharSequence[videosListDemo.size()];
			int i = 0;
			for(HashMap<String,String> videoDetail: videosListDemo ){
				videoTitless[i++] = videoDetail.get("videoTitle");
			}
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(folderList.get(index).get("folderName")+" Playlist");
			builder.setItems(videoTitless, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					videoIndex = which;
					folderIndex = index;
					videosList = videosListDemo;
					setup(videoIndex);
					videoView.setOnCompletionListener(completionListener);
					dialog.dismiss();
				}
				
			});
			
			AlertDialog alert = builder.create();
			//alert.setContentView(R.layout.playlist);
			WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
			lp.copyFrom(alert.getWindow().getAttributes());
			lp.gravity = Gravity.LEFT;
			lp.width = 200;
			lp.height=300;
			
			alert.getWindow().setAttributes(lp);
			alert.show();
			makeFolderActive();
			
		}
		
		if(v.getId() == R.id.bYouTube){
			Intent intent= new Intent(this, VideoActivity.class);
			startActivity(intent);
			this.finish();
		} else if(v.getId() == R.id.scrollb){
			horScrollView.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					horScrollView.smoothScrollBy(-80, 0);
				}
			});
		} else if (v.getId() == R.id.scrollf){
				horScrollView.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					horScrollView.smoothScrollBy(80, 0);
				}
			});
		} else if(v.getId() == R.id.bFullScreen){
			try{
				Intent fullScreen = new Intent(getApplicationContext(),FullScreenVideo.class);
				int position = videoView.getCurrentPosition();
				String videoPosition = "" + position;
				CharSequence [] videoData = {videosList.get(videoIndex).get("videoPath"), videoPosition};
				fullScreen.putExtra("videoPlaying", videoData);
				videoView.stopPlayback();
				startActivityForResult(fullScreen, 100);
				}catch(Exception e){
					
				}
		} else if(v.getId() == maximizeButton.getId()){
			try{
				Intent fullScreen = new Intent(getApplicationContext(),FullScreenVideo.class);
				int position = videoView.getCurrentPosition();
				String videoPosition = "" + position;
				CharSequence [] videoData = {videosList.get(videoIndex).get("videoPath"), videoPosition};
				fullScreen.putExtra("videoPlaying", videoData);
				videoView.stopPlayback();
				startActivityForResult(fullScreen, 100);
				}catch(Exception e){
					
				}
		} else if(v.getId() == videoView.getId()){
			if(!videoView.isPlaying()){
				videoView.setBackgroundDrawable(null);
				videoView.start();
			} else {
				videoView.pause();
			}
		}
		
	}
	
	public void makeFolderActive(){
		for(int i = 0; i<horScroll.getChildCount();i++){
			LinearLayout ll = (LinearLayout) horScroll.getChildAt(i);
			TextView tv = (TextView) ll.getChildAt(1);
			ImageView img = (ImageView) ll.getChildAt(0);
			if(i == folderIndex){
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
		try{
		Bundle bundle = data.getExtras();
		videoView.stopPlayback();
		CharSequence[] videoData = bundle.getCharSequenceArray("videoData");
		videoView.setVideoPath(videoData[0].toString());
		makeFolderActive();
		videoView.seekTo(Integer.parseInt(videoData[1].toString()));
		videoView.start();
		videoView.setOnCompletionListener(completionListener);
		
		}catch(Exception e){
			e.printStackTrace();
		}
		}
		
	}
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent intent = new Intent(this, Home.class);
		startActivity(intent);
		this.finish();
	}



	
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public boolean onTouch(View v, MotionEvent arg1) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.video_view){
			try{
			Intent fullScreen = new Intent(getApplicationContext(),FullScreenVideo.class);
			int position = videoView.getCurrentPosition();
			String videoPosition = "" + position;
			CharSequence [] videoData = {videosList.get(videoIndex).get("videoPath"), videoPosition};
			fullScreen.putExtra("videoPlaying", videoData);
			videoView.stopPlayback();
			startActivityForResult(fullScreen, 100);
			}catch(Exception e){
				
			}
			
		}
		return false;
	}

	
	
	
	
	
}
