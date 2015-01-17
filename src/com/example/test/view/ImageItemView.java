package com.example.test.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test.R;

public class ImageItemView extends FrameLayout {
	
	ImageView iconView;
	TextView  nameView;
	View      selectView;
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
		iconView = (ImageView) this.findViewById(R.id.id_icon);
		nameView = (TextView) this.findViewById(R.id.id_name);
		selectView=this.findViewById(R.id.id_select);		
		
	}
	

	public void setName(String name){
		nameView.setText(name);
	}
	
	public void setIcon(Drawable draw){
		iconView.setImageDrawable(draw);
	}
	public void setSelect(boolean selected){
		if(selected){
			selectView.setVisibility(View.VISIBLE);
		}else{
			selectView.setVisibility(View.GONE);
		}
	}
	
}
