package com.compassites.sai;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.R.color;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnFullscreenListener;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

public class VideoActivity extends YouTubeBaseActivity implements
		OnInitializedListener, OnClickListener, OnFullscreenListener {

	YouTubeStandalonePlayer youTubeplayer;

	public static final String VEDIO_PLAY_LIST_ID = "PLE3LwuJ0cfvck9PVvWffX1FClzqVZOdHL"; // for
																							// play
																							// list
	public static final String DEVELOPER_KEY = "AI39si46t-0iJW8dJLj45TLAovlfiIRAGH5Yg0p4PgHMNuU-moFtA8Gs6ZVpuC9Gk1kz4B4jhmqN6JKtTZ2uPuPcyo9MbRNN9w";
	YouTubePlayerView playerView;
	JSONObject json;
	Button myVideos;
	LinearLayout tabButtons;
	boolean isFullscreen = false;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.online_video_activity);

		playerView = (YouTubePlayerView) findViewById(R.id.player_view);
		myVideos = (Button) findViewById(R.id.bLocalVideo);
		tabButtons = (LinearLayout) findViewById(R.id.llTabButtons);
		myVideos.setOnClickListener(this);
		// playerView.initialize(DEVELOPER_KEY, (OnInitializedListener) this);
		playerView.initialize(DEVELOPER_KEY, this);

		tabButtons.setBackgroundColor(Color.BLACK | color.transparent);
		tabButtons.setAlpha(0.8f);

		if (!checkConnectivity()) {
			Toast.makeText(getApplicationContext(),
					"Check Your Internet Connection To Play Youtube Video !!",
					Toast.LENGTH_LONG).show();
			onBackPressed();
			startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
		}
	}

	@Override
	public void onInitializationSuccess(YouTubePlayer.Provider provider,
			YouTubePlayer player, boolean wasRestored) {
		if (!wasRestored) {
			player.loadPlaylist(VEDIO_PLAY_LIST_ID);

		}
	}

	protected YouTubePlayer.Provider getYouTubePlayerProvider() {
		return (YouTubePlayerView) findViewById(R.id.player_view);
	}

	@Override
	public void onInitializationFailure(
			com.google.android.youtube.player.YouTubePlayer.Provider arg0,
			YouTubeInitializationResult arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.bLocalVideo) {
			Intent intent = new Intent(this, LocalVideo.class);
			startActivity(intent);
			this.finish();
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (!isFullscreen) {
			super.onBackPressed();
			Intent intent = new Intent(this, Home.class);
			startActivity(intent);
			this.finish();
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

	@Override
	public void onFullscreen(boolean arg0) {
		// TODO Auto-generated method stub
		isFullscreen = true;

	}

}
