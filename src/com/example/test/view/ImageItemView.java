package com.example.test.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.example.test.util.ImageItem;

public class ImageItemView extends FrameLayout {
	
	ImageItem item;
	
	public ImageItemView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}
	public ImageItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}
	public ImageItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
	}


	
}
