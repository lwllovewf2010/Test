package com.allen.test.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.allen.test.R;

public class SelectorView extends ImageView {
	private final static String TAG = "SelectorView";
	
	Drawable mDrawable = null;
	Drawable mSelectDrawable = null;
	Paint backPaint = null;
	int height=0;
	int width=0;
	int selectionNum=6;
	int select = -1;
	SweepGradient mSweepGradient;
	Paint mPaint = null;
	
	float degrees = -180.0f;
	float tarDegrees = -180.0f;
	float accelete = 10;
	
	Thread mThread= null;
	public SelectorView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}
	public SelectorView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}
	public SelectorView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		mDrawable = this.getDrawable();
		mSelectDrawable = this.getResources().getDrawable(R.drawable.selector);
 

	}
	
	
	public void setSelectionNum(int num){
		selectionNum = num;
	}
	

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		width = MeasureSpec.getSize(widthMeasureSpec);
		
		Drawable drawable = this.getDrawable();
		
		int srcWith=drawable.getIntrinsicWidth();
		int srcHeight=drawable.getIntrinsicHeight();
		
		height = width*srcHeight/srcWith;
		
		
		this.setMeasuredDimension(width, height);
		
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub

		mDrawable.setBounds(0, 0, width, height);
		mDrawable.draw(canvas);	
		canvas.save();

		
		canvas.rotate(degrees,width/2,height/2);
		mSelectDrawable.setBounds((width - mSelectDrawable.getIntrinsicWidth())/2,
				(height - mSelectDrawable.getIntrinsicHeight())/2,
				(width + mSelectDrawable.getIntrinsicWidth())/2,
				(height + mSelectDrawable.getIntrinsicHeight())/2
				);
		mSelectDrawable.draw(canvas);
		canvas.restore();
		
		boolean needInvalidate = false;
		
		if(tarDegrees-degrees > 1000.0f){
			accelete = 10;
			needInvalidate = true;
		}else if(tarDegrees - degrees > 100.0f){
			accelete = (tarDegrees-degrees)/100;
			needInvalidate = true;
		}else if(tarDegrees > degrees ){
			accelete=1;
			needInvalidate = true;
		}
		
		if(needInvalidate){
			degrees += accelete;
			this.invalidate();
		}else{
			degrees=degrees%360;
			tarDegrees = degrees;
		}
	}	
	

}
