package com.compassites.sai;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Environment;

public class SongsManager {
	// SDCard Path
	final String MEDIA_PATH = new String("/sdcard/Music/Bhajans"); // for tab

	// Constructor
	public SongsManager() {

	}

	public ArrayList<HashMap<String, String>> getPlayList(String currentFolder) {
		File home = new File(currentFolder);
		ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

		if (home.listFiles(new FileExtensionFilter()).length > 0) {
			for (File file : home.listFiles(new FileExtensionFilter())) {
				HashMap<String, String> song = new HashMap<String, String>();
				song.put(
						"songTitle",
						file.getName().substring(0,
								(file.getName().length() - 4)));
				song.put("songPath", file.getPath());

				// Adding each song to SongList
				songsList.add(song);
			}
		}
		// return songs list array
		return songsList;
	}

	public List<HashMap<String, String>> getFolderList(String path) {

		File home = new File(path);
		List<HashMap<String, String>> folderList = new ArrayList<HashMap<String, String>>();
		File[] allFolders = home.listFiles();
		if (allFolders != null && allFolders.length != 0) {
			for (File folder : allFolders) {
				if (folder.isDirectory()) {
					HashMap<String, String> folderDetail = new HashMap<String, String>();
					folderDetail.put("folderName", folder.getName());
					folderDetail.put("folderPath", folder.getPath());
					folderList.add(folderDetail);
				}
			}
		}

		try {
			String externalSdCard = Environment.getExternalStorageDirectory()
					.getPath() + "/external_sd/SaiMusic";
			File external = new File(externalSdCard);
			File[] allExternalFolders = external.listFiles();
			if (allExternalFolders != null && allExternalFolders.length != 0) {
				for (File folder : allExternalFolders) {
					if (folder.isDirectory()) {
						HashMap<String, String> folderDetail = new HashMap<String, String>();
						folderDetail.put("folderName", folder.getName());
						folderDetail.put("folderPath", folder.getPath());
						folderList.add(folderDetail);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return folderList;
	}

	/**
	 * Class to filter files which are having .mp3 extension
	 * */
	class FileExtensionFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			return (name.endsWith(".mp3") || name.endsWith(".MP3"));
		}
	}
}
