package com.compassites.sai;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.VideoView;

public class MyVideoView extends VideoView {

	private int mForceHeight = 0;
	private int mForceWidth = 0;
	FrameLayout layout;

	public MyVideoView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyVideoView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyVideoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setDimensions(int w, int h) {
		this.mForceHeight = h;
		this.mForceWidth = w;

	}

	public void setParentLayout() {
		layout = (FrameLayout) findViewById(R.id.flVideoHolder);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(mForceWidth, mForceHeight);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		super.pause();
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		super.start();
		this.setBackgroundDrawable(null);
	}

}
