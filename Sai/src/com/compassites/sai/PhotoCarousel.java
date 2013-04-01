package com.compassites.sai;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class PhotoCarousel implements Runnable {

	int index = 0;
	private String photofilepath = Environment.getExternalStorageDirectory()
			.getPath() + "/SaiPhotos";

	RelativeLayout bgLayout;

	public boolean suspendFlag = false;
	ArrayList<String> photoList;

	public PhotoCarousel(RelativeLayout bgLayout) {
		// TODO Auto-generated constructor stub

		this.bgLayout = bgLayout;
		photoList = (ArrayList<String>) getPhotos(photofilepath);
	}

	public List<String> getPhotos(String directoryPath) {
		List<String> photoList = new ArrayList<String>();
		File file = new File(directoryPath);
		File[] photos = file.listFiles();
		if (photos != null) {
			for (File photo : photos) {
				photoList.add(photo.getPath());
			}
		}
		return photoList;
	}

	@Override
	public void run() {
		int count = 0;
		// TODO Auto-generated method stub
		try {
			while (true) {

				synchronized (this) {
					while (suspendFlag) {
						wait();
					}
				}
				bgLayout.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Bitmap bitmap = BitmapFactory.decodeFile(photoList
								.get(index));
						Drawable drawableImg = new BitmapDrawable(bitmap);
						bgLayout.setBackgroundDrawable(drawableImg);
					}
				});

				index++;
				if (index == photoList.size() - 1) {
					index = 0;
				}
				if (count == 0 || count == 1) {
					Thread.sleep(1000);
				} else {
					Thread.sleep(4000);
				}

				count++;

			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

	}

	public void mysuspend() {
		suspendFlag = true;
	}

	public synchronized void myresume() {
		suspendFlag = false;
		notify();
	}

	public void nextPic() {
		index++;
		if (index == photoList.size() - 1) {
			index = 0;
		}
		bgLayout.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Bitmap bitmap = BitmapFactory.decodeFile(photoList.get(index));
				Drawable drawableImg = new BitmapDrawable(bitmap);
				bgLayout.setBackgroundDrawable(drawableImg);
			}
		});
	}

	public void previousPic() {
		index--;
		if (index == 0) {
			index = photoList.size() - 1;
		}
		bgLayout.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Bitmap bitmap = BitmapFactory.decodeFile(photoList.get(index));
				Drawable drawableImg = new BitmapDrawable(bitmap);
				bgLayout.setBackgroundDrawable(drawableImg);
			}
		});
	}

}
