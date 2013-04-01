package com.compassites.sai;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Environment;

public class VideoManager {
	final String MEDIA_PATH = new String("/sdcard/Video"); // for tab

	public VideoManager() {
		// TODO Auto-generated constructor stub
	}

	public ArrayList<HashMap<String, String>> getPlayList(String videoListPath) {
		File home = new File(videoListPath);
		ArrayList<HashMap<String, String>> videosList = new ArrayList<HashMap<String, String>>();
		if (home.listFiles(new FileExtensionFilter()).length > 0) {
			for (File file : home.listFiles(new FileExtensionFilter())) {
				HashMap<String, String> video = new HashMap<String, String>();
				video.put(
						"videoTitle",
						file.getName().substring(0,
								(file.getName().length() - 4)));
				video.put("videoPath", file.getPath());

				// Adding each song to SongList
				videosList.add(video);
			}
		}
		// return songs list array
		return videosList;
	}

	/**
	 * Getting List of Folders in media folder
	 * 
	 * @return
	 */
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
					.getPath() + "/external_sd/SaiVideos";
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
	 * Class to filter files which are having .mp4 extension
	 * */
	class FileExtensionFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			return (name.endsWith(".mp4") || name.endsWith(".MP4")
					|| name.endsWith(".avi") || name.endsWith(".AVI"));
		}
	}
}
