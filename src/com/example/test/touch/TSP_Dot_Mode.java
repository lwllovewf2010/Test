package com.example.test.touch;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class TSP_Dot_Mode extends Activity {
	private final static String TAG = "TSP_Grid_Mode";
	private final static float LINE_WEIGHT = 1.0f;
	private final static float TOUCH_RADIUS = 20.0f;
	private final static float TOUCH_RECT= 4.0f;
	
	View contentView;
	int width,height;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate start");
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
		          WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.getWindow().getDecorView().setBackgroundColor(0xfff5f5f5);
	    
		Display  disply = getWindowManager().getDefaultDisplay();
		
		DisplayMetrics outMetrics= new DisplayMetrics();
		disply.getMetrics(outMetrics);
		width = outMetrics.widthPixels;
		height = outMetrics.heightPixels;
		
		disableStatusBar();
		contentView = new DrawView(this);
		this.setContentView(contentView);
			
	}

	private void disableStatusBar(){
		Object service = getSystemService("statusbar");
		try {
			Class<?> statusBarManager = Class
					.forName("android.app.StatusBarManager");
			Method expand = statusBarManager.getMethod("disable", int.class); // getMethod("collapse")似乎有点效果
			expand.invoke(service, 0x00000001);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	class DrawView extends View{

		
		private final Paint mLinePaint = new Paint();
		private final Paint mTouchPaint = new Paint();
		
		ArrayList<Point> Dots;
		Point cDot;
		boolean isTouched = false;
		public DrawView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
//			ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
//			this.setLayoutParams(params);
			mLinePaint.setARGB(150, 255, 154, 255);
			mLinePaint.setStrokeWidth(LINE_WEIGHT);
			mTouchPaint.setAntiAlias(false);
			
			mTouchPaint.setARGB(255, 255, 154, 255);
			mTouchPaint.setAntiAlias(false);
			mTouchPaint.setStyle(Paint.Style.FILL);
			
			Dots = new ArrayList<Point>();
		}
		

		
		@Override
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			super.onDraw(canvas);
			
			for(Point dot:Dots){
				canvas.drawLine(dot.x-TOUCH_RECT, dot.y-TOUCH_RECT, 
						dot.x+TOUCH_RECT, dot.y+TOUCH_RECT, mLinePaint);
				canvas.drawLine(dot.x+TOUCH_RECT, dot.y-TOUCH_RECT, 
						dot.x-TOUCH_RECT, dot.y+TOUCH_RECT, mLinePaint);
			}
			
			if(isTouched){
				canvas.drawCircle(cDot.x, cDot.y, TOUCH_RADIUS, mTouchPaint);
			}

		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			// TODO Auto-generated method stub
		      int action = event.getAction();
		      Log.d(TAG,"onTouchEvent" );
		      float x = event.getX();
		      float y = event.getY();
		      switch(action){
		      case  MotionEvent.ACTION_DOWN:
		    	  isTouched = true;
		    	  cDot = new Point(x,y);
		    	  break;
		      case MotionEvent.ACTION_MOVE:
		    	  cDot.x=x;
		    	  cDot.y=y;
		    	  break;
		      case MotionEvent.ACTION_UP:
		      case MotionEvent.ACTION_CANCEL:
		    	  cDot.x=x;
		    	  cDot.y=y;
		    	  Dots.add(cDot);
		    	  isTouched = false;
		    	  break;
		    	  
		      }

	    	  
		      invalidate(); 
			
			
			
			return true;
		}
		
		
		
		
		
		

	}
	

	
	class Point{
		public Point(Float fx, Float fy) {
			// TODO Auto-generated constructor stub
			x=fx;
			y=fy;
		}
		float x;
		float y;
	}
	
}
