package com.compassites.sai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class VideoPlayList extends ListActivity {

	private MediaPlayer mp;
	// Handler to update UI timer, progress bar etc,.
	private SongsManager songManager;
	private VideoManager videoManager;
	String currentFolderPath;
	private ArrayList<HashMap<String, String>> videoListData = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playlist);

		mp = new MediaPlayer();

		videoManager = new VideoManager();
		Bundle gotBasket = getIntent().getExtras();
		currentFolderPath = gotBasket.getString("folderPath");
		videoListData = videoManager.getPlayList(currentFolderPath);
		ArrayList<String> songTitles = new ArrayList<String>();

		for (HashMap<String, String> videoDetail : videoListData) {
			songTitles.add(videoDetail.get("videoTitle"));
		}

		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, songTitles));

		ListView lv = getListView();
		// listening to single listitem click
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting listitem index
				int songIndex = position;

				// Starting new intent
				Intent in = new Intent(getApplicationContext(),
						LocalVideo.class);
				// Sending songIndex to PlayerActivity
				in.putExtra("songIndex", songIndex);
				setResult(100, in);
				// Closing PlayListView
				finish();
			}

		});

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

}
